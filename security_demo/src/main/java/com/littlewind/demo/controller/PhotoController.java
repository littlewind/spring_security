package com.littlewind.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		int img_order = (int) map.get("img_order"); 
		String photo_url = (String) map.get("photo_url");
		System.out.println(photo_url.substring(0, 15));
		Map<String, Object> result = uploadPhoto(userId, shop_id, item_id, img_order, photo_url);
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
	
	
	
	@RequestMapping(value = "/image/delete", method = RequestMethod.DELETE)
	public Object deleteImg(String shop_id, String item_id, int order, @RequestHeader("Authorization") String token) throws Exception {
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
		path.append(order);
		ApiResponse response = cloudinary.api().deleteResources(Arrays.asList(path.toString()), ObjectUtils.emptyMap());  	
		Object result = response.get("deleted");
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
    		System.out.println(temp.get("url").toString());
    		photos.add(temp.get("url").toString());
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
    		photos.add(temp.get("url").toString());
    	}
    	return photos;
		
	}
	
	
	public Map<String, Object> uploadPhoto(String userId, String shop_id, String productCode, int id, String photo){
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
			logger.debug(uploadResult.toString());
//			Set load = uploadResult.entrySet();
//	    	for (Object obj : load) {
//	    		System.out.println(obj);
//	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	    return result;
	}

}
