package com.ibm.cloudoe.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ParseEntities {

  public static void main(String[] args) throws Exception {
    SAXParserFactory parserFactor = SAXParserFactory.newInstance();
    SAXParser parser = parserFactor.newSAXParser();
    SAXHandler handler = new SAXHandler();
    
    String xmlFile = "/projects/DBPRO-IMPRO/ibm-watson/relation-extraction/re-java.git/response-example.xml";
    //BufferedReader inputStream = null;
    //inputStream = new BufferedReader(new FileReader(xmlFile));  
    
    
    parser.parse(new File(xmlFile), handler);

    //Printing the list of employees obtained from XML
    for ( Entity emp : handler.empList){
      System.out.println(emp);
    }
  }
}

class SAXHandler extends DefaultHandler {
	 
	  List<Entity> empList = new ArrayList<Entity>();
	  Entity en = null;
	  String content = null;
	  @Override
	  //Triggered when the start of tag is found.
	  public void startElement(String uri, String localName,
	                           String qName, Attributes attributes)
	                           throws SAXException {
	 
	    switch(qName){
	      //Create a new Entity object when the start tag is found
	      case "entity":
	        en = new Entity();
	        en.eid = attributes.getValue("eid");
	        en.classEntity = attributes.getValue("class");
	        en.levelEntity = attributes.getValue("level");
	        en.typeEntity = attributes.getValue("type");
	        break;
	    }
      }
	  
	  @Override
	  	  public void endElement(String uri, String localName,
	  	                         String qName) throws SAXException {
	  	   switch(qName){
	  	     //Add the employee to list once end tag is found
	  	     case "entity":
	  	       empList.add(en);      
	  	       break;
	  	     //For all other end tags the employee has to be updated.
	  	     case "class":
	  	       en.classEntity = content;
	  	       break;
	  	     case "level":
	  	       en.levelEntity = content;
	  	       break;
	  	     case "type":
	  	       en.typeEntity = content;
	  	       break;
	  	   }
	  	  }
	  	 
	  	  @Override
	  	  public void characters(char[] ch, int start, int length)
	  	          throws SAXException {
	  	    content = String.copyValueOf(ch, start, length).trim();
	  	  }	  	
}
	  
class Entity {
	 
	  String eid;
	  String typeEntity;
	  String classEntity;
	  String levelEntity;
	 
	  @Override
	  public String toString() {
	    return typeEntity + " " + classEntity + "(" + eid + ")" + levelEntity;
	  }
	}

