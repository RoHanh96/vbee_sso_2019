package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.ScheduleArticle;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleAdvertisement;

public interface IScheduleArticleService {

	List<ScheduleArticle> saveScheduleArticles(Article article, List<ScheduleArticle> scheduleArticles);

	List<ScheduleArticle> findByWeek(long startWeek, long endWeek, Integer categoryId, Integer articleId);

	List<ScheduleArticle> findByArticleId(Integer articleId);

	List<ArticleAdvertisement> findArticleByNow(Integer categoryId);

	ScheduleArticle findById(String id);

	void delete(ScheduleArticle scheduleArticle);

}
