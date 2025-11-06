package com.microservices.gateway.repository;

import com.microservices.gateway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsernameIgnoreCase(String username);
    
    Optional<User> findByEmailIgnoreCase(String email);
    
    boolean existsByUsernameIgnoreCase(String username);
    
    boolean existsByEmailIgnoreCase(String email);
}
