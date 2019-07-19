package com.littlewind.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.littlewind.demo.model.User;
import com.littlewind.demo.model.UserLite;
import com.littlewind.demo.service.UserDetailsServiceImpl;
import com.littlewind.demo.service.UserService;
import com.littlewind.demo.util.JwtTokenUtil;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
//	@Autowired
//	private UserDetailsServiceImpl userDetailsService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserLite createAuthenticationToken(@RequestBody User user) throws Exception {

		authenticate(user.getEmail(), user.getPassword());
		
//		System.out.println(	"\nJwtAuthenticationController_user.getEmail(): "+user.getEmail() + "\n");

//		final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		
//		System.out.println(	"\nJwtAuthenticationController_userDetails.getUsername(): "+userDetails.getUsername()
//							+"\nuserDetails.toString(): "+ userDetails.toString() + "\n");
		User stored_user = userService.findByEmail(user.getEmail());
		
		System.out.println(	"\nJwtAuthenticationController_user: name-"+stored_user.getUsername() + "\tid-" + stored_user.getId()+ "\n");
		final String token = jwtTokenUtil.generateToken(stored_user);
		
		UserLite user_info = new UserLite(stored_user);
		user_info.setSuccess(1);
		user_info.setToken(token);
		
		return user_info;
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
}
