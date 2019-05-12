package com.vbee.springbootmongodbnewspapersrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Config;
import com.vbee.springbootmongodbnewspapersrestapi.model.ResponseMessage;
import com.vbee.springbootmongodbnewspapersrestapi.service.IConfigService;

@RestController
@RequestMapping("/api/v1/configs")
public class ConfigController {
	
	@Autowired
	IConfigService configService;
	
	@Autowired
	com.vbee.springbootmongodbnewspapersrestapi.model.Config configPropeties;
	
	@PostMapping
	public ResponseEntity<ResponseMessage> createConfig(@RequestBody Config config) {
		ResponseMessage resMessage = new ResponseMessage();
		configService.create(config);
		resMessage.setStatus(1);
		resMessage.setResults(config); 
		resMessage.setMessage("Lấy thành công");
		return ResponseEntity.ok(resMessage);
	}
	
	@GetMapping("/check-version")
	public ResponseEntity<ResponseMessage> checkVersion() {
		ResponseMessage resMessage = new ResponseMessage();
		Config config = configService.findByVersion(configPropeties.getConfigVersion());
		if(config == null) {
			resMessage.setStatus(0);
			resMessage.setMessage("config version không tồn tại. Kiểm tra lại db");
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setStatus(1); 
		resMessage.setResults(config);
		resMessage.setMessage("Lấy thành công");
		return ResponseEntity.ok(resMessage);
	}
	
	@PostMapping("/update-version")
	public ResponseEntity<ResponseMessage> updateVersion(@RequestBody Config config) {
		ResponseMessage resMessage = new ResponseMessage();
		Config configExists = configService.findByVersion(configPropeties.getConfigVersion());
		if(configExists == null) {
			resMessage.setStatus(0);
			resMessage.setMessage("config version không tồn tại. Kiểm tra lại db");
			return ResponseEntity.ok(resMessage);
		}
		Config configUpdated = configService.updateConfig(config, configExists);
		resMessage.setStatus(1);
		resMessage.setResults(configUpdated);
		resMessage.setMessage("Cập nhật thành công");
		return ResponseEntity.ok(resMessage);
	}
}
