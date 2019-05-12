package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.servlet.http.HttpServletRequest;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.collections.VoiceSynthesis;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleProcessing;
import com.vbee.springbootmongodbnewspapersrestapi.model.Config;
import com.vbee.springbootmongodbnewspapersrestapi.repository.VoiceSynthesisMongoRepository;

@Service
public class SynthesisService implements ISynthesisService{

	@Autowired
	Config config;

	@Autowired
	VoiceSynthesisMongoRepository voiceSynthesisRepository;

	@Autowired
	IVoiceService voiceService;

	@Autowired
	IArticleService articleService;

	private static final Logger logger = LoggerFactory.getLogger(SynthesisService.class);
	private WebSocketClient ws = null;
	private String content;
	private boolean canRun = true;
	private String APP_ID = "";
	private String DNS = "";

	private static Queue<Article> queueArticleSynthesize = new LinkedList<Article>();

	@Override
	public void putArticleSynthesize(Article article, HttpServletRequest request) {
		DNS = config.getIp() + ":" + config.getPort();
		APP_ID = config.getAppId();
		queueArticleSynthesize.offer(article);
	}

	@Override
	public void start() {
		System.out.println("size queue synthesize: " + queueArticleSynthesize.size());
		if (!queueArticleSynthesize.isEmpty() && canRun) {
			Article article = queueArticleSynthesize.poll();
			System.out.println("Synthesizing article: " + article.getId());
			synthesizeArticle(article);
		}
	}

	@Override
	public void synthesizeArticle(Article article) {
		try {
			ws = null;
			List<VoiceSynthesis> voiceSynthesises = voiceSynthesisRepository.findAll();
			if (article.getText() == null || article.getText().isEmpty()) {
				content = ArticleProcessing.processingContent(article.getTitle() + " . " + article.getContent());
			} else {
				content = ArticleProcessing.processingContent(article.getText());
			}

			if (article.getPreviewValue() != null && !article.getPreviewValue().isEmpty()) {
				content = ArticleProcessing.prepareSynthesize(article.getPreviewValue(), content);
			}
			System.out.println("data: " + content);
			article.setCountAudioSynthesize(0);
			article.setSynthesisType(1);
			articleService.updateArticle(article);
			System.out.println("tts: " + config.getTtsServicePath());
			ws = new WebSocketClient(new URI(config.getTtsServicePath()), new Draft_10()) {

				@Override
				public void onClose(int i, String reason, boolean remote) {
					logger.info("Connection closed by " + (remote ? "remote peer" : "us") + " and reason is " + reason);
					canRun = true;
					start();
				}

				@Override
				public void onError(Exception arg0) {
					arg0.printStackTrace();
					close();
					canRun = true;
					start();

				}

				@Override
				public void onMessage(String messageContent) {
					System.out.println(messageContent);
				}

				@Override
				public void onOpen(ServerHandshake arg0) {
					logger.info("server is opened");
					canRun = false;
					JSONObject messenger = new JSONObject();
					JSONArray input = new JSONArray();
					for (VoiceSynthesis voiceSynthesis : voiceSynthesises) {
						JSONObject jsonContent = new JSONObject();
						jsonContent.put("content", content);
						jsonContent.put("voice", voiceSynthesis.getName());
						input.add(jsonContent);
					}
					String callback = "http://" + DNS + "/api/v1/voices/save-synthesized-cms?article-id="
							+ article.getId();
					System.out.println("callback: " + callback);
					messenger.put("INPUT_TEXT", input);
					messenger.put("APP_ID", APP_ID);
					messenger.put("HTTP_CALLBACK", callback);
					send(messenger.toString());
					close();
				}
			};

			ws.connect();

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error when synthesize article: " + article.getTitle() + " ------------- " + e.getMessage());
		}
	}
}
