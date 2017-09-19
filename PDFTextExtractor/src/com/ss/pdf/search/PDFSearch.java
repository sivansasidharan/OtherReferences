package com.ss.pdf.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDFSearch {

	private final Logger logger = LoggerFactory.getLogger(PDFSearch.class);

	public List<Integer> search(PDDocument document, String searchTerm)
			throws IOException {

		logger.info("* Looking for " + searchTerm);
		List<Integer> pageList = new ArrayList<Integer>();
		for (int page = 1; page <= document.getNumberOfPages(); page++) {
			List<TextPositionSequence> hits = findSubwords(document, page,
					searchTerm);
			for (TextPositionSequence hit : hits) {
				TextPosition lastPosition = hit
						.textPositionAt(hit.length() - 1);
				pageList.add(page);
				logger.info("Found Page @====" + page, hit.getX(), hit.getY(),
						//hit.getWidth(), 
						lastPosition.getUnicode(),
						lastPosition.getXDirAdj(), lastPosition.getYDirAdj());
			}
		}
		return pageList;
	}

	public List<TextPositionSequence> findSubwords(PDDocument document,
			int page, String searchTerm) throws IOException {
		final List<TextPositionSequence> hits = new ArrayList<TextPositionSequence>();
		PDFTextStripper stripper = new PDFTextStripper() {
			@Override
			protected void writeString(String text,
					List<TextPosition> textPositions) throws IOException {
				TextPositionSequence word = new TextPositionSequence(
						textPositions);
				String string = word.toString();

				int fromIndex = 0;
				int index;
				while ((index = string.indexOf(searchTerm, fromIndex)) > -1) {
					hits.add(word.subSequence(index,
							index + searchTerm.length()));
					fromIndex = index + 1;
				}
				super.writeString(text, textPositions);
			}
		};

		stripper.setSortByPosition(true);
		stripper.setStartPage(page);
		stripper.setEndPage(page);
		stripper.getText(document);
		return hits;
	}

}
