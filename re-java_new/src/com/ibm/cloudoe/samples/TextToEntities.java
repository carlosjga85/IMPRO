package com.ibm.cloudoe.samples;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class TextToEntities {
	
	private static Logger logger = Logger.getLogger(DemoServlet.class.getName());
	private static final long serialVersionUID = 1L;

	static String serviceName = "relationship_extraction";
	
    // If running locally complete the variables below with the information in VCAP_SERVICES
    static String baseURL = "https://gateway.watsonplatform.net/relationship-extraction-beta/api";
    static String username = "14237d33-bcd7-410d-a19a-2212f4d86e71";
    static String password = "9c0UOWKHfLft";

		
	public static void main(String[] args) throws Exception {
	
		TextToEntities  tte = new TextToEntities();
		tte.processVCAP_Services();
		
		
		//req.setCharacterEncoding("UTF-8");
		//String text = "Philip Johnston, who learned Friday while campaigning with first lady Hillary Clinton that a state court ruled his opponent, William Delahunt, won the Sept 17 Democratic primary, filed suit today with the Supreme Judicial Court of Massachusetts.";
		String text ="Eurozone finance ministers say they expect to hear new proposals from Greece after the country voted to reject the terms of a bailout.";
		String sid = "ie-en-news";  //ie-en-news" English News   "ie-es-news"  Spanish News
		
		
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("txt",text ));
		qparams.add(new BasicNameValuePair("sid",sid ));
		qparams.add(new BasicNameValuePair("rt","xml" ));
    	
    	try {
    		Executor executor = Executor.newInstance().auth(username, password);
    		URI serviceURI = new URI(baseURL).normalize();
    	    String auth = username + ":" + password;
    	    byte[] responseB = executor.execute(Request.Post(serviceURI)
			    .addHeader("Authorization", "Basic "+ Base64.encodeBase64String(auth.getBytes()))
			    .bodyString(URLEncodedUtils.format(qparams, "utf-8"), 
			    		ContentType.APPLICATION_FORM_URLENCODED)
			    ).returnContent().asBytes();

    	    String response = new String (responseB,"UTF-8");
    	    
    	    System.out.println(response);
			
    	    // Parse the response
    	    
    	    SAXParserFactory parserFactor = SAXParserFactory.newInstance();
    	    SAXParser parser = parserFactor.newSAXParser();
    	    SAXHandlerEntity handler = new SAXHandlerEntity();
    	    
    	    parser.parse(new InputSource(new StringReader(response)), handler);
    	    	 
    	    //Printing the list of entities obtained from XML
    	    System.out.println("CLASS LEVEL TYPE TXT-REF ");
    	    String entities = "";
    	    for ( Entity2 en : handler.empList){
    	       System.out.println(en); 	    	
    	    }
    	        	    		
			
		} catch (Exception e) {
			// Log something and return an error message
			logger.log(Level.SEVERE, "got error: "+e.getMessage(), e);
			//req.setAttribute("error", e.getMessage());
		}
	
	}
	

    /**
     * If exists, process the VCAP_SERVICES environment variable in order to get the 
     * username, password and baseURL
     */
    private void processVCAP_Services() {
    	logger.info("Processing VCAP_SERVICES");
        JSONObject sysEnv = getVcapServices();
        if (sysEnv == null) return;
        logger.info("Looking for: "+ serviceName );
        
        if (sysEnv.containsKey(serviceName)) {
			JSONArray services = (JSONArray)sysEnv.get(serviceName);
			JSONObject service = (JSONObject)services.get(0);
			JSONObject credentials = (JSONObject)service.get("credentials");
			baseURL = (String)credentials.get("url");
			username = (String)credentials.get("username");
			password = (String)credentials.get("password");
			logger.info("baseURL  = "+baseURL);
			logger.info("username   = "+username);
			logger.info("password = "+password);
    	} else {
        	logger.warning(serviceName + " is not available in VCAP_SERVICES, "
        			+ "please bind the service to your application");
        }
    }

    /**
     * Gets the <b>VCAP_SERVICES</b> environment variable and return it
     *  as a JSONObject.
     *
     * @return the VCAP_SERVICES as Json
     */
    private JSONObject getVcapServices() {
        String envServices = System.getenv("VCAP_SERVICES");
        if (envServices == null) return null;
        JSONObject sysEnv = null;
        try {
        	 sysEnv = JSONObject.parse(envServices);
        } catch (IOException e) {
        	// Do nothing, fall through to defaults
        	logger.log(Level.SEVERE, "Error parsing VCAP_SERVICES: "+e.getMessage(), e);
        }
        return sysEnv;
    }
    

}	
	
class SAXHandlerEntity extends DefaultHandler {
		 
		  List<Entity2> empList = new ArrayList<Entity2>();
		  Entity2 en = null;
		  String content = null;
		  @Override
		  //Triggered when the start of tag is found.
		  public void startElement(String uri, String localName,
		                           String qName, Attributes attributes)
		                           throws SAXException {
		 
		    switch(qName){
		      //Create a new Entity object when the start tag is found
		      case "entity":
		        en = new Entity2();
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
		  	     case "mentref":
		  	       en.nameEntity = content;
		  	       break;		  	     
		  	   }
		  	  }
		  	 
		  	  @Override
		  	  public void characters(char[] ch, int start, int length)
		  	          throws SAXException {
		  	    content = String.copyValueOf(ch, start, length).trim();
		  	  }	  
		  	  
		  	  
	}
		  

class Entity2 {		 
		  String eid;
		  String typeEntity;
		  String classEntity;
		  String levelEntity;
		  String nameEntity;
		 
		  @Override
		  public String toString() {
		    //return typeEntity + " " + classEntity + "(" + eid + ")" + levelEntity;
			  return typeEntity + "  " + classEntity  + "  " + levelEntity + "  Text:\"" + nameEntity + "\"";
		  }
}
	

