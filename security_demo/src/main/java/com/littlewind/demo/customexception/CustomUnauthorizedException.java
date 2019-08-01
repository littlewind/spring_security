package com.littlewind.demo.customexception;

public class CustomUnauthorizedException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3886720730798373797L;
	private String msg;

	public CustomUnauthorizedException(String msg) {
		super(msg);
	}
	
	
}
