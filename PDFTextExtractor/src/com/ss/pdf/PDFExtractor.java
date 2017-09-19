package com.ss.pdf;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ss.pdf.search.PDFSearch;

public class PDFExtractor {

	private final static Logger logger = LoggerFactory.getLogger(PDFExtractor.class);

	public static void main(String args[]) {

		File file = new File(
				"C:\\sivansasidharan\\D\\Workspace\\Eclipse\\MES_Analytics\\PDFTextExtractor\\"
						+ "LO Performance=4&5 TOTAL GROUP Report July2013-June2014.pdf");

		PDDocument pddDocument = null;
		List<Integer> pageList = null;
		try {
			pddDocument = PDDocument.load(file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PDFSearch PDFSearch = new PDFSearch();
		{

			try {

				pageList = PDFSearch.search(pddDocument,
						"Leaving Group Profile");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info("pageList ======" + pageList);
		if (pageList.size() == 2) {


			PDFTableExtractor extractor = new PDFTableExtractor();

			// int[] omitLines = { 0, 1, 2 };

			List<Table> tables = extractor
					.setSource(
							"C:\\sivansasidharan\\D\\Workspace\\Eclipse\\MES_Analytics\\PDFTextExtractor\\"
									+ "LO Performance=4&5 TOTAL GROUP Report July2013-June2014.pdf")

					 .addPage(1)

					// .addPage(7)
					// .addPage(8) //BHP Page -8
					// .addPage(pageList.get(0) - 1) // QBE 7

					// .exceptLine(7, omitLines) // the first line in each page
					// .exceptLine(pageList.get(0) - 1, omitLines)
					// .exceptLine(9, omitLines)
					// .exceptLine(6, new int[1]) // the second line in each
					// page

					// .exceptLine(6,{-1}) ; // the last line in each page

					.extract();

			String html = tables.get(0).toHtml();// table in html format

			String csv = tables.get(0).toString();// table in csv format using
													// semicolon as a delimiter

			System.out.println(html);
			// System.out.println(csv);

			/*
			 * CSVUtils csvUtils = new CSVUtils(); try {
			 * 
			 * csvUtils.CSVFileWriter(csv);
			 * 
			 * } catch (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		}

	}

}
