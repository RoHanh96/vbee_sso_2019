package com.vbee.springbootmongodbnewspapersrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.UserCategoryChosen;

public interface UserCategoryChosenRepository extends MongoRepository<UserCategoryChosen, String>{

	UserCategoryChosen findByUserIdAndCategoryId(String id, Integer id2);

}
