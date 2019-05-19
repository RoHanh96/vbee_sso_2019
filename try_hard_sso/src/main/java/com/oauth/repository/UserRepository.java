package com.oauth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oauth.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query("select u from User u where u.username = ?1")
	User findByUsername(String username);
	
	@Query("select u from User u where u.email = ?1")
	User findByEmail(String email);
	
	@Query("select u from User u")
	List<User> getAllUser();
	
	@Query("select u from User u where u.id = ?1")
	User findByUserId(Integer userId);
}
