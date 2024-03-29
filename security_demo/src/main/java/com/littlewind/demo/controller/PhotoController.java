package com.littlewind.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.littlewind.demo.model.Shop;
import com.littlewind.demo.model.User;
import com.littlewind.demo.repository.UserRepository;
import com.littlewind.demo.util.JwtTokenUtil;

@RestController
@RequestMapping("/api/v1")
public class PhotoController {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
    private UserRepository userRepository;
	
	Logger logger = LoggerFactory.getLogger(PhotoController.class);
	
	Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
    			  "cloud_name", "sapodecor",
    			  "api_key", "744836913578885",
    			  "api_secret", "NelBzUIFN9StEnrxysN44rUpeH4"));


	@RequestMapping(value = "/resources/search", method = RequestMethod.GET)
	public List<Object> getGallery(@RequestHeader("Authorization") String token) throws Exception {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String userId = jwtTokenUtil.getIdFromToken(token);
		User user = userRepository.findById(Long.valueOf(userId)).get();
		return listPhotosWithShops(user);
	}
	

	@RequestMapping(value = "/resources/search/prod", method = RequestMethod.GET)
	public List<String> listPhotosViaProduct1(String shop_id, String item_id, @RequestHeader("Authorization") String token) throws Exception{
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String userId = jwtTokenUtil.getIdFromToken(token);
		return listPhotosViaProduct(userId, shop_id, item_id);	
	}


//	@RequestMapping(value = "/upload", method = RequestMethod.POST)
//	public void uploadPhoto1(String item_id, int img_order, String photo_url, @RequestHeader("Authorization") String token){
//		if (token.startsWith("Bearer ")) {
//			token = token.substring(7);
//		}
//		String userId = jwtTokenUtil.getIdFromToken(token);
//		
//		uploadPhoto(userId, item_id, img_order, photo_url);
//	}
	
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public <T> Map<String, Object> uploadPhoto2(@RequestBody Map<String, T> map, @RequestHeader("Authorization") String token){
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String userId = jwtTokenUtil.getIdFromToken(token);
		String shop_id = String.valueOf(map.get("shop_id"));
		String item_id = String.valueOf(map.get("item_id"));
//		int img_order = (int) map.get("img_order"); 
		String id = UUID.randomUUID().toString().replace("-", "");
		String photo_url = (String) map.get("photo_url");
//		System.out.println(photo_url.substring(0, 15));
		Map<String, Object> result = uploadPhoto(userId, shop_id, item_id, id, photo_url);
		return result;
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
	
	
	
	@RequestMapping(value = "/image/update", method = RequestMethod.POST)
	public <T> Map<String, Object> updPhoto(@RequestBody Map<String, T> body, @RequestHeader("Authorization") String token){
		Map<String, Object> result = new HashMap<>();
		String old_url = String.valueOf(body.get("old_url"));
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String userId = jwtTokenUtil.getIdFromToken(token);
		if(!userId.equals(old_url.split("/")[7])) {
			result.put("success", 0);
			return result;
		}
		String[] array = old_url.split("/",8);
		String tmp = array[7];
		String public_id = tmp.split("\\.")[0];
		String photo_url = (String) body.get("update_url");
		result = updatePhoto(public_id, photo_url);
		return result;
	}
	
	/*
	@RequestMapping(value = "/image/delete", method = RequestMethod.DELETE)
	public Object deleteImg(String shop_id, String item_id, String img_id, @RequestHeader("Authorization") String token) throws Exception {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String userId = jwtTokenUtil.getIdFromToken(token);
		StringBuilder path = new StringBuilder();
		path.append(userId);
		path.append("/");
		path.append(shop_id);
		path.append("/");
		path.append(item_id);
		path.append("/");
		path.append(img_id);
		ApiResponse response = cloudinary.api().deleteResources(Arrays.asList(path.toString()), ObjectUtils.emptyMap());  	
		Object result = response.get("deleted");
		return result;
	}
	*/
	
	@RequestMapping(value = "/image/delete", method = RequestMethod.DELETE)
//	public Object deleteImg(String url, @RequestHeader("Authorization") String token) throws Exception {
	public Object deleteImg(@RequestBody Map<String, List<String>> body, @RequestHeader("Authorization") String token) throws Exception {
		List<String> urlList = body.get("URLs");
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String userId = jwtTokenUtil.getIdFromToken(token);
		
		List<Object> result = new ArrayList<>();
		for( String url:urlList) {
			if(!userId.equals(url.split("/")[7])) {
				return new HashMap<>().put("deleted", -1);
			}
			String[] array = url.split("/",8);
			String tmp = array[7];
			String public_id = tmp.split("\\.")[0];
			ApiResponse response = cloudinary.api().deleteResources(Arrays.asList(public_id), ObjectUtils.emptyMap());
			HashMap<String, String> tempObject = (HashMap<String, String>) response.get("deleted");
			Map<String, String> map = new HashMap<>();
			map.put("url", url);
			map.put("status", tempObject.get(public_id));
			result.add(map);
		}
		return result;
	}
	
//	@RequestMapping(value = "/image/removeshop", method = RequestMethod.DELETE)
//	public Object deleteShop(String userId, String shop_id) throws Exception {
//		StringBuilder path = new StringBuilder();
//		path.append(userId);
//		path.append("/");
//		path.append(shop_id);
//		ApiResponse response = cloudinary.api().deleteResourcesByPrefix(path.toString(), ObjectUtils.emptyMap());
//		Object result = response.get("deleted");
//		return result;
//	}
//	@RequestMapping(value = "/image/removeuser", method = RequestMethod.DELETE)
//	public Object deleteUser(String userId) throws Exception {
//		ApiResponse response = cloudinary.api().deleteResourcesByPrefix(userId, ObjectUtils.emptyMap());
//		Object result = response.get("deleted");
//		return result;
//	}
	
	
	///////////////////////////////////////////////////////////////////
	/////
	/////
	/////
	///////////////////////////////////////////////////////////////////
//	public List<String> listPhotos(String userId) throws Exception {
//		StringBuilder param = new StringBuilder();
//		List<String> photos = new ArrayList<>();
//		param.append("folder = ");
//		param.append(userId);
//		param.append("/*");
//		ApiResponse result = cloudinary.search().expression(param.toString()).execute();
//		Object tmp = result.get("resources");
//    	ArrayList arr = (ArrayList)tmp;
//    	Object[] objs = arr.toArray();
//    	for (Object obj : objs) {
//    		HashMap temp = (HashMap) obj;
////    		System.out.println(temp.get("url").toString());
//    		photos.add(temp.get("url").toString());
//    	}
//    	return photos;
//	}
	
	public List<String> listShopPhotos(String userId, String shop_id) throws Exception {
		StringBuilder param = new StringBuilder();
		List<String> photos = new ArrayList<>();
		param.append("folder = ");
		param.append(userId);
		param.append("/");
		param.append(shop_id);
		param.append("/*");
		ApiResponse result = cloudinary.search().expression(param.toString()).execute();
		Object tmp = result.get("resources");
    	ArrayList arr = (ArrayList)tmp;
    	Object[] objs = arr.toArray();
    	for (Object obj : objs) {
    		HashMap temp = (HashMap) obj;
//    		System.out.println(temp.get("url").toString());
    		photos.add(temp.get("secure_url").toString());
    	}
    	return photos;
	}
	
	public List<Object> listPhotosWithShops(User user) throws Exception {
		String userId = String.valueOf(user.getId());
		List<Object> result = new ArrayList<>();
		for (Shop shop:user.getShop()) {
			String shop_id = String.valueOf(shop.getShop_id());
			List<String> photoList = listShopPhotos(userId, shop_id);
			Map<String, Object> shop_photos = new HashMap<>();
			shop_photos.put("shop_id", shop.getShop_id());
			shop_photos.put("name", shop.getName());
			shop_photos.put("photos", photoList);
			result.add(shop_photos);
		}
		return result;
	}
	
	
	
	public List<String> listPhotosViaProduct(String userId, String shop_id, String prod) throws Exception{
		StringBuilder param = new StringBuilder();
		List<String> photos = new ArrayList<>();
		param.append("folder:");
		param.append(userId);
		param.append("/");
		param.append(shop_id);
		param.append("/");
		param.append(prod);
		param.append("/*");
		ApiResponse result = cloudinary.search().expression(param.toString()).execute();
		Object tmp = result.get("resources");
    	ArrayList arr = (ArrayList)tmp;
    	Object[] objs = arr.toArray();
    	for (Object obj : objs) {
    		HashMap temp = (HashMap) obj;
//    		System.out.println(temp.get("url").toString());
    		photos.add(temp.get("secure_url").toString());
    	}
    	return photos;
		
	}
	
	
	public Map<String, Object> uploadPhoto(String userId, String shop_id, String productCode, String id, String photo){
		Map<String, Object> result = new HashMap<>();
		result.put("success", 0);
		
		 StringBuilder path = new StringBuilder();
	     path.append(userId);
	     path.append("/");
	     path.append(shop_id);
	     path.append("/");
	     path.append(productCode);
	     path.append("/");
	     path.append(id);

	     Map params = ObjectUtils.asMap("public_id", path.toString());
	     try {
			Map uploadResult = cloudinary.uploader().upload(photo, params);
			result.put("success", 1);
//			logger.debug(uploadResult.toString());
//			logger.debug(uploadResult.get("secure_url").toString());
			result.put("url", uploadResult.get("secure_url").toString());
			
//			Set load = uploadResult.entrySet();
//	    	for (Object obj : load) {
//	    		System.out.println(obj);
//	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	     
	    return result;
	}
	
	public Map<String, Object> updatePhoto(String public_id, String photo_url) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", 0);
		
		Map params = ObjectUtils.asMap("public_id", public_id);
	     try {
			Map uploadResult = cloudinary.uploader().upload(photo_url, params);
			result.put("success", 1);
			result.put("url", uploadResult.get("secure_url").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	     
	    return result;
	}

}
