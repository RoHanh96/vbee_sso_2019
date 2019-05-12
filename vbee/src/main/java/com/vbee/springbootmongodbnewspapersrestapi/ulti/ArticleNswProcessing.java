package com.vbee.springbootmongodbnewspapersrestapi.ulti;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleProcessing;
import com.vbee.springbootmongodbnewspapersrestapi.model.Config;
import com.vbee.springbootmongodbnewspapersrestapi.service.IArticleService;

public class ArticleNswProcessing extends Thread {

	private Thread t;
	private String threadName;
	private Article articleNsw = null;
	private static final Logger logger = LoggerFactory.getLogger(ArticleNswProcessing.class);
	private IArticleService articleService = null;
	private Config config;

	public ArticleNswProcessing(String name, Article articleNsw, IArticleService articleService, Config config) {
		this.articleService = articleService;
		this.threadName = name;
		this.articleNsw = articleNsw;
		this.config = config;
	}

	public void run() {
		if (articleNsw == null) {
			return;
		}
		try {
			WebSocketClient ws = new WebSocketClient(new URI(config.getNormalizationServicePath()), new Draft_10()) {

				@Override
				public void onOpen(ServerHandshake sh) {
					logger.info("server is opened");
					JSONObject messenger = new JSONObject();
					String content;
					if (articleNsw.getText() == null || articleNsw.getText().isEmpty()) {
						content = ArticleProcessing
								.processingContent(articleNsw.getTitle() + " . " + articleNsw.getContent());
					} else {
						content = ArticleProcessing.processingContent(articleNsw.getText());
					}
//					logger.info(content);
					messenger.put("APP_ID", "Article_Service");
					messenger.put("INPUT_TEXT", content);
					messenger.put("OUTPUT_TYPE", "NSW");
					messenger.put("OTHER", "" + articleNsw.getId());
					send(messenger.toString());
				}

				@Override
				public void onMessage(String message) {
					logger.info("received: " + message);
					System.out.println("Processing done article: " + articleNsw.getTitle() + " !!! ");
					handleResponse(message);
					close();
					QueueNSW.canRun = true;
					QueueNSW.start();
				}

				@Override
				public void onClose(int i, String reason, boolean remote) {
					logger.info("Connection closed by " + (remote ? "remote peer" : "us") + " and reason is " + reason);
					QueueNSW.canRun = true;
					QueueNSW.start();
				}

				@Override
				public void onError(Exception ex) {
					ex.printStackTrace();
					close();
					QueueNSW.canRun = true;
					QueueNSW.start();
				}

			};
			ws.connect();
			while (true) {
				if (ws.getConnection().isOpen()) {
					logger.info("CONNECTING...");
					break;
				}
				Thread.sleep(100);
			}
			

		} catch (Exception e) {
			logger.info("Error when normalization Article: " + articleNsw.getTitle() + " ----" + e.getMessage());
			QueueNSW.canRun = true;
			QueueNSW.start();
		}
		QueueNSW.canRun = true;
		logger.info("Thread " + threadName + " exiting.");
	}

	public void start() {
		logger.info("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

	protected void handleResponse(String message) {
		try {
			JSONObject response = (JSONObject) new JSONParser().parse(message);
			Integer articleId = Integer.parseInt("" + response.get("other"));
			JSONObject content = (JSONObject) response.get("content");
			String value = content.toString();
			if (value != null && !value.isEmpty()) {
				Article article = articleService.getArticleById(articleId);
				if (article != null) {
					article.setPreviewValue(value);
					article.setNumDoubt(ArticleProcessing.countNumDoubtOfPreviewValue(article));
					articleService.updateArticle(article);
					logger.info("Article " + article.getId() + " normalization saved");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in handleResponse - NormalizationService. message: " + e.getMessage());
		}
	}
}
