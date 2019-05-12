package com.vbee.springbootmongodbnewspapersrestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Tag;
import com.vbee.springbootmongodbnewspapersrestapi.model.ResponseMessage;
import com.vbee.springbootmongodbnewspapersrestapi.service.ITagService;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {
	
	@Autowired
	ITagService tagService;
	
	@GetMapping()
	public ResponseEntity<ResponseMessage> getAllTag(){
		List<Tag> list = tagService.findAll();
		 ResponseMessage resMessage = new ResponseMessage();
		if(list.isEmpty()) {
			resMessage.setMessage("Không tìm thấy tags!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage); 
		}
		resMessage.setMessage("Đã tìm thấy: " + list.size() + " tags");
		resMessage.setStatus(1);
		resMessage.setResults(list);
		return ResponseEntity.ok(resMessage);
	}
}
