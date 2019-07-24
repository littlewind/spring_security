package com.littlewind.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.littlewind.demo.customexception.CustomUnauthorizedException;
import com.littlewind.demo.model.Shop;
import com.littlewind.demo.model.User;
import com.littlewind.demo.model.UserLite;
import com.littlewind.demo.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;
  
    @GetMapping("/registration")
    public String registration() {
        return "Registration";
    }
    
    @PostMapping("/registration")
    public UserLite registration(@RequestBody User userForm) {
        User new_user = userService.save(userForm);
        UserLite new_user_lite = new UserLite(new_user);
        return new_user_lite;
    }
    
    @GetMapping("/login")
    public String login() {
        return "Login";
    }
    

    @GetMapping({"/", "/welcome"})
    public HashMap<String, String> welcome() {
    	HashMap<String, String> result = new HashMap<>();
    	result.put("message", "Hello World");
    	return result;
    }
    
    
    @GetMapping("/shop")
    public List<Shop> getShop(@RequestHeader("Authorization") String token) {
        return userService.getShop(token);
    }
    
    @PostMapping("/shop")
    public HashMap<String, Object> addshop(@RequestBody Shop newShop, @RequestHeader("Authorization") String token) {
    	boolean success = false;
    	HashMap<String, Object> result = new HashMap<>();
    	if (newShop != null) {
    		System.out.println("shop_id: "+newShop.toString()+"\n");
    		success = userService.addShop(newShop, token);
    	}
    	

		if (success) {
			result.put("success", 1);
		} else {
			result.put("success", 0);
		}
        return result;
    }
    
    @DeleteMapping("/shop/{shop_id}")
    public Set<Shop> removeShop(@PathVariable long shop_id,@RequestHeader("Authorization") String token) {
        return userService.removeShop(shop_id, token);
    }
    
    @RequestMapping(value = "/users/{userid}", method = RequestMethod.GET)
    public User findUserByUserId(@PathVariable("userid") long userId, @RequestHeader("Authorization") String token) throws CustomUnauthorizedException {
        return userService.findOne(userId, token);
    }
    
    @PostMapping("/user/updateinfo")
    public Map<String, Object> changeInfo(@RequestBody UserLite user, @RequestHeader("Authorization") String token) {
    	return userService.changeInfo(user, token);
    }
    
    @PostMapping("/user/changepassword")
    public Map<String, Object> changePassword(@RequestBody Map<String, String> map, @RequestHeader("Authorization") String token) {
    	String old_password = map.get("old_password");
    	String new_password = map.get("new_password");
    	return userService.changePassword(old_password, new_password, token);
    }
}
