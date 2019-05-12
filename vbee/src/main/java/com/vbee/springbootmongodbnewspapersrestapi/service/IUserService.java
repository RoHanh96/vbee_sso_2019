package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.User;

public interface IUserService {
	User insertUser(User user);
	
	User getUserById(String userId);
	
	User updateUser(User user, User userExists);
	
	void deleteUser(String userId);
	
	boolean checkUserExists(String userId);

	List<Integer> getArticleIdsByUserId(String userId);

	void updateArticleIds(User user, Integer articleId);

	List<User> findAll();

	void removeArticleIds(User user, Integer articleId);

	boolean saveUserToUserService(User userExists);

	List<Article> findArticleByRecommendation(User userExists, String fields, String articleIds, Integer sIZE_OF_RECOMMEND);

	List<Article> checkArticleVoicesAndRemoveNullAttribute(List<Article> articles);

	boolean saveActionPlayArticle(User userExists, Article articleExists, String voiceName, Integer duration,
			Integer listen);

	boolean saveActionChooseArticle(String userId, Article articleExists);

	void saveUncheckCategoryWebsites(String unActiveCategoryIds, String unActiveWebsiteIds, String activeCategoryIds,
			String activeWebsiteIds);


}
