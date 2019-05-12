package com.vbee.springbootmongodbnewspapersrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Website;

public interface WebsiteMongoRepository extends MongoRepository<Website, String>{

	Website findByName(String name);

}
