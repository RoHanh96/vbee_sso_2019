package com.oauth.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oauth.util.CookieUtil;

@Controller
public class ClientController {
	private static final String jwtTokenCookieName = "JWT-TOKEN";
	
	@RequestMapping("/")
	public String home() {
		return "redirect:/protected-resource";
	}
	
	@RequestMapping("/protected-resource")
	public String protectedResource() {
		return "protected-resource";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletResponse httpServletResponse) {
		CookieUtil.clear(httpServletResponse, jwtTokenCookieName);
		return "redirect:/";
	}
	
}
