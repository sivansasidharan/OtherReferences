package com.ss.pdf;

import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;

public class CSVUtils {

	private final Logger logger = LoggerFactory.getLogger(CSVUtils.class);

	public void CSVFileWriter(String csvContent) throws IOException {

		String[] csvlines = csvContent.split("\n");

		CSVWriter writer = new CSVWriter(
				new FileWriter(
						"C:\\sivan\\workspace\\PDFTextExtractor\\src\\com\\ss\\pdf\\csvoutput.csv"),
				',');

		String[] colArray = null;
		String entry = "", lineItem = "", columnItem = "";
		int spaceLastIndex = 0, colArrayIndex = 0;
		for (String csvLine : csvlines) {

			logger.info("csvLine===" + csvLine);
			logger.info("colArray===" + colArray);
			colArrayIndex = 0;
			colArray = new String[10];

			String[] entries = csvLine.split(";");

			if (entries.length > 1) {

				logger.info("entries.length===" + entries.length);
				for (int ix = 0; ix < entries.length; ix++) {

					entry = entries[ix];
					if (ix == 0) {

						spaceLastIndex = entry.lastIndexOf(" ");
						lineItem = entry.substring(0, spaceLastIndex);
						columnItem = entry.substring(entry.length()
								- spaceLastIndex, entry.length());
						colArray[colArrayIndex] = lineItem;
						colArray[colArrayIndex + 1] = columnItem;
					} else {
						colArrayIndex++;
						colArray[colArrayIndex] = entry;
					}

					logger.info("entry===" + entry);

				}
			} else
				colArray[colArrayIndex] = entries[colArrayIndex];

			writer.writeNext(colArray);

		}

		writer.close();

	}

}
