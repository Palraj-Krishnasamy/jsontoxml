package com.example.jsontoxml.services;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;

public interface FileServiceI {
	
	public String readFile(File input) throws IOException;
	public void writeFile(File input,Document doc) throws IOException, TransformerException;

}
