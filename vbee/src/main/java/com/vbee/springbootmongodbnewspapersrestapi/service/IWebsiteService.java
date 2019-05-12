package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Website;

public interface IWebsiteService {

	Website insertWebsite(String websiteName, String websiteUrl);

	Website findByName(String websiteName);

	Website findById(String websiteId);

	Website insertWebsite(Website website);

	List<Website> findAll();

}
