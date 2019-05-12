package com.vbee.springbootmongodbnewspapersrestapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.User;

@Repository
public interface UserMongoRepository extends MongoRepository<User, String>{

}
