package com.vbee.springbootmongodbnewspapersrestapi.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Voice;

@Repository
public class VoiceDao implements IVoiceDao {
	private final MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(VoiceDao.class);

	@Autowired
	VoiceDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Voice findUrlExists(String oldUrl) {
		Query query = new Query();
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();
		criterias.add(Criteria.where("contentAudioLink").is(oldUrl));
		criterias.add(Criteria.where("summaryAudioLink").is(oldUrl));
		criteriaFinal = criteriaFinal.orOperator(criterias.toArray(new Criteria[criterias.size()]));
		query.addCriteria(criteriaFinal);
		List<Voice> voices = mongoTemplate.find(query, Voice.class);
		if(voices.size() == 0)
			return null;
		else
			return voices.get(0);
	}

	@Override
	public void removeVoice() {
		Query query = new Query();
		Long time = System.currentTimeMillis() - 7*3600*1000;
		query.addCriteria(Criteria.where("createdDate").lt(time));
		mongoTemplate.remove(query, Voice.class);
	}

	@Override
	public List<Voice> findByArticleId(Integer articleId, String fields) {
		Query query = new Query();
		query.addCriteria(Criteria.where("articleId").is(articleId));
		if(fields != null && !fields.isEmpty()) {
			String[] properties = fields.split(",");
			for (String property : properties) {
				query.fields().include(property.trim());
			}
		}
		List<Voice> voices = mongoTemplate.find(query, Voice.class);
		return voices;
	}
	
	
}
