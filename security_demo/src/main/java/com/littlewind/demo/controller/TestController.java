package com.littlewind.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {
	@GetMapping("/test/JSON")
	public JSONObject testJSON() {
		JSONObject result = new JSONObject();
		result.put("success", true);
    	return result;
	}
	
	@GetMapping("/test/JSONString")
	public String testJSONString() {
		String result = "{\"message\": \"Hello World\"}";
    	return result;
	}
	
	@GetMapping("/test/list")
	public List<Long> testListString() {
		List<Long> mList = new ArrayList<>();
		mList.add(1023L);
		mList.add(2048L);
    	return mList;
	}


}
