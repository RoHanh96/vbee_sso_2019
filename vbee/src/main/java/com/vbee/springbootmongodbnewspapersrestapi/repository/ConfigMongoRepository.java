package com.vbee.springbootmongodbnewspapersrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Config;

public interface ConfigMongoRepository extends MongoRepository<Config, String> {

	Config findByVersion(String version);

}
