package com.littlewind.demo.model.shopeerequest;

import java.util.List;

public class UpdateItemImgBody {
	long item_id;
	List<String> images;
	long partner_id;
	long shopid;
	
	public UpdateItemImgBody() {
	}
	public UpdateItemImgBody(long item_id, List<String> images, long partner_id, long shopid) {
		this.item_id = item_id;
		this.images = images;
		this.partner_id = partner_id;
		this.shopid = shopid;
	}
	public long getItem_id() {
		return item_id;
	}
	public void setItem_id(long item_id) {
		this.item_id = item_id;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
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
	
	public String serializeImgURL() {
		if (this.images == null || this.images.size()==0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String url:this.images) {
			sb.append("\"");
			sb.append(url);
			sb.append("\",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
