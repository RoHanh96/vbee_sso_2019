package com.vbee.springbootmongodbnewspapersrestapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Voice;

@Repository
public interface VoiceMongoRepository extends MongoRepository<Voice, String>{

	Voice findVoiceByName(String name);

	Voice findVoicesByNameAndArticleId(String name, int articleId);

	Voice findVoicesByNameAndCrawlerId(String name, Integer crawlerId);

	List<Voice> findByArticleId(Integer articleId);


}
