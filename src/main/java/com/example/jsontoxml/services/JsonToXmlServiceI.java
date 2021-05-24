package com.example.jsontoxml.services;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public interface JsonToXmlServiceI {
	
	public void convertJsonToXml(File json, File xml) throws ParserConfigurationException, TransformerException;

}
