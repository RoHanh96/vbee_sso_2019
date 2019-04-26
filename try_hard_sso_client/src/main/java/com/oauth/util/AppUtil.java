package com.oauth.util;

import javax.servlet.http.HttpSession;

public class AppUtil {
	public static void storeLoginedUser(HttpSession session, String username) {
		session.setAttribute("logined_user", username);
	}
	
	public static String getLoginedUser(HttpSession session) {
		return (String) session.getAttribute("logined_user");
	}
	
	public static void storeRediectUrl(HttpSession session, String redirectUrl) {
		session.setAttribute("redirectUrl", redirectUrl);
	}
	
	public static String getRedirectUrl(HttpSession session) {
		return (String) session.getAttribute("redirectUrl");
	}
}
