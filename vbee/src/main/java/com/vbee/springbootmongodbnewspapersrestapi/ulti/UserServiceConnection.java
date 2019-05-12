package com.vbee.springbootmongodbnewspapersrestapi.ulti;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;
import com.vbee.springbootmongodbnewspapersrestapi.collections.User;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Website;

public class UserServiceConnection {

	public static void saveUser(User user, String userServicePath) throws MalformedURLException, IOException, ParseException {
		String url = userServicePath + "/api/v1/users";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// Setting basic put request
		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		// String data
		JSONObject data = new JSONObject();
		data.put("id", user.getId());
		JSONObject setting = new JSONObject();
		setting.put("websiteUnCheckIds", user.getWebsiteUnCheckIds().toString());
		setting.put("categoryUnCheckIds", user.getCategoryUnCheckIds().toString());
		data.put("setting", setting);
		String postJsonData = data.toString();
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postJsonData.getBytes("UTF-8"));
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("Sending 'PUT' request to URL : " + url);
		System.out.println("Put Data : " + postJsonData);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String output;
		StringBuffer response = new StringBuffer();

		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();

		// printing result from response
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(response.toString());
		long status = (long) json.get("status");
		if (status == 1) {
			System.out.println("save user for user service success !!!!");
		} else {
			System.out.println("save user for user service error !!!");
		}
	}
	
	public static void updateSettingDefault(Website website, Category category, String userServicePath, String userSettingDefaultVersion) throws MalformedURLException, IOException, ParseException {
		String url = null;
		if(website == null) {
			url = userServicePath + "/api/v1/settingDefaults/updateCategory";
		}else {
			url = userServicePath + "/api/v1/settingDefaults/updateWebsite";
		}
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// Setting basic put request
		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		// String data
		JSONObject data = new JSONObject();
		data.put("version", userSettingDefaultVersion);
		if (website == null) {
			JSONArray categories = new JSONArray();
			JSONObject categoryObj = new JSONObject();
			categoryObj.put("name", category.getName());
			categoryObj.put("id", category.getId());
			categories.add(categoryObj);
			data.put("categories", categories);
		}else {
			JSONArray websites = new JSONArray();
			JSONObject websiteObj = new JSONObject();
			websiteObj.put("name", website.getName());
			websiteObj.put("id", website.getId());
			websites.add(websiteObj);
			data.put("websites", websites);
		}
		String putJsonData = data.toString();
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(putJsonData.getBytes("UTF-8"));
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("Sending 'PUT' request to URL : " + url);
		System.out.println("Put Data : " + putJsonData);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String output;
		StringBuffer response = new StringBuffer();

		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();

		// printing result from response
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(response.toString());
		long status = (long) json.get("status");
		if (status == 1) {
			System.out.println("save user for user service success !!!!");
		} else {
			System.out.println("save user for user service error !!!");
		}
	}
}
