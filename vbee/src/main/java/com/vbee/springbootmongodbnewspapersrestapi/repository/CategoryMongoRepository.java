package com.vbee.springbootmongodbnewspapersrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;

@Repository
public interface CategoryMongoRepository extends MongoRepository<Category, Integer>{

	Category findCategoryByName(String name);
	
}
