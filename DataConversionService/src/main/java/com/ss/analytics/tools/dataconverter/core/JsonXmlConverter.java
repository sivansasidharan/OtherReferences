/**
 * @author Sivan.Sasidharan
 *
 */

package com.ss.analytics.tools.dataconverter.core;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.jaxb.JAXBModifier;
import com.ss.converter.model.Array;
import com.ss.converter.model.Member;
import com.ss.converter.model.Null;
import com.ss.converter.model.ObjectFactory;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javax.xml.bind.JAXB;

public class JsonXmlConverter {
	
	private final Log logger = LogFactory.getLog(JsonXmlConverter.class);
	
	public void convertJson( String pJsonString, String pXmlFilePath) {
		
		JAXB.marshal(convertJsonToFixedFormat(pJsonString), pXmlFilePath);
	}
	
	private JsonNode convertStringToJson( String JsonStr) {
		JsonFactory factory = new JsonFactory();
		JsonNode actualObj = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			actualObj = mapper.readValue(JsonStr, JsonNode.class);
			factory.createJsonParser(JsonStr);
		}
		catch (Exception e) {
			logger.error("Error in Converting String to JSON ", e);
		}
		return actualObj;
	}
	
	/**
	 * 
	 * Converts Json to XML in a fixed format.<p>
	 * <br>
	 * @param pJson - Json String Value 
	 * @return <code>Member</code>
	 * <code>{@link com.ss.converter.model.Member}</code>
	 * Member Class 
	 * </p>
	 */
	private Member convertJsonToFixedFormat( String pJson) {
		Member myMember = parseJsonNode(convertStringToJson(pJson));
		return myMember;
	}
	
	private Member parseArrayNode( JsonNode pArray) {
		ObjectFactory aFactoryObject = new ObjectFactory();
		Member aReturnMemberVal = aFactoryObject.createMember();
		Array aXMLArray = aFactoryObject.createArray();
		Iterator<JsonNode> aNodeIterator = pArray.elements();
		logger.debug("Array size = " + ( (ArrayNode) pArray ).size());
		while (aNodeIterator.hasNext()) {
			JsonNode element = aNodeIterator.next();
			aXMLArray.getValue().add(parseJsonNode(element).getValue());
		}
		
		JAXBElement<Array> aJaxbElementArr = aFactoryObject.createArray(aXMLArray);
		aReturnMemberVal.setValue(aJaxbElementArr);
		return aReturnMemberVal;
	}
	
	private Member parseBooleanNode( JsonNode pNumber) {
		ObjectFactory aFactoryObject = new ObjectFactory();
		Member aMember = aFactoryObject.createMember();
		JAXBElement<Boolean> aJaxbElement = aFactoryObject.createBoolean(pNumber.asBoolean());
		aMember.setValue(aJaxbElement);
		logger.debug("BooleanNode = " + pNumber.asBoolean());
		return aMember;
	}
	
	private Member parseDoubleNode( JsonNode pNumber) {
		ObjectFactory aFactoryObject = new ObjectFactory();
		Member aMember = aFactoryObject.createMember();
		JAXBElement<Double> aJaxbElement = aFactoryObject.createNumber(pNumber.asDouble());
		aMember.setValue(aJaxbElement);
		logger.debug("DoubleNode = " + pNumber.asDouble());
		return aMember;
	}
	
	private Member parseJsonNode( JsonNode pJsonNode) {
		ObjectFactory myFact = new ObjectFactory();
		Member aMember = myFact.createMember();
		if (pJsonNode != null) {
			if (pJsonNode.isArray()) {
				logger.debug("Node is an Array ");
				aMember.setValue(parseArrayNode(pJsonNode).getValue());
			}
			if (pJsonNode.isObject()) {
				logger.debug("Node is object");
				aMember.setValue(parseObjectNode(pJsonNode).getValue());
			}
			if (pJsonNode.isTextual()) {
				logger.debug("Node is textual");
				aMember.setValue(parseTextNode(pJsonNode).getValue());
			}
			if (pJsonNode.isNumber()) {
				logger.debug("Node is number");
				aMember.setValue(parseDoubleNode(pJsonNode).getValue());
			}
			if (pJsonNode.isBoolean()) {
				logger.debug("Node is boolean");
				aMember.setValue(parseBooleanNode(pJsonNode).getValue());
			}
			if (pJsonNode.isNull()) {
				logger.debug("Node is null");
				aMember.setValue(parseNullNode(pJsonNode).getValue());
			}
		}
		
		return aMember;
	}
	
	private Member parseNullNode( JsonNode pText) {
		ObjectFactory aFactoryObject = new ObjectFactory();
		Member aMember = aFactoryObject.createMember();
		JAXBElement<Null> aJaxbElement = aFactoryObject.createNull(new Null());
		aMember.setValue(aJaxbElement);
		logger.debug("NullNode");
		return aMember;
	}
	
	private Member parseObjectNode( JsonNode pObject) {
		ObjectFactory aFactoryObject = new ObjectFactory();
		Member aReturnMemberVal = aFactoryObject.createMember();
		com.ss.converter.model.Object aXMLObject = aFactoryObject.createObject();
		Iterator<Map.Entry<String,JsonNode>> aObjectFilelds = pObject.fields();
		while (aObjectFilelds.hasNext()) {
			Map.Entry<String,JsonNode> element = aObjectFilelds.next();
			logger.debug("Object name = " + element.getKey());
			Member aMemberElement = parseJsonNode(element.getValue());
			aMemberElement.setName(element.getKey());
			aXMLObject.getMember().add(aMemberElement);
		}
		JAXBElement<com.ss.converter.model.Object> aObjectXML = aFactoryObject.createObject(aXMLObject);
		aReturnMemberVal.setValue(aObjectXML);
		return aReturnMemberVal;
	}
	
	private Member parseTextNode( JsonNode pText) {
		ObjectFactory aFactoryObject = new ObjectFactory();
		Member aMember = aFactoryObject.createMember();
		JAXBElement<String> aJaxbElement = aFactoryObject.createString(pText.textValue());
		aMember.setValue(aJaxbElement);
		logger.debug("TextNode = " + pText.textValue());
		return aMember;
	}
	
}
