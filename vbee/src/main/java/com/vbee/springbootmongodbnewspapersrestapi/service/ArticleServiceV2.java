package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Voice;
import com.vbee.springbootmongodbnewspapersrestapi.config.AppConstant;
import com.vbee.springbootmongodbnewspapersrestapi.dao.IArticleDAOV2;

@Service
public class ArticleServiceV2 implements IArticleServiceV2 {

	@Autowired
	IArticleDAOV2 articleDAOV2;

	@Override
	public List<Article> findArticleByCategoryIdsAndWebsiteIds(String categoryIds, String websiteIds, Integer page,
			Integer size, String fields) {
		return articleDAOV2.findArticleByCategoryIdsAndWebsiteIds(categoryIds, websiteIds, page, size, fields);
	}

	@Override
	public List<Article> getVoicesSelected(List<Article> articles, String voices) {
		if(voices == null || voices.isEmpty()) voices = AppConstant.VOICE_DEFAULT;
		String[] voiceArray = voices.split(",");
		List<String> voiceList = Arrays.asList(voiceArray); 
		for (Article article : articles) {
			JSONObject audios = new JSONObject();
			for (Voice voice : article.getVoices()) {
				if(voiceList.contains(voice.getName())) {
					JSONObject voiceJSON = new JSONObject();
					voiceJSON.put("content", voice.getContentAudioVirtualLink());
					voiceJSON.put("summary", voice.getSummaryAudioVirtualLink());
					audios.put(voice.getName(), voiceJSON);
				}
			}
			article.setAudios(audios);
			article.setVoices(null);
		}
		return articles;
	}

}
