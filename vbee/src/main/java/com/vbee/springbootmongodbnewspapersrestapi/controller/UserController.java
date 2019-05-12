package com.vbee.springbootmongodbnewspapersrestapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;
import com.vbee.springbootmongodbnewspapersrestapi.collections.User;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleAdvertisement;
import com.vbee.springbootmongodbnewspapersrestapi.model.ResponseMessage;
import com.vbee.springbootmongodbnewspapersrestapi.service.IArticleService;
import com.vbee.springbootmongodbnewspapersrestapi.service.ICategoryService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IScheduleArticleService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IUserService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IVoiceService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IWebsiteService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	IUserService userService;

	@Autowired
	IArticleService articleService;

	@Autowired
	ICategoryService categoryService;

	@Autowired
	IWebsiteService websiteService;

	@Autowired
	IVoiceService voiceService;

	@Autowired
	IScheduleArticleService scheduleArticleService;

	// Get All users
	@GetMapping()
	public ResponseEntity<ResponseMessage> getAllUser() {
		List<User> list = userService.findAll();
		ResponseMessage resMessage = new ResponseMessage();
		if (list.isEmpty()) {
			resMessage.setMessage("Không tìm thấy users!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Đã tìm thấy: " + list.size() + " users");
		resMessage.setStatus(1);
		resMessage.setResults(list);
		return ResponseEntity.ok(resMessage);
	}

	// Create a Singer user
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> addUser(@RequestBody @Valid User user) {
		ResponseMessage resMessage = new ResponseMessage();
		if (userService.checkUserExists(user.getId())) {
			resMessage.setMessage("User: " + user.getId() + "đã tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		User newUser = userService.insertUser(user);
		resMessage.setMessage("Tạo thành công!!!");
		resMessage.setStatus(1);
		resMessage.setResults(newUser);
		return ResponseEntity.ok(resMessage);
	}

	// Get Singer a user
	@GetMapping("/{userId}")
	public ResponseEntity<ResponseMessage> getUserByUserId(@PathVariable String userId) {
		ResponseMessage resMessage = new ResponseMessage();
		User user = userService.getUserById(userId);
		if (user == null) {
			resMessage.setMessage("Không tìm thấy user!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Đã tìm thấy userId: " + user.getId());
		resMessage.setStatus(1);
		resMessage.setResults(user);
		return ResponseEntity.ok(resMessage);
	}

	// Update a singer user
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> updateUser(@RequestBody @Valid User user) {
		ResponseMessage resMessage = new ResponseMessage();
		User userExists = userService.getUserById(user.getId());
		if (userExists == null) {
			resMessage.setMessage("User: " + user.getId() + " không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		User updateUser = userService.updateUser(user, userExists);
		resMessage.setMessage("Update thành công");
		resMessage.setStatus(1);
		resMessage.setResults(updateUser);
		return ResponseEntity.ok(resMessage);

	}

	// Delete a singer category
	@DeleteMapping("/{userId}")
	public ResponseEntity<ResponseMessage> delete(@PathVariable String userId) {
		ResponseMessage resMessage = new ResponseMessage();
		User user = userService.getUserById(userId);
		if (user == null) {
			resMessage.setMessage("User không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		userService.deleteUser(userId);
		resMessage.setMessage("Delete thành công");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);

	}

	// add favorite article by user
	@PostMapping("/edit/{userId}/article/{articleId}")
	public ResponseEntity<ResponseMessage> addFavorite(@PathVariable String userId, @PathVariable Integer articleId) {
		ResponseMessage resMessage = new ResponseMessage();
		User user = userService.getUserById(userId);
		if (user == null) {
			resMessage.setMessage("User Id không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		Article article = articleService.getArticleById(articleId);
		if (article == null) {
			resMessage.setMessage("Article Id không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}

		userService.updateArticleIds(user, articleId);
		articleService.updateUserIds(article, userId);
		resMessage.setMessage("Add favorite thành công!!!");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// Remove favorite article by user
	@PostMapping("/delete/{userId}/article/{articleId}")
	public ResponseEntity<ResponseMessage> removeFavorite(@PathVariable String userId,
			@PathVariable Integer articleId) {
		ResponseMessage resMessage = new ResponseMessage();
		User user = userService.getUserById(userId);
		if (user == null) {
			resMessage.setMessage("User Id không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		Article article = articleService.getArticleById(articleId);
		if (article == null) {
			resMessage.setMessage("Article Id không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		userService.removeArticleIds(user, articleId);
		articleService.removeUserIds(article, userId);
		resMessage.setMessage("Remove favorite thành công!!!");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// Get Articles By Hot News
	@GetMapping("/{userId}/articles/hot-news")
	public ResponseEntity<ResponseMessage> getHotNews(@PathVariable String userId, Integer page, Integer size,
			String fields, HttpServletRequest request) {
		ResponseMessage resMessage = new ResponseMessage();
		List<Article> articles = null;
		if (page == null)
			page = 0;
		if (size == null)
			size = 10;
		if (fields == null || fields.isEmpty())
			fields = "all";
		User userExists = userService.getUserById(userId);
		if (userExists == null) {
			userExists = userService.getUserById("5adeb18f5474bf665fd8ac0d");
			// resMessage.setMessage("Not found userId: " + userId);
			// resMessage.setStatus(0);
			// return ResponseEntity.ok(resMessage);
		}
		articles = articleService.getArticleByFieldsAndPageHadSynthesized(fields, page, size,
				userExists.getWebsiteUnCheckIds(), userExists.getCategoryUnCheckIds());

		if (articles == null || articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy article!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		// get article advertisement follow postion
		if (page == 0) {
			List<ArticleAdvertisement> articleAdvertisements = scheduleArticleService.findArticleByNow(0);
			if (articleAdvertisements != null) {
				for (ArticleAdvertisement articleAdvertisement : articleAdvertisements) {
					if (articles.stream()
							.anyMatch(item -> articleAdvertisement.getArticle().getId().equals(item.getId()))) {
						Article articleDuplicate = articles.stream()
								.filter(item -> articleAdvertisement.getArticle().getId().equals(item.getId()))
								.findFirst().get();
						articles.remove(articleDuplicate);
						logger.info("remove article dupliacte: " + articleDuplicate.getTitle() + " !!!");
					}

				}
				for (ArticleAdvertisement articleAdvertisement : articleAdvertisements) {
					articles.add(articleAdvertisement.getPosition() - 1, articleAdvertisement.getArticle());
				}
			}
		}
		articles = userService.checkArticleVoicesAndRemoveNullAttribute(articles);
		if (articles.size() != 0) {
			String remoteAddr = "";
			if (request != null) {
				remoteAddr = request.getHeader("X-FORWARDED-FOR");
				if (remoteAddr == null || "".equals(remoteAddr)) {
					remoteAddr = request.getRemoteAddr();
				}
			}
			logger.info("Request from: " + remoteAddr + " --- Get article with size: " + articles.size() + "and page: "
					+ page + " --- lastest article with publicDate: " + articles.get(0).getPublicDate());
			resMessage.setMessage("Đã tìm thấy: " + articles.size() + " articles");

		}

		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	// Get Articles by categoryId
	@GetMapping("/{userId}/categories/{categoryId}/articles")
	public ResponseEntity<ResponseMessage> getArticlesByCategoryId(@PathVariable String userId,
			@PathVariable Integer categoryId, Integer page, Integer size, String fields, HttpServletRequest request) {
		ResponseMessage resMessage = new ResponseMessage();
		List<Article> articles = null;
		if (page == null)
			page = 0;
		if (size == null)
			size = 10;
		if (fields == null)
			fields = "all";
		User userExists = userService.getUserById(userId);
		if (userExists == null) {
			userExists = userService.getUserById("5adeb18f5474bf665fd8ac0d");
			// resMessage.setMessage("Not found userId: " + userId);
			// resMessage.setStatus(0);
			// return ResponseEntity.ok(resMessage);
		}
		Category category = categoryService.getCategoryById(categoryId);
		if (category == null) {
			resMessage.setMessage("Category không tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		if (userExists.getCategoryUnCheckIds().contains(category.getId())) {
			resMessage.setMessage("CategoryId not in favorite user");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		articles = articleService.findByCategorySynthesizedAndPaging(userExists, category, size, page, fields);
		if (articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy articles!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		// get article advertisement follow postion
		if (page == 0) {
			List<ArticleAdvertisement> articleAdvertisements = scheduleArticleService.findArticleByNow(categoryId);
			if (articleAdvertisements != null) {
				for (ArticleAdvertisement articleAdvertisement : articleAdvertisements) {
					if (articles.stream()
							.anyMatch(item -> articleAdvertisement.getArticle().getId().equals(item.getId()))) {
						Article articleDuplicate = articles.stream()
								.filter(item -> articleAdvertisement.getArticle().getId().equals(item.getId()))
								.findFirst().get();
						articles.remove(articleDuplicate);
						logger.info("remove article dupliacte: " + articleDuplicate.getTitle() + " !!!");
					}
				}
				for (ArticleAdvertisement articleAdvertisement : articleAdvertisements) {
					articles.add(articleAdvertisement.getPosition() - 1, articleAdvertisement.getArticle());
				}
			}
		}
		articles = userService.checkArticleVoicesAndRemoveNullAttribute(articles);
		if (articles.size() != 0) {
			String remoteAddr = "";
			if (request != null) {
				remoteAddr = request.getHeader("X-FORWARDED-FOR");
				if (remoteAddr == null || "".equals(remoteAddr)) {
					remoteAddr = request.getRemoteAddr();
				}
			}
			logger.info("Request from: " + remoteAddr + " --- Get article with size: " + articles.size() + "and page: "
					+ page + " --- lastest article with publicDate: " + articles.get(0).getPublicDate());
			resMessage.setMessage("Đã tìm thấy: " + articles.size() + " articles");

		}
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	// Get Articles recommend
	@GetMapping("/{userId}/articles/recommendation")
	public ResponseEntity<ResponseMessage> getArticlesRecommend(@PathVariable String userId, String fields,
			String articleIds) {
		ResponseMessage resMessage = new ResponseMessage();
		User userExists = userService.getUserById(userId);
		if (userExists == null) {
			resMessage.setMessage("Not found userId: " + userId);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		if (fields == null || fields.isEmpty()) {
			fields = "all";
		}
		List<Article> articles = null;
		Integer SIZE_OF_RECOMMEND = 5;
		if (articleIds == null || articleIds.isEmpty()) {
			resMessage.setMessage("Không có article để gợi ý!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		} else
			articles = userService.findArticleByRecommendation(userExists, fields, articleIds, SIZE_OF_RECOMMEND);
		if (articles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy articles!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		articles = userService.checkArticleVoicesAndRemoveNullAttribute(articles);
		resMessage.setMessage("Lấy dữ liệu thành công");
		resMessage.setStatus(1);
		resMessage.setResults(articles);
		return ResponseEntity.ok(resMessage);
	}

	// Statistics article play
	@PostMapping("/{userId}/articles/{articleId}/voices/{voiceName}")
	public ResponseEntity<ResponseMessage> saveActionPlayArticle(@PathVariable String userId,
			@PathVariable Integer articleId, @PathVariable String voiceName, @RequestParam("duration") Integer duration,
			@RequestParam("listen") Integer listen) {
		ResponseMessage resMessage = new ResponseMessage();
		User userExists = userService.getUserById(userId);
		if (userExists == null) {
			resMessage.setMessage("Not found userId: " + userId);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		Article articleExists = articleService.getArticleById(articleId);
		if (articleExists == null) {
			resMessage.setMessage("Not found articleId: " + articleId);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		if (!userService.saveActionPlayArticle(userExists, articleExists, voiceName, duration, listen)) {
			resMessage.setMessage("Fail call back statistic service !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("call back success !!!");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// Statistics article chosen
	@PostMapping("/{userId}/articles/{articleId}/chosen")
	public ResponseEntity<ResponseMessage> saveActionChooseArticle(@PathVariable String userId,
			@PathVariable Integer articleId) {
		ResponseMessage resMessage = new ResponseMessage();
		User userExists = userService.getUserById(userId);
		if (userExists == null) {
			resMessage.setMessage("Not found userId: " + userId);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		Article articleExists = articleService.getArticleById(articleId);
		if (articleExists == null) {
			resMessage.setMessage("Not found articleId: " + articleId);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		if (!userService.saveActionChooseArticle(userId, articleExists)) {
			resMessage.setMessage("Fail call back statistic service !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("call back success !!!");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

}
