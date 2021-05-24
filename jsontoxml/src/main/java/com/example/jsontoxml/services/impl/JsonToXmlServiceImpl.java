package com.example.jsontoxml.services.impl;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.example.jsontoxml.common.JsonToXmlConverterConstants;
import com.example.jsontoxml.exception.JsonToXmlBusinessException;
import com.example.jsontoxml.services.FileServiceI;
import com.example.jsontoxml.services.JsonToXmlServiceI;

public class JsonToXmlServiceImpl implements JsonToXmlServiceI {

	FileServiceI fileService = null;

	public JsonToXmlServiceImpl() {
		fileService = new FileServiceImpl();
	}

	@SuppressWarnings("unchecked")
	public void convertJsonToXml(File json, File xml) throws ParserConfigurationException, TransformerException {
		try {

			findJsonObjectType(parseJson(fileService.readFile(json)), xml);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			new JsonToXmlBusinessException("Error While read the Json File" + e.getMessage());
		}

	}

	private JSONObject parseJson(String inputJson) {
		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(inputJson);

			return (JSONObject) obj;
		} catch (ParseException pe) {
			new JsonToXmlBusinessException("Error While Parsing the Json" + pe.getMessage());
		}

		return null;
	}

	private String findJsonObjectType(JSONObject jsonObject, File input)
			throws ParserConfigurationException, IOException, TransformerException {
		// XML Configuration
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();

		Element rootElement = doc.createElement("object");
		doc.appendChild(rootElement);

		for (Object key : jsonObject.keySet()) {
			if (null != key) {
				Object value = jsonObject.get(key);
				
				if (null != value) {
					String dataType = value.getClass().getSimpleName();

					if (dataType.equalsIgnoreCase("Integer") || dataType.equalsIgnoreCase("Long")
							|| dataType.equalsIgnoreCase("Double") || dataType.equalsIgnoreCase("Float")) {
						
					if (!key.toString().isEmpty())
							createTag(key, value, JsonToXmlConverterConstants.NUMBER, doc, rootElement);

					} else if (dataType.equalsIgnoreCase("Boolean")) {
						createTag(key, value, JsonToXmlConverterConstants.BOOLEAN, doc, rootElement);

					} else if (dataType.equalsIgnoreCase("String")) {
						createTag(key, value, JsonToXmlConverterConstants.STRING, doc, rootElement);

					} else if (dataType.equalsIgnoreCase("JSONArray") || dataType.equalsIgnoreCase("Array")) {
						Element arrayElement = doc.createElement("array");
						createArrayTag(value, doc, arrayElement);
						Attr attr = doc.createAttribute("name");
						attr.setValue(key.toString());
						arrayElement.setAttributeNode(attr);
						rootElement.appendChild(arrayElement);

					} else {
						Element objectElement = doc.createElement("object");
						createObjectTag(value, doc, objectElement);
						Attr attr = doc.createAttribute("name");
						attr.setValue(key.toString());
						objectElement.setAttributeNode(attr);
						rootElement.appendChild(objectElement);
					}

				}
			} else {
				/*null tag yet to be develop*/
			}
		}
		fileService.writeFile(input, doc);

		return null;
	}

	private void createTag(Object key, Object value, String objectType, Document doc, Element superElelement) {
		if (!key.toString().isEmpty()) {
			Element element = doc.createElement(objectType);
			Attr attr = doc.createAttribute("name");
			attr.setValue(key.toString());
			element.setAttributeNode(attr);
			element.appendChild(doc.createTextNode(value.toString()));
			superElelement.appendChild(element);

		} else {
			Element element = doc.createElement(objectType);
			element.appendChild(doc.createTextNode(value.toString()));
			superElelement.appendChild(element);

		}

	}

	private void createArrayTag(Object value, Document doc, Element arrayElement) {
		JSONArray arrayValues = (JSONArray) value;
		
		for (Object object : arrayValues) {
			JSONObject arrayObject = (JSONObject) object;
			
			for (Object arrayKey : arrayObject.keySet()) {
				Object arrayValue = arrayObject.get(arrayKey);
				
				if (!(arrayValue instanceof JSONArray)) {
					createTag(arrayKey, arrayValue, arrayValue.getClass().getSimpleName(), doc, arrayElement);
					
				} else {
					Element arrayInnerElement = doc.createElement("array");
					createArrayTag(arrayValue, doc, arrayInnerElement);
					Attr attr = doc.createAttribute("name");
					attr.setValue(arrayKey.toString());
					arrayInnerElement.setAttributeNode(attr);
					arrayElement.appendChild(arrayInnerElement);
					
				}

			}
		}

	}

	private void createObjectTag(Object value, Document doc, Element arrayElement) {
		JSONObject arrayObject = (JSONObject) value;
		
		for (Object arrayKey : arrayObject.keySet()) {
			Object arrayValue = arrayObject.get(arrayKey);
			
			if (!(arrayValue instanceof JSONObject)) {
				createTag(arrayKey, arrayValue, arrayValue.getClass().getSimpleName(), doc, arrayElement);
				
			} else {
				Element objectInnerElement = doc.createElement("object");
				createObjectTag(arrayValue, doc, objectInnerElement);
				Attr attr = doc.createAttribute("name");
				attr.setValue(arrayKey.toString());
				objectInnerElement.setAttributeNode(attr);
				arrayElement.appendChild(objectInnerElement);
				
			}
		}
	}

}
