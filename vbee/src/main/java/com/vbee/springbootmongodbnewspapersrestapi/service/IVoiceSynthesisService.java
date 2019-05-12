package com.vbee.springbootmongodbnewspapersrestapi.service;

import com.vbee.springbootmongodbnewspapersrestapi.collections.VoiceSynthesis;

public interface IVoiceSynthesisService {

	VoiceSynthesis findByName(String name);

	VoiceSynthesis insertVoiceSynthesis(VoiceSynthesis voiceSynthesis);

}
