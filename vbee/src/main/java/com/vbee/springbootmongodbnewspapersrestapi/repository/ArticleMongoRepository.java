package com.vbee.springbootmongodbnewspapersrestapi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Category;

@Repository
public interface ArticleMongoRepository extends MongoRepository<Article, Integer>, PagingAndSortingRepository<Article, Integer>{

	Article findArticleByUrlAndTitle(String url, String title);

	Page<Article> findAll(Pageable pageable);

	Page<Article> findArticlesByCategory(Category category, Pageable pageRequest);

	Page<Article> findArticlesByTags(String name, Pageable pageRequest);

	Article findArticleByTitle(String title);

	Article findArticleByUrl(String url);

	Article findArticleByPublicDate(Long publicDate);

	Article findByCrawlerId(int crawlerId);

	Article findArticleByCrawlerId(Integer crawlerId);

	List<Article> findByWebsiteName(String websiteName);
}
