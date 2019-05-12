package com.vbee.springbootmongodbnewspapersrestapi.model;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;

public class ArticleSearchReponse {

	private List<Article> articles;
	private long totalResults;
	private long totalPages;
	private int maxAudio;
	public ArticleSearchReponse() {
		
	}
	
	public long getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}
	public List<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public int getMaxAudio() {
		return maxAudio;
	}

	public void setMaxAudio(int maxAudio) {
		this.maxAudio = maxAudio;
	}
}
