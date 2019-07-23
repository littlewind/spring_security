package com.littlewind.demo.controller;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test/JSON")
  public JSONObject testJSON() {
    	JSONObject result = new JSONObject();
    	result.put("success", true);
    	return result;
  }

}
