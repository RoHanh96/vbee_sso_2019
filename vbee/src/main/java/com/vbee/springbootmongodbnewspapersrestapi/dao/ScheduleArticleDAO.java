package com.vbee.springbootmongodbnewspapersrestapi.dao;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.ScheduleArticle;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleAdvertisement;

@Repository
public class ScheduleArticleDAO implements IScheduleArticleDAO{

	private final MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(ScheduleArticleDAO.class);

	@Autowired
	ScheduleArticleDAO(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public List<ScheduleArticle> findByWeekAndCategory(long startWeek, long endWeek, Integer categoryId, Integer articleId) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("start").gt(startWeek));
		query.addCriteria(Criteria.where("end").lt(endWeek));
		query.addCriteria(Criteria.where("categoryId").is(categoryId));
		if(articleId != null && articleId != 0) {
			query.addCriteria(Criteria.where("articleId").is(articleId));
		}
		
		List<ScheduleArticle> scheduleArticles = mongoTemplate.find(query, ScheduleArticle.class);
		return scheduleArticles;
	}

	@Override
	public List<ArticleAdvertisement> findArticleByNow(Integer categoryId) {
		Query query = new Query();
		List<ArticleAdvertisement> advertisements = new ArrayList<>();
		long now = System.currentTimeMillis();
		query.addCriteria(Criteria.where("start").lte(now));
		query.addCriteria(Criteria.where("end").gte(now));
		query.addCriteria(Criteria.where("categoryId").is(categoryId));
		query.with(new Sort(Sort.DEFAULT_DIRECTION.ASC, "position"));
		List<ScheduleArticle> scheduleArticles = mongoTemplate.find(query, ScheduleArticle.class);
		System.out.println("Query advertisement: " + scheduleArticles.size());
		if(scheduleArticles.isEmpty()) {
			advertisements = null;
			System.gc();
			return null;
		}else {
			for (ScheduleArticle scheduleArticle : scheduleArticles) {
				ArticleAdvertisement advertisement = new ArticleAdvertisement();
				Article article = mongoTemplate.findOne(new Query(Criteria.where("id").is(scheduleArticle.getArticleId())), Article.class);
				article.setStatus(1);
				advertisement.setArticle(article);
				advertisement.setPosition(scheduleArticle.getPosition());
				advertisements.add(advertisement);
			}
			return advertisements;
		}
	}

	@Override
	public boolean checkExists(ScheduleArticle scheduleArticle) {
		Query query = new Query();
		DateTime dateTime = new DateTime(scheduleArticle.getStart());
		Long startOfDay = dateTime.withTimeAtStartOfDay().getMillis();
		Long endOfDay = startOfDay + ( 24 * 60 * 60 * 1000);
		query.addCriteria(Criteria.where("start").gte(startOfDay));
		query.addCriteria(Criteria.where("end").lt(endOfDay));
		List<ScheduleArticle> scheduleArticlesExists = mongoTemplate.find(query, ScheduleArticle.class);
		for (ScheduleArticle scheduleArticleExists : scheduleArticlesExists) {
			if(!(scheduleArticleExists.getStart() >= scheduleArticle.getEnd() || scheduleArticleExists.getEnd() <= scheduleArticle.getStart())) {
				if(scheduleArticle.getPosition() == scheduleArticleExists.getPosition()) {
					return true;
				}
			}
		}
		return false;
		
	}

}
