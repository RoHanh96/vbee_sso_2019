package com.vbee.springbootmongodbnewspapersrestapi.ulti;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AuthenCookie {
	// public static boolean checkAuthen(String token, String authenServicePath){
	// try {
	//// String url = authenServicePath + "/check_authen";
	// String url = "https://maps.baonoivn.vn/check_authen";
	// URL obj = new URL(url);
	// HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	//
	// // Setting basic put request
	// con.setRequestMethod("GET");
	//// con.setRequestProperty("Authorization", "1234567");
	// // Send post request
	// con.setDoOutput(true);
	// DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	// wr.flush();
	// wr.close();
	//
	// int responseCode = con.getResponseCode();
	// System.out.println("Sending 'GET' request to URL : " + url);
	// System.out.println("Response Code : " + responseCode);
	//
	// BufferedReader in = new BufferedReader(new
	// InputStreamReader(con.getInputStream()));
	// String output;
	// StringBuffer response = new StringBuffer();
	//
	// while ((output = in.readLine()) != null) {
	// response.append(output);
	// }
	// in.close();
	//
	// // printing result from response
	// JSONParser parser = new JSONParser();
	// JSONObject json = (JSONObject) parser.parse(response.toString());
	// System.out.println(json);
	// if (responseCode == 200) {
	// System.out.println("check login success !!!!");
	// return true;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// System.out.println("Error in check Cookie ------AuthenCookie");
	// }
	// System.out.println("check login error !!!");
	// return false;
	// }

	public static boolean checkAuthen(String token, String authenServicePath) {
		try {
			URL url = new URL(authenServicePath + "/check_authen");
			URLConnection con = url.openConnection();
			con.setRequestProperty("Authorization", token);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
			in.close();
			// printing result from response
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(inputLine);
			long status = (long) json.get("status");
			if (status == 1) {
				System.out.println("check login success !!!!");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in check Cookie ------AuthenCookie");
		}
		System.out.println("check login error !!!");
		return false;
	}
}
