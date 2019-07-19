package com.littlewind.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.littlewind.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
