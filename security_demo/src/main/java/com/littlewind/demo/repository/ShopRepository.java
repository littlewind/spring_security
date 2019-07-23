package com.littlewind.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.littlewind.demo.model.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long>{

}
