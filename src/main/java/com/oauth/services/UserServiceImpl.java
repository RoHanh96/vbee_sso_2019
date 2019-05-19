package com.oauth.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oauth.entity.Role;
import com.oauth.entity.User;
import com.oauth.repository.RoleRepository;
import com.oauth.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	public static final String defaultRole = "ROLE_USER"; 
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public User getUserByName(String username) {
		// TODO Auto-generated method stub
		User user = null;
		if(!username.equals(null)) {
			try {
				user = userRepository.findByUsername(username);
			} catch (Exception e) {
				// TODO: handle exception
				return user;
			}
		}
		return user;
	}
	
	@Override
	public User getUserByEmail(String email) {
		User user = null;
		if(email!=null) {
			try {
				user = userRepository.findByEmail(email);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
			}
		}
		return user;
	}

	@Override
	public void save(User user) {
		roleRepository.getRoleByName(defaultRole).map(role -> {
			user.setRole(role);
			return userRepository.save(user);
		});
	}
	
	public void beforeSave() {
		if(!roleRepository.getRoleByName(defaultRole).isPresent()) {
			Role role = new Role();
			role.setName(defaultRole);
			roleRepository.save(role);
		}
	}

	@Override
	public List<User> getAllUser() {
		return userRepository.getAllUser();
	}

	@Override
	public User getUserById(Integer userId) {
		// TODO Auto-generated method stub
		return userRepository.findByUserId(userId);
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		User updateUser = userRepository.getOne(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setUsername(user.getUsername());
		updateUser.setRole(user.getRole());
		if(updateUser != null) {
			userRepository.save(updateUser);
		}
	}

	@Override
	public void deleteById(User user) {
		// TODO Auto-generated method stub
		userRepository.delete(user);
	}
}
