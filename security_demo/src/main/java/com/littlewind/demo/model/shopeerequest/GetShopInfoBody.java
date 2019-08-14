package com.littlewind.demo.model.shopeerequest;

public class GetShopInfoBody {
	long partner_id;
	long shopid;
	
	public GetShopInfoBody() {
		super();
	}
	public GetShopInfoBody(long partner_id, long shopid) {
		super();
		this.partner_id = partner_id;
		this.shopid = shopid;
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
