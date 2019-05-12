package com.vbee.springbootmongodbnewspapersrestapi.ulti;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;

public class StatisticsServiceConnection {

	public static void saveArticleStaistics(String userId, Article article, String voiceName, Integer duration,
			Integer listen, String statisticsServicePath) throws MalformedURLException, IOException, ParseException {
		String url = statisticsServicePath + "/api/v1/article-histories";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// Setting basic post request
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		// String data
		JSONObject data = new JSONObject();
		data.put("articleId", article.getId());
		data.put("articleTitle", article.getTitle());
		data.put("categoryId", article.getCategory().getId());
		data.put("categoryName", article.getCategory().getName());
		data.put("websiteId", article.getWebsiteId());
		data.put("websiteName", article.getWebsiteName());
		JSONArray articleHistoryVoices = new JSONArray();
		JSONObject articleHistoryVoice = new JSONObject();
		articleHistoryVoice.put("duration", duration);
		articleHistoryVoice.put("listen", listen);
		articleHistoryVoice.put("articleId", article.getId());
		JSONObject voice = new JSONObject();
		voice.put("name", voiceName);
		articleHistoryVoice.put("voice", voice);
		articleHistoryVoices.add(articleHistoryVoice);
		data.put("articleHistoryVoices", articleHistoryVoices);
		String postJsonData = data.toString();
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postJsonData.getBytes("UTF-8"));
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("Sending 'POST' request to URL : " + url);
		System.out.println("Post Data : " + postJsonData);
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
			System.out.println("save statistic article for statistics service success !!!!");
		} else {
			System.out.println("save statistic article for statistics service error !!!");
		}
	}

}
