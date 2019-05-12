package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;

public interface IArticleServiceV2 {

	List<Article> findArticleByCategoryIdsAndWebsiteIds(String categoryIds, String websiteIds, Integer page, Integer size, String fields);

	List<Article> getVoicesSelected(List<Article> articles, String voices);

}
