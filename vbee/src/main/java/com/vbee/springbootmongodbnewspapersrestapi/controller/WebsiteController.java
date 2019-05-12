package com.vbee.springbootmongodbnewspapersrestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Website;
import com.vbee.springbootmongodbnewspapersrestapi.model.ResponseMessage;
import com.vbee.springbootmongodbnewspapersrestapi.service.IWebsiteService;

@RestController
@RequestMapping("/api/v1/websites")
public class WebsiteController {
	
	@Autowired
	IWebsiteService websiteService;
	
	@GetMapping
	public ResponseEntity<ResponseMessage> getAllWebsite(){
		ResponseMessage resMessage = new ResponseMessage();
		List<Website> list = websiteService.findAll();
		if(list.isEmpty()) {
			resMessage.setMessage("Không tìm thấy kết quả !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Tìm thấy: " + list.size() + " kết quả !!!");
		resMessage.setStatus(1);
		resMessage.setResults(list);
		return ResponseEntity.ok(resMessage); 
	}
	
	@PostMapping
	public ResponseEntity<ResponseMessage> createWebsite(@RequestBody Website website ){
		ResponseMessage resMessage = new ResponseMessage();
		Website websiteExists = websiteService.findByName(website.getName());
		if(websiteExists != null) {
			resMessage.setMessage("Website đã tồn tại !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		Website websiteNew = websiteService.insertWebsite(website.getName(), website.getUrl());
		resMessage.setMessage("Tạo mới thành công !!!");
		resMessage.setStatus(1);
		resMessage.setResults(websiteNew);
		return ResponseEntity.ok(resMessage); 
	}
}
