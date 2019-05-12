package com.vbee.springbootmongodbnewspapersrestapi.service;

import javax.servlet.http.HttpServletRequest;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;

public interface ISynthesisService {

	void synthesizeArticle(Article article);

	void putArticleSynthesize(Article article, HttpServletRequest request);

	void start();

}
