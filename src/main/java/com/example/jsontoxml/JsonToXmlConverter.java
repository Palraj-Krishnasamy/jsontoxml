package com.example.jsontoxml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.example.jsontoxml.exception.JsonToXmlBusinessException;
import com.example.jsontoxml.services.JsonToXmlServiceI;
import com.example.jsontoxml.services.impl.JsonToXmlServiceImpl;

public class JsonToXmlConverter {

	JsonToXmlServiceI toXmlService = null;
	


	public JsonToXmlConverter() {
		toXmlService = new JsonToXmlServiceImpl();
	}

	public static void main(String[] args) throws JsonToXmlBusinessException, IOException, ParserConfigurationException, TransformerException {
		if (args == null || args.length < 2) {
			throw new JsonToXmlBusinessException("Invalid Inputs");
			
		} else {
			JsonToXmlConverter converter = new JsonToXmlConverter();
			
			try {
				converter.convert(args[0], args[1]);
			} catch (FileNotFoundException e) {
				File xmlFile=new File(args[1]);
				xmlFile.createNewFile();
			}
		}
	}

	private void convert(String json, String xml) throws FileNotFoundException, ParserConfigurationException, TransformerException {
		File jsonFile=new File(json);
		File xmlFile=new File(xml);
		toXmlService.convertJsonToXml(jsonFile, xmlFile);
		
	}
	
	
}
