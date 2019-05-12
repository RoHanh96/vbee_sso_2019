package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;
import com.vbee.springbootmongodbnewspapersrestapi.collections.User;
import com.vbee.springbootmongodbnewspapersrestapi.config.AppConstant;
import com.vbee.springbootmongodbnewspapersrestapi.dao.IArticleDAO;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleSearchReponse;
import com.vbee.springbootmongodbnewspapersrestapi.repository.ArticleMongoRepository;
import com.vbee.springbootmongodbnewspapersrestapi.repository.CategoryMongoRepository;
import com.vbee.springbootmongodbnewspapersrestapi.repository.UserMongoRepository;

@Service
public class ArticleService implements IArticleService {

	@Autowired
	ArticleMongoRepository articleRepository;

	@Autowired
	CategoryMongoRepository categoryRepository;

	@Autowired
	UserMongoRepository userRepository;

	@Autowired
	IArticleDAO articleDAO;

	@Autowired
	NextSquenceService nextSquenceService;
	
	@Autowired
	IVoiceService voiceService;

	private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

	@Override
	public Article insertArticle(Article article) {
		article.setId(nextSquenceService.getNextSquence("article"));
		article.setCountAudioSynthesize(0);
		article.setStatus(0);
		article.setTotalListen(0);
		article.setTotalChoose(0);
		article.setListeningRate(0.00);
		return articleRepository.save(article);
	}

	@Override
	public Article updateArticle(Article article) {
		return articleRepository.save(article);
	}

	@Override
	public Article getArticleById(Integer articleId) {
		return articleRepository.findOne(articleId);
	}

	@Override
	public void deleteArticleById(Integer articleId) {
		Article article = articleRepository.findOne(articleId);
		articleRepository.delete(article);
	}

	@Override
	public boolean checkArticleExist(Article article) {
		// if (articleRepository.exists(article.getId())) {
		// return true;
		// }
		Article articleExistsTitle = articleRepository.findArticleByTitle(article.getTitle());
		if (articleExistsTitle != null) {
			logger.info("------- Trùng title ------ crawlerId: " + article.getCrawlerId());
			return true;
		}
		if (article.getUrl() != null) {
			Article articleExistUrl = articleRepository.findArticleByUrl(article.getUrl());
			if (articleExistUrl != null) {
				logger.info("------- Trùng url ------ crawlerId: " + article.getCrawlerId());
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Article> fullTextSearch(String keyword, int page, int size, String fields) {
		List<Article> list = articleDAO.fullTextSearch(keyword, fields,
				new PageRequest(page, size, sortByPublicDateAsc()));
		return list;
	}

	@Override
	public void updateUserIds(Article article, String userId) {
		if (!article.getUsers().contains(userId)) {
			article.getUsers().add(userId);
			articleRepository.save(article);
		}
	}

	@Override
	public void removeUserIds(Article article, String userId) {
		if (article.getUsers().contains(userId)) {
			article.getUsers().remove(userId);
			articleRepository.save(article);
		}
	}

	@Override
	public List<Article> getArticlesByPage(int page, int size) {
		Page<Article> articlesPage = articleRepository.findAll(new PageRequest(page, size, sortByPublicDateAsc()));
		if (articlesPage == null)
			return new ArrayList<>();
		return articlesPage.getContent();
	}

	private Sort sortByPublicDateAsc() {
		return new Sort(Sort.DEFAULT_DIRECTION.DESC, "publicDate");
	}

	@Override
	public List<Article> getArticleByTagPage(String name, int page, int size, String fields) {
		List<Article> articlesPage = articleDAO.getArticlesByTagName(name, fields,
				new PageRequest(page, size, sortByPublicDateAsc()));
		return articlesPage;
	}

	@Override
	public List<Article> getArticlesCategoryPage(Category category, int page, int size) {
		Page<Article> articlesPage = articleRepository.findArticlesByCategory(category,
				new PageRequest(page, size, sortByPublicDateAsc()));
		if (articlesPage == null || articlesPage.getSize() == 0)
			return new ArrayList<>();
		return articlesPage.getContent();
	}

	@Override
	public List<Article> getArticleByFieldsAndPage(String fields, Integer page, Integer size) {
		String[] properties = fields.split(",");
		List<Article> articlesPage = articleDAO.getArticlesByFields(properties,
				new PageRequest(page, size, sortByPublicDateAsc()));
		return articlesPage;
	}

	@Override
	public Article getArticleByIdAndFields(Integer articleId, String fields) {
		Article article = null;
		if (fields.isEmpty() || fields.equals("all")) {
			article = articleRepository.findOne(articleId);
		} else {
			String[] properties = fields.split(",");
			article = articleDAO.getArticleByIdAndFields(articleId, properties);
		}
		if(article == null) return null;
		if (article.getVoices() == null || article.getVoices().isEmpty()) {
			article.setVoices(voiceService.findByArticleId(article.getId(), ""));
		}
		return article;
	}

	@Override
	public List<Article> getArticlesCategoryPageAndFields(Category category, int page, int size, String fields) {
		String[] properties = fields.split(",");
		return articleDAO.getArticlesCategoryPageAndFields(category, properties,
				new PageRequest(page, size, sortByPublicDateAsc()));
	}

	@Override
	public List<Article> getArticlesByPageHadSynthesized(Integer page, Integer size, String websites,
			String categoryIds) {
		try {
			if (websites.equals("all") && categoryIds.equals("all")) {
				return articleDAO.getArticlesByPageHadSynthesized(new PageRequest(page, size, sortByPublicDateAsc()));
			} else if (websites.equals("all") && !categoryIds.equals("all")) {
				String[] categoriesId = categoryIds.split(",");
				List<Category> categories = new ArrayList<>();
				for (String categoryId : categoriesId) {
					int id = Integer.parseInt(categoryId);
					Category category = categoryRepository.findOne(id);
					categories.add(category);
				}
				return articleDAO.getArticlesByPageAndCategoriesHadSynthesized(categories,
						new PageRequest(page, size, sortByPublicDateAsc()));
			} else if (!websites.equals("all") && categoryIds.equals("all")) {
				String[] websiteNames = websites.split(",");
				return articleDAO.getArticlesByPageAndWebsitesHadSynthesized(websiteNames,
						new PageRequest(page, size, sortByPublicDateAsc()));
			} else {
				String[] websiteNames = websites.split(",");
				String[] categoriesId = categoryIds.split(",");
				List<Category> categories = new ArrayList<>();
				for (String categoryId : categoriesId) {
					int id = Integer.parseInt(categoryId);
					categories.add(categoryRepository.findOne(id));
				}
				return articleDAO.getArticlesByPageAndWebsitesAndCategoriesHadSynthesized(websiteNames, categories,
						new PageRequest(page, size, sortByPublicDateAsc()));
			}
		} catch (Exception e) {
			logger.info("Error in getArticlesByPageHadSynthesized. Exception: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	@Override
	public List<Article> getArticleByFieldsAndPageHadSynthesized(String fields, Integer page, Integer size,
			String websites, String categoryIds) {
		String[] properties = fields.split(",");
		try {
			if (websites.equals("all") && categoryIds.equals("all")) {
				return articleDAO.getArticlesByFiledsAndPageHadSynthesized(properties,
						new PageRequest(page, size, sortByPublicDateAsc()));
			} else if (websites.equals("all") && !categoryIds.equals("all")) {
				String[] categoriesId = categoryIds.split(",");
				List<Category> categories = new ArrayList<>();
				for (String categoryId : categoriesId) {
					int id = Integer.parseInt(categoryId);
					categories.add(categoryRepository.findOne(id));
				}
				return articleDAO.getArticlesByFieldsAndPageAndCategoriesHadSynthesized(properties, categories,
						new PageRequest(page, size, sortByPublicDateAsc()));
			} else if (!websites.equals("all") && categoryIds.equals("all")) {
				String[] websiteNames = websites.split(",");
				return articleDAO.getArticlesByFieldsAndPageAndWebsitesHadSynthesized(properties, websiteNames,
						new PageRequest(page, size, sortByPublicDateAsc()));
			} else {
				String[] websiteNames = websites.split(",");
				String[] categoriesId = categoryIds.split(",");
				List<Category> categories = new ArrayList<>();
				for (String categoryId : categoriesId) {
					int id = Integer.parseInt(categoryId);
					categories.add(categoryRepository.findOne(id));
				}
				return articleDAO.getArticlesByFieldsAndPageAndWebsitesAndCategoriesHadSynthesized(properties,
						websiteNames, categories, new PageRequest(page, size, sortByPublicDateAsc()));
			}
		} catch (Exception e) {
			logger.info("Error in getArticlesByPageHadSynthesized. Exception: " + e.getMessage());
			return new ArrayList<>();
		}

	}

	@Override
	public List<Article> getArticlesHadSynthesizedCategoryPage(Category category, int page, int size) {
		return articleDAO.getArticlesHadSynthesizedCategoryPage(category,
				new PageRequest(page, size, sortByPublicDateAsc()));
	}

	@Override
	public List<Article> getArticlesHadSynthesizedCategoryPageAndFields(Category category, int page, int size,
			String fields) {
		String[] properties = fields.split(",");
		return articleDAO.getArticlesHadSynthesizedCategoryPageAndFields(category, properties,
				new PageRequest(page, size, sortByPublicDateAsc()));
	}

	@Override
	public Article getArticleByCrawlerId(Integer crawlerId) {
		return articleRepository.findArticleByCrawlerId(crawlerId);
	}

	@Override
	public List<Article> fullTextSearchAndWebsiteAndCategories(String keyword, int page, int size, String fields,
			String websiteNames, String categoriesId) {
		return articleDAO.fullTextSearchAndCategoriesAndWebsites(keyword, fields, websiteNames, categoriesId,
				new PageRequest(page, size, sortByPublicDateAsc()));
	}

	@Override
	public List<Article> getArticleByFieldsAndPageHadSynthesized(String fields, Integer page, Integer size,
			List<String> websiteUnCheckIds, List<Integer> categoryUnCheckIds) {
		String[] properties = fields.split(",");
		try {
			if (websiteUnCheckIds.isEmpty() && categoryUnCheckIds.isEmpty()) {
				// non
				return articleDAO.getArticlesByFiledsAndPageHadSynthesized(properties,
						new PageRequest(page, size, sortByPublicDateAsc()));
			} else if (websiteUnCheckIds.isEmpty() && !categoryUnCheckIds.isEmpty()) {
				// only categories
				List<Category> categories = new ArrayList<>();
				for (Integer categoryId : categoryUnCheckIds) {
					categories.add(categoryRepository.findOne(categoryId));
				}
				return articleDAO.getArticlesByFieldsAndPageAndCategoriesUncheckHadSynthesized(properties, categories,
						new PageRequest(page, size, sortByPublicDateAsc()));
			} else if (!websiteUnCheckIds.isEmpty() && categoryUnCheckIds.isEmpty()) {
				// only websites
				return articleDAO.getArticlesByFieldsAndPageAndWebsitesUncheckHadSynthesized(properties,
						websiteUnCheckIds, new PageRequest(page, size, sortByPublicDateAsc()));
			} else {
				// both categories and websites

				List<Category> categories = new ArrayList<>();
				for (Integer categoryId : categoryUnCheckIds) {
					categories.add(categoryRepository.findOne(categoryId));
				}
				return articleDAO.getArticlesByFieldsAndPageAndWebsitesUncheckAndCategoriesUncheckHadSynthesized(
						properties, websiteUnCheckIds, categories, new PageRequest(page, size, sortByPublicDateAsc()));
			}
		} catch (Exception e) {
			logger.info("Error in getArticlesByPageHadSynthesized. Exception: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	@Override
	public List<Article> getArticleByOrderArticleIds(String articleIds, String fields) {
		String[] ids = articleIds.split(",");
		List<Article> articles = new ArrayList<>();
		if (fields.equals("all")) {
			for (String articleId : ids) {
				Article article = getArticleById(Integer.parseInt(articleId));
				articles.add(article);
			}
			return articles;
		}
		for (String articleId : ids) {
			Article article = getArticleByIdAndFields(Integer.parseInt(articleId), fields);
			articles.add(article);
		}
		return articles;

	}

	@Override
	public ArticleSearchReponse searchArticleMvc(Integer page, Integer size, String fields, String keyword,
			Integer categoryId, String websiteName, String sort, Integer synthesisType) {
		return articleDAO.searchArticleMvc(page, size, fields, keyword, categoryId, websiteName, sort, synthesisType);
	}

	@Override
	public void updateActiveArticle(Article article, Integer status) {
		article.setStatus(status);
		System.out.println("status: " + status);
		articleRepository.save(article);
	}

	@Override
	public List<Article> findArticlesByArticleNext(Article article, String fields, Integer categoryId, Integer size) {
		return articleDAO.findArticlesByArticleNext(article, fields, categoryId, size);
	}

	@Override
	public List<Article> findByCategorySynthesizedAndPaging(User userExists, Category category, Integer size,
			Integer page, String fields) {
		return articleDAO.findByCategorySynthesizedAndPaging(userExists, category, fields,
				new PageRequest(page, size, sortByPublicDateAsc()));
	}

	@Override
	public Article updateArticleMvc(Article articleExsists, Article article) {
		if (article.getContent() != null && !article.getContent().isEmpty()) {
			articleExsists.setContent(article.getContent());
		}
		if (article.getText() != null && !article.getText().isEmpty()) {
			articleExsists.setText(article.getText());
		}
		if (article.getTitle() != null && !article.getTitle().isEmpty()) {
			articleExsists.setTitle(article.getTitle());
		}
		if (article.getLead() != null && !article.getLead().isEmpty()) {
			articleExsists.setLead(article.getLead());
		}
		if (article.getPicture() != null && !article.getPicture().isEmpty()) {
			articleExsists.setPicture(article.getPicture());
		}
		if (article.getCategory() != null) {
			articleExsists.setCategory(article.getCategory());
		}
		if (article.getTags() != null) {
			articleExsists.setTags(article.getTags());
		}
		return articleRepository.save(articleExsists);
	}

	@Override
	public List<Article> findAll() {
		return articleRepository.findAll();
	}

	@Override
	public Article findByWeather(String fields) {
		return articleDAO.findByWeather(fields);
	}

	@Override
	public List<Article> findByWebsite(String websiteName) {
		return articleRepository.findByWebsiteName(websiteName);
	}

}
