package com.real_estate.real_estate_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.real_estate.real_estate_backend.models.Users;

public interface UserRepository extends JpaRepository<Users, Long>{
	//Custom query to find the users by email
	
	Optional<Users> findByEmail(String email);
	
	//For Registration, Check if Email already Exists
	boolean existsByEmail(String email);
}
