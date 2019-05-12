package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;
import com.vbee.springbootmongodbnewspapersrestapi.collections.User;
import com.vbee.springbootmongodbnewspapersrestapi.collections.UserCategoryChosen;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Voice;
import com.vbee.springbootmongodbnewspapersrestapi.dao.IArticleDAO;
import com.vbee.springbootmongodbnewspapersrestapi.model.Config;
import com.vbee.springbootmongodbnewspapersrestapi.repository.ArticleMongoRepository;
import com.vbee.springbootmongodbnewspapersrestapi.repository.UserCategoryChosenRepository;
import com.vbee.springbootmongodbnewspapersrestapi.repository.UserMongoRepository;
import com.vbee.springbootmongodbnewspapersrestapi.ulti.UserServiceConnection;

@Service
public class UserService implements IUserService {

	@Autowired
	UserMongoRepository userRepository;

	@Autowired
	IArticleDAO articleDao;

	@Autowired
	ArticleMongoRepository articleMongoRepository;

	@Autowired
	IVoiceService voiceService;

	@Autowired
	Config config;

	@Autowired
	ICategoryService categoryService;

	@Autowired
	UserCategoryChosenRepository userCategoryChosenRepository;

	@Override
	public User insertUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User getUserById(String useryId) {
		return userRepository.findOne(useryId);
	}

	@Override
	public void deleteUser(String userId) {
		userRepository.delete(userId);
	}

	@Override
	public boolean checkUserExists(String userId) {
		if (userRepository.exists(userId)) {
			return true;
		}
		return false;
	}

	@Override
	public List<Integer> getArticleIdsByUserId(String userId) {
		User user = userRepository.findOne(userId);
		if (user == null)
			return new ArrayList<>();
		return user.getArticleIds();
	}

	@Override
	public void updateArticleIds(User user, Integer articleId) {
		List<Integer> list = user.getArticleIds();
		if (!list.contains(articleId)) {
			list.add(articleId);
		}
		userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void removeArticleIds(User user, Integer articleId) {
		List<Integer> list = user.getArticleIds();
		if (list.contains(articleId)) {
			list.remove(articleId);
			userRepository.save(user);
		}
	}

	@Override
	public boolean saveUserToUserService(User userExists) {
		try {
			UserServiceConnection.saveUser(userExists, config.getUserServicePath());
		} catch (Exception e) {
			System.out.println("Error in saveUserToUserService ---- message: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public User updateUser(User user, User userExists) {
		if (user.getVoiceSelected() != null && !user.getVoiceSelected().isEmpty()) {
			userExists.setVoiceSelected(user.getVoiceSelected());
		}
		userExists.setCategoryUnCheckIds(user.getCategoryUnCheckIds());
		userExists.setWebsiteUnCheckIds(user.getWebsiteUnCheckIds());
		return userRepository.save(userExists);
	}

	@Override
	public List<Article> findArticleByRecommendation(User userExists, String fields, String articleIds, Integer size) {
		List<Category> categoriesChosen = new ArrayList<Category>();
		// sort category Chosen theo totalChosen;
		Collections.sort(userExists.getCategoriesChosen(), new Comparator<UserCategoryChosen>() {

			@Override
			public int compare(UserCategoryChosen uc1, UserCategoryChosen uc2) {
				return uc2.getTotalChosen().compareTo(uc1.getTotalChosen());
			}
		});
		// Lấy 3 category được chọn nhiều nhất
		int count = 0;
		for (UserCategoryChosen categoryChosen : userExists.getCategoriesChosen()) {
			if (count == 3) {
				break;
			}
			System.out.println("categoryId: " + categoryChosen.getCategoryId() + " -------- totalChosen: "
					+ categoryChosen.getTotalChosen());
			categoriesChosen.add(categoryService.getCategoryById(categoryChosen.getCategoryId()));
			count++;
		}
		StringBuilder sb = new StringBuilder(articleIds);
		for (Integer articleIdPlayed : userExists.getArticleIdsPlayed()) {
			if (!sb.toString().contains("" + articleIdPlayed))
				sb.append("," + articleIdPlayed);
		}
		System.out.println("article unselect: " + sb.toString());
		return articleDao.getArticlesRecommended(userExists, fields, sb.toString(), categoriesChosen,
				new PageRequest(0, size, new Sort(Sort.DEFAULT_DIRECTION.DESC, "publicDate")));
	}

	@Override
	public List<Article> checkArticleVoicesAndRemoveNullAttribute(List<Article> articles) {
		try {
			JSONArray articlesJSONArray = new JSONArray();
			for (Article article : articles) {
				if (article.getVoices() == null || article.getVoices().isEmpty()) {
					article.setVoices(voiceService.findByArticleId(article.getId(), "name,contentAudioLink,summaryAudioLink"));
				}
				articlesJSONArray.add(article);
			}
			return articlesJSONArray;
		} catch (Exception e) {
			System.out.println("error in checkArticleVoices -- message: " + e.getMessage());
			e.printStackTrace();

		}
		return new ArrayList<>();
	}

	@Override
	public boolean saveActionPlayArticle(User userExists, Article articleExists, String voiceName, Integer duration,
			Integer listen) {
		try {
			// calculator %
			int countListen = articleExists.getTotalListen() + 1;
			articleExists.setTotalListen(countListen);
			double percent = (((double) listen / duration + articleExists.getListeningRate())) / (double) countListen;
			articleExists.setListeningRate(percent);
			if (!userExists.getArticleIdsPlayed().stream().anyMatch(item -> articleExists.getId().equals(item))) {
				userExists.getArticleIdsPlayed().add(articleExists.getId());
			}
			// save category chosen

			if (userExists.getCategoriesChosen().stream()
					.anyMatch(item -> articleExists.getCategory().getId().equals(item.getCategoryId()))) {
				UserCategoryChosen categoryChosen = userCategoryChosenRepository
						.findByUserIdAndCategoryId(userExists.getId(), articleExists.getCategory().getId());
				int totalChosen = categoryChosen.getTotalChosen() + 1;
				categoryChosen.setTotalChosen(totalChosen);
				userExists.getCategoriesChosen().stream()
						.filter(item -> articleExists.getCategory().getId().equals(item.getCategoryId())).findFirst()
						.get().setTotalChosen(totalChosen);
				categoryChosen = userCategoryChosenRepository.save(categoryChosen);
			} else {
				UserCategoryChosen categoryChosenNew = new UserCategoryChosen(userExists.getId(),
						articleExists.getCategory().getId());
				categoryChosenNew.setTotalChosen(1);
				userCategoryChosenRepository.save(categoryChosenNew);
				userExists.getCategoriesChosen().add(categoryChosenNew);
			}
			userRepository.save(userExists);
			articleMongoRepository.save(articleExists);
			// StatisticsServiceConnection.saveArticleStaistics(userExists.getId(),
			// articleExists, voiceName, duration, listen,
			// config.getStatisticsServicePath());
		} catch (Exception e) {
			System.out.println("Error in saveStatisticsArticle ---- message: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean saveActionChooseArticle(String userId, Article articleExists) {
		try {
			int countChoose = articleExists.getTotalChoose() + 1;
			articleExists.setTotalChoose(countChoose);
			articleMongoRepository.save(articleExists);
		} catch (Exception e) {
			System.out.println("Error in save Choose Article statistics ---- message: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	public void saveUncheckCategoryWebsites(String unActiveCategoryIds, String unActiveWebsiteIds,
			String activeCategoryIds, String activeWebsiteIds) {
		String[] categoryIdsUnActive = unActiveCategoryIds.split(",");
		String[] websiteIdsUnActive = unActiveWebsiteIds.split(",");
		String[] categoryIdsActive = activeCategoryIds.split(",");
		String[] websiteIdsActive = activeWebsiteIds.split(",");
		for (User user : userRepository.findAll()) {
			// check un Active
			if (!unActiveCategoryIds.isEmpty()) {
				List<Integer> addIds = new ArrayList<>();
				for (String categoryId : categoryIdsUnActive) {
					if (!user.getCategoryUnCheckIds().contains(Integer.parseInt(categoryId))) { 
						addIds.add(Integer.parseInt(categoryId));
					}
				}
				user.getCategoryUnCheckIds().addAll(addIds);
			}
			if (!unActiveWebsiteIds.isEmpty()) {
				List<String> addIds = new ArrayList<>();
				for (String websiteId : websiteIdsUnActive) {
					if (!user.getWebsiteUnCheckIds().contains(websiteId)) {
						addIds.add(websiteId);
					}
				}
				user.getWebsiteUnCheckIds().addAll(addIds);
			}

			// check Active
			if (!activeCategoryIds.isEmpty()) {
				List<Integer> removeIds = new ArrayList<>();
				System.out.println(user.getCategoryUnCheckIds().toString());
				for (String categoryId : categoryIdsActive) {
					if (user.getCategoryUnCheckIds().contains(Integer.parseInt(categoryId))) {
						removeIds.add(Integer.parseInt(categoryId));
					}
				}
				user.getCategoryUnCheckIds().removeAll(removeIds);
			}
			if (!activeWebsiteIds.isEmpty()) {
				System.out.println(user.getWebsiteUnCheckIds().toString());
				List<String> removeIds = new ArrayList<>();
				for (String websiteId : websiteIdsActive) {
					if (user.getWebsiteUnCheckIds().contains(websiteId)) {
						removeIds.add(websiteId);
					}
				}
				user.getWebsiteUnCheckIds().removeAll(removeIds);
			}
			userRepository.save(user);
		}

	}

}
