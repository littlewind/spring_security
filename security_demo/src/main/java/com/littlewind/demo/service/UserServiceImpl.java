package com.littlewind.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.littlewind.demo.model.PasswordResetToken;
import com.littlewind.demo.model.Shop;
import com.littlewind.demo.model.User;
import com.littlewind.demo.model.UserBasicInfo;
import com.littlewind.demo.model.UserLite;
import com.littlewind.demo.repository.PasswordTokenRepository;
import com.littlewind.demo.repository.ShopRepository;
import com.littlewind.demo.repository.UserRepository;
import com.littlewind.demo.util.JwtTokenUtil;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ShopRepository shopRepository;
    
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Override
    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        if (findByEmail(user.getEmail()) != null) {
        	user.setSuccess(0);
        	return user;
        }
        userRepository.save(user);
        User returned_user = findByEmail(user.getEmail());
        returned_user.setSuccess(1);
        return returned_user;
    }
    
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserBasicInfo findOne(String token) {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String email = jwtTokenUtil.getUsernameFromToken(token);
		User user = userRepository.findByEmail(email);
		UserBasicInfo info = new UserBasicInfo(user);
		return info;
	}
	
	@Override
	public List<Shop> getShop(String token) {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String uid = jwtTokenUtil.getIdFromToken(token);
		User user = userRepository.findById(Long.valueOf(uid)).get();
		
		List<Shop> shopList = new ArrayList<>(user.getShop());
		return shopList;
	}

	@Override
	public int addShop(Shop shop, String token) {
		Long shop_id = shop.getShop_id();
//		System.out.println("UserServiceImpl: shop_id: "+shop_id);
		logger.debug("shop_id: "+shop_id);
		String shop_name = shop.getName();
//		System.out.println("UserServiceImpl: shop_name: "+shop_name);
		logger.debug("shop_name: "+shop_name);
		
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		
//		if (!shopRepository.exists(Example.of(shop))) {
//			shopRepository.saveAndFlush(shop);
//		}
		
		String uid = jwtTokenUtil.getIdFromToken(token);
		User user = userRepository.findById(Long.valueOf(uid)).get();
		for (Shop mShop : user.getShop()) {
			if (mShop.getShop_id().equals(shop_id)) {
				logger.error("Duplicate id");
				return 2;
			}
			
			if (mShop.getName().equals(shop_name)) {
				logger.error("Duplicate shop_name");
				return 0;
			}
		}
		
		shop.setCreateDate(new Date());
		shopRepository.saveAndFlush(shop);
		user.addShop(shop);
		

//		if (!user.addShop(shop)) {
//			return false;
//		}
		
		userRepository.save(user);
		return 1;
	}


	@Override
	public Set<Shop> deleteShop(long shop_id, String token) {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String uid = jwtTokenUtil.getIdFromToken(token);
		User user = userRepository.findById(Long.valueOf(uid)).get();
		for (Shop shop: user.getShop()) {
			if (shop.getShop_id()==shop_id) {
				if (user.removeShop(shop)) {
					userRepository.save(user);
					return user.getShop();
				}
			}
		}

		return user.getShop();
	}

	
	@Override
	public Set<Shop> removeShop(long shop_id, String token) {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String uid = jwtTokenUtil.getIdFromToken(token);
		User user = userRepository.findById(Long.valueOf(uid)).get();
		for (Shop shop: user.getShop()) {
			if (shop.getShop_id()==shop_id) {
				shop.setStatus(0L);
				shopRepository.save(shop);
				return user.getShop();
			}
		}

		return user.getShop();
	}
	
	@Override
	public Set<Shop> activateShop(long shop_id, String token) {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String uid = jwtTokenUtil.getIdFromToken(token);
		User user = userRepository.findById(Long.valueOf(uid)).get();
		for (Shop shop: user.getShop()) {
			if (shop.getShop_id()==shop_id) {
				shop.setStatus(1L);
				shopRepository.save(shop);
				return user.getShop();
			}
		}

		return user.getShop();
	}

	@Override
	public Map<String, Object> changeInfo(UserLite user, String token) {
		// get the user
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String uid = jwtTokenUtil.getIdFromToken(token);
		User mUser = userRepository.findById(Long.valueOf(uid)).get();
		
		//change info
		if (user.getUsername()!=null) {
			mUser.setUsername(user.getUsername());
		}
		if (user.getPhone()!=null) {
			mUser.setPhone(user.getPhone());
		}
		
		// save back to the database
		userRepository.save(mUser);
		
		Map<String, Object> map = new HashMap<>();
		map.put("username", mUser.getUsername());
		map.put("phone", mUser.getPhone());
		
		return map;
	}

	@Override
	public Map<String, Object> changePassword(String old_password, String new_password, String token) {
		Map<String, Object> result = new HashMap<>();
		
		// get the user
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String uid = jwtTokenUtil.getIdFromToken(token);
		User mUser = userRepository.findById(Long.valueOf(uid)).get();
		
		// check the confirm password, if not match -> return false
		if (!bCryptPasswordEncoder.matches(old_password, mUser.getPassword())) {
			result.put("success", 0);
			return result;
		}
		
		// change password
		mUser.setPassword(bCryptPasswordEncoder.encode(new_password));
		
		// save back to the database
		userRepository.save(mUser);
			
		result.put("success", 1);
		
		return result;
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		final PasswordResetToken myToken = new PasswordResetToken(user, token);
        passwordTokenRepository.save(myToken);
	}

	@Override
	public void resetPassword(User user, String new_password) {
		user.setPassword(bCryptPasswordEncoder.encode(new_password));
		userRepository.save(user);
	}




}
