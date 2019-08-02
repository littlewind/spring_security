package com.littlewind.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.littlewind.demo.model.Shop;
import com.littlewind.demo.model.User;
import com.littlewind.demo.model.UserBasicInfo;
import com.littlewind.demo.model.UserLite;

public interface UserService {
    User save(User user);
    
//    User login(User user);
    
    User findByEmail(String email);

	UserBasicInfo findOne(String token);

	int addShop(Shop shop, String token);

	Set<Shop> removeShop(long shop_id, String token);
	
	Set<Shop> deleteShop(long shop_id, String token);

	Map<String, Object> changeInfo(UserLite user, String token);

	Map<String, Object> changePassword(String old_password, String new_password, String token);

	List<Shop> getShop(String token);
	
	public void createPasswordResetTokenForUser(final User user, final String token);
	
	public void resetPassword(User user, String new_password);


}
