package com.littlewind.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.littlewind.demo.model.PasswordResetToken;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	PasswordResetToken findByToken(String token);
}
