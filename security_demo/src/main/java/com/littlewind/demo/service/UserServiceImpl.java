package com.littlewind.demo.service;

//import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.littlewind.demo.customexception.CustomUnauthorizedException;
import com.littlewind.demo.model.User;
import com.littlewind.demo.repository.UserRepository;
import com.littlewind.demo.util.JwtTokenUtil;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

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

}
