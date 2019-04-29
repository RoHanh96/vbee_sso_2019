package com.oauth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oauth.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	@Query("select r from Role r where r.name = ?1")
	public Optional<Role> getRoleByName(String roleName);
}
