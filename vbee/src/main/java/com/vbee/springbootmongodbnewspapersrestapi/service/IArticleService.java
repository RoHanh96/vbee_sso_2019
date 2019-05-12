package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;
import com.vbee.springbootmongodbnewspapersrestapi.collections.User;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleSearchReponse;

public interface IArticleService {
	Article insertArticle(Article article);
	
	Article updateArticle(Article article);
	
	Article getArticleById(Integer articleId);
	
	void deleteArticleById(Integer articleId);
	
	boolean checkArticleExist(Article article);
	
	List<Article> fullTextSearch(String keyword, int page, int size, String fields);

	void updateUserIds(Article article, String userId);

	void removeUserIds(Article article, String userId);

	List<Article> getArticlesByPage(int start, int size);

	List<Article> getArticleByTagPage(String name, int page, int size, String fields);

	List<Article> getArticlesCategoryPage(Category category, int page, int size);

	List<Article> getArticleByFieldsAndPage(String fields, Integer page, Integer size);

	Article getArticleByIdAndFields(Integer articleId, String fields);

	List<Article> getArticlesCategoryPageAndFields(Category category, int page, int size, String fields);

	List<Article> getArticlesByPageHadSynthesized(Integer page, Integer size, String websites, String categoryIds);

	List<Article> getArticleByFieldsAndPageHadSynthesized(String fields, Integer page, Integer size, String websites, String categoryIds);

	List<Article> getArticlesHadSynthesizedCategoryPage(Category category, int page, int size);

	List<Article> getArticlesHadSynthesizedCategoryPageAndFields(Category category, int page, int size, String fields);

	Article getArticleByCrawlerId(Integer crawlerId);

	List<Article> fullTextSearchAndWebsiteAndCategories(String keyword, int page, int size, String fields,
			String websites, String categories);

	List<Article> getArticleByFieldsAndPageHadSynthesized(String fields, Integer page, Integer size,
			List<String> websiteUnCheckIds, List<Integer> categoryUnCheckIds);

	List<Article> getArticleByOrderArticleIds(String articleIds, String fields);

	ArticleSearchReponse searchArticleMvc(Integer page, Integer size, String fields, String keyword, Integer categoryId,
			String websiteName, String sort, Integer synthesisType);

	void updateActiveArticle(Article article, Integer status);

	List<Article> findArticlesByArticleNext(Article article, String fields, Integer categoryId, Integer size);

	List<Article> findByCategorySynthesizedAndPaging(User userExists, Category category, Integer size, Integer page,
			String fields);

	Article updateArticleMvc(Article articleExsists, Article article);

	List<Article> findAll();

	Article findByWeather(String fields);

	List<Article> findByWebsite(String websiteName);

}
