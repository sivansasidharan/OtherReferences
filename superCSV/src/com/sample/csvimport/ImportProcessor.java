package com.sample.csvimport;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.supercsv.cellprocessor.constraint.StrNotNullOrEmpty;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

class ImportProcessor {
	static final CellProcessor[] gCELLProcessor_SET = new CellProcessor[] {
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(),
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(),
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(),
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(), null };

	static final CellProcessor[] gCELLProcessor_CRE = new CellProcessor[] {
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(),
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(),
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(), null, null, null };

	static final CellProcessor[] gCELLProcessor_DEL = new CellProcessor[] {
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(),
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(),
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(), null, null, null };

	static final CellProcessor[] gCELLProcessor_APRT = new CellProcessor[] {
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(),
			new StrNotNullOrEmpty(), null, null, null, null,
			new StrNotNullOrEmpty(), null };

	static final CellProcessor[] gCELLProcessor_ASRT = new CellProcessor[] {
			new StrNotNullOrEmpty(), new StrNotNullOrEmpty(),
			new StrNotNullOrEmpty(), null, null, null, null,
			new StrNotNullOrEmpty(), null };

	public static void main(String[] args) throws Exception {
		List<PAImportBean> aPaSETOperations = new ArrayList<PAImportBean>();
		List<PAImportBean> aPaCREOperations = new ArrayList<PAImportBean>();
		List<PAImportBean> aPaDELOperations = new ArrayList<PAImportBean>();
		List<PAImportBean> aPaAPRTOperations = new ArrayList<PAImportBean>();
		List<PAImportBean> aPaASRTOperations = new ArrayList<PAImportBean>();

		try {
			getSEToperations(aPaSETOperations);
			getCREoperations(aPaCREOperations);
			getDELoperations(aPaDELOperations);
			getAPRToperations(aPaAPRTOperations);
			getASRToperations(aPaASRTOperations);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Complete Operations Defined In the Excel :: \n"
					+ "\n SET Operations :: \n" + aPaSETOperations
					+ "\n CRE Operations :: \n" + aPaCREOperations
					+ "\n DEL Operations :: \n" + aPaDELOperations
					+ "\n APRT Operations :: \n" + aPaAPRTOperations
					+ "\n ASRT Operations :: \n" + aPaASRTOperations);

		}
	}

	private static List<PAImportBean> getSEToperations(
			List<PAImportBean> aPaSETOperations) throws IOException {

		List<String> improperSEToperations = new ArrayList<String>();
		PAImportBean aPAimport = null;
		ICsvBeanReader inFile = loadCSVFile();
		final String[] header = inFile.getCSVHeader(true);
		while (true) {
			try {
				while ((aPAimport = inFile.read(PAImportBean.class, header,
						gCELLProcessor_SET)) != null) {
					if (aPAimport.getOperation().equalsIgnoreCase("SET")) {
						aPaSETOperations.add(aPAimport);
					}
				}

			} catch (SuperCSVException e) {
				String[] valueNotSet = e.toString().split(":");
				if (valueNotSet[4].split(",")[2].equalsIgnoreCase(" SET"))
					improperSEToperations.add(valueNotSet[4].split("]")[0]);
				continue;
			}
			System.out.println("Improper Set Operations - "
					+ improperSEToperations);
			closeStream(inFile);
			return aPaSETOperations;
		}

	}

	private static List<PAImportBean> getCREoperations(
			List<PAImportBean> aPaCREOperations) throws IOException {

		List<String> improperCREoperations = new ArrayList<String>();
		ICsvBeanReader inFile = loadCSVFile();
		final String[] header = inFile.getCSVHeader(true);
		PAImportBean aPAimport;
		while (true) {
			try {
				while ((aPAimport = inFile.read(PAImportBean.class, header,
						gCELLProcessor_CRE)) != null) {
					if (aPAimport.getOperation().equalsIgnoreCase("CRE")) {
						aPaCREOperations.add(aPAimport);
					}
				}
			} catch (SuperCSVException e) {
				String[] valueNotSet = e.toString().split(":");
				if (valueNotSet[4].split(",")[2].equalsIgnoreCase(" SET"))
					improperCREoperations.add(valueNotSet[4].split("]")[0]);
				continue;
			}
			System.out.println("Improper CRE Operations - "
					+ improperCREoperations);
			closeStream(inFile);
			return aPaCREOperations;
		}
	}

	private static List<PAImportBean> getDELoperations(
			List<PAImportBean> aPaDELOperations) throws IOException {

		List<String> improperDELoperations = new ArrayList<String>();
		ICsvBeanReader inFile = loadCSVFile();
		final String[] header = inFile.getCSVHeader(true);
		PAImportBean aPAimport;
		while (true) {
			try {
				while ((aPAimport = inFile.read(PAImportBean.class, header,
						gCELLProcessor_DEL)) != null) {
					if (aPAimport.getOperation().equalsIgnoreCase("DEL")) {
						aPaDELOperations.add(aPAimport);
					}
				}
			} catch (SuperCSVException e) {
				String[] valueNotSet = e.toString().split(":");
				if (valueNotSet[4].split(",")[2].equalsIgnoreCase(" DEL"))
					improperDELoperations.add(valueNotSet[4].split("]")[0]);
				continue;
			}
			System.out.println("Improper DEL Operations - "
					+ improperDELoperations);
			closeStream(inFile);
			return aPaDELOperations;
		}
	}

	private static List<PAImportBean> getAPRToperations(
			List<PAImportBean> aPaAPRTOperations) throws IOException {

		List<String> improperAPRToperations = new ArrayList<String>();
		ICsvBeanReader inFile = loadCSVFile();
		final String[] header = inFile.getCSVHeader(true);
		PAImportBean aPAimport;
		while (true) {
			try {
				while ((aPAimport = inFile.read(PAImportBean.class, header,
						gCELLProcessor_APRT)) != null) {
					if (aPAimport.getOperation().equalsIgnoreCase("APRT")) {
						aPaAPRTOperations.add(aPAimport);
					}
				}
			} catch (SuperCSVException e) {
				String[] valueNotSet = e.toString().split(":");
				if (valueNotSet[4].split(",")[2].equalsIgnoreCase(" APRT"))
					improperAPRToperations.add(valueNotSet[4].split("]")[0]);
				continue;
			}
			System.out.println("Improper APRT Operations - "
					+ improperAPRToperations);
			closeStream(inFile);
			return aPaAPRTOperations;
		}
	}

	private static List<PAImportBean> getASRToperations(
			List<PAImportBean> aPaASRTOperations) throws IOException {

		List<String> improperASRToperations = new ArrayList<String>();
		ICsvBeanReader inFile = loadCSVFile();
		final String[] header = inFile.getCSVHeader(true);
		PAImportBean aPAimport;
		while (true) {
			try {
				while ((aPAimport = inFile.read(PAImportBean.class, header,
						gCELLProcessor_ASRT)) != null) {
					if (aPAimport.getOperation().equalsIgnoreCase("ASRT")) {
						aPaASRTOperations.add(aPAimport);
					}
				}
			} catch (SuperCSVException e) {
				String[] valueNotSet = e.toString().split(":");
				if (valueNotSet[4].split(",")[2].equalsIgnoreCase(" ASRT"))
					improperASRToperations.add(valueNotSet[4].split("]")[0]);
				continue;
			}
			System.out.println("Improper ASRT Operations - "
					+ improperASRToperations);
			closeStream(inFile);
			return aPaASRTOperations;
		}
	}

	private static ICsvBeanReader loadCSVFile() {

		ICsvBeanReader aInFile = null;
		try {
			aInFile = new CsvBeanReader(new FileReader(
					"E:\\Sivan\\NetBeans-WorkSpace\\superCSV//Sample_import.csv"),
					CsvPreference.EXCEL_PREFERENCE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return aInFile;
	}

	private static void closeStream(ICsvBeanReader aInFile) throws IOException {
		aInFile.close();
	}
}
