/**
 *
 * @author Sivan.Sasidharan
 */
package com.ss.analytics.tools.dataconverter.core;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import jlibs.xml.sax.XMLDocument;
import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;
import org.apache.xerces.xs.XSModel;

public class XsdXmlConverter {

	private final Log logger = LogFactory.getLog(XsdXmlConverter.class);
	final String copyXMLFile = "copiedXML.xml";

	public void convertXSDToXML(String pXSDPath, String pXMLPath)
			throws Exception {

		File[] aGeneratedXSDs = new File(pXSDPath).listFiles();
		File aXMLPath = new File(pXMLPath);
		if (!aXMLPath.exists()) {
			if (aXMLPath.mkdir()) {
				logger.info(" Drectory Created @ " + aXMLPath.getAbsolutePath());
			} else {
				throw new Exception("Unable to Create Directory !!");
			}
		}
		// JLIB XSD Converter options
		XSInstance instance = new XSInstance();
		instance.generateOptionalElements = Boolean.TRUE;
		instance.generateOptionalAttributes = Boolean.TRUE;
		instance.generateFixedAttributes = Boolean.TRUE;
		instance.generateDefaultAttributes = Boolean.TRUE;
		QName root = new QName("Root");
		for (File aGeneratedXSD : aGeneratedXSDs) {
			if (aGeneratedXSD.isFile()
					&& aGeneratedXSD.getName().contains("xsd")) {
				XSModel xsModel = new XSParser().parse(aGeneratedXSD
						.getAbsolutePath());
				// Name the result
				XMLDocument aXMLDocumentGenerated = null;
				try {
					aXMLDocumentGenerated = new XMLDocument(new StreamResult(
							pXMLPath + "\\" + copyXMLFile), false, 4, null);
				} catch (TransformerConfigurationException ex) {
					logger.error("Configuration Exception in XSD TO XML Conversion "
							+ ex);
					throw new TransformerConfigurationException(
							"Configuration Exception in XSD TO XML Conversion");
				}
				try {
					instance.generate(xsModel, root, aXMLDocumentGenerated);
				} catch (IllegalArgumentException e) {
					throw new IllegalAccessException(
							"Exception in XSD TO XML Generation");
				}
			}
		}
	}
}
