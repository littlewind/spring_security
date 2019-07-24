package com.littlewind.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.littlewind.demo.customexception.CustomUnauthorizedException;
import com.littlewind.demo.model.Shop;
import com.littlewind.demo.model.User;
import com.littlewind.demo.model.UserLite;

public interface UserService {
    User save(User user);
    
//    User login(User user);
    
    User findByEmail(String email);

	User findOne(long userId, String token) throws CustomUnauthorizedException;

	boolean addShop(Shop shop, String token);

	Set<Shop> removeShop(long shop_id, String token);

	Map<String, Object> changeInfo(UserLite user, String token);

	Map<String, Object> changePassword(String old_password, String new_password, String token);

	List<Shop> getShop(String token); 
}
