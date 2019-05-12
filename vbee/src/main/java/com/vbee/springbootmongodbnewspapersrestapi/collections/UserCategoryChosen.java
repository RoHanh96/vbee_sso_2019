package com.vbee.springbootmongodbnewspapersrestapi.collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userCategoryChosen")
public class UserCategoryChosen {
	@Id
	private String id;
	private String userId;
	private Integer categoryId;
	private Integer totalChosen;

	public UserCategoryChosen(String userId, Integer categoryId) {
		this.totalChosen = 0;
		this.userId = userId;
		this.categoryId = categoryId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getTotalChosen() {
		return totalChosen;
	}

	public void setTotalChosen(Integer totalChosen) {
		this.totalChosen = totalChosen;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return this.id;
	}
}
