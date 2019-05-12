package com.vbee.springbootmongodbnewspapersrestapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.model.Config;
import com.vbee.springbootmongodbnewspapersrestapi.model.ResponseMessage;
import com.vbee.springbootmongodbnewspapersrestapi.service.IArticleServiceV2;

@RestController
@RequestMapping("/api/v2/articles")
public class ArticleControllerV2 {

	private static final Logger logger = LoggerFactory.getLogger(ArticleControllerV2.class);

	@Autowired
	IArticleServiceV2 articleServiceV2;

	@Autowired
	Config configProperties;

	@GetMapping()
	public ResponseEntity<ResponseMessage> getArticlesByCategoryIdAndWebsiteId(String categoryIds, String websiteIds,
			Integer page, Integer size, String fields, String voices,
			@RequestHeader(value = "accessToken", required = false) String accessToken) {
		ResponseMessage resMessage = new ResponseMessage();
		if (accessToken == null || !accessToken.equals(configProperties.getAccessTokenCRM())) {
			resMessage.setMessage("Required accessToken");
			return new ResponseEntity<ResponseMessage>(resMessage, HttpStatus.FORBIDDEN);
		}
		List<Article> articles = articleServiceV2.findArticleByCategoryIdsAndWebsiteIds(categoryIds, websiteIds, page,
				size, fields);
		articles = articleServiceV2.getVoicesSelected(articles, voices);
		resMessage.setMessage("Found: " + articles.size() + " articles");
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}
}
