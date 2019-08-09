package com.littlewind.demo.service;

import java.util.List;
import java.util.Map;

import com.littlewind.demo.model.Product;

public interface ProductService {

	List<Product> findByShopid(long shopid);

	Product save(Product product);

}
