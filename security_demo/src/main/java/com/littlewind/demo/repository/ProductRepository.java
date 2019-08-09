package com.littlewind.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.littlewind.demo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByShopid(long shopid);
}
