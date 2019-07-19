package com.littlewind.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.littlewind.demo.customexception.CustomUnauthorizedException;
import com.littlewind.demo.model.User;
import com.littlewind.demo.model.UserLite;
//import com.littlewind.demo.service.SecurityService;
import com.littlewind.demo.service.UserService;

//@Controller
@RestController
public class UserController {
    @Autowired
    private UserService userService;
  
    @GetMapping("/registration")
//    public String registration(@RequestBody User userForm) {
    public String registration() {
//        userService.save(userForm);

//        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "Registration";
    }
    
    @PostMapping("/registration")
    public UserLite registration(@RequestBody User userForm) {
        User new_user = userService.save(userForm);
        UserLite new_user_lite = new UserLite(new_user);
        return new_user_lite;
    }
    
    @GetMapping("/login")
//    public String login(@RequestBody User userForm) {
    public String login() {
//        userService.save(userForm);

//        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "Login";
    }
    

    @GetMapping({"/", "/welcome"})
    public String welcome() {
        return "Hello World";
    }
    
    
    @GetMapping("/addshop")
    public String addshop() {
        return "Add new Shop";
    }
    
    @RequestMapping(value = "/users/{userid}", method = RequestMethod.GET)
    public User findUserByUserId(@PathVariable("userid") long userId, @RequestHeader("Authorization") String token) throws CustomUnauthorizedException {
        return userService.findOne(userId, token);
    }
}
