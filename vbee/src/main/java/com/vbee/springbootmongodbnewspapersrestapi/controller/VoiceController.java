package com.vbee.springbootmongodbnewspapersrestapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.vbee.springbootmongodbnewspapersrestapi.SpringbootMongodbNewspapersRestapiApplication;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.Voice;
import com.vbee.springbootmongodbnewspapersrestapi.collections.VoiceSynthesis;
import com.vbee.springbootmongodbnewspapersrestapi.config.AppConstant;
import com.vbee.springbootmongodbnewspapersrestapi.model.ResponseMessage;
import com.vbee.springbootmongodbnewspapersrestapi.service.IArticleService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IConfigService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IVoiceService;
import com.vbee.springbootmongodbnewspapersrestapi.service.IVoiceSynthesisService;

@RestController
@RequestMapping("/api/v1/voices")
public class VoiceController {

	@Autowired
	IVoiceService voiceService;

	@Autowired
	IVoiceSynthesisService voiceSynthesisService;

	@Autowired
	IConfigService configService;

	@Autowired
	IArticleService articleService;

	private static final Logger logger = LoggerFactory.getLogger(SpringbootMongodbNewspapersRestapiApplication.class);

	// get all voice
	@GetMapping()
	public ResponseEntity<ResponseMessage> getAllVoice() {
		ResponseMessage resMessage = new ResponseMessage();
		List<Voice> list = voiceService.getAllVoice();
		if (list.isEmpty()) {
			resMessage.setMessage("Không tìm thấy voices !!!");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Đã tìm thấy: " + list.size() + " voices");
		resMessage.setStatus(1);
		resMessage.setResults(list);
		return ResponseEntity.ok(resMessage);
	}

	// api/v1/voices/check-voice/{voice}/article/{article}
	// check article voice exists
	@GetMapping("check-voice/article/{articleId}/voice/{name}")
	public ResponseEntity<ResponseMessage> checkVoiceExists(@PathVariable String name, @PathVariable int articleId) {
		ResponseMessage resMessage = new ResponseMessage();
		Voice voiceExists = voiceService.getVoicesByNameAndArticleId(name, articleId);
		if (voiceExists == null) {
			resMessage.setMessage("Không tìm thấy voice tương ứng với article id: " + articleId);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}

		resMessage.setMessage("Đã tìm thấy voice tương ứng");
		resMessage.setStatus(1);
		resMessage.setResults(voiceExists);
		return ResponseEntity.ok(resMessage);
	}

	// @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<ResponseMessage> insertVoice(@RequestBody @Valid Voice
	// voice) {
	// ResponseMessage resMessage = new ResponseMessage();
	// Voice voiceExists = voiceService.getVoicesByNameAndArticleId(voice.getName(),
	// voice.getArticleId());
	// if (voiceExists == null) {
	// Voice newVoice = voiceService.insertVoice(voice);
	// resMessage.setMessage("Tạo mới voice thành công!!!");
	// resMessage.setStatus(1);
	// resMessage.setResults(newVoice);
	// return ResponseEntity.ok(resMessage);
	// }
	// resMessage.setMessage("voice đã tồn tại!!!");
	// resMessage.setStatus(0);
	// return ResponseEntity.ok(resMessage);
	// }

	// api/v1/voices/add?article-id=7399&article-part=3&result-type=success&tts-processing-time=247420&tts-completion-time=2017-10-27+16%3A20%3A58.743&voice=female_tt_vdts_48k-hsmm&bit-rate=64000&sample-rate=48000&url-audio=http%3A%2F%2F43.239.223.139%3A8888%2Fstore2%2Faudio%2F%2Fbook%2F3789%2FNenGiaoDucCuaNguoiGiauNhungTayTiPhuHocGiTuTruongDoi-chapter-1.mp3&tts-version=1.0
	// Create voice had synthesized
	@GetMapping("/add")
	public ResponseEntity<ResponseMessage> addVoice(@RequestParam("tts-processing-time") String ttsProcessingTime,
			@RequestParam("tts-completion-time") String ttsCompletionTime, @RequestParam("voice") String name,
			@RequestParam("bit-rate") String bitRate, @RequestParam("sample-rate") String sampleRate,
			@RequestParam("tts-version") String ttsVersion, @RequestParam("url-audio") String audioLink,
			@RequestParam("result-type") String resultType, @RequestParam("article-id") String itemId,
			@RequestParam("article-part") Integer part) {
		ResponseMessage resMessage = new ResponseMessage();
		if (ttsProcessingTime == null || ttsCompletionTime == null || name == null || bitRate == null
				|| sampleRate == null || ttsVersion == null || audioLink == null || resultType == null || itemId == null
				|| part == null) {
			resMessage.setMessage("Thành phần param chứa null");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		if (resultType.equals("success")) {
			try {
				int articleId = Integer.parseInt(itemId);
				Voice voiceExists = voiceService.getVoicesByNameAndArticleId(name, articleId);
				if (voiceExists == null) {
					Voice newVoice = new Voice();
					voiceService.updateVoiceByMultiParam(newVoice, name, audioLink, part, articleId);
				} else {
					voiceService.updateVoiceByMultiParam(voiceExists, name, audioLink, part, articleId);
				}
				resMessage.setMessage("Lưu thành công!!!");
				resMessage.setStatus(1);
				return ResponseEntity.ok(resMessage);
			} catch (Exception e) {
				logger.info("insert voice error!!! Exception: " + e);
			}
		}
		resMessage.setMessage("Lưu thất bại vì result-type: " + resultType);
		resMessage.setStatus(0);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("/adds")
	public ResponseEntity<ResponseMessage> addVoices(@RequestParam(name = "voice", required = false) String name,
			@RequestParam(name = "url-audio", required = false) String audioLink,
			@RequestParam("result-type") String resultType, @RequestParam("crawler-id") Integer crawlerId,
			@RequestParam(name = "content-type", required = false) String contentType,
			@RequestParam(name = "virtual-link", required = false) String virtualLink) {
		ResponseMessage resMessage = new ResponseMessage();
		if (name == null || audioLink == null || resultType == null || crawlerId == null || contentType == null) {
			resMessage.setMessage("Thành phần param chứa null");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		if (resultType.equals("success")) {
			try {
				Voice voiceExists = voiceService.getVoicesByNameAndCrawlerId(name, crawlerId);
				if (voiceExists == null) {
					Voice newVoice = new Voice();
					voiceService.updateVoiceByMultiParams(newVoice, name, audioLink, virtualLink, contentType,
							crawlerId);
				} else {
					voiceService.updateVoiceByMultiParams(voiceExists, name, audioLink, virtualLink, contentType,
							crawlerId);
				}
				resMessage.setMessage("Lưu thành công!!!");
				resMessage.setStatus(1);
				return ResponseEntity.ok(resMessage);
			} catch (Exception e) {
				logger.info("insert voice error!!! Exception: " + e);
			}
		} else if (resultType.equals("error")) {
			Article article = articleService.getArticleByCrawlerId(crawlerId);
			if (article == null) {
				resMessage.setMessage("CrawlerId không đúng !!!");
				resMessage.setStatus(0);
				return ResponseEntity.ok(resMessage);
			}
			article.setSynthesisType(AppConstant.SYNTHESIZED_ERROR);
			articleService.updateArticle(article);

			resMessage.setMessage("Lưu tổng hợp lỗi thành công!!!");
			resMessage.setStatus(1);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Lưu thất bại!!!");
		resMessage.setStatus(0);
		return ResponseEntity.ok(resMessage);
	}

	@PutMapping("/voiceSynthesis")
	public ResponseEntity<ResponseMessage> updateListVoice(@RequestBody List<VoiceSynthesis> voices) {
		ResponseMessage resMessage = new ResponseMessage();

		int count = 0;
		for (VoiceSynthesis voiceSynthesis : voices) {
			VoiceSynthesis voiceSynthesisExists = voiceSynthesisService.findByName(voiceSynthesis.getName());
			if (voiceSynthesisExists == null) {
				count++;
				voiceSynthesis = voiceSynthesisService.insertVoiceSynthesis(voiceSynthesis);
			}
		}
		int voiceAvaiableNumber = voices.size() * 2;
		System.out.println("voiceAvaiable: " + voiceAvaiableNumber);
		configService.updateConfig(voiceAvaiableNumber);
		resMessage.setMessage("Updated: " + count + " voice!!!");
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("/save-synthesized-cms")
	public ResponseEntity<ResponseMessage> addCmsVoices(@RequestParam(name = "voice", required = false) String name,
			@RequestParam(name = "url-audio", required = false) String audioLink,
			@RequestParam("result-type") String resultType, @RequestParam("article-id") Integer articleId) {
		ResponseMessage resMessage = new ResponseMessage();
		if (resultType == null || articleId == null) {
			resMessage.setMessage("Thành phần result-type hoặc article-id không được null");
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		if (resultType.equals("success")) {
			try {
				if (audioLink == null || audioLink.isEmpty()) {
					resMessage.setMessage("Lỗi. Không có url audio!!!");
					resMessage.setStatus(0);
					return ResponseEntity.ok(resMessage);
				}
				Voice voiceExists = voiceService.getVoicesByNameAndArticleId(name, articleId);
				logger.info("voice: " + name + " --- articleId: " + articleId);
				if (voiceExists == null) {
					Voice newVoice = new Voice();
					voiceService.updateVoice(newVoice, name, articleId, audioLink);
				} else {
					voiceService.updateVoice(voiceExists, name, articleId, audioLink);
				}
				resMessage.setMessage("Lưu thành công!!!");
				resMessage.setStatus(1);
				return ResponseEntity.ok(resMessage);
			} catch (Exception e) {
				logger.info("insert voice error!!! Exception: " + e);
			}
		} else if (resultType.equals("error")) {
			Article article = articleService.getArticleById(articleId);
			if (article == null) {
				resMessage.setMessage("article-id không đúng !!!");
				resMessage.setStatus(0);
				return ResponseEntity.ok(resMessage);
			}
			article.setSynthesisType(AppConstant.SYNTHESIZED_ERROR);
			articleService.updateArticle(article);

			resMessage.setMessage("Lưu tổng hợp lỗi thành công --- articleId = " + articleId + " !!!");
			resMessage.setStatus(1);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Lưu thất bại!!!");
		resMessage.setStatus(0);
		return ResponseEntity.ok(resMessage);
	}

	@GetMapping("/updateUrl")
	public ResponseEntity<ResponseMessage> updateUrlVoice(String oldUrl, String newUrl) {
		ResponseMessage resMessage = new ResponseMessage();
		if (voiceService.updateUrlVoice(oldUrl, newUrl)) {
			resMessage.setMessage("Update success!!!");
			resMessage.setStatus(1);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setMessage("Update error!!!");
		resMessage.setStatus(0);
		return ResponseEntity.ok(resMessage);
	}

	// @GetMapping("/removeVoice")
	// public ResponseEntity<ResponseMessage> removeVoice() {
	// ResponseMessage resMessage = new ResponseMessage();
	// voiceService.removeVoice();
	// resMessage.setMessage("success");
	// resMessage.setStatus(1);
	// return ResponseEntity.ok(resMessage);
	// }
}
