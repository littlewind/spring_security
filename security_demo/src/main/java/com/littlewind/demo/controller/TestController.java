package com.littlewind.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
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
	
	@GetMapping("/test/log")
	public String testLog() {
		logger.debug("debug");
		logger.warn("warn");
		logger.error("error");
		logger.info("info");
		logger.trace("trace");
		return "ahihi";
	}


}
