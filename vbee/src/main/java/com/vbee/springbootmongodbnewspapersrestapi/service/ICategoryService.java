package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;

public interface ICategoryService {
	
	Category insertCategory(Category category);
	
	Category getCategoryById(Integer categoryId);
	
	Category updateCategory(Category category);
	
	void deleteCategory(Integer categoryId);
	
	boolean checkCategoryExists(Category category);

	List<Category> findAll();
	
}
