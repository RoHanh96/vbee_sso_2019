package com.vbee.springbootmongodbnewspapersrestapi.ulti;

import java.util.LinkedList;
import java.util.Queue;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.model.Config;
import com.vbee.springbootmongodbnewspapersrestapi.service.IArticleService;

public class QueueNSW {
	public static Queue<Article> queueArticleNSW = new LinkedList<Article>();
	public static IArticleService articleService;
	public static Config config;
	public static boolean canRun = true;

	public static void putArticleNSW(Article article, IArticleService articleService, Config config) {
		QueueNSW.articleService = articleService;
		QueueNSW.config = config;
		queueArticleNSW.offer(article);
	}

	public static void start() {
		System.out.println("size queue nsw: " + queueArticleNSW.size());
		if (!queueArticleNSW.isEmpty() && canRun) {
			canRun = false;
			Article article = queueArticleNSW.poll();
			if (article == null) {
				canRun = true;
				start();
			}else {
				System.out.println("NSW new article: " + article.getTitle());
				ArticleNswProcessing articleNswProcessing = new ArticleNswProcessing("articleId: " + article.getId(),
						article, articleService, config);
				articleNswProcessing.start();
			}

		}
	}
}
