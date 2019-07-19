package com.littlewind.demo.customexception;

public class CustomUnauthorizedException extends Exception{
	private String msg;

	public CustomUnauthorizedException(String msg) {
		super(msg);
	}
	
	
}
