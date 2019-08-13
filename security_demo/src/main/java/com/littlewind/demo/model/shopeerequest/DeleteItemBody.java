package com.littlewind.demo.model.shopeerequest;

import java.util.List;

public class DeleteItemBody {
	long item_id;
	long partner_id;
	long shopid;
	
	public DeleteItemBody() {
		super();
	}
	public DeleteItemBody(long item_id, long partner_id, long shopid) {
		super();
		this.item_id = item_id;
		this.partner_id = partner_id;
		this.shopid = shopid;
	}
	public long getItem_id() {
		return item_id;
	}
	public void setItem_id(long item_id) {
		this.item_id = item_id;
	}
	public long getPartner_id() {
		return partner_id;
	}
	public void setPartner_id(long partner_id) {
		this.partner_id = partner_id;
	}
	public long getShopid() {
		return shopid;
	}
	public void setShopid(long shopid) {
		this.shopid = shopid;
	}
	
	
}
