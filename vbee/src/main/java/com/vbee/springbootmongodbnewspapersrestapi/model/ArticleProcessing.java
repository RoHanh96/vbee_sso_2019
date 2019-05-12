package com.vbee.springbootmongodbnewspapersrestapi.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Tag;

public class ArticleProcessing {
	
	private static final Logger logger = LoggerFactory.getLogger(ArticleProcessing.class);
	
	public static String prepareSynthesize(String value, String content) {
		try {
			JSONObject json = new JSONObject(value);
			JSONArray words = json.getJSONArray("words");
			for (int i = 0; i < words.length(); i++) {
				JSONObject word = new JSONObject(words.get(i).toString());
				if (word.getString("status").equals("checked")) {
					String keyword = word.getString("key");
					JSONArray expandations = word.getJSONArray("expandations");
					for (int j = 0; j < expandations.length(); j++) {
						JSONObject context = new JSONObject(expandations.get(j).toString());
						String expandation = context.getString("expandation");
//						String contextContentOrigin = context.getString("context");
//						String contextContentReplace = context.getString("context").replaceFirst(keyword, expandation);
 						content = content.replaceFirst(keyword, expandation);
					}
				}
			}
		} catch (JSONException ex) {
			logger.info("Error when replacePreviewItem !!! ------ " + ex.getMessage());
		}
		return content;
	}

	public static String processingContent(String content) {
		content = content.replaceAll("<p>", "").replaceAll("</p>", ". ");
		// remove all HTML entities
		content = content.replaceAll("&.*?;", "");
		// //remove all HTML tags
		content = content.replaceAll("<[^>]*>", "");
		return content;
	}
	
	public static int countNumDoubtOfPreviewValue(Article article) {
		int countNumDoubt = 0;
		try {
			JSONObject json = new JSONObject(article.getPreviewValue());
			JSONArray words = json.getJSONArray("words");
			for (int i = 0; i < words.length(); i++) {
				JSONObject word = new JSONObject(words.get(i).toString());
				if (word.getString("status").equals("unchecked")) {
					if(word.getBoolean("doubt")) {
						countNumDoubt ++;
					}
				}
				
			}
		} catch (JSONException ex) {
			logger.info("Error when countNumDoubtOfPreviewValue --- article id: " + article.getId() + " !!! ------ " + ex.getMessage());
		}
		
		return countNumDoubt;
	}

	public static String getTags(Article articleExists) {
		StringBuilder tags = new StringBuilder();
		for (Tag tag : articleExists.getTags()) {
			tags.append(tag.getName() + ", "); 
		}
		return tags.toString();
	} 
}
