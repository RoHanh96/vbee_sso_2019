package com.oauth.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oauth.entity.User;

@Service
public interface UserService {
	public User getUserByName(String username);
	public User getUserByEmail(String email);
	public void save(User user);
	public List<User> getAllUser();
	public User getUserById(Integer userId);
	public void update(User user);
	public void deleteById(User user);
}
