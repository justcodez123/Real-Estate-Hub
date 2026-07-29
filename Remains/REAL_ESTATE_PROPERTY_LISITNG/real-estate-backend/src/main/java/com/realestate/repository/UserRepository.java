package com.realestate.repository;

import com.realestate.model.User;
import com.realestate.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    List<User> findByUserType(UserType userType);
    
    List<User> findByActiveTrue();
    
    List<User> findByUserTypeAndActiveTrue(UserType userType);
}
