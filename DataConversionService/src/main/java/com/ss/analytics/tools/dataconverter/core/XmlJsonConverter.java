/**
 * @author Sivan.Sasidharan
 *
 */

package com.ss.analytics.tools.dataconverter.core;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.collections.SequencedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlJsonConverter {
	
	class DataTextListener implements XmlJsonListener {
		
		public String readText( String text) throws Exception {
			return text;
		}
	}
	
	private String basePath;
	
	private XmlJsonListener internalListner = new DataTextListener();
	
	private final List<String> keyPaths = new ArrayList<String>();
	
	private final Map<String,String> namespaceResolvers = new HashMap<String,String>();
	
	private final Map<String,String> pathMaps = new HashMap<String,String>();
	
	private final List<String> repeatingValues = new ArrayList<String>();
	
	private final List<String> singleTypes = new ArrayList<String>();
	
	private final List<String> skips = new ArrayList<String>();
	
	private final List<String> xPathArrays = new ArrayList<String>();
	
	public void addArrayPath( String pXpath) {
		xPathArrays.add(pXpath);
	}
	
	public void addNamespaceResolver( String pPrefix, String pUri) {
		namespaceResolvers.put(pUri, pPrefix);
	}
	
	public void addPathRule( String pXpath, String pKeyAttrName, boolean isRepeatable, boolean isSingle) {
		if (pKeyAttrName != null) {
			keyPaths.add(pXpath);
			pathMaps.put(pXpath, pKeyAttrName);
		}
		if (isRepeatable) {
			repeatingValues.add(pXpath);
		}
		if (isSingle) {
			singleTypes.add(pXpath);
		}
	}
	
	public void addSkipRule( String pXpath) {
		skips.add(pXpath);
	}
	
	private String getTagName( Element pElement) {
		String aLocalName = pElement.getLocalName();
		if (aLocalName == null) {
			aLocalName = pElement.getNodeName();
		}
		return aLocalName;
	}
	
	private String getXPath( Element pElement) {
		if (pElement == null) { return null; }
		StringBuffer aXpath = new StringBuffer();
		aXpath.append("/");
		String aURI = pElement.getNamespaceURI();
		String aPrefix = namespaceResolvers.get(aURI);
		if (aPrefix != null) {
			aXpath.append(aPrefix).append(":");
		}
		aXpath.append(getTagName(pElement));
		Element aParentElement = pElement;
		try {
			while (true) {
				aParentElement = (Element) aParentElement.getParentNode();
				if (aParentElement == null) {
					break;
				}
				aXpath.insert(0, getTagName(aParentElement));
				aURI = aParentElement.getNamespaceURI();
				aPrefix = namespaceResolvers.get(aURI);
				if (aPrefix != null) {
					aXpath.insert(0, aPrefix + ":");
				}
				aXpath.insert(0, "/");
			}
		}
		catch (ClassCastException e) {
			
		}
		String aXpathString = aXpath.toString();
		if (basePath != null) {
			aXpathString = aXpathString.replaceFirst("^" + basePath, "");
		}
		return aXpathString;
	}
	
	private Object nodeJsonMapping( Element pElement) throws Exception {
		//Map<String,Object> aSeqMap = new SequencedHashMap();
                Multimap<String, Object> aSeqMap = ArrayListMultimap.create();
		String aXpath = getXPath(pElement);
		if (singleTypes.contains(aXpath)) {
			if (pElement.getFirstChild() != null) {
				return internalListner.readText(pElement.getFirstChild().getNodeValue());
			}
			else {
				return "";
			}
		}
		NamedNodeMap aNodeMapAttributes = pElement.getAttributes();
		for (int i = 0; i < aNodeMapAttributes.getLength(); i++) {
			Node aNodeAttribute = aNodeMapAttributes.item(i);
			String aNodeName = aNodeAttribute.getNodeName();
			String aNodeValue = aNodeAttribute.getNodeValue();
			aSeqMap.put(aNodeName, internalListner.readText(aNodeValue));
		}
		NodeList aChildNodes = pElement.getChildNodes();
		nodeListJsonMapping(aSeqMap, aChildNodes);
		return new JSONObject(aSeqMap.asMap());
	}
	
	private void nodeListJsonMapping( Multimap<String,Object> pMapData, NodeList pNodes) throws Exception {
		for (int i = 0; i < pNodes.getLength(); i++) {
			Node aNodeItem = pNodes.item(i);
			switch (aNodeItem.getNodeType()) {
				case Node.TEXT_NODE:
				case Node.CDATA_SECTION_NODE:
					String aTextValue = aNodeItem.getNodeValue().trim();
					if (aTextValue.length() > 0) {
						pMapData.put("content", internalListner.readText(aNodeItem.getNodeValue()));
					}
					break;
				case Node.ELEMENT_NODE:
					Element aChildElement = (Element) aNodeItem;
					String aChildXPath = getXPath(aChildElement);
					if (skips.contains(aChildXPath)) {
						nodeListJsonMapping(pMapData, aChildElement.getChildNodes());
					}
					else if (xPathArrays.contains(aChildXPath)) {
						JSONArray aChildArrayobjects = (JSONArray) pMapData.get(aChildElement.getNodeName());
						if (aChildArrayobjects == null) {
							aChildArrayobjects = new JSONArray();
							pMapData.put(aChildElement.getNodeName(), aChildArrayobjects);
						}
						JSONArray aJsonArray = new JSONArray();
						NodeList childNodes = aChildElement.getChildNodes();
						for (int j = 0; j < childNodes.getLength(); j++) {
							Node aChildNodeItem = childNodes.item(j);
							
							if (aChildNodeItem.getNodeType() != Node.ELEMENT_NODE) {
								continue;
							}//TODO need to support the other node types.
							aJsonArray.put(nodeJsonMapping((Element) aChildNodeItem));
						}
						aChildArrayobjects.put(aJsonArray);
					}
					else {
						String aChildNodeName = aChildElement.getNodeName();
						boolean isRepeatable = repeatingValues.contains(aChildXPath);
						boolean hasKey = keyPaths.contains(aChildXPath);
						if (isRepeatable && hasKey) {
							JSONObject aJsonChildObject = (JSONObject) pMapData.get(aChildNodeName);
							if (aJsonChildObject == null) {
								aJsonChildObject = new JSONObject();
								pMapData.put(aChildNodeName, aJsonChildObject);
							}
							String aAttributeName = pathMaps.get(aChildXPath);
							String aAttributeValue = aChildElement.getAttribute(aAttributeName);
							aJsonChildObject.put(aAttributeValue, nodeJsonMapping(aChildElement));
						}
						else if (isRepeatable && !hasKey) {
							JSONArray aJsonArrayChildObject = (JSONArray) pMapData.get(aChildNodeName);
							if (aJsonArrayChildObject == null) {
								aJsonArrayChildObject = new JSONArray();
								pMapData.put(aChildNodeName, aJsonArrayChildObject);
							}
							aJsonArrayChildObject.put(nodeJsonMapping(aChildElement));
						}
						else if (hasKey) {
							String aAttributeName = pathMaps.get(aChildXPath);
							String aAttributeValue = aChildElement.getAttribute(aAttributeName);
							pMapData.put(aAttributeValue, nodeJsonMapping(aChildElement));
						}
						else {
							pMapData.put(aChildNodeName, nodeJsonMapping(aChildElement));
						}
					}
					break;
				default:
					break;
			}
		}
	}
	
	public void setListner( XmlJsonListener pTextFilter) {
		internalListner = pTextFilter;
	}
	
	public String xmltoJsonConverter( String pXML) throws Exception {
		DocumentBuilder aDocumnetBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document aDocument = aDocumnetBuilder.parse(new InputSource(new StringReader(pXML)));
		Element aRootElement = aDocument.getDocumentElement();
		return xmltoJsonElementConverter(aRootElement);
	}
	
	public String xmltoJsonElementConverter( Element pElement) throws Exception {
		JSONObject aJsonObject = xmltoJsonObjectElement(pElement);
		return aJsonObject.toString(1);
	}
	
	public JSONObject xmltoJsonObjectElement( Element pElement) throws Exception {
		basePath = null;
		Node aBaseNode = pElement.getParentNode();
		if ( ( aBaseNode != null ) && ( aBaseNode.getNodeType() == Node.ELEMENT_NODE )) {
			basePath = getXPath((Element) aBaseNode);
		}
		JSONObject aJsonObject = (JSONObject) nodeJsonMapping(pElement);
		return aJsonObject;
	}
	
	/*public JSONObject xmltoJsonObjectNodeListConverter( NodeList pNodes) throws Exception {
		basePath = null;
		if ( ( pNodes == null ) || ( pNodes.getLength() == 0 )) { return null; }
		Node aBaseNode = pNodes.item(0).getParentNode();
		if (aBaseNode == null) { return null; }
		basePath = getXPath((Element) aBaseNode);
		Map<String,Object> aMap = new SequencedHashMap();
		nodeListJsonMapping(aMap, pNodes);
		return new JSONObject(aMap);
	}
	
	public String xmltoJsonStringNodeListConverter( NodeList pNodes) throws Exception {
		JSONObject aJsonObject = xmltoJsonObjectNodeListConverter(pNodes);
		if (aJsonObject == null) { return ""; }
		return aJsonObject.toString(1);
	}*/
	
}
