package com.vbee.springbootmongodbnewspapersrestapi.dao;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Voice;

public interface IVoiceDao {

	Voice findUrlExists(String oldUrl);

	void removeVoice();

	List<Voice> findByArticleId(Integer articleId, String fields);
	
}
