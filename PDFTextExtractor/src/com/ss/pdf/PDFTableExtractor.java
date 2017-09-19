/**
 * Copyright (C) 2015, GIAYBAC
 *
 * Released under the MIT license
 */
package com.ss.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Range;

/**
 *
 * @author THOQ LUONG Mar 22, 2015 3:34:29 PM
 */
public class PDFTableExtractor {

	// --------------------------------------------------------------------------
	// Members
	private final Logger logger = LoggerFactory
			.getLogger(PDFTableExtractor.class);

	// contains pages that will be extracted table content.
	// If this variable doesn't contain any page, all pages will be extracted
	private final List<Integer> extractedPages = new ArrayList<>();
	private final List<Integer> exceptedPages = new ArrayList<>();
	// contains avoided line idx-s for each page,
	// if this multimap contains only one element and key of this element equals
	// -1
	// then all lines in extracted pages contains in multi-map value will be
	// avoided
	private final Multimap<Integer, Integer> pageNExceptedLinesMap = HashMultimap
			.create();

	private InputStream inputStream;
	private PDDocument document;
	private String password;

	// --------------------------------------------------------------------------
	// Initialization and releasation
	// --------------------------------------------------------------------------
	// Getter N Setter
	// --------------------------------------------------------------------------
	// Method binding
	public PDFTableExtractor setSource(InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	public PDFTableExtractor setSource(InputStream inputStream, String password) {
		this.inputStream = inputStream;
		this.password = password;
		return this;
	}

	public PDFTableExtractor setSource(File file) {
		try {
			return this.setSource(new FileInputStream(file));
		} catch (FileNotFoundException ex) {
			throw new RuntimeException("Invalid pdf file", ex);
		}
	}

	public PDFTableExtractor setSource(String filePath) {
		return this.setSource(new File(filePath));
	}

	public PDFTableExtractor setSource(File file, String password) {
		try {
			return this.setSource(new FileInputStream(file), password);
		} catch (FileNotFoundException ex) {
			throw new RuntimeException("Invalid pdf file", ex);
		}
	}

	public PDFTableExtractor setSource(String filePath, String password) {
		return this.setSource(new File(filePath), password);
	}

	/**
	 * This page will be analyze and extract its table content
	 *
	 * @param pageIdx
	 * @return
	 */
	public PDFTableExtractor addPage(int pageIdx) {
		extractedPages.add(pageIdx);
		return this;
	}

	public PDFTableExtractor exceptPage(int pageIdx) {
		exceptedPages.add(pageIdx);
		return this;
	}

	/**
	 * Avoid a specific line in a specific page. LineIdx can be negative number,
	 * -1 is the last line
	 *
	 * @param pageIdx
	 * @param lineIdxs
	 * @return
	 */
	public PDFTableExtractor exceptLine(int pageIdx, int[] lineIdxs) {
		for (int lineIdx : lineIdxs) {
			pageNExceptedLinesMap.put(pageIdx, lineIdx);
		}
		return this;
	}

	/**
	 * Avoid this line in all extracted pages. LineIdx can be negative number,
	 * -1 is the last line
	 *
	 * @param lineIdxs
	 * @return
	 */
	public PDFTableExtractor exceptLine(int[] lineIdxs) {
		this.exceptLine(-1, lineIdxs);
		return this;
	}

	public List<Table> extract() {
		List<Table> retVal = new ArrayList<>();
		Multimap<Integer, Range<Integer>> pageIdNLineRangesMap = LinkedListMultimap
				.create();
		Multimap<Integer, TextPosition> pageIdNTextsMap = LinkedListMultimap
				.create();
		try {
			this.document = this.password != null ? PDDocument.load(
					inputStream, this.password) : PDDocument.load(inputStream);
			for (int pageId = 0; pageId < document.getNumberOfPages(); pageId++) {
				boolean b = !exceptedPages.contains(pageId)
						&& (extractedPages.isEmpty() || extractedPages
								.contains(pageId));
				if (b) {

					List<TextPosition> texts = extractTextPositions(pageId);// sorted
																			// by
																			// .getY()
																			// ASC
					// logger.info("texts ======" + texts);

					groupTextContent(texts);

					List<Range<Integer>> lineRanges = getLineRanges(pageId,
							texts);

					logger.info("lineRanges ======" + lineRanges);

					List<TextPosition> reOrderedTexts = texts;

					reOrderedTexts = reOrderContent(reOrderedTexts);
					// System.out.println(reOrderedTexts);
					// extract line ranges

					// System.out.println(lineRanges);
					// extract column ranges
					List<TextPosition> textsByLineRanges = getTextsByLineRanges(
							lineRanges, texts);

					// System.out.println(textsByLineRanges);
					logger.info("textsByLineRanges ======" + textsByLineRanges);

					pageIdNLineRangesMap.putAll(pageId, lineRanges);
					pageIdNTextsMap.putAll(pageId, textsByLineRanges);
				}
			}
			// Calculate columnRanges
			List<Range<Integer>> columnRanges = getColumnRanges(pageIdNTextsMap
					.values());
			logger.info("columnRanges ======" + columnRanges);

			for (int pageId : pageIdNTextsMap.keySet()) {
				Table table = buildTable(pageId,
						(List) pageIdNTextsMap.get(pageId),
						(List) pageIdNLineRangesMap.get(pageId), columnRanges);
				retVal.add(table);
				// debug
				logger.debug("Found " + table.getRows().size() + " row(s) and "
						+ columnRanges.size()
						+ " column(s) of a table in page " + pageId);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Parse pdf file fail", ex);
		} finally {
			if (this.document != null) {
				try {
					this.document.close();
				} catch (IOException ex) {
					logger.error(null, ex);
				}
			}
		}
		// return
		return retVal;
	}

	// --------------------------------------------------------------------------
	// Implement N Override
	// --------------------------------------------------------------------------
	// Utils
	/**
	 * Texts in tableContent have been ordered by .getY() ASC
	 *
	 * @param pageIdx
	 * @param tableContent
	 * @param rowTrapRanges
	 * @param columnTrapRanges
	 * @return
	 */
	private Table buildTable(int pageIdx, List<TextPosition> tableContent,
			List<Range<Integer>> rowTrapRanges,
			List<Range<Integer>> columnTrapRanges) {
		Table retVal = new Table(pageIdx, columnTrapRanges.size());
		int idx = 0;
		int rowIdx = 0;
		List<TextPosition> rowContent = new ArrayList<>();

		// logger.info("tableContent ======" + tableContent);
		logger.info("rowTrapRanges ======" + rowTrapRanges);
		logger.info("columnTrapRanges ======" + columnTrapRanges);

		while (idx < tableContent.size()) {
			TextPosition textPosition = tableContent.get(idx);

			Range<Integer> rowTrapRange = rowTrapRanges.get(rowIdx);
			Range<Integer> textRange = Range.closed((int) textPosition.getY(),
					(int) (textPosition.getY() + textPosition.getHeight()));

			logger.info("textRange ======" + textRange);

			if (rowTrapRange.encloses(textRange)
					|| textPosition.getX() > tableContent.get(idx - 1).getX()
					|| textPosition.getY() - tableContent.get(idx - 1).getY() < 7) {
				logger.info("textRange rowContent===" + textPosition);
				rowContent.add(textPosition);
				idx++;
			} else {

				logger.info("rowContent===" + rowContent);
				// if (rowContent.size() > 0) {

				if (rowContent.size() > 0) {
					TableRow row = buildRow(rowIdx, rowContent,
							columnTrapRanges);
					retVal.getRows().add(row);
				}
				// next row: clear rowContent
				rowContent.clear();

				// }
				// rowContent.clear();
				rowIdx++;
			}
		}
		// last row
		if (!rowContent.isEmpty() && rowIdx < rowTrapRanges.size()) {
			TableRow row = buildRow(rowIdx, rowContent, columnTrapRanges);
			retVal.getRows().add(row);
		}
		// return
		return retVal;
	}

	/**
	 *
	 * @param rowIdx
	 * @param rowContent
	 * @param columnTrapRanges
	 * @return
	 */
	private TableRow buildRow(int rowIdx, List<TextPosition> rowContent,
			List<Range<Integer>> columnTrapRanges) {
		TableRow retVal = new TableRow(rowIdx);

		Map<Double, List<TextPosition>> groupedTextPosition = groupTextContent(rowContent);
		logger.info("groupedTextPosition=====" + groupedTextPosition);

		Object[] keyObjArray = groupedTextPosition.keySet().toArray();
		Double[] keyStrArray = new Double[3];

		int j = 0;

		if (keyObjArray != null && keyObjArray.length == 3) {
			for (int i = 0; i < keyObjArray.length; i++) {

				if (i == 0)
					j = 0;
				if (i == 1)
					j = 2;
				if (i == 2)
					j = 1;

				keyStrArray[i] = Double.parseDouble(keyObjArray[j].toString());

			}

			logger.info("keyStrArray=====" + keyStrArray);

			rowContent = new ArrayList<TextPosition>();

			for (Double key : keyStrArray) {
				logger.info("keyStrArray=====" + key);
				List<TextPosition> TextPositionList = groupedTextPosition
						.get(key);

				for (TextPosition textPosition : TextPositionList)
					rowContent.add(textPosition);

			}

			logger.info("reorderrowContent=====" + rowContent);

		} else {

			// List<TextPosition> mapGroupedTextPosition =
			// groupedTextPosition.get(keyArray);

			logger.info("columnTrapRanges=====" + columnTrapRanges);
			logger.info("rowContent=====" + rowContent);
			// Sort rowContent
			Collections.sort(rowContent, new Comparator<TextPosition>() {
				@Override
				public int compare(TextPosition o1, TextPosition o2) {
					int retVal = 0;

					logger.info("Te	xtPosition==" + o1 + "===TextPosition=="
							+ o2);
					if (o1.getX() < o2.getX()) {

						logger.info("o1.getX()=====" + o1.getX()
								+ "==o2.getX()=====" + o2.getX());
						// if (o1.getY() < o2.getY()) {
						retVal = -1;
						// }
					} else if (o1.getX() >= o2.getX()) {
						logger.info("o1.getX()=====" + o1.getX()
								+ "==o2.getX()=====" + o2.getX());

						logger.info("o1.getY)=====" + o1.getY()
								+ "==o2.getY()=====" + o2.getY());

						retVal = 1;

					}

					logger.info("retVal=====" + retVal);

					return retVal;
				}
			});

			logger.info("buildRow===rowContent=====" + rowContent);

		}

		logger.info("END===rowContent=====" + rowContent);

		int idx = 0;
		int columnIdx = 0;
		List<TextPosition> cellContent = new ArrayList<>();
		while (idx < rowContent.size()) {
			TextPosition textPosition = rowContent.get(idx);
			Range<Integer> columnTrapRange = columnTrapRanges.get(columnIdx);
			Range<Integer> textRange = Range.closed((int) textPosition.getX(),
					(int) (textPosition.getX() + textPosition.getWidth()));

			if (columnTrapRange.encloses(textRange)) {

				logger.info("CC TP==" + textPosition + "==columnTRange=="
						+ columnTrapRange + "==tRange==" + textRange
						+ "==getX==" + textPosition.getX() + "===getY==="
						+ textPosition.getY());

				cellContent.add(textPosition);
				idx++;
			} else {

				logger.info("cellContent ======" + cellContent);

				TableCell cell = buildCell(columnIdx, cellContent);
				retVal.getCells().add(cell);
				// next column: clear cell content
				cellContent.clear();
				columnIdx++;
			}
		}
		if (!cellContent.isEmpty() && columnIdx < columnTrapRanges.size()) {
			TableCell cell = buildCell(columnIdx, cellContent);
			retVal.getCells().add(cell);
		}
		// return
		return retVal;
	}

	private TableCell buildCell(int columnIdx, List<TextPosition> cellContent) {

		logger.info("buildCell cellContent BEFORE======" + cellContent);

		/*
		 * Collections.sort(cellContent, new Comparator<TextPosition>() {
		 * 
		 * @Override public int compare(TextPosition o1, TextPosition o2) { int
		 * retVal = 0; if (o1.getX() < o2.getX()) { retVal = -1; } else if
		 * (o1.getX() > o2.getX()) { retVal = 1; } return retVal; } });
		 */

		logger.info("buildCell cellContent AFTER======" + cellContent);

		// String cellContentString =
		// Joiner.on("").join(cellContent.stream().map(e ->
		// e.getCharacter()).iterator());
		StringBuilder cellContentBuilder = new StringBuilder();
		for (TextPosition textPosition : cellContent) {
			cellContentBuilder.append(textPosition.getUnicode());
		}
		String cellContentString = cellContentBuilder.toString();
		return new TableCell(columnIdx, cellContentString);
	}

	private List<TextPosition> extractTextPositions(int pageId)
			throws IOException {
		TextPositionExtractor extractor = new TextPositionExtractor(document,
				pageId);
		return extractor.extract();
	}

	private boolean isExceptedLine(int pageIdx, int lineIdx) {
		boolean retVal = this.pageNExceptedLinesMap.containsEntry(pageIdx,
				lineIdx)
				|| this.pageNExceptedLinesMap.containsEntry(-1, lineIdx);
		return retVal;
	}

	/**
	 *
	 * Remove all texts in excepted lines
	 *
	 * TexPositions are sorted by .getY() ASC
	 *
	 * @param lineRanges
	 * @param textPositions
	 * @return
	 */
	private List<TextPosition> getTextsByLineRanges(
			List<Range<Integer>> lineRanges, List<TextPosition> textPositions) {
		List<TextPosition> retVal = new ArrayList<>();
		int idx = 0;
		int lineIdx = 0;
		while (idx < textPositions.size() && lineIdx < lineRanges.size()) {
			TextPosition textPosition = textPositions.get(idx);
			Range<Integer> textRange = Range.closed((int) textPosition.getY(),
					(int) (textPosition.getY() + textPosition.getHeight()));
			Range<Integer> lineRange = lineRanges.get(lineIdx);
			if (lineRange.encloses(textRange)) {
				retVal.add(textPosition);
				idx++;
			} else if (lineRange.upperEndpoint() < textRange.lowerEndpoint()) {
				lineIdx++;
			} else {
				idx++;
			}
		}

		// System.out.println(retVal);
		// return
		return retVal;
	}

	/**
	 * @param texts
	 * @return
	 */
	private List<Range<Integer>> getColumnRanges(Collection<TextPosition> texts) {
		TrapRangeBuilder rangesBuilder = new TrapRangeBuilder();
		for (TextPosition text : texts) {
			Range<Integer> range = Range.closed((int) text.getX(),
					(int) (text.getX() + text.getWidth()));
			rangesBuilder.addRange(range);
		}
		return rangesBuilder.build();
	}

	// @SuppressWarnings("unused")
	private Map<Double, List<TextPosition>> groupTextContent(
			List<TextPosition> textContent) {

		Map<Double, List<TextPosition>> groupedTextPosition = new TreeMap<Double, List<TextPosition>>();

		try {

			for (TextPosition textPosition : textContent) {
				double key = textPosition.getY();
				if (groupedTextPosition.get(key) == null) {
					groupedTextPosition.put(key, new ArrayList<TextPosition>());
				}
				groupedTextPosition.get(key).add(textPosition);

				// logger.info("==textPosition==" + textPosition
				// + "==textPosition.getTextMatrix==="
				// + textPosition.getTextMatrix());
			}

			// logger.info("====groupedTextPosition=====" +
			// groupedTextPosition);

			/*
			 * List<Double> yPositionsList = new ArrayList<Double>(
			 * groupedTextPosition.keySet());
			 * 
			 * double diff = 0.0, temp = 0.0, reMap = 0.0;
			 * 
			 * Map<Double, Double> yPositionMap = new TreeMap<Double, Double>();
			 * 
			 * for (Double yPositionsStr : yPositionsList) {
			 * 
			 * diff = yPositionsStr - temp;
			 * 
			 * logger.info("====temp=====" + temp + "====diff=====" + diff);
			 * 
			 * if (diff <= 8) {
			 * 
			 * if (reMap == 0) reMap = yPositionsStr;
			 * 
			 * yPositionMap.put(temp, reMap); yPositionMap.put(yPositionsStr,
			 * reMap);
			 * 
			 * } else reMap = 0;
			 * 
			 * temp = yPositionsStr;
			 * 
			 * }
			 */

			// logger.info("====yPositionMap=====" + yPositionMap);

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return groupedTextPosition;

	}

	private List<TextPosition> reOrderContent(List<TextPosition> textContent) {

		// List<TextPosition> textContent = new ArrayList<TextPosition>();

		Collections.sort(textContent, new Comparator<TextPosition>() {

			@Override
			public int compare(TextPosition textPosition1,
					TextPosition textPosition2) {
				// TODO Auto-generated method stub

				// comparing both coordinates
				int result = Double.compare(textPosition1.getX(),
						textPosition2.getX());

				if (result == 0) {
					// both X are equal -> compare Y too
					result = Double.compare(textPosition1.getY(),
							textPosition2.getY());
				}

				return Float.compare(textPosition1.getY(), textPosition2.getY());

				// return ((int) ((textPosition1.getX() + textPosition1.getY())
				// - (textPosition2
				// .getX() + textPosition2.getY())));

				// return result;
			}
		});

		for (TextPosition textPosition : textContent) {

			/*
			 * System.out.println("====reOrderContent=====" + textPosition +
			 * "====x===" + textPosition.getX() + "Y===" + textPosition.getY() +
			 * "Height===" + textPosition.getHeight());
			 */
			// + "lineRange=====" + lineRange);

			/*
			 * logger.info("====reOrderContent=====" + textPosition + "====x==="
			 * + textPosition.getX() + "Y===" + textPosition.getY() +
			 * "Height===" + textPosition.getHeight());
			 */
		}

		return textContent;

	}

	private List<Range<Integer>> getLineRanges(int pageId,
			List<TextPosition> pageContent) {
		TrapRangeBuilder lineTrapRangeBuilder = new TrapRangeBuilder();
		Range<Integer> tempLineRange = null;
		for (TextPosition textPosition : pageContent) {
			// System.out.println(textPosition);
			// Range<Integer> lineRange = Range.closed((int)
			// textPosition.getY(),
			// (int) (textPosition.getY() + textPosition.getHeight()));

			Range<Integer> lineRange = Range.closed((int) textPosition.getY(),
					(int) (textPosition.getY() + textPosition.getHeight()));
			// add to builder

			if (tempLineRange != null) {

				/*
				 * logger.info("tempLineRange.lowerEndpoint=====" +
				 * tempLineRange.lowerEndpoint().intValue() +
				 * "lineRange.lowerEndpoint=======" +
				 * lineRange.lowerEndpoint().intValue());
				 */

				if ((lineRange.lowerEndpoint().intValue() - tempLineRange
						.lowerEndpoint().intValue()) < textPosition.getHeight() + 4) {

					// lineRange = tempLineRange;

				}

			}

			tempLineRange = lineRange;

			// System.out.println(textPosition + "====x===" +
			// textPosition.getX()
			// + "Y===" + textPosition.getY() + "Height==="
			// + textPosition.getHeight() + "lineRange=====" + lineRange);

			/*
			 * logger.info("textPosition=====" + textPosition + "==x==" +
			 * textPosition.getX() + "===Y==" + textPosition.getY() +
			 * "===Height===" + textPosition.getHeight() + "===lineRange===" +
			 * lineRange);
			 */

			lineTrapRangeBuilder.addRange(lineRange);
		}
		List<Range<Integer>> lineTrapRanges = lineTrapRangeBuilder.build();
		// System.out.println("lineTrapRanges===" + lineTrapRanges);
		List<Range<Integer>> retVal = removeExceptedLines(pageId,
				lineTrapRanges);
		return retVal;
	}

	private List<Range<Integer>> removeExceptedLines(int pageIdx,
			List<Range<Integer>> lineTrapRanges) {
		List<Range<Integer>> retVal = new ArrayList<>();
		for (int lineIdx = 0; lineIdx < lineTrapRanges.size(); lineIdx++) {
			boolean isExceptedLine = isExceptedLine(pageIdx, lineIdx)
					|| isExceptedLine(pageIdx, lineIdx - lineTrapRanges.size());
			if (!isExceptedLine) {
				retVal.add(lineTrapRanges.get(lineIdx));
			}
		}
		// return
		return retVal;
	}

	// --------------------------------------------------------------------------
	// Inner class
	private static class TextPositionExtractor extends PDFTextStripper {

		private final List<TextPosition> textPositions = new ArrayList<>();
		private final int pageId;

		private TextPositionExtractor(PDDocument document, int pageId)
				throws IOException {
			super();
			super.setSortByPosition(true);
			super.document = document;
			this.pageId = pageId;
		}

		public void stripPage(int pageId) throws IOException {
			this.setStartPage(pageId + 1);
			this.setEndPage(pageId + 1);
			try (Writer writer = new OutputStreamWriter(
					new ByteArrayOutputStream())) {
				writeText(document, writer);
			}
		}

		@Override
		protected void writeString(String string,
				List<TextPosition> textPositions) throws IOException {
			this.textPositions.addAll(textPositions);
		}

		/**
		 * and order by textPosition.getY() ASC
		 *
		 * @return
		 * @throws IOException
		 */
		private List<TextPosition> extract() throws IOException {
			this.stripPage(pageId);
			// sort
			Collections.sort(textPositions, new Comparator<TextPosition>() {
				@Override
				public int compare(TextPosition o1, TextPosition o2) {
					int retVal = 0;
					if (o1.getY() < o2.getY()) {
						retVal = -1;
					} else if (o1.getY() > o2.getY()) {
						retVal = 1;
					}
					return retVal;

				}
			});
			return this.textPositions;
		}
	}
}
