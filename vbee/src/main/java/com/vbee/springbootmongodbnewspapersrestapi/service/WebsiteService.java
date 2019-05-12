package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Website;
import com.vbee.springbootmongodbnewspapersrestapi.model.Config;
import com.vbee.springbootmongodbnewspapersrestapi.repository.WebsiteMongoRepository;
import com.vbee.springbootmongodbnewspapersrestapi.ulti.UserServiceConnection;

@Service
public class WebsiteService implements IWebsiteService{

	@Autowired
	WebsiteMongoRepository websiteMongoRepository;
	
	@Autowired
	Config config;
	
	@Override
	public Website insertWebsite(String websiteName, String websiteUrl) {
		Website website = new Website();
		website.setName(websiteName);
		website.setUrl(websiteUrl);
		website = websiteMongoRepository.save(website);
		try {
			UserServiceConnection.updateSettingDefault(website, null, config.getUserServicePath(), config.getUserSettingDefaultVersion());
		} catch (Exception e) {
			System.out.println("Error in updateSettingDefault -- WebsiteService. Message: " + e.getMessage());
			e.printStackTrace();
		}
		return website;
	}

	@Override
	public Website findByName(String websiteName) {
		return websiteMongoRepository.findByName(websiteName.trim());
	}

	@Override
	public Website findById(String websiteId) {
		return websiteMongoRepository.findOne(websiteId);
	}

	@Override
	public Website insertWebsite(Website website) {
		return websiteMongoRepository.save(website);
	}

	@Override
	public List<Website> findAll() {
		return websiteMongoRepository.findAll();
	}

}
