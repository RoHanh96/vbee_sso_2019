package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Voice;
import com.vbee.springbootmongodbnewspapersrestapi.config.AppConstant;
import com.vbee.springbootmongodbnewspapersrestapi.dao.IVoiceDao;
import com.vbee.springbootmongodbnewspapersrestapi.repository.ArticleMongoRepository;
import com.vbee.springbootmongodbnewspapersrestapi.repository.VoiceMongoRepository;

@Service
public class VoiceService implements IVoiceService {

	@Autowired
	VoiceMongoRepository voiceRepository;

	@Autowired
	ArticleMongoRepository articleRepository;

	// @Autowired
	// NotificationService notificationService;

	@Autowired
	IVoiceDao voiceDao;

	private static final Logger logger = LoggerFactory.getLogger(VoiceService.class);

	@Override
	public boolean checkVoiceExist(String name, int articleId) {
		Voice voiceExists = voiceRepository.findVoicesByNameAndArticleId(name, articleId);
		if (voiceExists == null)
			return false;
		return true;
	}

	@Override
	public Voice findVoiceByName(String name) {
		return voiceRepository.findVoiceByName(name);
	}

	@Override
	public Voice getVoicesByNameAndArticleId(String name, int articleId) {
		return voiceRepository.findVoicesByNameAndArticleId(name, articleId);
	}

	@Override
	public Voice insertVoice(Voice voice) {
		voice.setCreatedDate(System.currentTimeMillis());
		return voiceRepository.save(voice);
	}

	@Override
	public List<Voice> getAllVoice() {
		return voiceRepository.findAll();
	}

	@Override
	public void updateVoiceByMultiParam(Voice voice, String name, String audioLink, Integer part, int articleId) {
		voice.setArticleId(articleId);
		voice.setName(name);
		if (part == AppConstant.TITLE_PART) {
			voice.setTitleAudioLink(audioLink);
		} else if (part == AppConstant.CONTENT_PART) {
			voice.setContentAudioLink(audioLink);
		} else if (part == AppConstant.SUMMARY_PART) {
			voice.setSummaryAudioLink(audioLink);
		}
		voice.setCreatedDate(System.currentTimeMillis());
		voiceRepository.save(voice);
	}

	@Override
	public void updateVoiceByMultiParams(Voice voice, String name, String audioLink, String virtualLink,
			String contentType, int crawlerId) {
		voice.setCrawlerId(crawlerId);
		Article article = articleRepository.findByCrawlerId(crawlerId);
		if (article == null) {
			logger.info("Không tìm thầy article có crawlerId = " + crawlerId);
			return;
		}
		logger.info("receive voice: " + voice.getName() + " --- crawlerId: " + crawlerId + " --- articleId: "
				+ article.getId());
		int countIncrease = article.getCountAudioSynthesize();
		voice.setArticleId(article.getId());
		voice.setName(name);
		if (contentType.equals(AppConstant.TITLE_TYPE)) {
			if (voice.getTitleAudioLink() == null || voice.getTitleAudioLink().isEmpty())
				countIncrease++;
			voice.setTitleAudioLink(audioLink);
			voice.setTitleAudioVirtualLink(virtualLink);
		} else if (contentType.equals(AppConstant.CONTENT_TYPE)) {
			if (voice.getContentAudioLink() == null || voice.getContentAudioLink().isEmpty())
				countIncrease++;
			voice.setContentAudioLink(audioLink);
			voice.setContentAudioVirtualLink(virtualLink);
		} else if (contentType.equals(AppConstant.SUMMARY_TYPE)) {
			if (voice.getSummaryAudioLink() == null || voice.getSummaryAudioLink().isEmpty())
				countIncrease++;
			voice.setSummaryAudioLink(audioLink);
			voice.setSummaryAudioVirtualLink(virtualLink);
		}

		voice.setCreatedDate(System.currentTimeMillis());
		voiceRepository.save(voice);
		if (countIncrease == AppConstant.AUDIO_MAX) {
			logger.info("ARTICLE SYNTHESIZED: " + article.getId() + " --- with publicDate: " + article.getPublicDate()
					+ " --- number audio max: " + AppConstant.AUDIO_MAX);
			article.setVoices(voiceRepository.findByArticleId(article.getId()));
			article.setSynthesisType(AppConstant.SYNTHESIZED);
		}
		article.setCountAudioSynthesize(countIncrease);
		articleRepository.save(article);
	}

	@Override
	public Voice getVoicesByNameAndCrawlerId(String name, Integer crawlerId) {
		return voiceRepository.findVoicesByNameAndCrawlerId(name, crawlerId);
	}

	@Override
	public List<Voice> findByArticleId(Integer articleId, String fields) {
		return voiceDao.findByArticleId(articleId, fields);
	}

	@Override
	public void updateVoice(Voice voice, String name, Integer articleId, String url) {
		voice.setArticleId(articleId);
		voice.setName(name);
		voice.setSummaryAudioLink(url);
		voice.setContentAudioLink(url);
		Article article = articleRepository.findOne(articleId);
		if (article == null) {
			logger.info("Không tìm thầy article có articleId = " + articleId);
			return;
		}
		int countIncrease = article.getCountAudioSynthesize() + 2;
		if (countIncrease == AppConstant.AUDIO_MAX) {
			// String message = "Bài báo: " + article.getTitle() + " đã tổng hợp xong";
			// notificationService.notify(new Notification(message));
			article.setSynthesisType(AppConstant.SYNTHESIZED);
			logger.info("CMS SYNTHESIZED: aricleId" + articleId + " --- voice: " + voice.getName());
		}
		article.setCountAudioSynthesize(countIncrease);
		articleRepository.save(article);
		voiceRepository.save(voice);
	}

	@Override
	public boolean updateUrlVoice(String oldUrl, String newUrl) {
		Voice voiceExists = voiceDao.findUrlExists(oldUrl);
		if (voiceExists == null)
			return false;
		if (voiceExists.getContentAudioLink().equals(oldUrl)) {
			voiceExists.setContentAudioLink(newUrl);
		}
		if (voiceExists.getSummaryAudioLink().equals(oldUrl))
			voiceExists.setSummaryAudioLink(newUrl);
		voiceRepository.save(voiceExists);
		return true;
	}

	@Override
	public void removeVoice() {
		voiceDao.removeVoice();
	}
}
