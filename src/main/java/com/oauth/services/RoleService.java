package com.oauth.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oauth.entity.Role;

@Service
public interface RoleService {
	public Role getRoleById(int id);
	public void save(Role role);
	public List<Role> getAllRole();
}
