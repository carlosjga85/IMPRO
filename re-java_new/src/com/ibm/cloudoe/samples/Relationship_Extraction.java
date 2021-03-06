package com.ibm.cloudoe.samples;


import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class Relationship_Extraction {
	
	private static Logger logger = Logger.getLogger(Relationship_Extraction.class.getName());
	private String serviceName = "relationship_extraction";
	
	// If running locally complete the variables below with the information in VCAP_SERVICES
	public static String baseURL = "https://gateway.watsonplatform.net/relationship-extraction-beta/api";
	public static String username = "dadfab39-cc48-45cd-a37a-c4b747e59b97";
	public static String password = "jcWM9PJkHFKc";
		
	/**
	 * Forward the request to the index.jsp file
	 *
	 * @param req the req
	 * @param resp the resp
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String analyze(String text, String sid) {
		//String text = req.getParameter("txt");
		//String sid = req.getParameter("sid");
		
	
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
			
			//Send question and answers to index.jsp
			//req.setAttribute("txt", text);
			//req.setAttribute("relationship", response);
    	    //System.out.println(response);
    	    return response;
			
		} catch (Exception e) {
			// Log something and return an error message
			logger.log(Level.SEVERE, "got error: "+e.getMessage(), e);
			System.out.println("got error: "+e.getMessage());
			//req.setAttribute("error", e.getMessage());
			return null;
		}
	}
	
	private void processVCAP_Services() {
    	logger.info("Processing VCAP_SERVICES");
        JSONObject sysEnv = getVcapServices();
        if (sysEnv == null) return;
        logger.info("Looking for: "+ serviceName );
        
        /*if (sysEnv.containsKey(serviceName)) {
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
        */
		//JSONObject sysEnv = getVcapServices();
    }
	
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
