package com.ss.pdf;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class PDFReader {

	public static void main(String[] args) {

		// TODO, add your application code
		System.out.println("Lecteur PDF");
		System.out
				.println(ReadPDF("C:\\sivansasidharan\\D\\Workspace\\Eclipse\\MES_Analytics\\PDFTextExtractor\\"
						+ "LO Performance=4&5 TOTAL GROUP Report July2013-June2014.pdf"));
	}

	private static String ReadPDF(String pdf_url) {
		StringBuilder str = new StringBuilder();
		try {

			PdfReader reader = new PdfReader(pdf_url);
			int n = reader.getNumberOfPages();
			for (int i = 3; i < 4; i++) {
				String str2 = PdfTextExtractor.getTextFromPage(reader, i);
				str.append(str2);
				System.out.println(str);
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return String.format("%s", str);
	}
}
