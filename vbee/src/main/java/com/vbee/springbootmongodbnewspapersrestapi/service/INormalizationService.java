package com.vbee.springbootmongodbnewspapersrestapi.service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;

public interface INormalizationService {
	void putArticleNSW(Article article);

	void start();

	void reset();

	void setRun();
}
