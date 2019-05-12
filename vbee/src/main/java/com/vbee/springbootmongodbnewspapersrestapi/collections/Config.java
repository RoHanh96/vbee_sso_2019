package com.vbee.springbootmongodbnewspapersrestapi.collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config")
public class Config {
	@Id
	String id;
	String version;
	Integer countVoiceAvaiable;
	String googleVersion;
	String appleVersion;
	
	public Config() {
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getCountVoiceAvaiable() {
		return countVoiceAvaiable;
	}

	public void setCountVoiceAvaiable(Integer countVoiceAvaiable) {
		this.countVoiceAvaiable = countVoiceAvaiable;
	}

	public String getGoogleVersion() {
		return googleVersion;
	}

	public void setGoogleVersion(String googleVersion) {
		this.googleVersion = googleVersion;
	}

	public String getAppleVersion() {
		return appleVersion;
	}

	public void setAppleVersion(String appleVersion) {
		this.appleVersion = appleVersion;
	}

}
