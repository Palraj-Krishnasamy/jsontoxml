package com.example.jsontoxml.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.example.jsontoxml.services.FileServiceI;

public class FileServiceImpl implements FileServiceI {

	@SuppressWarnings("resource")
	public String readFile(File input) throws IOException {
	try{	
	InputStream stream=new FileInputStream(input);
	byte[] readContent=new byte[stream.available()];
	stream.read(readContent);
	stream.close();
	
	return new String(readContent);//.replaceAll("null", "\"null\"");
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return null;
			
	}

	public void writeFile(File input,Document doc) throws IOException, TransformerException {
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        
        StreamResult result = new StreamResult(input);
        transformer.transform(source, result);
		
	}

}
