package com.vbee.springbootmongodbnewspapersrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.VoiceSynthesis;

public interface VoiceSynthesisMongoRepository extends MongoRepository<VoiceSynthesis, String>{

	VoiceSynthesis findByName(String name);
	
}
