//package com.littlewind.demo.model;
//
//import java.util.List;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "product")
//public class Product {
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
//	Long SKU;
////	List<String> photos;
//	@OneToMany
//	String photo;
//	String name;
//	String status;
//	public Product() {
//		super();
//	}
////	public Product(Long sKU, List<String> photos, String name, String status) {
////		super();
////		SKU = sKU;
////		this.photos = photos;
////		this.name = name;
////		this.status = status;
////	}
////	public Product(Long sKU, String name, String status) {
////		super();
////		SKU = sKU;
////		this.name = name;
////		this.status = status;
////	}
//	
//	public Product(Long sKU, String photo, String name, String status) {
//	super();
//	SKU = sKU;
//	this.photo = photo;
//	this.name = name;
//	this.status = status;
//}
//	
//	public Long getSKU() {
//		return SKU;
//	}
//	public void setSKU(Long sKU) {
//		SKU = sKU;
//	}
////	public List<String> getPhotos() {
////		return photos;
////	}
////	public void setPhotos(List<String> photos) {
////		this.photos = photos;
////	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getStatus() {
//		return status;
//	}
//	public void setStatus(String status) {
//		this.status = status;
//	}
//	
//	
//}
