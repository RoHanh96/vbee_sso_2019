package com.oauth.util;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {
	
	public static Object getAttribute(HttpServletRequest request, Serializable key) {
		HttpSession session = request.getSession(false);
		if(session == null) {
			return null;
		}
		return session.getAttribute(key.toString());
	}
	
	public static void setAtribute(HttpServletRequest request, String key, Object value) {
		HttpSession session = request.getSession(true);
		session.setAttribute(key, value);
	}
}
