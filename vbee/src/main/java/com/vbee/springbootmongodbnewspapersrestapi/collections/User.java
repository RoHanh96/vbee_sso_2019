package com.vbee.springbootmongodbnewspapersrestapi.collections;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User {
	
	@Id
	String id;
	String voiceSelected;
	List<Integer> articleIds;
	List<Integer> categoryUnCheckIds;
	List<String> websiteUnCheckIds;
	List<UserCategoryChosen> categoriesChosen;
	List<Integer> articleIdsPlayed;
	
	
	public User() {
		this.voiceSelected = "";
		this.articleIds = new ArrayList<>();
		this.categoryUnCheckIds = new ArrayList<>();
		this.websiteUnCheckIds = new ArrayList<>();
		this.categoriesChosen = new ArrayList<>();
		this.articleIdsPlayed = new ArrayList<>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVoiceSelected() {
		return voiceSelected;
	}
	public void setVoiceSelected(String voiceSelected) {
		this.voiceSelected = voiceSelected;
	}

	public List<Integer> getArticleIds() {
		return articleIds;
	}

	public void setArticleIds(List<Integer> articleIds) {
		this.articleIds = articleIds;
	}

	public List<Integer> getCategoryUnCheckIds() {
		return categoryUnCheckIds;
	}

	public void setCategoryUnCheckIds(List<Integer> categoryUnCheckIds) {
		this.categoryUnCheckIds = categoryUnCheckIds;
	}

	public List<String> getWebsiteUnCheckIds() {
		return websiteUnCheckIds;
	}

	public void setWebsiteUnCheckIds(List<String> websiteUnCheckIds) {
		this.websiteUnCheckIds = websiteUnCheckIds;
	}

	public List<UserCategoryChosen> getCategoriesChosen() {
		return categoriesChosen;
	}

	public void setCategoriesChosen(List<UserCategoryChosen> categoriesChosen) {
		this.categoriesChosen = categoriesChosen;
	}

	public List<Integer> getArticleIdsPlayed() {
		return articleIdsPlayed;
	}

	public void setArticleIdsPlayed(List<Integer> articleIdsPlayed) {
		this.articleIdsPlayed = articleIdsPlayed;
	}
	
}
