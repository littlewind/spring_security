package com.littlewind.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.littlewind.demo.model.Product;
import com.littlewind.demo.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
    private ProductRepository productRepository;
	
	@Override
	public List<Product> findByShopid(long shopid) {
		return productRepository.findByShopid(shopid);
	}

	@Override
	public Product save(Product product) {
		productRepository.save(product);
		return null;
	}

}
