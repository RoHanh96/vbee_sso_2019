package com.vbee.springbootmongodbnewspapersrestapi.model;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;

public class ArticleAdvertisement {
	
	private Article article;
	private Integer position;
	
	public ArticleAdvertisement() {}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
	
}
