package com.vbee.springbootmongodbnewspapersrestapi.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "cust.data.config")
@Configuration("configProperties")
public class Config {
	private String statisticsServicePath;
	private String userServicePath;
	private String ttsServicePath;
	private String userSettingDefaultVersion;
	private String configVersion;
	private String loginUrl;
	private String authenServiceUrl;
	private String mapServiceUrl;
	private String normalizationServicePath;
	private String crawlerServiceUrl;
	private String ip;
	private String port;
	private String appId;
	private String userServiceClientPath;
	private String accessTokenCRM;
	private String callbackUrl;
	private String ssoUrl;

	public Config() {
		System.out.println("1122");
	}

	public String getStatisticsServicePath() {
		return statisticsServicePath;
	}

	public void setStatisticsServicePath(String statisticsServicePath) {
		this.statisticsServicePath = statisticsServicePath;
	}

	public String getUserServicePath() {
		return userServicePath;
	}

	public void setUserServicePath(String userServicePath) {
		this.userServicePath = userServicePath;
	}

	public String getTtsServicePath() {
		return ttsServicePath;
	}

	public void setTtsServicePath(String ttsServicePath) {
		this.ttsServicePath = ttsServicePath;
	}

	public String getUserSettingDefaultVersion() {
		return userSettingDefaultVersion;
	}

	public void setUserSettingDefaultVersion(String userSettingDefaultVersion) {
		this.userSettingDefaultVersion = userSettingDefaultVersion;
	}

	public String getConfigVersion() {
		return configVersion;
	}

	public void setConfigVersion(String configVersion) {
		this.configVersion = configVersion;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getAuthenServiceUrl() {
		return authenServiceUrl;
	}

	public void setAuthenServiceUrl(String authenServiceUrl) {
		this.authenServiceUrl = authenServiceUrl;
	}

	public String getMapServiceUrl() {
		return mapServiceUrl;
	}

	public void setMapServiceUrl(String mapServiceUrl) {
		this.mapServiceUrl = mapServiceUrl;
	}

	public String getNormalizationServicePath() {
		return normalizationServicePath;
	}

	public void setNormalizationServicePath(String normalizationServicePath) {
		this.normalizationServicePath = normalizationServicePath;
	}

	public String getCrawlerServiceUrl() {
		return crawlerServiceUrl;
	}

	public void setCrawlerServiceUrl(String crawlerServiceUrl) {
		this.crawlerServiceUrl = crawlerServiceUrl;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserServiceClientPath() {
		return userServiceClientPath;
	}

	public void setUserServiceClientPath(String userServiceClientPath) {
		this.userServiceClientPath = userServiceClientPath;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAccessTokenCRM() {
		return accessTokenCRM;
	}

	public void setAccessTokenCRM(String accessTokenCRM) {
		this.accessTokenCRM = accessTokenCRM;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getSsoUrl() {
		return ssoUrl;
	}

	public void setSsoUrl(String ssoUrl) {
		this.ssoUrl = ssoUrl;
	}

}
