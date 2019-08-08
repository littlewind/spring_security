package com.littlewind.demo.model.shopeerequest;

import java.util.List;

public class DeleteItemImgBody {
	long item_id;
	List<String> images;
	List<Integer> positions;
	long partner_id;
	long shopid;
	
	public DeleteItemImgBody() {
		super();
	}

	public DeleteItemImgBody(long item_id, List<String> images, List<Integer> positions, long partner_id, long shopid) {
		super();
		this.item_id = item_id;
		this.images = images;
		this.positions = positions;
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

	public List<Integer> getPositions() {
		return positions;
	}

	public void setPositions(List<Integer> positions) {
		this.positions = positions;
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
		if (this.images==null || this.images.size()==0) {
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
	
	public String serializePositions() {
		if (this.positions==null || this.positions.size()==0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Integer position:this.positions) {
			sb.append(position);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
