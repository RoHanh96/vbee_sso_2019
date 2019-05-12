package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;
import com.vbee.springbootmongodbnewspapersrestapi.model.Config;
import com.vbee.springbootmongodbnewspapersrestapi.repository.CategoryMongoRepository;
import com.vbee.springbootmongodbnewspapersrestapi.ulti.UserServiceConnection;

@Service
public class CategoryService implements ICategoryService{

	@Autowired
	CategoryMongoRepository categoryRepository;
	
	@Autowired
	Config config;
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
	
	@Override
	public Category insertCategory(Category category) {
		category = categoryRepository.save(category);
		try {
			UserServiceConnection.updateSettingDefault(null, category, config.getUserServicePath(), config.getUserSettingDefaultVersion());
		} catch (Exception e) {
			logger.info("Error in updateSettingDefault --- error: " + e.getMessage());
			e.printStackTrace();
		}
		return category;
	}

	@Override
	public Category getCategoryById(Integer id) {
		return categoryRepository.findOne(id);
	}

	@Override
	public Category updateCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		Category category = categoryRepository.findOne(categoryId);
		categoryRepository.delete(category);
	}

	@Override
	public boolean checkCategoryExists(Category category) {
		if(categoryRepository.exists(category.getId())) {
			return true;	
		}
		Category categoryExists = categoryRepository.findCategoryByName(category.getName());
		if(categoryExists != null)
			return true;	
		return false;
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

}
