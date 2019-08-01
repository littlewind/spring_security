package com.littlewind.demo.model;

public class UserBasicInfo {
	private Long id;

    private String username;
    
    private String email;
    
    private String phone;

	public UserBasicInfo() {
		super();
	}

	public UserBasicInfo(Long id, String username, String email, String phone) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.phone = phone;
	}
    
    public UserBasicInfo(User user) {
    	this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.phone = user.getPhone();
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
    
    
}
