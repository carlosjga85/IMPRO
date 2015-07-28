package com.ibm.cloudoe.samples;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XmlToJson {

	public static void main(String[] args) throws Exception {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();

		String path = "C:/Users/Carlos Jesus/Documents/Germany/TU-Berlin/Semestre 1/IMPRO/Nueva carpeta/";
		File dirXml = new File(path);
		String[] xmlfiles = dirXml.list();
		System.out.println(xmlfiles.length);

		System.out.print("{\"news\" : [ ");

		for (int i = 0; i < xmlfiles.length; i++) {
			if (xmlfiles[i].contains(".xml")) {
				 //System.out.println("i:" + i + "  " + xmlfiles[i]);

				String xmlFile = path + xmlfiles[i];
				// String xmlFile = "/projects/data/Reuters/100001newsML.xml";
				// BufferedReader inputStream = null;
				// inputStream = new BufferedReader(new FileReader(xmlFile));

				SAXHandlerXmlToJson handler = new SAXHandlerXmlToJson();
				parser.parse(new File(xmlFile), handler);

				// Printing the list of employees obtained from XML

				for (NewsText emp : handler.empList) {
					// System.out.println(emp);
					// System.out.print("\n{\n\"newsitem\":  {");
					/*System.out.print("\n{\n\"itemid\": \"" + emp.itemid
							+ "\",\n");
					System.out.print("\"Title\": \"" + emp.title + "\",\n");
					System.out.print("\"Headline\": \"" + emp.headline
							+ "\",\n");

					// NOTE: the text should be clean up in case it has " or
					// strange characters
					System.out.print("\"Text\": \"");
					for (String p : emp.pList) {
						System.out.print(p + " ");
					}

					System.out.print("\"\n},");
					*/
				}
			}
		}
		System.out.println("] }");
	}

}

class SAXHandlerXmlToJson extends DefaultHandler {
	 
	  List<NewsText> empList = new ArrayList<NewsText>();
	  NewsText news = null;
	  String content = null;
	  
	  
	  @Override
	  //Triggered when the start of tag is found.
	  public void startElement(String uri, String localName,
	                           String qName, Attributes attributes)
	                           throws SAXException {
	 
	    switch(qName){
	      //Create a new Entity object when the start tag is found
	    case "newsitem":
	        news = new NewsText();
	        news.itemid = attributes.getValue("itemid");
	        break;
	    case "text":
	    	news.pList = new ArrayList<String>();	
	    	break;
	    case "entities":
	    	news = new NewsText();
	    	news.entities = attributes.getValue("entities");
	    	break;
	    }
      }
	  
	  @Override
	  	  public void endElement(String uri, String localName,
	  	                         String qName) throws SAXException {
	  	   switch(qName){
	  	     case "newsitem":
	  	       empList.add(news);      
	  	       break;
	  	     //For all other end tags the employee has to be updated.
	  	     case "title":
	  	       news.title = content;
	  	       break;
	  	     case "headline":
	  	       news.headline = content;
	  	       break;
	  	     case "dateline":
	  	       news.dateline = content;
	  	       break;
	  	     case "text":
	  	       news.text = "";
	  	       break;
	  	     case "p": {
	  	       news.pList.add(content);	 
	  	       //news.paragraph = content;
	  	       //news.text += content + " ";
	  	       //System.out.println("text: " + news.text);
	  	     } break;
	  	   }
	  	  }
	  	 
	  	  @Override
	  	  public void characters(char[] ch, int start, int length)
	  	          throws SAXException {
	  	    content = String.copyValueOf(ch, start, length).trim();
	  	  }	  	
}
	  

