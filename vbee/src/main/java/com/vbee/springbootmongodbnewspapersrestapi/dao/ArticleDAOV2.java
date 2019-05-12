package com.vbee.springbootmongodbnewspapersrestapi.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;
import com.vbee.springbootmongodbnewspapersrestapi.config.AppConstant;

@Repository
public class ArticleDAOV2 implements IArticleDAOV2 {

	private static final Logger logger = LoggerFactory.getLogger(ArticleDAOV2.class);

	private final MongoTemplate mongoTemplate;

	private final List<String> allowProperties = getListAllowProperties();

	@Autowired
	com.vbee.springbootmongodbnewspapersrestapi.model.Config configProperties;

	@Autowired
	ArticleDAOV2(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private List<String> getListAllowProperties() {
		List<String> list = new ArrayList<>();
		String[] properties = "title,lead,content,publicDate,voices,websiteName,websiteId,url,category,summary"
				.split(",");
		for (String property : properties) {
			list.add(property);
		}
		return list;
	}

	@Override
	public List<Article> findArticleByCategoryIdsAndWebsiteIds(String categoryIds, String websiteIds, Integer page,
			Integer size, String fields) {
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// Criteria websites
		if (websiteIds != null && !websiteIds.isEmpty()) {
			Criteria criteriaAndWebsite = new Criteria();
			String[] listWebsiteIds = websiteIds.split(",");
			List<Criteria> criteriaWebsites = new ArrayList<Criteria>(listWebsiteIds.length);
			for (String websiteId : listWebsiteIds) {
				criteriaWebsites.add(Criteria.where("websiteId").is(websiteId));
			}
			criteriaAndWebsite = criteriaAndWebsite
					.orOperator(criteriaWebsites.toArray(new Criteria[listWebsiteIds.length]));
			criterias.add(criteriaAndWebsite);
		}

		// Criteria categories
		if (categoryIds != null && !categoryIds.isEmpty()) {
			Criteria criteriaAndCategory = new Criteria();
			String[] listCategoryIds = categoryIds.split(",");
			List<Criteria> criteriaCategories = new ArrayList<Criteria>(listCategoryIds.length);
			for (String categoryStringId : listCategoryIds) {
				try {
					Integer categoryId = Integer.parseInt(categoryStringId);
					criteriaCategories.add(Criteria.where("category.id").is(categoryId));
				} catch (NumberFormatException e) {
					// ignore categoryId
				}
			}
			criteriaAndCategory = criteriaAndCategory
					.orOperator(criteriaCategories.toArray(new Criteria[listCategoryIds.length]));
			criterias.add(criteriaAndCategory);
		}

		// Criteria And Operator
		criterias.add(Criteria.where("countAudioSynthesize").gte(AppConstant.AUDIO_MAX));
		// Criteria Article active;
		criterias.add(Criteria.where("status").is(AppConstant.ARTICLE_ACTIVE));
		// Criteria voice chosen
		criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));

		Query query = new Query();
		query.addCriteria(criteriaFinal);
		if (fields != null && !fields.isEmpty()) {
			String[] properties = fields.split(",");
			for (String property : properties) {
				if (allowProperties.contains(property)) {
					query.fields().include(property.trim());
				}
			}
		} else {
			for (String property : allowProperties) {
				query.fields().include(property.trim());
			}
		}
		// alway include voices field
		query.fields().include("voices");
		if(page == null) page = 0;
		if(size == null || size == 0) size = 10;
		else if(size > 50) size = 50;
		query.with(new PageRequest(page, size, new Sort(Sort.DEFAULT_DIRECTION.DESC, "publicDate")));
		List<Article> articles = mongoTemplate.find(query, Article.class);
		criteriaFinal = null;
		criterias = null;
		query = null;
		return articles;
	}

}
