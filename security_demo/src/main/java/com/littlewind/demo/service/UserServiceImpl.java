package com.littlewind.demo.service;

import java.util.Set;

//import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.littlewind.demo.customexception.CustomUnauthorizedException;
import com.littlewind.demo.model.Shop;
import com.littlewind.demo.model.User;
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
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


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
	public User findOne(long userId, String token) throws CustomUnauthorizedException {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String id_stored_in_token = jwtTokenUtil.getIdFromToken(token);
		if (!id_stored_in_token.equals(String.valueOf(userId))) {
			throw new CustomUnauthorizedException("Unauthorized");
		}
		
		return userRepository.findById(userId).get();
	}

	@Override
	public boolean addShop(Shop shop, String token) {
		
		Long shop_id = shop.getShop_id();
		System.out.println("shop_id: "+shop_id);
		
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		
//		if (!shopRepository.exists(Example.of(shop))) {
//			shopRepository.saveAndFlush(shop);
//		}
		
		String uid = jwtTokenUtil.getIdFromToken(token);
		User user = userRepository.findById(Long.valueOf(uid)).get();
		for (Shop mShop : user.getShop()) {
			System.out.println(mShop.getShop_id());
			if (mShop.getShop_id().equals(shop_id)) {
				return false;
			}
		}
		
		shopRepository.saveAndFlush(shop);
		user.addShop(shop);

//		if (!user.addShop(shop)) {
//			return false;
//		}
		
		userRepository.save(user);
		return true;
	}

	@Override
	public Set<Shop> removeShop(long shop_id, String token) {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String uid = jwtTokenUtil.getIdFromToken(token);
		User user = userRepository.findById(Long.valueOf(uid)).get();
		for (Shop shop: user.getShop()) {
			System.out.println(shop.getShop_id());
			if (shop.getShop_id()==shop_id) {
				if (user.removeShop(shop)) {
					userRepository.save(user);
					return user.getShop();
				}
//				return user.getShop();
			}
		}

		return user.getShop();
	}

}
