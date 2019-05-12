package com.vbee.springbootmongodbnewspapersrestapi.dao;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;
import com.vbee.springbootmongodbnewspapersrestapi.collections.User;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleSearchReponse;

public interface IArticleDAO {

	List<Article> fullTextSearch(String keyword, String fields, Pageable pageable);

	List<Article> getArticlesByTagName(String tagName, String fields, Pageable pageable);

	List<Article> getArticlesByFields(String[] fields, Pageable pageable);

	Article getArticleByIdAndFields(Integer articleId, String[] fields);

	List<Article> getArticlesCategoryPageAndFields(Category category, String[] properties, Pageable pageable);

	List<Article> getArticlesByPageHadSynthesized(PageRequest pageRequest);

	List<Article> getArticlesByFiledsAndPageHadSynthesized(String[] properties, PageRequest pageRequest);

	List<Article> getArticlesHadSynthesizedCategoryPage(Category category, PageRequest pageRequest);

	List<Article> getArticlesHadSynthesizedCategoryPageAndFields(Category category, String[] properties,
			PageRequest pageRequest);

	List<Article> getArticlesByPageAndCategoriesHadSynthesized(List<Category> categories, PageRequest pageRequest);

	List<Article> getArticlesByPageAndWebsitesHadSynthesized(String[] websiteNames, PageRequest pageRequest);

	List<Article> getArticlesByPageAndWebsitesAndCategoriesHadSynthesized(String[] websiteNames,
			List<Category> categories, PageRequest pageRequest);

	List<Article> getArticlesByFieldsAndPageAndCategoriesHadSynthesized(String[] properties, List<Category> categories,
			PageRequest pageRequest);

	List<Article> getArticlesByFieldsAndPageAndWebsitesHadSynthesized(String[] properties, String[] websiteNames, PageRequest pageRequest);

	List<Article> getArticlesByFieldsAndPageAndWebsitesAndCategoriesHadSynthesized(String[] properties, String[] websiteNames,
			List<Category> categories, PageRequest pageRequest);

	List<Article> fullTextSearchAndCategoriesAndWebsites(String keyword, String fields, String websiteNames,
			String categoriesId, PageRequest pageRequest);

	List<Article> getArticlesByFieldsAndPageAndCategoriesUncheckHadSynthesized(String[] properties,
			List<Category> categories, PageRequest pageRequest);

	List<Article> getArticlesByFieldsAndPageAndWebsitesUncheckHadSynthesized(String[] properties,
			List<String> websiteIds, PageRequest pageRequest);

	List<Article> getArticlesByFieldsAndPageAndWebsitesUncheckAndCategoriesUncheckHadSynthesized(String[] properties,
			List<String> websiteIds, List<Category> categories, PageRequest pageRequest);

	List<Article> getArticlesRecommended(User userExists, String fields, String articleIds, List<Category> categoriesChosen, PageRequest pageRequest);

	ArticleSearchReponse searchArticleMvc(Integer page, Integer size, String fields, String keyword, Integer categoryId,
			String websiteName, String sort, Integer synthesisType);

	List<Article> findArticlesByArticleNext(Article article, String fields, Integer categoryId, Integer size);

	List<Article> findByCategorySynthesizedAndPaging(User userExists, Category category, String fields,
			PageRequest pageRequest);

	Article findByWeather(String fields);


}
