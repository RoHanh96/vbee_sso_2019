package com.oauth.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oauth.services.UserServiceImpl;
import com.oauth.util.AppUtil;
import com.oauth.util.CookieUtil;
import com.oauth.util.JwtUtil;
import com.oauth.util.RefererUtil;

@Controller
public class LoginController {
	
	private static final String jwtTokenCookieName = "JWT-TOKEN";
	private static final String signingKey = "signingKey";
	private static final Map<String, String> credentials = new HashMap<>();
	
	@Autowired
	private UserServiceImpl userService;
	
	public LoginController() {
		
	}
	
	@RequestMapping("/")
	public String home() {
		return "redirect:/login";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(HttpServletResponse httpServletResponse, String username, String password, HttpServletRequest request, Model model) {
		String redirect = RefererUtil.getRedirectUrl(request);
		if(username == null || userService.getUserByName(username) == null || !userService.getUserByName(username).getPassword().equals(password)) {
			model.addAttribute("error","Invalid username or password");
//			System.out.println("loi cmnr");
			return "login";
		}
		String token = JwtUtil.generateToken(signingKey, username);
		CookieUtil.create(httpServletResponse, jwtTokenCookieName, token, false, -1, "localhost");
		System.out.println("redirect: " + redirect);
//		AppUtil.storeLoginedUser(request.getSession(), userService.getUserByName(username));
//		model.addAttribute("user", userService.getUserByName(username));
		ObjectMapper mapper = new ObjectMapper();
		String userData = new String();
		try {
			userData = mapper.writeValueAsString(userService.getUserByName(username));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("userData", userData);
		return "redirect:" + AppUtil.getRedirectUrl(request.getSession());
	}
}
