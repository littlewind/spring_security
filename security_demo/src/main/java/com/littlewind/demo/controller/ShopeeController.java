package com.littlewind.demo.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.littlewind.demo.util.MyConst;

@RestController
@RequestMapping("/api/v1")
public class ShopeeController {
	
	public static final long SHOP_ID = 205134;
	
	Logger logger = LoggerFactory.getLogger(ShopeeController.class);
	
	@GetMapping("/test/getItemList")
	public String ahihi(int offset, int entries, long shopid) {
		return getItemList(offset, entries, shopid);
	}
	
	@GetMapping("/test/getItemDetail")
	public String getProductDetail(long item_id, long shopid) {
		return getItemDetail(item_id, shopid);
	}
	
	@PostMapping("/test/updateItemImg")
	public String updateProductImg(){
		return updateItemImgTest();
	}
	
	@GetMapping("/test/getItemImg")
	public List<String> getProductImg(long item_id, long shopid){		
		return getItemImg(item_id, shopid);
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////
	///////////////////
	///////////////////
	///////////////////
	///////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private String getItemList(int offset, int entries, long shopid) {
		String bodyStr = String.format("{\"partner_id\": %d, \"shopid\": %d,\"pagination_entries_per_page\": %d, \"pagination_offset\": %d, \"timestamp\": ", MyConst.TEST_PARTNER_ID, shopid, entries, offset);
		return callShopeeAPI(MyConst.GetItemList_URL, bodyStr);
	}
	
	private String getItemDetail(long item_id, long shopid) {
		String bodyStr = String.format("{\"item_id\": %d, \"partner_id\": %d, \"shopid\": %d, \"timestamp\": ", item_id, MyConst.TEST_PARTNER_ID, shopid);
		return callShopeeAPI(MyConst.GetItemDetail_URL, bodyStr);
	}
	
	public List<String> getItemImg(long item_id, long shopid){
		String itemDetail = getItemDetail(item_id, shopid);
		JSONObject jObject = new JSONObject(itemDetail);
		JSONObject item = jObject.getJSONObject("item");
		JSONArray photoArray = item.getJSONArray("images");
		List<String> photos = new ArrayList<>();
		for( int i=0; i<photoArray.length(); i++) {
			String photo_url = photoArray.getString(i);
			photos.add(photo_url);
		}
		
		return photos;
	}
	
	private String updateItemImgTest() {
		String bodyStr = "{" + 
				"\"shopid\": 205134," + 
				"\"partner_id\": 840386," + 
				"\"item_id\": 1531804," + 
				"\"images\": [" + 
				"\"https://res.cloudinary.com/sapodecor/image/upload/v1565151104/test_zbeip1.png\"" + 
				"]," + 
				"\"timestamp\": ";
		return callShopeeAPI(MyConst.UpdateItemImg_URL, bodyStr);
	}
	
	
	private String callShopeeAPI(String URL, String bodyStr) {
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    headers.set("Accept", "application/json");
	      
		String timestamp = String.valueOf(Instant.now().getEpochSecond());	
	    bodyStr = bodyStr+ timestamp +"}";
		try {
			String authen = encode(MyConst.TEST_KEY, URL+"|"+bodyStr);
	        headers.set("Authorization", authen); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(bodyStr, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(URL, request, String.class);
	    
//	    System.out.println(result);
        logger.debug("\n"+result.toString());
		return result.getBody();
	}
	
	public static String encode(String key, String data) throws Exception {
		  Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		  SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		  sha256_HMAC.init(secret_key);

		  return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
		}


	////////////
	private void callGetAPI(String URL) {
		RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(URL, String.class);
	    System.out.println(result);
	}
	
	
}
