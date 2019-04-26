package com.oauth.util;

import javax.servlet.http.HttpSession;

import com.oauth.entity.User;

public class AppUtil {
	public static void storeLoginedUser(HttpSession session,User user) {
		session.setAttribute("logined_user", user);
	}
	
	public static User getLoginedUser(HttpSession session) {
		return (User) session.getAttribute("logined_user");
	}
	
	public static void storeRediectUrl(HttpSession session, String redirectUrl) {
		session.setAttribute("redirectUrl", redirectUrl);
	}
	
	public static String getRedirectUrl(HttpSession session) {
		return (String) session.getAttribute("redirectUrl");
	}
}
