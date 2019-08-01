package com.littlewind.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.littlewind.demo.util.JwtTokenUtil;

@RestController
@RequestMapping("/api/v1")
public class PhotoController {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
    			  "cloud_name", "sapodecor",
    			  "api_key", "744836913578885",
    			  "api_secret", "NelBzUIFN9StEnrxysN44rUpeH4"));


	@RequestMapping(value = "/resources/search", method = RequestMethod.GET)
	public List<String> getGallery(@RequestHeader("Authorization") String token) throws Exception {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String userId = jwtTokenUtil.getIdFromToken(token);
		return listPhotos(userId);
	}
	

	@RequestMapping(value = "/resources/search/prod", method = RequestMethod.GET)
	public List<String> listPhotosViaProduct1(String item_id, @RequestHeader("Authorization") String token) throws Exception{
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String userId = jwtTokenUtil.getIdFromToken(token);
		return listPhotosViaProduct(userId, item_id);	
	}


	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void uploadPhoto1(String item_id, int img_order, String photo_url, @RequestHeader("Authorization") String token){
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String userId = jwtTokenUtil.getIdFromToken(token);
		
		uploadPhoto(userId, item_id, img_order, photo_url);
	}
	
	
//	@RequestMapping(value = "/uploadlist", method = RequestMethod.POST)
//	public void uploadPhotoList(String item_id, List<String> photo_url_list, @RequestHeader("Authorization") String token){
//		if (token.startsWith("Bearer ")) {
//			token = token.substring(7);
//		}
//		String userId = jwtTokenUtil.getIdFromToken(token);
//		
//		for (int i=0; i<photo_url_list.size(); ++i) {
//			String photo_url = photo_url_list.get(i);
//			uploadPhoto(userId, item_id, i, photo_url);
//		}
//		
//	}
	
	
	
	///////////////////////////////////////////////////////////////////
	public List<String> listPhotos(String userId) throws Exception {
		StringBuilder param = new StringBuilder();
		List<String> photos = new ArrayList<>();
		param.append("folder = ");
		param.append(userId);
		param.append("/*");
		ApiResponse result = cloudinary.search().expression(param.toString()).execute();
		Object tmp = result.get("resources");
    	ArrayList arr = (ArrayList)tmp;
    	Object[] objs = arr.toArray();
    	for (Object obj : objs) {
    		HashMap temp = (HashMap) obj;
    		System.out.println(temp.get("url").toString());
    		photos.add(temp.get("url").toString());
    	}
    	return photos;
	}
	
	
	
	public List<String> listPhotosViaProduct(String userId, String prod) throws Exception{
		StringBuilder param = new StringBuilder();
		List<String> photos = new ArrayList<>();
		param.append("folder:");
		param.append(userId);
		param.append("/");
		param.append(prod);
		param.append("/*");
		ApiResponse result = cloudinary.search().expression(param.toString()).execute();
		Object tmp = result.get("resources");
    	ArrayList arr = (ArrayList)tmp;
    	Object[] objs = arr.toArray();
    	for (Object obj : objs) {
    		HashMap temp = (HashMap) obj;
    		System.out.println(temp.get("url").toString());
    		photos.add(temp.get("url").toString());
    	}
    	return photos;
		
	}
	
	
	public void uploadPhoto(String userId, String productCode, int id, String photo){
		 StringBuilder path = new StringBuilder();
	     path.append(userId);
	     path.append("/");
	     path.append(productCode);
	     path.append("/");
	     path.append(id);

	     Map params = ObjectUtils.asMap("public_id", path.toString());
	     try {
			Map uploadResult = cloudinary.uploader().upload(photo, params);
			Set load = uploadResult.entrySet();
//	    	for (Object obj : load) {
//	    		System.out.println(obj);
//	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
