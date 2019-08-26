package com.littlewind.demo.customexception;

public class ShopNotLinkedException extends Exception{

	private static final long serialVersionUID = -781394480584718958L;
	
	public ShopNotLinkedException() {
		super("The shop has not been linked with this account!");
	}
	
	public ShopNotLinkedException(String msg) {
		super(msg);
	}
	
}
