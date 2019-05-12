package com.vbee.springbootmongodbnewspapersrestapi.dao;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.ScheduleArticle;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleAdvertisement;

public interface IScheduleArticleDAO {

	List<ScheduleArticle> findByWeekAndCategory(long startWeek, long endWeek, Integer categoryId, Integer articleId);

	List<ArticleAdvertisement> findArticleByNow(Integer categoryId);

	boolean checkExists(ScheduleArticle scheduleArticle);

}
