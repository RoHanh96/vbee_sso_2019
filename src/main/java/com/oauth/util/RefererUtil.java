package com.oauth.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RefererUtil {

	public static String getRedirectUrl(final HttpServletRequest request) {
		final String referer = request.getHeader("Referer");
		System.out.println("referer:" + referer);
		String[] arr = referer.split("redirect=");
		if(arr.length == 1) {
			return referer;
		}
		String redirectUrl = arr[1];
		AppUtil.storeRediectUrl(request.getSession(), redirectUrl);
		return redirectUrl;
	}
}
