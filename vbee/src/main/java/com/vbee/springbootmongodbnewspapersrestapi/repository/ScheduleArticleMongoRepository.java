package com.vbee.springbootmongodbnewspapersrestapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.ScheduleArticle;

public interface ScheduleArticleMongoRepository extends MongoRepository<ScheduleArticle, String>{

	List<ScheduleArticle> findByStartBetween(long startWeek, long endWeek);

	List<ScheduleArticle> findByArticleId(Integer articleId);

}
