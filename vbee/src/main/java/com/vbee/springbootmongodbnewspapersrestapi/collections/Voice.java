package com.vbee.springbootmongodbnewspapersrestapi.collections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hibernate.validator.constraints.NotBlank;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Document(collection = "voice")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Voice {

	@Id
	String id;

	@NotBlank
	String name;
	String titleAudioLink;
	String contentAudioLink;
	String summaryAudioLink;
	String titleAudioVirtualLink;
	String contentAudioVirtualLink;
	String summaryAudioVirtualLink;
	Integer articleId;
	Integer crawlerId;
	Long createdDate;

	public Voice() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitleAudioLink() {
		return titleAudioLink;
	}

	public void setTitleAudioLink(String titleAudioLink) {
		this.titleAudioLink = titleAudioLink;
	}

	public String getContentAudioLink() {
		return contentAudioLink;
	}

	public void setContentAudioLink(String contentAudioLink) {
		this.contentAudioLink = contentAudioLink;
	}

	public String getSummaryAudioLink() {
		return summaryAudioLink;
	}

	public void setSummaryAudioLink(String summaryAudioLink) {
		this.summaryAudioLink = summaryAudioLink;
	}

	public Integer getCrawlerId() {
		return crawlerId;
	}

	public void setCrawlerId(Integer crawlerId) {
		this.crawlerId = crawlerId;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public String getTitleAudioVirtualLink() {
		return titleAudioVirtualLink;
	}

	public void setTitleAudioVirtualLink(String titleAudioVirtualLink) {
		this.titleAudioVirtualLink = titleAudioVirtualLink;
	}

	public String getContentAudioVirtualLink() {
		return contentAudioVirtualLink;
	}

	public void setContentAudioVirtualLink(String contentAudioVirtualLink) {
		this.contentAudioVirtualLink = contentAudioVirtualLink;
	}

	public String getSummaryAudioVirtualLink() {
		return summaryAudioVirtualLink;
	}

	public void setSummaryAudioVirtualLink(String summaryAudioVirtualLink) {
		this.summaryAudioVirtualLink = summaryAudioVirtualLink;
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
						object.put(field, method.invoke(this).toString());
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
