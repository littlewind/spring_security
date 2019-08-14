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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.littlewind.demo.model.shopeerequest.AddItemImgBody;
import com.littlewind.demo.model.shopeerequest.DeleteItemBody;
import com.littlewind.demo.model.shopeerequest.DeleteItemImgBody;
import com.littlewind.demo.model.shopeerequest.GetShopInfoBody;
import com.littlewind.demo.model.shopeerequest.UpdateItemImgBody;
import com.littlewind.demo.util.MyConst;

@RestController
@RequestMapping("/api/v1")
public class ShopeeController {
	
	public static final long SHOP_ID = 205134;
	public static final long TRANG_SHOP_ID = 94115363;
	
	Logger logger = LoggerFactory.getLogger(ShopeeController.class);
	
	@GetMapping("/test/getItemList")
	public String ahihi(int offset, int entries, long shopid) {
		return getItemList(offset, entries, shopid);
	}
	
	@GetMapping("/test/getItemDetail")
	public String getProductDetail(long item_id, long shopid) {
		return getItemDetail(item_id, shopid);
	}
	
	@GetMapping("/test/getItemImg")
	public List<String> getProductImg(long item_id, long shopid){		
		return getItemImg(item_id, shopid);
	}
	
	@PostMapping("/test/updateItemImg")
	public String updateProductImg(@RequestBody UpdateItemImgBody body){
		return updateItemImg(body);
	}

	@PostMapping("/test/addItemImg")
	public String addProductImg(@RequestBody AddItemImgBody body){		
		return addItemImg(body);
	}
	
//	@PostMapping("/test/insertItemImg")
//	public String insertProductImg(){		
//		return insertItemImgTest();
//	}
	
	@PostMapping("/test/deleteItemImg")
	public String deleteProductImg(@RequestBody DeleteItemImgBody body){		
		return deleteItemImg(body);
	}
	
	@PostMapping("/test/deleteItem")
	public String deleteProduct(@RequestBody DeleteItemBody body){		
		return deleteItem(body);
	}
	
//	@PostMapping("/test/noItems")
//	public Map<String, Integer> getNoItems(long partner_id, long shopid){		
//		Map<String, Integer> result = new HashMap<>();
//		String bodyStr = 
//		String itemListStr = 
//		return result;
//	}
	
	@PostMapping("/test/getShopInfo")
	public String getShopInfo(@RequestBody GetShopInfoBody body){		
		return getShopInformation(body);
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
	
//	private void getFullItemList(long shopid) {
//		int offset = 0;
//		int entries = 100;
//		String bodyStr = String.format("{\"partner_id\": %d, \"shopid\": %d,\"pagination_entries_per_page\": %d, \"pagination_offset\": %d, \"timestamp\": ", MyConst.TEST_PARTNER_ID, shopid, entries, offset);
//	}
	
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
	
	/*
//	private String updateItemImgTest() {
//		String bodyStr = "{" + 
//				"\"shopid\": 94115363," + 
//				"\"partner_id\": "+ MyConst.TEST_PARTNER_ID +"," + 
//				"\"item_id\": 2625380256," + 
//				"\"images\": [" + 
//				"\"https://res.cloudinary.com/sapodecor/image/upload/v1565228874/op_lung_meo_2.jpg\"" + "," +
//				"\"https://res.cloudinary.com/sapodecor/image/upload/v1565151104/test_zbeip1.png\"" +
//				"]," + 
//				"\"timestamp\": ";
//		return callShopeeAPI(MyConst.UpdateItemImg_URL, bodyStr);
//	}
//	
	*/
	private String updateItemImg(UpdateItemImgBody body) {
		String bodyStr = "{" + 
				"\"shopid\": "+body.getShopid()+"," + 
				"\"partner_id\": "+body.getPartner_id() +"," + 
				"\"item_id\": "+body.getItem_id()+"," + 
				"\"images\": [" + 
				body.serializeImgURL() +
				"]," + 
				"\"timestamp\": ";
		return callShopeeAPI(MyConst.UpdateItemImg_URL, bodyStr);
	}
	
	/*
//	private String addItemImgTest() {
//		String bodyStr = "{"+
//				"\"item_id\": 2625380256," + 
//				"\"images\": [" + 
//				"\"https://res.cloudinary.com/sapodecor/image/upload/v1565151104/test_zbeip1.png\"" + 
//				"]," + 
//				"\"partner_id\": 842940," + 
//				"\"shopid\": 94115363," + 
//				"\"timestamp\": ";
//		return callShopeeAPI(MyConst.AddItemImg_URL, bodyStr);
//	}
	*/
	
	private String addItemImg(AddItemImgBody body) {
		String bodyStr = "{"+
				"\"item_id\": "+body.getItem_id()+"," + 
				"\"images\": [" + 
				body.serializeImgURL()+ 
				"]," + 
				"\"partner_id\": "+body.getPartner_id()+"," + 
				"\"shopid\": "+body.getShopid()+"," + 
				"\"timestamp\": ";
		return callShopeeAPI(MyConst.AddItemImg_URL, bodyStr);
	}
	
	/*
//	private String insertItemImgTest() {
//		String bodyStr = "{" + 
//				"\"item_id\": 2625380256," + 
//				"\"image_url\": " + "\"https://res.cloudinary.com/sapodecor/image/upload/v1563240575/samples/animals/cat.jpg\"" + ","+
//				"\"image_position\": 2," + 
//				"\"partner_id\": 842940," + 
//				"\"shopid\": 94115363," + 
//				"\"timestamp\": ";
//		return callShopeeAPI(MyConst.InsertItemImg_URL, bodyStr);
//	}
	*/

	/*
	private String deleteItemImgTest() {
		String bodyStr = "{" + 
				"\"item_id\": 2625380256," + 
				"\"images\": [\"https://cf.shopee.vn/file/03408b99a4500dfcc4fa0680b4142460\"]," + 
				"\"positions\": [" + 
				"3" + 
				"]," + 
				"\"partner_id\": 842940," + 
				"\"shopid\": 94115363," + 
				"\"timestamp\": ";
		return callShopeeAPI(MyConst.DeleteItemImg_URL, bodyStr);
	}
	*/
	
	private String deleteItemImg(DeleteItemImgBody body) {
		String bodyStr = "{" + 
				"\"item_id\": "+body.getItem_id()+"," + 
				"\"images\": ["+
				body.serializeImgURL() +
				"]," + 
				"\"positions\": [" + 
				body.serializePositions() + 
				"]," + 
				"\"partner_id\": "+body.getPartner_id()+"," + 
				"\"shopid\": "+body.getShopid()+"," + 
				"\"timestamp\": ";
		return callShopeeAPI(MyConst.DeleteItemImg_URL, bodyStr);
	}
	
	
	private String deleteItem(DeleteItemBody body) {
		String bodyStr = "{" + 
				"\"item_id\": "+body.getItem_id()+"," + 
				"\"partner_id\": "+body.getPartner_id()+"," + 
				"\"shopid\": "+body.getShopid()+"," + 
				"\"timestamp\": ";
		
		return callShopeeAPI(MyConst.DeleteItem_URL, bodyStr);
	}
	
	private String getShopInformation(GetShopInfoBody body) {
		String bodyStr = "{" + 
				"\"partner_id\": "+body.getPartner_id()+"," + 
				"\"shopid\": "+body.getShopid()+"," + 
				"\"timestamp\": ";
		return callShopeeAPI(MyConst.GetShopInfo_URL, bodyStr);
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
	/*
	 * private void callGetAPI(String URL) { RestTemplate restTemplate = new
	 * RestTemplate(); String result = restTemplate.getForObject(URL, String.class);
	 * System.out.println(result); }
	 */
	
	
}
