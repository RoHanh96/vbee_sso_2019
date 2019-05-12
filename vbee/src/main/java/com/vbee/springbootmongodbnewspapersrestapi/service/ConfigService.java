package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Config;
import com.vbee.springbootmongodbnewspapersrestapi.config.AppConstant;
import com.vbee.springbootmongodbnewspapersrestapi.repository.ConfigMongoRepository;

@Service
public class ConfigService implements IConfigService{

	@Autowired
	ConfigMongoRepository configMongo;
	
	@Autowired
	com.vbee.springbootmongodbnewspapersrestapi.model.Config configProperties;
	
	@Override
	public void updateConfig(int voiceAvaiableNumber) {
		Config config = configMongo.findByVersion(configProperties.getConfigVersion());
		if(config == null) {
			System.out.println("Not found config with: " + configProperties.getConfigVersion());
			return;
		}
		AppConstant.AUDIO_MAX = voiceAvaiableNumber;
		config.setCountVoiceAvaiable(voiceAvaiableNumber);
		configMongo.save(config);
	}

	@Override
	public Config findByVersion(String configVersion) {
		Config config = configMongo.findByVersion(configVersion);
		if(config == null) {
		 if (!configMongo.findAll().isEmpty())
			return configMongo.findAll().get(0);
		}
		return config;
	}

	@Override
	public List<Config> findAll() {
		return configMongo.findAll();
	}

	@Override
	public Config updateConfig(Config config, Config configExists) {
		if(config.getAppleVersion() != null && !config.getAppleVersion().isEmpty()) {
			configExists.setAppleVersion(config.getAppleVersion());
		}
		if(config.getGoogleVersion() != null && !config.getGoogleVersion().isEmpty()) {
			configExists.setGoogleVersion(config.getGoogleVersion());
		}
		return configMongo.save(configExists);
	}

	@Override
	public void create(Config config) {
		configMongo.save(config);
	}

}
