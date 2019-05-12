package com.vbee.springbootmongodbnewspapersrestapi.service;

import java.util.List;

import com.vbee.springbootmongodbnewspapersrestapi.collections.Config;

public interface IConfigService {

	void updateConfig(int voiceAvaiableNumber);

	Config findByVersion(String configVersion);

	List<Config> findAll();

	Config updateConfig(Config config, Config configExists);

	void create(Config config);

}
