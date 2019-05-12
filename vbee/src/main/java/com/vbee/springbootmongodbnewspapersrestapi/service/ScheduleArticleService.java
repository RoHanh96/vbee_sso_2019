package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.ScheduleArticle;
import com.vbee.springbootmongodbnewspapersrestapi.dao.IScheduleArticleDAO;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleAdvertisement;
import com.vbee.springbootmongodbnewspapersrestapi.repository.ArticleMongoRepository;
import com.vbee.springbootmongodbnewspapersrestapi.repository.ScheduleArticleMongoRepository;

@Service
public class ScheduleArticleService implements IScheduleArticleService{

	@Autowired
	ScheduleArticleMongoRepository scheduleArticleRepository;
	
	@Autowired
	IScheduleArticleDAO scheduleArticleDAO;
	
	@Autowired
	ArticleMongoRepository articleMongoRepository;
	
	@Override
	public List<ScheduleArticle> saveScheduleArticles(Article article, List<ScheduleArticle> scheduleArticles) {
		for (ScheduleArticle scheduleArticle : scheduleArticles) {
			if(!scheduleArticleDAO.checkExists(scheduleArticle)){
				scheduleArticle = scheduleArticleRepository.save(scheduleArticle);
			}
		}
		return scheduleArticles;
	}

	@Override
	public List<ScheduleArticle> findByArticleId(Integer articleId) {
		return scheduleArticleRepository.findByArticleId(articleId);
	}

	@Override
	public List<ScheduleArticle> findByWeek(long startWeek, long endWeek, Integer categoryId, Integer articleId) {
		return scheduleArticleDAO.findByWeekAndCategory(startWeek, endWeek, categoryId, articleId);
	}

	@Override
	public List<ArticleAdvertisement> findArticleByNow(Integer categoryId) {
		return scheduleArticleDAO.findArticleByNow(categoryId);
	}

	@Override
	public ScheduleArticle findById(String id) {
		return scheduleArticleRepository.findOne(id);
	}

	@Override
	public void delete(ScheduleArticle scheduleArticle) {
		scheduleArticleRepository.delete(scheduleArticle);
	}

}
