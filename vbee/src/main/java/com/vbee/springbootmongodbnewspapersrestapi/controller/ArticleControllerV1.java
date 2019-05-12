package com.vbee.springbootmongodbnewspapersrestapi.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Tag;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Website;
import com.vbee.springbootmongodbnewspapersrestapi.config.AppConstant;
import com.vbee.springbootmongodbnewspapersrestapi.model.ResponseMessage;
import com.vbee.springbootmongodbnewspapersrestapi.service.IArticleService;
import com.vbee.springbootmongodbnewspapersrestapi.service.ICategoryService;
import com.vbee.springbootmongodbnewspapersrestapi.service.INormalizationService;
import com.vbee.springbootmongodbnewspapersrestapi.service.ITagService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IUserService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IVoiceService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IWebsiteService;
import com.vbee.springbootmongodbnewspapersrestapi.ulti.Slug;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleControllerV1 {

	@Autowired
	IArticleService articleService;

	@Autowired
	ICategoryService categoryService;

	@Autowired
	IUserService userService;

	@Autowired
	ITagService tagService;

	@Autowired
	IVoiceService voiceService;

	@Autowired
	IWebsiteService websiteService;

	@Autowired
	INormalizationService normalizationService;

	private static final Logger logger = LoggerFactory.getLogger(ArticleControllerV1.class);

	// Get Articles pagination
	@GetMapping("/page/{page}/size/{size}")
	public ResponseEntity<ResponseMessage> getArticlesByPage(@PathVariable Integer page, @PathVariable Integer size,
			String fields) {
		List<Article> articles = null;
		ResponseMessage resMessage = new ResponseMessage();

		if (fields == null || fields.equals("all"))
			articles = articleService.getArticlesByPage(page, size);
		else
			articles = articleService.getArticleByFieldsAndPage(fields, page, size);

		if (articles == null || articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy article!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Đã tìm thấy: " + articles.size() + " articles");
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	// Get Articles had synthesized pagination
	@GetMapping("/synthesized/page/{page}/size/{size}")
	public ResponseEntity<ResponseMessage> getArticlesByPageHadSynthesized(@PathVariable Integer page,
			@PathVariable Integer size, @RequestParam("fields") String fields,
			@RequestParam("websites") String websites, @RequestParam("categories") String categoryIds) {
		List<Article> articles = null;
		ResponseMessage resMessage = new ResponseMessage();
		if (fields.equals("all"))
			articles = articleService.getArticlesByPageHadSynthesized(page, size, websites, categoryIds);
		else
			articles = articleService.getArticleByFieldsAndPageHadSynthesized(fields, page, size, websites,
					categoryIds);

		if (articles == null || articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy article!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Đã tìm thấy: " + articles.size() + " articles");
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	// Get All Article
	// @GetMapping
	// public ResponseEntity<ResponseMessage> getAllCategory() {
	// List<Article> articles = articleService.getArticles();
	// ResponseMessage resMessage = new ResponseMessage();
	// if (articles.isEmpty()) {
	// resMessage.setMessage("Không tìm thấy article!!!");
	// resMessage.setStatus(0);
	// return ResponseEntity.ok(resMessage);
	// }
	// resMessage.setMessage("Đã tìm thấy: " + articles.size() + " articles");
	// resMessage.setStatus(1);
	// resMessage.setResults(articles);
	// return ResponseEntity.ok(resMessage);
	// }

	// Create a singer Article
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> createArticle(@RequestBody @Valid Article article) {
		ResponseMessage resMessage = new ResponseMessage();
		// check article exists
		if (articleService.checkArticleExist(article)) {
			resMessage.setMessage("Article đã tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		logger.info("Create article: " + article.getTitle() + " with publicDate: " + article.getPublicDate());
		// insert category if not exists
		Category categoryExists = categoryService.getCategoryById(article.getCategory().getId());
		if (categoryExists == null)
			categoryService.insertCategory(article.getCategory());
		System.out.println("website name: " + article.getWebsiteName());
		Website websiteExists = websiteService.findByName(article.getWebsiteName());
		if (websiteExists == null) {
			System.out.println("create website: " + websiteExists);
			websiteExists = websiteService.insertWebsite(article.getWebsiteName(), article.getWebsiteUrl());
		}
		article.setWebsiteId(websiteExists.getId());
		// insert tags if not exists
		for (Tag tag : article.getTags()) {
			Tag tagExists = tagService.findTagByName(tag.getName());
			if (tagExists == null)
				tagService.inserTag(tag);
		}
		article.setType(AppConstant.CRAWLER_TYPE);
		article.setSynthesisType(AppConstant.SYNTHESIZING);
		Article newArticle = articleService.insertArticle(article);
		normalizationService.putArticleNSW(newArticle);
		normalizationService.start();
		resMessage.setMessage("Tạo mới thành công");
		resMessage.setStatus(1);
		resMessage.setResults(newArticle);
		logger.info("Insert success article: " + newArticle.getTitle() + " with publicDate: " + article.getPublicDate());
		return ResponseEntity.ok(resMessage);
	}

	// Get a singer Article
	@GetMapping("/{articleId}")
	public ResponseEntity<ResponseMessage> getArticleById(@PathVariable Integer articleId,
			@RequestParam(value = "fields", required = false) String fields) {
		Article article = null;
		if (fields == null)
			fields = "";
		article = articleService.getArticleByIdAndFields(articleId, fields);
		ResponseMessage resMessage = new ResponseMessage();
		if (article == null) {
			resMessage.setMessage("Không tìm thấy article!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}

		resMessage.setMessage("Đã tìm thấy article id: " + articleId);
		resMessage.setStatus(1);
		resMessage.setResults(article);
		return ResponseEntity.ok(resMessage);
	}

	// Update a singer Article
	@PutMapping()
	public ResponseEntity<ResponseMessage> updateArticle(@RequestBody @Valid Article article) {
		ResponseMessage resMessage = new ResponseMessage();
		if (articleService.checkArticleExist(article)) {
			Article newArticle = articleService.updateArticle(article);
			resMessage.setMessage("Update thành công");
			resMessage.setStatus(1);
			resMessage.setResults(newArticle);
			return ResponseEntity.ok(resMessage);
		} else {
			resMessage.setMessage("article không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}

	}

	// Update summary a singer Article
	@CrossOrigin
	@PutMapping(value = "/update-summary")
	public ResponseEntity<ResponseMessage> updateSummaryArticle(@RequestBody Article article) {
		ResponseMessage resMessage = new ResponseMessage();
		Article articleExists = articleService.getArticleByCrawlerId(article.getCrawlerId());
		if (articleExists == null) {
			resMessage.setMessage("Article không tồn tại !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		if (article.getSummary() == null || article.getSummary().isEmpty()) {
			resMessage.setMessage("Field summary is null or empty !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		articleExists.setSummary(article.getSummary());
		Article newArticle = articleService.updateArticle(articleExists);
		resMessage.setMessage("Update thành công");
		resMessage.setStatus(1);
		resMessage.setResults(newArticle);
		return ResponseEntity.ok(resMessage);
	}

	// Delete a singer Article
	@DeleteMapping("/{articleId}")
	public ResponseEntity<ResponseMessage> deleteArticle(@PathVariable Integer articleId) {
		Article articleExists = articleService.getArticleById(articleId);
		ResponseMessage resMessage = new ResponseMessage();
		if (articleExists == null) {
			resMessage.setMessage("article không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		articleService.deleteArticleById(articleId);
		resMessage.setMessage("Delete thành công");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// Get List Article by categoryId
	@GetMapping("/category/{categoryId}/page/{page}/size/{size}")
	public ResponseEntity<ResponseMessage> getArticlesByCategoryId(@PathVariable Integer categoryId,
			@PathVariable int page, @PathVariable int size, @RequestParam("fields") String fields) {
		Category category = categoryService.getCategoryById(categoryId);
		ResponseMessage resMessage = new ResponseMessage();
		if (category == null) {
			resMessage.setMessage("Category không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		List<Article> articles = null;
		if (fields.equals("all"))
			articles = articleService.getArticlesCategoryPage(category, page, size);
		else
			articles = articleService.getArticlesCategoryPageAndFields(category, page, size, fields);
		if (articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy articles!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Lấy dữ liệu thành công");
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	// Get List Article had synthesized by categoryId
	@GetMapping("synthesized/category/{categoryId}/page/{page}/size/{size}")
	public ResponseEntity<ResponseMessage> getArticlesHadSynthesizedByCategoryId(@PathVariable Integer categoryId,
			@PathVariable int page, @PathVariable int size, @RequestParam("fields") String fields) {
		Category category = categoryService.getCategoryById(categoryId);
		ResponseMessage resMessage = new ResponseMessage();
		if (category == null) {
			resMessage.setMessage("Category không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		List<Article> articles = null;
		if (fields.equals("all"))
			articles = articleService.getArticlesHadSynthesizedCategoryPage(category, page, size);
		else
			articles = articleService.getArticlesHadSynthesizedCategoryPageAndFields(category, page, size, fields);
		if (articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy articles!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Lấy dữ liệu thành công");
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	// Get List Article by User
	@GetMapping("/user/{userId}")
	public ResponseEntity<ResponseMessage> getArticlesFavorite(@PathVariable String userId) {
		ResponseMessage resMessage = new ResponseMessage();
		if (userService.checkUserExists(userId)) {
			List<Article> articles = new ArrayList<>();
			List<Integer> list = userService.getArticleIdsByUserId(userId);
			if (list.isEmpty()) {
				resMessage.setMessage("User Id: " + userId + " không chứa article!!!");
				resMessage.setStatus(0);
				return ResponseEntity.ok(resMessage);
			}
			for (Integer id : list) {
				articles.add(articleService.getArticleById(id));
			}
			resMessage.setMessage("Lấy dữ liệu thành công");
			resMessage.setStatus(1);
			resMessage.setResults(articles);
			return ResponseEntity.ok(resMessage);
		} else {
			resMessage.setMessage("User không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
	}

	// Get Articles by Tag
	//
	@GetMapping("/page/{page}/size/{size}/tags")

	public ResponseEntity<ResponseMessage> getArticlesByTagName(@RequestParam(value = "t") String tagName,
			@PathVariable int page, @PathVariable int size, @RequestParam("fields") String fields) {
		Tag tag = tagService.findTagByName(tagName);
		ResponseMessage resMessage = new ResponseMessage();
		if (tag == null) {
			resMessage.setMessage("Tag không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		List<Article> articles = articleService.getArticleByTagPage(tagName, page, size, fields);
		if (articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy articles!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Lấy dữ liệu thành công");
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	// Get List Article by Keyword
	@GetMapping("/search/page/{page}/size/{size}")
	public ResponseEntity<ResponseMessage> getArticlesByFullTextSearch(@RequestParam(value = "q") String keyword,
			@PathVariable int page, @PathVariable int size, @RequestParam("fields") String fields) {
		ResponseMessage resMessage = new ResponseMessage();
		List<Article> articles = articleService.fullTextSearch(keyword, page, size, fields);
		if (articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy bài báo liên quan");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Đã tìm thấy: " + articles.size() + " bài báo liên quan");
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	// Get List Article by Keyword and Website and categories
	@GetMapping("/search-advance/page/{page}/size/{size}")
	public ResponseEntity<ResponseMessage> getArticlesByFullTextSearchAndWebsiteAndCategories(
			@RequestParam(value = "q") String keyword, @PathVariable int page, @PathVariable int size,
			@RequestParam("fields") String fields, @RequestParam("websites") String websites,
			@RequestParam("categories") String categories) {
		ResponseMessage resMessage = new ResponseMessage();
		if (keyword == null || keyword.isEmpty() || websites == null || websites.isEmpty() || categories == null
				|| categories.isEmpty()) {
			resMessage.setMessage("Param truyền lên không đúng để tìm kiếm. Yêu cầu q, websites, categories");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		List<Article> articles = articleService.fullTextSearchAndWebsiteAndCategories(keyword, page, size, fields,
				websites, categories);
		if (articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy bài báo liên quan");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Đã tìm thấy: " + articles.size() + " bài báo liên quan");
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	// Get Articles By articleId
	@GetMapping("/article-order")
	public ResponseEntity<ResponseMessage> getArticlesAroundArticleId(String articleIds, String fields) {
		ResponseMessage resMessage = new ResponseMessage();
		if (fields == null || fields.isEmpty()) {
			fields = "all";
		}
		if (articleIds == null || articleIds.isEmpty()) {
			resMessage.setMessage("article ids null hoặc không có !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		List<Article> articles = articleService.getArticleByOrderArticleIds(articleIds, fields);
		if (articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy bài báo liên quan");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		articles = userService.checkArticleVoicesAndRemoveNullAttribute(articles);
		resMessage.setMessage("Đã tìm thấy: " + articles.size() + " articles");
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	// Get Articles Next by id and category
	@GetMapping("/article-next")
	public ResponseEntity<ResponseMessage> getArticlesAroundArticleId(Integer articleId, String fields,
			Integer categoryId, Integer size) {
		ResponseMessage resMessage = new ResponseMessage();
		if (fields == null || fields.isEmpty()) {
			fields = "all";
		}
		Article article = articleService.getArticleByIdAndFields(articleId, "publicDate," + fields);
		if (article == null) {
			resMessage.setMessage("article không tồn tại !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		if (categoryId == null) {
			categoryId = 0;
		}
		if (size == null) {
			size = 10;
		}
		List<Article> articles = articleService.findArticlesByArticleNext(article, fields, categoryId, size);
		if (articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy bài báo liên quan");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		articles = userService.checkArticleVoicesAndRemoveNullAttribute(articles);
		resMessage.setMessage("Đã tìm thấy: " + articles.size() + " articles");
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("/weathers")
	public ResponseEntity<ResponseMessage> getArticlesByWeather(String fields) {
		ResponseMessage resMessage = new ResponseMessage();
		if (fields == null)
			fields = "";
		Article article = articleService.findByWeather(fields);
		if (article == null) {
			resMessage.setMessage("Not found");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("success");
		resMessage.setStatus(1);
		resMessage.setResults(article);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("/restore-website")
	public ResponseEntity<ResponseMessage> restoreArticleForWebsite() {
		ResponseMessage resMessage = new ResponseMessage();
		// String websiteName = "Đại kỷ nguyên";
		// List<Article> articles = articleService.findByWebsite(websiteName);
		// Website website = websiteService.findByName(websiteName);
		// System.out.println(articles.size());
		// int count = 0;
		// for (Article article : articles) {
		// if(!article.getWebsiteId().equals(website.getId())) {
		// article.setWebsiteId(website.getId());
		// articleService.updateArticle(article);
		// count ++;
		// }
		// }
		// System.out.println(count);
		// resMessage.setMessage("success");
		// resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("/printData")
	public ResponseEntity<ResponseMessage> printData() {
		ResponseMessage resMessage = new ResponseMessage();
		List<Article> articles = articleService.findAll();
		logger.info("size: " + articles.size());
		for (Article article : articles) {
			try {
				File file = new File(
						"C:\\newspapers\\Export-Article-20-7-2018\\" + Slug.makeSlug(article.getTitle()) + ".txt");
				// if file doesnt exists, then create it
				// if (!file.exists()) {
				// file.createNewFile();
				// }
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				Writer writer = new java.io.OutputStreamWriter(fileOutputStream, "utf8");
				BufferedWriter bw = new BufferedWriter(writer);
				// write title
				bw.write("#" + System.lineSeparator());
				bw.write(article.getTitle() + System.lineSeparator());
				// write lead
				bw.write("##" + System.lineSeparator());
				bw.write(article.getLead() + System.lineSeparator());
				// replace <p></p> like paragraph
				String content = article.getContent().replaceAll("<p>", System.lineSeparator()).replaceAll("</p>",
						System.lineSeparator());
				// remove all HTML entities
				content = content.replaceAll("&.*?;", "");
				// remove all HTML tags
				content = content.replaceAll("<[^>]*>", "");
				// write content
				// using jsoup
				// content = Jsoup.parse(content).text();
				bw.write("###" + System.lineSeparator());
				bw.write(content);
				bw.close();
				System.out.println("Done Article:  " + article.getTitle());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(System.lineSeparator() + "Error in printData() --- ArticleControllerV1 -- article: "
						+ article.getTitle());
			}
		}
		System.out.println("Process done");

		return ResponseEntity.ok(resMessage);
	}

}
