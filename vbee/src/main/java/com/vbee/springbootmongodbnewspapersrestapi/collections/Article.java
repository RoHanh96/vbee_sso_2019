package com.vbee.springbootmongodbnewspapersrestapi.collections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Document(collection = "article")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Article {

	@Id
	Integer id;
	Integer crawlerId;
	Integer countAudioSynthesize;
	@TextIndexed()
	@NotBlank
	String title;
	@NotBlank
	String lead;
	@NotBlank
	String content;
	String summary;
	String picture;
	@NotBlank
	String websiteName;
	@NotBlank
	String websiteUrl;
	String websiteId;
	@NotBlank
	String url;
	@Indexed(direction = IndexDirection.DESCENDING)
	Long publicDate;
	Long crawlDate;
	@Indexed(direction = IndexDirection.DESCENDING)
	Integer totalChoose;
	Integer totalListen;
	@Indexed(direction = IndexDirection.DESCENDING)
	Double listeningRate;
	@Indexed(direction = IndexDirection.DESCENDING)
	String type;
	Integer synthesisType; // 1 - synthesizing, 2 - synthesized, 3 - synthesized error
	@Indexed(direction = IndexDirection.DESCENDING)
	Integer status; // -1 de-active, 0 active, 1 pushed
	List<Tag> tags;
	List<String> users;
	Category category;
	@TextScore
	Float score;
	List<Voice> voices;
	String text;
	String previewValue;
	Integer numDoubt;
	JSONObject audios;

	public Article() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer articleId) {
		this.id = articleId;
	}

	public Integer getCrawlerId() {
		return crawlerId;
	}

	public void setCrawlerId(Integer crawlerId) {
		this.crawlerId = crawlerId;
	}

	public Integer getCountAudioSynthesize() {
		return countAudioSynthesize;
	}

	public void setCountAudioSynthesize(Integer countAudioSynthesize) {
		this.countAudioSynthesize = countAudioSynthesize;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLead() {
		return lead;
	}

	public void setLead(String lead) {
		this.lead = lead;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getPublicDate() {
		return publicDate;
	}

	public void setPublicDate(Long publicDate) {
		this.publicDate = publicDate;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public Long getCrawlDate() {
		return crawlDate;
	}

	public void setCrawlDate(Long crawlDate) {
		this.crawlDate = crawlDate;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(String websiteId) {
		this.websiteId = websiteId;
	}

	public List<Voice> getVoices() {
		return voices;
	}

	public void setVoices(List<Voice> voices) {
		this.voices = voices;
	}

	public Integer getTotalChoose() {
		return totalChoose;
	}

	public void setTotalChoose(Integer totalChoose) {
		this.totalChoose = totalChoose;
	}

	public Integer getTotalListen() {
		return totalListen;
	}

	public void setTotalListen(Integer totalListen) {
		this.totalListen = totalListen;
	}

	public Double getListeningRate() {
		return listeningRate;
	}

	public void setListeningRate(Double listeningRate) {
		this.listeningRate = listeningRate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "com.vbee.collections.article [id= " + id + " ]";
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getSynthesisType() {
		return synthesisType;
	}

	public void setSynthesisType(Integer synthesisType) {
		this.synthesisType = synthesisType;
	}

	public String getPreviewValue() {
		return previewValue;
	}

	public void setPreviewValue(String previewValue) {
		this.previewValue = previewValue;
	}

	public Integer getNumDoubt() {
		return numDoubt;
	}

	public void setNumDoubt(Integer numDoubt) {
		this.numDoubt = numDoubt;
	}

	public JSONObject getAudios() {
		return audios;
	}

	public void setAudios(JSONObject audios) {
		this.audios = audios;
	}

	public JSONObject handle() {
		JSONObject object = new JSONObject();
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().indexOf("get") == 0) {
				try {
					if (method.invoke(this) != null) {
						String field = method.getName().substring(3, method.getName().length());
						field = Character.toLowerCase(field.charAt(0)) + field.substring(1);
						object.put(field, method.invoke(this));
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return object;
	}

}
