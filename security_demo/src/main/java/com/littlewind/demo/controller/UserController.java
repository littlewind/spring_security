package com.littlewind.demo.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.littlewind.demo.model.PasswordResetToken;
import com.littlewind.demo.model.Product;
import com.littlewind.demo.model.Shop;
import com.littlewind.demo.model.User;
import com.littlewind.demo.model.UserBasicInfo;
import com.littlewind.demo.model.UserLite;
import com.littlewind.demo.repository.PasswordTokenRepository;
import com.littlewind.demo.service.ProductService;
import com.littlewind.demo.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    Logger logger = LoggerFactory.getLogger(UserController.class);
  
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
    
    @GetMapping("/products")
    public List<Product> getProducts(long shopid) {
        return productService.findByShopid(shopid);
    }
    
//    @PostMapping("/products")
//    public Map<String, Object> addProduct(@RequestBody Product product) {
//        productService.save(product);
//        Map<String, Object> result = new HashMap<>();
//        result.put("success", 1);
//        return result;
//    }
    
    @PostMapping("/products")
    public Map<String, Object> addProduct(@RequestBody Map<String,List<Product>> body) {
    	List<Product> productList = body.get("products");
    	addProductList(productList);
        Map<String, Object> result = new HashMap<>();
        result.put("success", 1);
        return result;
    }
    
    @Transactional
    private void addProductList(@RequestBody List<Product> productList) {
    	for( Product product:productList) {
    		logger.debug(product.toString());
    		productService.save(product);
    	}
    }
    
    @GetMapping("/shop")
    public List<Shop> getShop(@RequestHeader("Authorization") String token) {
        return userService.getShop(token);
    }
    
    @PostMapping("/shop")
    public HashMap<String, Object> addshop(@RequestBody Shop newShop, @RequestHeader("Authorization") String token) {
    	int success = -1;
    	HashMap<String, Object> result = new HashMap<>();
    	if (newShop != null && newShop.getShop_id()!= null && newShop.getName()!= null) {
//    		System.out.println("shop_id: "+newShop.toString()+"\n");
    		logger.debug("shop_id: "+newShop.toString()+"\n");
    		success = userService.addShop(newShop, token);
    	}
    	


		result.put("success", success);

        return result;
    }
    
    @DeleteMapping("/shop/{shop_id}")
    public Set<Shop> removeShop(@PathVariable long shop_id,@RequestHeader("Authorization") String token) {
        return userService.removeShop(shop_id, token);
    }
    
    @DeleteMapping("/shop/delete/{shop_id}")
    public Set<Shop> deleteShop(@PathVariable long shop_id,@RequestHeader("Authorization") String token) {
        return userService.deleteShop(shop_id, token);
    }
    
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public UserBasicInfo findUserByUserId(@RequestHeader("Authorization") String token) {
        return userService.findOne(token);
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
    
    @PostMapping("/user/resetPassword")
	public Map<String, Object> resetPassword(@RequestParam("email") String userEmail) {
    Map<String, Object> result = new HashMap<>();
	User user = userService.findByEmail(userEmail);
	if (user == null) {
	    result.put("success", 0);
	    result.put("message", "Cannot find account with email: "+userEmail);
	    return result;
	}
	String token = UUID.randomUUID().toString().replace("-", "");
	userService.createPasswordResetTokenForUser(user, token);
	
	sendEmail(token, userEmail);
	
	result.put("success", 1);
	result.put("message", "A link to reset password has been sent to your email: "+userEmail);
	
	return result;
	}
    
    @PostMapping("/user/resetPassword/step2")
	public Map<String, Object> resetPassword2(@RequestBody Map<String, String> body) {
		String token = body.get("token");
		String new_password = body.get("new_password");
		Map<String, Object> result = new HashMap<>();
		
		PasswordResetToken passToken = validatePasswordResetToken(token);
		
		if (passToken==null) {
			result.put("success", 0);
			return result;
		}
		
		User user = passToken.getUser();
		userService.resetPassword(user, new_password);
		
		result.put("success", 1);
		return result;
    }
    
    @Async
    void sendEmail(String token, String receiver) {
    	int noOfQuickServiceThreads = 5;
    	
    	ScheduledExecutorService quickService = Executors.newScheduledThreadPool(noOfQuickServiceThreads); // Creates a thread pool that reuses fixed number of threads(as specified by noOfThreads in this case).
    	
    	
		String content = "Hello,\n" + 
				"\n" + 
				"Follow this link to reset your password( this link will expire in 15 minutes):\n" + 
				"\n\t" +
				"https://google.com?token="+
				token + 
				"\n\n" + 
				"If you didn’t ask to reset your password, you can ignore this email.\n" + 
				"\n" + 
				"Thanks,\n" + 
				"\n" + 
				"Your Sapo Decorate team";
		
        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo("to_1@gmail.com", "to_2@gmail.com", "to_3@yahoo.com");
        msg.setTo(receiver);
        
        msg.setSubject("Reset your password for Sapo Decorate");
        msg.setText(content);

//        javaMailSender.send(msg);
        quickService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					javaMailSender.send(msg);
				}catch(Exception e){
					logger.error("Exception occured while send a mail : ",e);
				}
			}
		});

    }
    
    public PasswordResetToken validatePasswordResetToken(String token) {
//    	System.out.println("_UserController: String token:  "+token);
    	logger.debug("String token:  "+token);
        PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        
        if (passToken == null ) {
//        	System.out.println("_UserController: PasswordResetToken passToken is null");
        	logger.error("PasswordResetToken passToken is null");
        	
        }
        if (passToken != null) {     
	        Calendar cal = Calendar.getInstance();
	        if ((passToken.getExpiryDate()
	            .getTime() - cal.getTime()
	            .getTime()) <= 0) {
//	        	System.out.println("_UserController: token expired");
	        	logger.error("token expired");
	            return null;	// expired token
	        }
        }
    
        return passToken; // valid token
    }
}
