package com.vbee.springbootmongodbnewspapersrestapi.model;

public class Notification {

	private String message;

	public Notification(String content) {
		this.message = content;
	}

	public String getContent() {
		return message;
	}
}
