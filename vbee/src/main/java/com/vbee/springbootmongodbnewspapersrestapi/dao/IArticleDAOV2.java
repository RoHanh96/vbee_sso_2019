package com.vbee.springbootmongodbnewspapersrestapi.dao;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;

public interface IArticleDAOV2 {

	List<Article> findArticleByCategoryIdsAndWebsiteIds(String categoryIds, String websiteIds, Integer page,
			Integer size, String fields);

}
