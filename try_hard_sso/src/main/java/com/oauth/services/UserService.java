package com.oauth.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oauth.entity.User;

@Service
public interface UserService {
	public User getUserByName(String username);
	public void save(User user);
}
