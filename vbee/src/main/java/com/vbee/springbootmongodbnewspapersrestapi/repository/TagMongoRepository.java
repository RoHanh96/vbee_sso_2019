package com.vbee.springbootmongodbnewspapersrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Tag;
@Repository
public interface TagMongoRepository extends MongoRepository<Tag, String>{

	Tag findTagByName(String name);

//	Tag findTagByName(String tagName);
//
//	List<Long> getArticleIdsByTagName(String tagName);

}
