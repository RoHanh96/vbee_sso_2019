package com.vbee.springbootmongodbnewspapersrestapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;
import com.vbee.springbootmongodbnewspapersrestapi.model.ResponseMessage;
import com.vbee.springbootmongodbnewspapersrestapi.service.ICategoryService;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
	@Autowired
	ICategoryService categoryService;
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
	
	// Create a Singer category
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> addCategory(@RequestBody @Valid Category category){
		 Category categoryExists = categoryService.getCategoryById(category.getId());
		 ResponseMessage resMessage = new ResponseMessage();
		 if(categoryExists == null) {
			 Category newCategory = categoryService.insertCategory(category);
			 resMessage.setMessage("Tạo mới thành công");
			 resMessage.setStatus(1);
			 resMessage.setResults(newCategory);
			 return ResponseEntity.ok(resMessage);
		 }
		 resMessage.setMessage("Category đã tồn tại!!!");
		 resMessage.setStatus(0);
		 return ResponseEntity.ok(resMessage);
	}
	
	// Get All category
	@GetMapping()
	public ResponseEntity<ResponseMessage> getAllCategory(){
		List<Category> list = categoryService.findAll();
		ResponseMessage resMessage = new ResponseMessage();
		if(list.isEmpty()) {
			resMessage.setMessage("Không tìm thấy category!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage); 
		}
		resMessage.setMessage("Đã tìm thấy: " + list.size() + " category");
		resMessage.setStatus(1);
		resMessage.setResults(list);
		return ResponseEntity.ok(resMessage);
	}
	
	// Get Singer a category
	@GetMapping("/{categoryId}")
	public ResponseEntity<ResponseMessage> getCategoryById(@PathVariable Integer categoryId) {
		ResponseMessage resMessage = new ResponseMessage();
		Category category = categoryService.getCategoryById(categoryId);
		if(category == null) {
			resMessage.setMessage("Không tìm thấy category!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage); 
		}
		resMessage.setMessage("Đã tìm thấy category: " + category.getName());
		resMessage.setStatus(1);
		resMessage.setResults(category);
		return ResponseEntity.ok(resMessage);
	}
	
	//Update a singer category
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> updateCategory(@RequestBody @Valid Category category) {
		ResponseMessage resMessage = new ResponseMessage();
		if (categoryService.checkCategoryExists(category)) {
			Category newCategory = categoryService.updateCategory(category);
			resMessage.setMessage("Update thành công");
			resMessage.setStatus(1);
			resMessage.setResults(newCategory);
			return ResponseEntity.ok(resMessage);
		} else {
			resMessage.setMessage("Category không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
	}
	
	//Delete a singer category
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ResponseMessage> delete(@PathVariable Integer categoryId) {
		ResponseMessage resMessage = new ResponseMessage();
		Category category = categoryService.getCategoryById(categoryId);
		if (category == null) {
			resMessage.setMessage("Category không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		} else {
			categoryService.deleteCategory(categoryId);
			resMessage.setMessage("Delete thành công");
			resMessage.setStatus(1);
			return ResponseEntity.ok(resMessage);
			
		}
	}
	
}