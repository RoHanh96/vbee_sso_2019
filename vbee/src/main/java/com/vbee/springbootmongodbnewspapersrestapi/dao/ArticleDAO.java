package com.vbee.springbootmongodbnewspapersrestapi.dao;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;
import com.vbee.springbootmongodbnewspapersrestapi.collections.User;
import com.vbee.springbootmongodbnewspapersrestapi.config.AppConstant;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleSearchReponse;

@Repository
public class ArticleDAO implements IArticleDAO {
	private final MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(ArticleDAO.class);

	@Autowired
	com.vbee.springbootmongodbnewspapersrestapi.model.Config configProperties;

	@Autowired
	ArticleDAO(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<Article> fullTextSearch(String keyword, String fields, Pageable pageable) {
		TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(keyword);
		Query query = TextQuery.queryText(criteria).sortByScore();
		if (!fields.equals("all")) {
			String[] properties = fields.split(",");
			for (String property : properties) {
				query.fields().include(property.trim());
			}
		}
		query.with(pageable);
		List<Article> recipes = mongoTemplate.find(query, Article.class);
		return recipes;
	}

	// { "notes" : { "$elemMatch" : { "title" : "Hello MongoDB"}
	@Override
	public List<Article> getArticlesByTagName(String tagName, String fields, Pageable pageable) {
		Query query = new Query(Criteria.where("tags").elemMatch(Criteria.where("name").is(tagName)));
		query.with(pageable);
		if (!fields.equals("all")) {
			String[] properties = fields.split(",");
			for (String property : properties) {
				query.fields().include(property.trim());
			}
		}
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesByFields(String[] fields, Pageable pageable) {
		Query query = new Query();
		query.with(pageable);
		for (String property : fields) {
			query.fields().include(property.trim());
		}

		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public Article getArticleByIdAndFields(Integer articleId, String[] fields) {
		Query query = new Query(Criteria.where("id").is(articleId));
		for (String property : fields) {
			query.fields().include(property.trim());
		}
		List<Article> articles = mongoTemplate.find(query, Article.class);
		if (articles.isEmpty())
			return null;
		else
			return articles.get(0);
	}

	@Override
	public List<Article> getArticlesCategoryPageAndFields(Category category, String[] properties, Pageable pageable) {
		Query query = new Query(Criteria.where("category").is(category));
		for (String property : properties) {
			query.fields().include(property.trim());
		}
		query.with(pageable);
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesByPageHadSynthesized(PageRequest pageRequest) {
		Query query = new Query(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		query.with(pageRequest);
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesByFiledsAndPageHadSynthesized(String[] properties, PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();
		// Criteria Article Synthesized
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		// Criteria Article active;
		criterias.add(Criteria.where("status").is(AppConstant.ARTICLE_ACTIVE));
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));
		Query query = new Query();
		query.addCriteria(criteriaFinal);
		query.with(pageRequest);
		if (!properties[0].equals("all")) {
			for (String property : properties) {
				query.fields().include(property.trim());
			}
		}
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesByPageAndCategoriesHadSynthesized(List<Category> categories,
			PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria categories
		Criteria criteriaOrCategory = new Criteria();
		List<Criteria> criteriaCategories = new ArrayList<Criteria>(categories.size());
		for (Category category : categories) {
			criteriaCategories.add(Criteria.where("category").is(category));
		}
		criteriaOrCategory = criteriaOrCategory.orOperator(criteriaCategories.toArray(new Criteria[categories.size()]));

		// Criteria And Operator
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		criterias.add(criteriaOrCategory);
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query();
		query.addCriteria(criteriaFinal);
		query.with(pageRequest);
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesByPageAndWebsitesHadSynthesized(String[] websiteNames, PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria websites
		Criteria criteriaOrWebsite = new Criteria();
		List<Criteria> criteriaWebsites = new ArrayList<Criteria>(websiteNames.length);
		for (String websiteName : websiteNames) {
			criteriaWebsites.add(Criteria.where("websiteName").is(websiteName));
		}
		criteriaOrWebsite = criteriaOrWebsite.orOperator(criteriaWebsites.toArray(new Criteria[websiteNames.length]));

		// Criteria And Operator
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		criterias.add(criteriaOrWebsite);
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query();
		query.addCriteria(criteriaFinal);
		query.with(pageRequest);
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesByPageAndWebsitesAndCategoriesHadSynthesized(String[] websiteNames,
			List<Category> categories, PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria websites
		Criteria criteriaOrWebsite = new Criteria();
		List<Criteria> criteriaWebsites = new ArrayList<Criteria>(websiteNames.length);
		for (String websiteName : websiteNames) {
			criteriaWebsites.add(Criteria.where("websiteName").is(websiteName));
		}
		criteriaOrWebsite = criteriaOrWebsite.orOperator(criteriaWebsites.toArray(new Criteria[websiteNames.length]));

		// Criteria categories
		Criteria criteriaOrCategory = new Criteria();
		List<Criteria> criteriaCategories = new ArrayList<Criteria>(categories.size());
		for (Category category : categories) {
			criteriaCategories.add(Criteria.where("category").is(category));
		}
		criteriaOrCategory = criteriaOrCategory.orOperator(criteriaCategories.toArray(new Criteria[categories.size()]));

		// Criteria And Operator
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		criterias.add(criteriaOrCategory);
		criterias.add(criteriaOrWebsite);
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query();
		query.addCriteria(criteriaFinal);
		query.with(pageRequest);
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesByFieldsAndPageAndCategoriesHadSynthesized(String[] properties,
			List<Category> categories, PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria categories
		Criteria criteriaOrCategory = new Criteria();
		List<Criteria> criteriaCategories = new ArrayList<Criteria>(categories.size());
		for (Category category : categories) {
			criteriaCategories.add(Criteria.where("category").is(category));
		}
		criteriaOrCategory = criteriaOrCategory.orOperator(criteriaCategories.toArray(new Criteria[categories.size()]));

		// Criteria And Operator
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		criterias.add(criteriaOrCategory);
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query();
		query.addCriteria(criteriaFinal);
		query.with(pageRequest);
		for (String property : properties) {
			query.fields().include(property.trim());
		}
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesByFieldsAndPageAndWebsitesHadSynthesized(String[] properties, String[] websiteNames,
			PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria websites
		Criteria criteriaOrWebsite = new Criteria();
		List<Criteria> criteriaWebsites = new ArrayList<Criteria>(websiteNames.length);
		for (String websiteName : websiteNames) {
			criteriaWebsites.add(Criteria.where("websiteName").is(websiteName));
		}
		criteriaOrWebsite = criteriaOrWebsite.orOperator(criteriaWebsites.toArray(new Criteria[websiteNames.length]));

		// Criteria And Operator
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		criterias.add(criteriaOrWebsite);
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query();
		query.addCriteria(criteriaFinal);
		query.with(pageRequest);
		for (String property : properties) {
			query.fields().include(property.trim());
		}
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesByFieldsAndPageAndWebsitesAndCategoriesHadSynthesized(String[] properties,
			String[] websiteNames, List<Category> categories, PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria websites
		Criteria criteriaOrWebsite = new Criteria();
		List<Criteria> criteriaWebsites = new ArrayList<Criteria>(websiteNames.length);
		for (String websiteName : websiteNames) {
			criteriaWebsites.add(Criteria.where("websiteName").is(websiteName));
		}
		criteriaOrWebsite = criteriaOrWebsite.orOperator(criteriaWebsites.toArray(new Criteria[websiteNames.length]));

		// Criteria categories
		Criteria criteriaOrCategory = new Criteria();
		List<Criteria> criteriaCategories = new ArrayList<Criteria>(categories.size());
		for (Category category : categories) {
			criteriaCategories.add(Criteria.where("category").is(category));
		}
		criteriaOrCategory = criteriaOrCategory.orOperator(criteriaCategories.toArray(new Criteria[categories.size()]));

		// Criteria And Operator
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		criterias.add(criteriaOrCategory);
		criterias.add(criteriaOrWebsite);
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query();
		query.addCriteria(criteriaFinal);
		query.with(pageRequest);
		for (String property : properties) {
			query.fields().include(property.trim());
		}
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesHadSynthesizedCategoryPage(Category category, PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();
		// Criteria Article Synthesized
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		// Criteria Article active;
		criterias.add(Criteria.where("status").is(AppConstant.ARTICLE_ACTIVE));
		// Criteria Category
		criterias.add(Criteria.where("category").is(category));
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));
		Query query = new Query();
		query.with(pageRequest);
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesHadSynthesizedCategoryPageAndFields(Category category, String[] properties,
			PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();
		// Criteria Article Synthesized
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		// Criteria Article active;
		criterias.add(Criteria.where("status").is(AppConstant.ARTICLE_ACTIVE));
		// Criteria Category
		criterias.add(Criteria.where("category").is(category));
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));
		Query query = new Query();
		query.addCriteria(criteriaFinal);
		for (String property : properties) {
			query.fields().include(property.trim());
		}
		query.with(pageRequest);
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> fullTextSearchAndCategoriesAndWebsites(String keyword, String fields, String websiteNames,
			String categoriesId, PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria websites

		Criteria criteriaOrWebsite = new Criteria();
		if (!websiteNames.equals("all")) {
			if (websiteNames.contains(",")) {
				String[] websiteNamesArrary = websiteNames.split(",");
				List<Criteria> criteriaWebsites = new ArrayList<Criteria>(websiteNamesArrary.length);
				for (String websiteName : websiteNamesArrary) {
					criteriaWebsites.add(Criteria.where("websiteName").is(websiteName));
				}
				criteriaOrWebsite = criteriaOrWebsite
						.orOperator(criteriaWebsites.toArray(new Criteria[websiteNamesArrary.length]));
			} else {
				criteriaOrWebsite = Criteria.where("websiteName").is(websiteNames);
			}

			// Criteria And Operator
			criterias.add(criteriaOrWebsite);
		}

		// Criteria categories
		Criteria criteriaOrCategory = new Criteria();
		if (!categoriesId.equals("all")) {
			if (categoriesId.contains(",")) {
				String[] categoriesIdsArrary = categoriesId.split(",");
				List<Criteria> criteriaCategories = new ArrayList<Criteria>(categoriesIdsArrary.length);
				for (String categoryId : categoriesIdsArrary) {
					criteriaCategories.add(Criteria.where("category").elemMatch(Criteria.where("id").is(categoryId)));
				}
				criteriaOrCategory = criteriaOrCategory
						.orOperator(criteriaCategories.toArray(new Criteria[categoriesIdsArrary.length]));
			} else {
				criteriaOrCategory = Criteria.where("category").elemMatch(Criteria.where("id").is(categoriesId));
			}

			// Criteria And Operator
			criterias.add(criteriaOrCategory);
		}

		// Criteria full text search
		TextCriteria criteriaFullTextSearch = TextCriteria.forDefaultLanguage().matchingAny(keyword);

		// Criteria And Operator
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = TextQuery.queryText(criteriaFullTextSearch).sortByScore();
		if (!categoriesId.equals("all") && !websiteNames.equals("all")) {
			query.addCriteria(criteriaFinal);
		}
		if (!fields.equals("all")) {
			String[] properties = fields.split(",");
			for (String property : properties) {
				query.fields().include(property.trim());
			}
		}
		query.with(pageRequest);
		List<Article> recipes = mongoTemplate.find(query, Article.class);
		return recipes;
	}

	@Override
	public List<Article> getArticlesByFieldsAndPageAndCategoriesUncheckHadSynthesized(String[] properties,
			List<Category> categories, PageRequest pageRequest) {

		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria categories
		Criteria criteriaAndCategory = new Criteria();
		List<Criteria> criteriaCategories = new ArrayList<Criteria>(categories.size());
		for (Category category : categories) {
			criteriaCategories.add(Criteria.where("category").ne(category));
		}
		criteriaAndCategory = criteriaAndCategory
				.andOperator(criteriaCategories.toArray(new Criteria[categories.size()]));

		// Criteria And Operator
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		criterias.add(Criteria.where("status").is(AppConstant.ARTICLE_ACTIVE));
		criterias.add(criteriaAndCategory);
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query();

		query.addCriteria(criteriaFinal);
		if (properties.length != 0) {
			for (String property : properties) {
				query.fields().include(property);
			}
		}
		query.with(pageRequest);
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesByFieldsAndPageAndWebsitesUncheckHadSynthesized(String[] properties,
			List<String> websiteIds, PageRequest pageRequest) {

		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria websites
		Criteria criteriaAndWebsite = new Criteria();
		List<Criteria> criteriaWebsites = new ArrayList<Criteria>(websiteIds.size());
		for (String websiteId : websiteIds) {
			criteriaWebsites.add(Criteria.where("websiteId").ne(websiteId));
		}
		criteriaAndWebsite = criteriaAndWebsite.andOperator(criteriaWebsites.toArray(new Criteria[websiteIds.size()]));

		// Criteria And Operator
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		// Criteria Article active;
		criterias.add(Criteria.where("status").is(AppConstant.ARTICLE_ACTIVE));
		criterias.add(criteriaAndWebsite);
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query();
		query.addCriteria(criteriaFinal);
		if (properties.length != 0) {
			for (String property : properties) {
				query.fields().include(property);
			}
		}
		query.with(pageRequest);
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesByFieldsAndPageAndWebsitesUncheckAndCategoriesUncheckHadSynthesized(
			String[] properties, List<String> websiteIds, List<Category> categories, PageRequest pageRequest) {

		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria websites
		Criteria criteriaAndWebsite = new Criteria();
		List<Criteria> criteriaWebsites = new ArrayList<Criteria>(websiteIds.size());
		for (String websiteId : websiteIds) {
			criteriaWebsites.add(Criteria.where("websiteId").ne(websiteId));
		}
		criteriaAndWebsite = criteriaAndWebsite.andOperator(criteriaWebsites.toArray(new Criteria[websiteIds.size()]));

		// Criteria categories
		Criteria criteriaAndCategory = new Criteria();
		List<Criteria> criteriaCategories = new ArrayList<Criteria>(categories.size());
		for (Category category : categories) {
			criteriaCategories.add(Criteria.where("category").ne(category));
		}
		criteriaAndCategory = criteriaAndCategory
				.andOperator(criteriaCategories.toArray(new Criteria[categories.size()]));

		// Criteria And Operator
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		// Criteria Article active;
		criterias.add(Criteria.where("status").is(AppConstant.ARTICLE_ACTIVE));
		criterias.add(criteriaAndCategory);
		criterias.add(criteriaAndWebsite);
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query();
		query.addCriteria(criteriaFinal);
		if (properties.length != 0) {
			for (String property : properties) {
				query.fields().include(property);
			}
		}
		query.with(pageRequest);
		List<Article> articles = mongoTemplate.find(query, Article.class);
		return articles;
	}

	@Override
	public List<Article> getArticlesRecommended(User userExists, String fields, String articleIds,
			List<Category> categoriesChosen, PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria websites unselect

		if (!userExists.getWebsiteUnCheckIds().isEmpty()) {
			Criteria criteriaAndWebsiteUnselect = new Criteria();
			List<Criteria> criteriaWebsites = new ArrayList<Criteria>(userExists.getWebsiteUnCheckIds().size());
			for (String websiteId : userExists.getWebsiteUnCheckIds()) {
				criteriaWebsites.add(Criteria.where("websiteId").ne(websiteId));
			}
			criteriaAndWebsiteUnselect = criteriaAndWebsiteUnselect
					.andOperator(criteriaWebsites.toArray(new Criteria[userExists.getWebsiteUnCheckIds().size()]));

			// Criteria And Operator
			criterias.add(criteriaAndWebsiteUnselect);
		}

		// Criteria categories unselect

		if (!userExists.getCategoryUnCheckIds().isEmpty()) {
			Criteria criteriaAndCategoryUnselect = new Criteria();
			List<Criteria> criteriaCategories = new ArrayList<Criteria>(userExists.getCategoryUnCheckIds().size());
			Category category = null;
			for (Integer categoryId : userExists.getCategoryUnCheckIds()) {
				category = mongoTemplate.findById(categoryId, Category.class);
				criteriaCategories.add(Criteria.where("category").ne(category));
			}
			category = null;
			criteriaAndCategoryUnselect = criteriaAndCategoryUnselect
					.andOperator(criteriaCategories.toArray(new Criteria[userExists.getCategoryUnCheckIds().size()]));
			// Criteria And Operator
			criterias.add(criteriaAndCategoryUnselect);
		}

		// Criteria categories selected
		if (!categoriesChosen.isEmpty()) {
			Criteria criteriaOrCategorySelect = new Criteria();
			List<Criteria> criteriaCategories = new ArrayList<Criteria>(categoriesChosen.size());
			for (Category categoryChosen : categoriesChosen) {
				criteriaCategories.add(Criteria.where("category").is(categoryChosen));
			}
			criteriaOrCategorySelect = criteriaOrCategorySelect
					.orOperator(criteriaCategories.toArray(new Criteria[categoriesChosen.size()]));

			// Criteria And Operator
			criterias.add(criteriaOrCategorySelect);
		}

		// Criteria unselect articles
		String[] articlesUnSelect = articleIds.split(",");
		Criteria criteriaAndArticleUnselect = new Criteria();
		List<Criteria> criteriaArticleUnSelects = new ArrayList<Criteria>(articlesUnSelect.length);
		for (String articleId : articlesUnSelect) {
			criteriaArticleUnSelects.add(Criteria.where("id").ne(Integer.parseInt(articleId)));
		}

		// criteria and opretor unselect articles
		criteriaAndArticleUnselect = criteriaAndArticleUnselect
				.andOperator(criteriaArticleUnSelects.toArray(new Criteria[articlesUnSelect.length]));
		criterias.add(criteriaAndArticleUnselect);
		// Criteria Article Synthesized
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		// Criteria Article active;
		criterias.add(Criteria.where("status").is(AppConstant.ARTICLE_ACTIVE));
		// Criteria And Operator
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query(criteriaFinal);
		if (!fields.equals("all")) {
			String[] properties = fields.split(",");
			for (String property : properties) {
				query.fields().include(property.trim());
			}
		}
		query.with(pageRequest);
		List<Article> recipes = mongoTemplate.find(query, Article.class);
		return recipes;
	}

	@Override
	public ArticleSearchReponse searchArticleMvc(Integer page, Integer size, String fields, String keyword,
			Integer categoryId, String websiteName, String sort, Integer synthesisType) {
		ArticleSearchReponse articleSearchReponse = new ArticleSearchReponse();
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria category
		if (categoryId != 0) {
			Category category = mongoTemplate.findById(categoryId, Category.class);
			Criteria criteriaCategory = Criteria.where("category").is(category);
			criterias.add(criteriaCategory);
		}
		// Criteria website
		if (!websiteName.equals("0")) {
			Criteria criteriaWebsite = Criteria.where("websiteName").is(websiteName);
			criterias.add(criteriaWebsite);
		}
		// Criteria full text search
		TextCriteria criteriaFullTextSearch = TextCriteria.forDefaultLanguage().matchingAny(keyword);
		// Criteria filter synthesis type
		if (synthesisType == AppConstant.SYNTHESIZING) {
			System.out.println("synthesizing");
			criterias.add(Criteria.where("countAudioSynthesize").lt(AppConstant.AUDIO_MAX));
		} else if (synthesisType == AppConstant.SYNTHESIZED) {
			System.out.println("synthesized");
			criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		} else if (synthesisType == AppConstant.SYNTHESIZED_ERROR) {
			criterias.add(Criteria.where("synthesisType").is(synthesisType));
		}

		// Criteria And Operator
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));
		Query query = new Query();
		if (!keyword.isEmpty()) {
			query = TextQuery.queryText(criteriaFullTextSearch).sortByScore();
		}
		if (!criterias.isEmpty())
			query.addCriteria(criteriaFinal);
		if (!fields.equals("all")) {
			String[] properties = fields.split(",");
			for (String property : properties) {
				query.fields().include(property.trim());
			}
		}
		long maxResults = mongoTemplate.count(query, Article.class);
		System.out.println("maxResults: " + maxResults);
		articleSearchReponse.setTotalResults(maxResults);
		PageRequest pageRequest = null;
		if (sort.contains("<")) {
			String property = sort.replace("<", "");
			pageRequest = new PageRequest(page, size, new Sort(Direction.DESC, property));
			query.with(pageRequest);
		} else if (sort.contains(">")) {
			String property = sort.replaceAll(">", "");
			pageRequest = new PageRequest(page, size, new Sort(Direction.ASC, property));
			query.with(pageRequest);
		}
		List<Article> recipes = mongoTemplate.find(query, Article.class);
		articleSearchReponse.setArticles(recipes);
		articleSearchReponse.setTotalPages(getTotalPages(size, maxResults));
		return articleSearchReponse;
	}

	private long getTotalPages(Integer size, long totalResults) {
		if (totalResults <= size)
			return 1;
		else {
			return totalResults / size + 1;
		}
	}

	@Override
	public List<Article> findArticlesByArticleNext(Article article, String fields, Integer categoryId, Integer size) {
		Query query = new Query();
		if (categoryId != 0) {
			Category category = mongoTemplate.findOne(new Query(Criteria.where("id").is(categoryId)), Category.class);
			if (category != null) {
				query.addCriteria(Criteria.where("category").is(category));
			}
		}
		System.out.println("category: " + categoryId);
		query.addCriteria(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		query.addCriteria(Criteria.where("status").is(AppConstant.ARTICLE_ACTIVE));
		query.addCriteria(Criteria.where("publicDate").lte(article.getPublicDate()));
		query.addCriteria(Criteria.where("id").ne(article.getId()));
		if (!fields.equals("all")) {
			String[] properties = fields.split(",");
			for (String property : properties) {
				query.fields().include(property.trim());
			}
		}
		query.with(new PageRequest(0, size - 1, new Sort(Direction.DESC, "publicDate")));
		List<Article> recipes = mongoTemplate.find(query, Article.class);
		if (recipes == null)
			return new ArrayList<>();
		recipes.add(0, article);
		return recipes;
	}

	@Override
	public List<Article> findByCategorySynthesizedAndPaging(User userExists, Category category, String fields,
			PageRequest pageRequest) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria websites
		if (!userExists.getWebsiteUnCheckIds().isEmpty()) {
			Criteria criteriaAndWebsite = new Criteria();
			List<Criteria> criteriaWebsites = new ArrayList<Criteria>(userExists.getWebsiteUnCheckIds().size());
			for (String websiteId : userExists.getWebsiteUnCheckIds()) {
				criteriaWebsites.add(Criteria.where("websiteId").ne(websiteId));
			}
			criteriaAndWebsite = criteriaAndWebsite
					.andOperator(criteriaWebsites.toArray(new Criteria[userExists.getWebsiteUnCheckIds().size()]));
			criterias.add(criteriaAndWebsite);
		}

		// Criteria categories
		if (!userExists.getCategoryUnCheckIds().isEmpty()) {
			Criteria criteriaAndCategory = new Criteria();
			List<Criteria> criteriaCategories = new ArrayList<Criteria>(userExists.getCategoryUnCheckIds().size());
			for (Integer categoryId : userExists.getCategoryUnCheckIds()) {
				Category categoryUncheck = mongoTemplate.findById(categoryId, Category.class);
				criteriaCategories.add(Criteria.where("category").ne(categoryUncheck));
			}
			criteriaAndCategory = criteriaAndCategory
					.andOperator(criteriaCategories.toArray(new Criteria[userExists.getCategoryUnCheckIds().size()]));
			criterias.add(criteriaAndCategory);
		}

		// Criteria And Operator
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		// Criteria Article active;
		criterias.add(Criteria.where("status").is(AppConstant.ARTICLE_ACTIVE));
		// Criteria filter category

		// categoryy "Thời tiết"
		// if(category.getId() == 20) {
		// DateTime dateTime = new DateTime(System.currentTimeMillis());
		// Long startOfDay = dateTime.withTimeAtStartOfDay().getMillis();
		// Long endOfDay = startOfDay + ( 24 * 60 * 60 * 1000);
		// criterias.add(Criteria.where("publicDate").gte(startOfDay));
		// criterias.add(Criteria.where("publicDate").lte(endOfDay));
		// }

		criterias.add(Criteria.where("category").is(category));
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query();
		query.addCriteria(criteriaFinal);
		if (!fields.equals("all")) {
			String[] properties = fields.split(",");
			for (String property : properties) {
				query.fields().include(property.trim());
			}
		}
		query.with(pageRequest);
		List<Article> articles = mongoTemplate.find(query, Article.class);
		criteriaFinal = null;
		criterias = null;
		query = null;
		return articles;
	}

	@Override
	public Article findByWeather(String fields) {
		List<Criteria> criterias = new ArrayList<Criteria>();
		Criteria criteriaFinal = new Criteria();
		DateTime dateTime = new DateTime(System.currentTimeMillis());
		Long startOfDay = dateTime.withTimeAtStartOfDay().getMillis();
		Long endOfDay = startOfDay + (24 * 60 * 60 * 1000);
		criterias.add(Criteria.where("publicDate").gte(startOfDay));
		criterias.add(Criteria.where("publicDate").lte(endOfDay));
		Category categoryWeather = mongoTemplate.findById(20, Category.class);
		criterias.add(Criteria.where("category").is(categoryWeather));
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));
		Query query = new Query();
		query.addCriteria(criteriaFinal);
		if (!fields.isEmpty()) {
			String[] properties = fields.split(",");
			for (String property : properties) {
				query.fields().include(property.trim());
			}
		}
		List<Article> articles = mongoTemplate.find(query, Article.class);
		if (articles.size() == 0)
			return null;
		else
			return articles.get(0);
	}

}
