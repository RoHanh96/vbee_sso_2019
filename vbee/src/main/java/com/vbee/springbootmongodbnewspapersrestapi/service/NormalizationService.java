package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.LinkedList;
import java.util.Queue;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Article;
import com.vbee.springbootmongodbnewspapersrestapi.model.ArticleProcessing;
import com.vbee.springbootmongodbnewspapersrestapi.model.Config;
import com.vbee.springbootmongodbnewspapersrestapi.ulti.QueueNSW;

@Service
public class NormalizationService implements INormalizationService {

	@Autowired
	Config config;

	@Autowired
	ArticleService articleService;

	@Override
	public void putArticleNSW(Article article) {
		QueueNSW.putArticleNSW(article, articleService, config);
	}

	@Override
	public void start() {
		QueueNSW.start();
	}


	@Override
	public void reset() {
		QueueNSW.canRun = true;
		QueueNSW.queueArticleNSW.clear();
	}

	@Override
	public void setRun() {
		QueueNSW.canRun = true;
	}

}
