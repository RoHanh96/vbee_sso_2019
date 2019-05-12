package com.vbee.springbootmongodbnewspapersrestapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.VoiceSynthesis;
import com.vbee.springbootmongodbnewspapersrestapi.repository.VoiceSynthesisMongoRepository;

@Service
public class VoiceSynthesisService implements IVoiceSynthesisService{

	@Autowired
	VoiceSynthesisMongoRepository voiceMongo;
	
	@Override
	public VoiceSynthesis findByName(String name) {
		return voiceMongo.findByName(name);
	}

	@Override
	public VoiceSynthesis insertVoiceSynthesis(VoiceSynthesis voiceSynthesis) {
		return voiceMongo.save(voiceSynthesis);
	}

}
