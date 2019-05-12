package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Voice;

public interface IVoiceService {

	Voice findVoiceByName(String name);

	Voice getVoicesByNameAndArticleId(String name, int articleId);

	Voice insertVoice(Voice voice);

	List<Voice> getAllVoice();

	boolean checkVoiceExist(String name, int articleId);

	void updateVoiceByMultiParam(Voice voice, String name, String audioLink, Integer part, int articleId);

	void updateVoiceByMultiParams(Voice voice, String name, String audioLink, String virtualLink, String contentType,
			int crawlerId);

	Voice getVoicesByNameAndCrawlerId(String name, Integer crawlerId);

	List<Voice> findByArticleId(Integer articleId, String fields);

	void updateVoice(Voice newVoice, String name, Integer id, String audioUrl);

	boolean updateUrlVoice(String oldUrl, String newUrl);

	void removeVoice();
}
