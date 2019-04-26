package com.oauth.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oauth.entity.User;
import com.oauth.repository.RoleRepository;
import com.oauth.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public User getUserByName(String username) {
		// TODO Auto-generated method stub
		User user = null;
		try {
			user = userRepository.findByUsername(username);
		} catch (Exception e) {
			// TODO: handle exception
			return user;
		}
		return user;
	}
}
