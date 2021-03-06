package com.ibm.cloudoe.samples;

import java.util.List;

import com.google.gson.Gson;

public class JsonUtils {
	public static String getJson(NewsText n)
	{
		Gson gson = new Gson();
		String json = gson.toJson(n);
		return json;
	}
	
	/*public static String getFollowup(NewsText p)
	{
		Gson gson = new Gson();
		String json = gson.toJson(p.getFollowUp());
		return json;
	}*/
	
	public static String getListPersonJson(List<NewsText> news)
	{
		Gson gson = new Gson();
		String json = gson.toJson(news);
		return json;
	}
	
	public static NewsText getNewsFromJson(String json)
	{
		Gson gson = new Gson();
		NewsText n = gson.fromJson(json, NewsText.class);
		return n;
	}
	
	/*public static AddPersonRequest getAPRFromJson(String json)
	{
		Gson gson = new Gson();
		AddPersonRequest p = gson.fromJson(json, AddPersonRequest.class);
		return p;
	}*/
}
