package com.vbee.springbootmongodbnewspapersrestapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.ScheduleArticle;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Tag;
import com.vbee.springbootmongodbnewspapersrestapi.config.AppConstant;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleAdvertisement;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleProcessing;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleSearchReponse;
import com.vbee.springbootmongodbnewspapersrestapi.model.Config;
import com.vbee.springbootmongodbnewspapersrestapi.model.ResponseMessage;
import com.vbee.springbootmongodbnewspapersrestapi.service.IArticleService;
import com.vbee.springbootmongodbnewspapersrestapi.service.ICategoryService;
import com.vbee.springbootmongodbnewspapersrestapi.service.INormalizationService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IScheduleArticleService;
import com.vbee.springbootmongodbnewspapersrestapi.service.ISynthesisService;
import com.vbee.springbootmongodbnewspapersrestapi.service.ITagService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IUserService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IWebsiteService;
import com.vbee.springbootmongodbnewspapersrestapi.service.NotificationService;
import com.vbee.springbootmongodbnewspapersrestapi.ulti.CookieUtil;
import com.vbee.springbootmongodbnewspapersrestapi.ulti.SessionUtil;

@RestController
public class ArticleViewController {

	@Autowired
	ICategoryService categoryService;

	@Autowired
	IArticleService articleService;

	@Autowired
	IWebsiteService websiteService;

	@Autowired
	ITagService tagService;

	@Autowired
	ISynthesisService synthesisService;

	@Autowired
	IScheduleArticleService scheduleArticleService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	Config config;

	@Autowired
	INormalizationService normalizationService;

	@Autowired
	IUserService userService;

	// Return VIEW

	private String userId = null;
	private boolean setSessionNull = false;
	
	@GetMapping("/")
	public ModelAndView home(HttpServletRequest request) {
		System.out.println("here");
		ModelAndView model = new ModelAndView();
		model.addObject("urlMap", config.getMapServiceUrl());
		model.addObject("urlCrawler", config.getCrawlerServiceUrl());
		model.addObject("urlEditor", config.getLoginUrl() + "/user");
		
		//check logout
		if(this.setSessionNull) {
			SessionUtil.setAtribute(request, "access-token", null);
			this.setSessionNull = true;
			}
		model.setViewName("dashboard");
		return model;
	}

	@GetMapping("/dashboard")
	public ModelAndView dashboard() {
		System.out.println("there");
		ModelAndView model = new ModelAndView();
		model.addObject("urlMap", config.getMapServiceUrl());
		model.addObject("urlCrawler", config.getCrawlerServiceUrl());
		model.setViewName("dashboard");
//		model.addObject("urlEditor", config.getLoginUrl() + "/user");
		return model;
	}

	@GetMapping("/newspapers")
	public ModelAndView newspapers() {
		ModelAndView model = new ModelAndView();
		model.addObject("urlMap", config.getMapServiceUrl());
		model.addObject("urlCrawler", config.getCrawlerServiceUrl());
		model.addObject("categories", categoryService.findAll());
		model.addObject("websites", websiteService.findAll());
		model.addObject("urlEditor", config.getLoginUrl() + "/user");
		model.setViewName("newspapers");
		return model;
	}

	@GetMapping("/newspapers/{articleId}")
	public ModelAndView viewDetail(@PathVariable Integer articleId) {
		ModelAndView model = new ModelAndView();
		Article articleExists = articleService.getArticleById(articleId);
		if (articleExists == null) {
			model.setViewName("error");
			return model;
		}
		model.addObject("urlMap", config.getMapServiceUrl());
		model.addObject("urlCrawler", config.getCrawlerServiceUrl());
		model.addObject("urlEditor", config.getLoginUrl() + "/user");
		model.addObject("categories", categoryService.findAll());
		model.addObject("websites", websiteService.findAll());
		model.addObject("article", articleExists);
		model.addObject("tags", ArticleProcessing.getTags(articleExists));
		model.setViewName("newspapers-detail");
		return model;
	}

	@GetMapping("/newspapers/create")
	public ModelAndView viewCreate() {
		ModelAndView model = new ModelAndView();
		model.addObject("urlMap", config.getMapServiceUrl());
		model.addObject("urlCrawler", config.getCrawlerServiceUrl());
		model.addObject("urlEditor", config.getLoginUrl() + "/user");
		model.addObject("categories", categoryService.findAll());
		model.addObject("websites", websiteService.findAll());
		model.setViewName("newspapers-create");
		return model;
	}

	@GetMapping("/advertisements")
	public ModelAndView viewAdvertisements(Integer articleId) {
		ModelAndView model = new ModelAndView();
		if (articleId != null) {
			Article article = articleService.getArticleById(articleId);
			if (article != null) {
				model.addObject("article", article);
			}
		}
		model.addObject("urlMap", config.getMapServiceUrl());
		model.addObject("urlEditor", config.getLoginUrl() + "/user");
		model.addObject("urlCrawler", config.getCrawlerServiceUrl());
		model.addObject("categories", categoryService.findAll());
		model.setViewName("advertisements");
		return model;
	}

	@GetMapping("/advertisements/create")
	public ModelAndView viewAdvertisementCreate() {
		ModelAndView model = new ModelAndView();
		model.addObject("urlMap", config.getMapServiceUrl());
		model.addObject("urlEditor", config.getLoginUrl() + "/user");
		model.addObject("urlCrawler", config.getCrawlerServiceUrl());
		model.addObject("categories", categoryService.findAll());
		model.setViewName("advertisement-create");
		return model;
	}

	@GetMapping("/sources")
	public ModelAndView viewSources() {
		ModelAndView model = new ModelAndView();
		model.addObject("urlMap", config.getMapServiceUrl());
		model.addObject("urlCrawler", config.getCrawlerServiceUrl());
		model.addObject("urlEditor", config.getLoginUrl() + "/user");
		model.addObject("userServicePath", config.getUserServiceClientPath());
		model.addObject("version", config.getUserSettingDefaultVersion());
		model.setViewName("sources");
		return model;
	}

	@GetMapping("/logout-cms")
	public ResponseEntity<ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response) {
		String jwtTokenSessionName = "access-token";
		String domain = "demo3.com";
//		CookieUtil.clear(response, jwtTokenSessionName, domain);
		RestTemplate rest = new RestTemplate();
		boolean checkLogout = true;
		rest.postForObject(config.getSsoUrl() + "/clearCookie", checkLogout, String.class);
		this.userId = SessionUtil.getAttribute(request, "userId").toString();
		rest.postForObject(config.getSsoUrl() + "/doLogout", SessionUtil.getAttribute(request, "userId"), String.class);
		ResponseMessage resMessage = new ResponseMessage();
		resMessage.setStatus(1);
		resMessage.setResults(config.getCallbackUrl());
		return ResponseEntity.ok(resMessage);

	}
	// handle API
	
	@RequestMapping(value = "/doLogout", method = RequestMethod.POST)
	public ResponseEntity<String> doLogout( @RequestBody String userId){
		System.out.println("go here");
		if(userId.equals(this.userId)) {
			this.setSessionNull = true;
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PostMapping(value = "/newspapers", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> createArticle(@RequestBody Article article) {
		ResponseMessage resMessage = new ResponseMessage();
		// check article exists
		if (articleService.checkArticleExist(article)) {
			resMessage.setMessage("Article đã tồn tại!!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		} else {
			// insert tags if not exists
			for (Tag tag : article.getTags()) {
				Tag tagExists = tagService.findTagByName(tag.getName());
				if (tagExists == null)
					tagService.inserTag(tag);
			}
			article.setType(AppConstant.ADVERTISEMENTS_TYPE);
			Article newArticle = articleService.insertArticle(article);
			// synthesisService.putArticleSynthesize(newArticle);
			// synthesisService.start();
			normalizationService.putArticleNSW(newArticle);
			normalizationService.start();
			resMessage.setMessage("Tạo mới thành công");
			resMessage.setStatus(1);
			resMessage.setResults(newArticle);
			return ResponseEntity.ok(resMessage);
		}
	}

	@PutMapping(value = "/newspapers", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> updateArticle(@RequestBody Article article, HttpServletRequest request) {
		ResponseMessage resMessage = new ResponseMessage();
		// check article exists
		Article articleExsists = articleService.getArticleById(article.getId());
		if (articleExsists == null) {
			resMessage.setMessage("Not found");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		Article updated = articleService.updateArticleMvc(articleExsists, article);
		synthesisService.putArticleSynthesize(updated, request);
		synthesisService.start();
		resMessage.setMessage("Update success");
		resMessage.setStatus(1);
		resMessage.setResults(updated);
		return ResponseEntity.ok(resMessage);
	}

	@PostMapping("/newspapers/updateActive")
	public ResponseEntity<ResponseMessage> updateActiveArticle(Integer articleId, Integer status) {
		ResponseMessage resMessage = new ResponseMessage();
		Article article = articleService.getArticleById(articleId);
		if (article == null) {
			resMessage.setMessage("Not found !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		articleService.updateActiveArticle(article, status);
		resMessage.setMessage("Update thành công");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	@PutMapping("/newspapers/update-image")
	public ResponseEntity<ResponseMessage> updateImageArticle(@RequestBody Article article) {
		ResponseMessage resMessage = new ResponseMessage();
		Article articleExists = articleService.getArticleById(article.getId());
		if (articleExists == null) {
			resMessage.setMessage("Not found !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		articleExists.setPicture(article.getPicture());
		articleService.updateArticle(articleExists);
		resMessage.setMessage("Update thành công");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	@PutMapping("/newspapers/update-preview")
	public ResponseEntity<ResponseMessage> updatePreviewArticle(@RequestBody Article article) {
		ResponseMessage resMessage = new ResponseMessage();
		Article articleExists = articleService.getArticleById(article.getId());
		if (articleExists == null) {
			resMessage.setMessage("Not found !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		articleExists.setPreviewValue(article.getPreviewValue());
		articleExists.setNumDoubt(ArticleProcessing.countNumDoubtOfPreviewValue(articleExists));
		articleService.updateArticle(articleExists);
		resMessage.setMessage("Update thành công");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// Update number doubt nsw
	@GetMapping("/newspapers/update-nsw")
	public ResponseEntity<ResponseMessage> updateNSWArticle(Integer id) {
		ResponseMessage resMessage = new ResponseMessage();
		Article articleExists = articleService.getArticleById(id);
		if (articleExists == null) {
			resMessage.setMessage("Not found !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		articleExists.setNumDoubt(0);
		articleService.updateArticle(articleExists);
		resMessage.setMessage("Update thành công");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// Synthesis Article
	@PostMapping("/newspapers/synthesis")
	public ResponseEntity<ResponseMessage> synthesisArticle(@RequestBody Article article, HttpServletRequest request) {
		ResponseMessage resMessage = new ResponseMessage();
		// check article exists
		Article articleExsists = articleService.getArticleById(article.getId());
		if (articleExsists == null) {
			resMessage.setMessage("Not found");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		synthesisService.putArticleSynthesize(articleExsists, request);
		synthesisService.start();
		resMessage.setMessage("Đang tổng hợp");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// Normalize Article
	@PostMapping("/newspapers/normalization")
	public ResponseEntity<ResponseMessage> normalizeArticle(@RequestBody Article article) {
		ResponseMessage resMessage = new ResponseMessage();
		// check article exists
		Article articleExsists = articleService.getArticleById(article.getId());
		if (articleExsists == null) {
			resMessage.setMessage("Not found");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		normalizationService.putArticleNSW(articleExsists);
		normalizationService.start();
		resMessage.setMessage("Đang chuẩn hóa");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("newspapers/search")
	public ResponseEntity<ResponseMessage> searchArticles(Integer page, Integer size, String fields, String keyword,
			Integer categoryId, String websiteName, String sort, String type, Integer synthesisType) {
		ResponseMessage resMessage = new ResponseMessage();
		if (page == null)
			page = 0;
		if (size == null)
			size = 10;
		if (categoryId == null)
			categoryId = 0;
		if (sort == null || sort.isEmpty())
			sort = "-publicDate";
		if (fields == null || fields.isEmpty())
			fields = "all";
		if (websiteName == null || websiteName.isEmpty())
			websiteName = "0";
		if (synthesisType == null)
			synthesisType = 0;
		ArticleSearchReponse articleSearchReponse = articleService.searchArticleMvc(page, size, fields, keyword,
				categoryId, websiteName, sort, synthesisType);
		// article view
		// push advertisement
		if ((type == null || type.isEmpty()) && synthesisType == AppConstant.SYNTHESIZED && page == 0) {
			List<ArticleAdvertisement> articleAdvertisements = scheduleArticleService.findArticleByNow(categoryId);
			if (articleAdvertisements != null) {
				for (ArticleAdvertisement articleAdvertisement : articleAdvertisements) {
					if (articleSearchReponse.getArticles().stream()
							.anyMatch(item -> articleAdvertisement.getArticle().getId().equals(item.getId()))) {
						Article articleDuplicate = articleSearchReponse.getArticles().stream()
								.filter(item -> articleAdvertisement.getArticle().getId().equals(item.getId()))
								.findFirst().get();
						articleSearchReponse.getArticles().remove(articleDuplicate);
						System.out.println("remove article dupliacte: " + articleDuplicate.getTitle() + " !!!");
					}
				}
				for (ArticleAdvertisement articleAdvertisement : articleAdvertisements) {
					articleSearchReponse.getArticles().add(articleAdvertisement.getPosition() - 1,
							articleAdvertisement.getArticle());
				}
			}
		}
		if (articleSearchReponse.getArticles().isEmpty()) {
			resMessage.setMessage("Không có kết quả !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		articleSearchReponse.setMaxAudio(AppConstant.AUDIO_MAX);
		int start = page * size + 1;
		resMessage
				.setMessage("Hiển thị từ " + start + " đến " + (page * size + articleSearchReponse.getArticles().size())
						+ " trong " + articleSearchReponse.getTotalResults() + " kết quả");
		resMessage.setStatus(1);
		resMessage.setResults(articleSearchReponse);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("/scheduleArticles")
	public ResponseEntity<ResponseMessage> getScheduleArticles(long startWeek, long endWeek, Integer categoryId,
			Integer articleId) {
		ResponseMessage resMessage = new ResponseMessage();
		List<ScheduleArticle> scheduleArticles = scheduleArticleService.findByWeek(startWeek, endWeek, categoryId,
				articleId);
		if (scheduleArticles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy");
			resMessage.setResults(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Có: " + scheduleArticles.size() + " kết quả !!!");
		resMessage.setResults(scheduleArticles);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("/delete-schedule")
	public ResponseEntity<ResponseMessage> deleteSchedule(String id) {
		ResponseMessage resMessage = new ResponseMessage();
		ScheduleArticle scheduleArticle = scheduleArticleService.findById(id);
		if (scheduleArticle == null) {
			resMessage.setMessage("ScheduleArticle không tồn tại !!!");
			resMessage.setResults(0);
			return ResponseEntity.ok(resMessage);
		}
		scheduleArticleService.delete(scheduleArticle);
		resMessage.setMessage("Xóa thành công !!!");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	@PostMapping("/scheduleArticles")
	public ResponseEntity<ResponseMessage> postScheduleArticles(@RequestBody List<ScheduleArticle> scheduleArticles) {
		ResponseMessage resMessage = new ResponseMessage();
		if (scheduleArticles.isEmpty()) {
			resMessage.setMessage("Array schedule Article rỗng !!!");
			resMessage.setResults(0);
			return ResponseEntity.ok(resMessage);
		}
		Article article = articleService.getArticleById(scheduleArticles.get(0).getArticleId());
		if (article == null) {
			resMessage.setMessage("Article không tồn tại !!!");
			resMessage.setResults(0);
			return ResponseEntity.ok(resMessage);
		}
		List<ScheduleArticle> scheduleArticlesUpdated = scheduleArticleService.saveScheduleArticles(article,
				scheduleArticles);
		resMessage.setMessage("Tạo mới thành công !!!");
		resMessage.setStatus(1);
		resMessage.setResults(scheduleArticlesUpdated);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("/scheduleArticles/findByArticleId")
	public ResponseEntity<ResponseMessage> getScheduleArticles(Integer articleId) {
		ResponseMessage resMessage = new ResponseMessage();
		if (articleId == null || articleId == 0) {
			resMessage.setMessage("Không tìm thấy articleId");
			resMessage.setResults(0);
			return ResponseEntity.ok(resMessage);
		}
		List<ScheduleArticle> scheduleArticles = scheduleArticleService.findByArticleId(articleId);
		if (scheduleArticles.isEmpty()) {
			resMessage.setMessage("Không tìm thấy");
			resMessage.setResults(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Có: " + scheduleArticles.size() + " kết quả !!!");
		resMessage.setResults(scheduleArticles);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("/update-categories-websites")
	public ResponseEntity<ResponseMessage> updateUncheckCategory(String unActiveCategoryIds, String unActiveWebsiteIds,
			String activeCategoryIds, String activeWebsiteIds) {
		ResponseMessage resMessage = new ResponseMessage();
		if (unActiveCategoryIds == null)
			unActiveCategoryIds = "";
		if (unActiveWebsiteIds == null)
			unActiveWebsiteIds = "";
		userService.saveUncheckCategoryWebsites(unActiveCategoryIds, unActiveWebsiteIds, activeCategoryIds,
				activeWebsiteIds);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("/normalization/reset")
	public ResponseEntity<ResponseMessage> resetNormalization() {
		ResponseMessage resMessage = new ResponseMessage();
		normalizationService.reset();
		resMessage.setStatus(1);
		resMessage.setMessage("reset success");
		return ResponseEntity.ok(resMessage);
	}
	
	@GetMapping("/normalization/run")
	public ResponseEntity<ResponseMessage> runNormalization() {
		ResponseMessage resMessage = new ResponseMessage();
		normalizationService.setRun();
		resMessage.setStatus(1);
		resMessage.setMessage("run success");
		return ResponseEntity.ok(resMessage);
	}
}
