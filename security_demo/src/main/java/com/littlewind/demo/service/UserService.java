package com.littlewind.demo.service;

import java.util.Set;

import com.littlewind.demo.customexception.CustomUnauthorizedException;
import com.littlewind.demo.model.Shop;
import com.littlewind.demo.model.User;

public interface UserService {
    User save(User user);
    
//    User login(User user);
    
    User findByEmail(String email);

	User findOne(long userId, String token) throws CustomUnauthorizedException;

	boolean addShop(Shop shop, String token);

	Set<Shop> removeShop(long shop_id, String token); 
}
