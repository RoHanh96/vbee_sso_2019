package com.oauth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.oauth.entity.User;
import com.oauth.util.CookieUtil;

@Controller
public class ClientController {
	private static final String jwtTokenCookieName = "JWT-TOKEN";
	private static final String userInfoApi = "http://localhost:8081/getUser";
	
	@RequestMapping("/")
	public String home() {
		return "redirect:/protected-resource";
	}
	
	@RequestMapping("/protected-resource")
	public String protectedResource(Model model, HttpServletRequest request) {
		RestTemplate rest = new RestTemplate();
		User user = null;
		try {
			user = rest.getForObject(userInfoApi, User.class);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(user!=null) {
			model.addAttribute("user", user);
		}
		return "protected-resource";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletResponse httpServletResponse) {
		CookieUtil.clear(httpServletResponse, jwtTokenCookieName);
		return "redirect:/";
	}
	
}
