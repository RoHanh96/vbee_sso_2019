package com.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oauth.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
}
