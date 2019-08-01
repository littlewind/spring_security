package com.littlewind.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.littlewind.demo.model.User;
import com.littlewind.demo.model.UserLite;
import com.littlewind.demo.repository.UserRepository;
import com.littlewind.demo.service.UserService;
import com.littlewind.demo.util.JwtTokenUtil;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class JwtAuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private UserRepository userRepository;
	
	Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserLite createAuthenticationToken(@RequestBody User user) throws Exception {

		authenticate(user.getEmail(), user.getPassword());
		
//		System.out.println(	"\nJwtAuthenticationController_userDetails.getUsername(): "+userDetails.getUsername()
//							+"\nuserDetails.toString(): "+ userDetails.toString() + "\n");
		User stored_user = userService.findByEmail(user.getEmail());
		
//		System.out.println(	"\nJwtAuthenticationController_user: name-"+stored_user.getUsername() + "\tid-" + stored_user.getId()+ "\n");
		logger.debug("email: "+stored_user.getEmail()+" , id: "+stored_user.getId());
		
		final String token = jwtTokenUtil.generateToken(stored_user);
		
		UserLite user_info = new UserLite(stored_user);
		user_info.setSuccess(1);
		user_info.setToken(token);
		
		return user_info;
	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.GET)
	public Map<String, Object> authen(@RequestHeader("Authorization") String token) {

		Map<String, Object> map = new HashMap<>();
//		map.put("success", 1L);
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		
		if (needExtending(token)) {
			String uid = jwtTokenUtil.getIdFromToken(token);
			User user = userRepository.findById(Long.valueOf(uid)).get();
			final String new_token = jwtTokenUtil.generateToken(user);
			map.put("token", new_token);
		} else {
			map.put("token", null);
		}
		
		return map;
	}
	
	@RequestMapping(value = "/authenticate/extend", method = RequestMethod.GET)
	public Map<String, String> authenExtend(@RequestHeader("Authorization") String token) {
		
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String uid = jwtTokenUtil.getIdFromToken(token);
		User user = userRepository.findById(Long.valueOf(uid)).get();
		
		final String new_token = jwtTokenUtil.generateToken(user);
		
		Map<String, String> map = new HashMap<>();
		map.put("new_token", new_token);
		
		return map;
	}

	private void authenticate(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	private boolean needExtending(String token) {
		final int hour = 24;
		Date expiry_date = jwtTokenUtil.getExpirationDateFromToken(token);
		Date cur_date = new Date();
		long diff = expiry_date.getTime() - cur_date.getTime();
		logger.debug("token will expire in diff/(hour * 60 * 60 * 1000) = " + diff / (hour * 60 * 60 * 1000) + "days");
		if ((diff / (hour * 60 * 60 * 1000)) < 1) {
			return true;
		} 
		return false;
	}
}
