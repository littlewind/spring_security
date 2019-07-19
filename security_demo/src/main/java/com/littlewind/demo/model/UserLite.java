package com.littlewind.demo.model;

import java.util.Set;

public class UserLite {
    private Long id;

    private String username;
    
    private String email;
    
    private String phone;

    private int success;
    
    private String token;

    private Set<Shop> shop;

	public UserLite() {
		super();
	}

	public UserLite(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.phone = user.getPhone();
		this.shop = user.getShop();
		this.success = user.getSuccess();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Set<Shop> getShop() {
		return shop;
	}

	public void setShop(Set<Shop> shop) {
		this.shop = shop;
	}
	
	
    
    
}
