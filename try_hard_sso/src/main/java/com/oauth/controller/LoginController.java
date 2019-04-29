package com.oauth.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oauth.entity.User;
import com.oauth.services.UserServiceImpl;
import com.oauth.util.AppUtil;
import com.oauth.util.CookieUtil;
import com.oauth.util.JwtUtil;
import com.oauth.util.RefererUtil;

@Controller
public class LoginController {
	
	private static final String jwtTokenCookieName = "JWT-TOKEN";
	private static final String signingKey = "signingKey";
	private static final String domainClient = "demo1.com";
	private static final String domainServer = "localhost";
	private static final String clientRedirectUrl = "clientRedirectURL";
	
	@Autowired
	private UserServiceImpl userService;
	
	private String jwtTokenStore;
	private User user;
	private String redirectUrl;
	
	public LoginController() {
		
	}
	
	@RequestMapping("/")
	public String home() {
		return "redirect:/login";
	}
	
	@RequestMapping("/login")
	public String login(Model model, HttpServletRequest request) {
		if(CookieUtil.getValue(request, jwtTokenCookieName) != null) {
			this.jwtTokenStore = CookieUtil.getValue(request, jwtTokenCookieName);
			return "redirect:" + this.redirectUrl;
		}
		model.addAttribute("userFormLogin", new User());
		System.out.println("cookie at 8081" + CookieUtil.getValue(request, jwtTokenCookieName));
		return "login";
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(HttpServletResponse httpServletResponse, @ModelAttribute("userFormLogin") User user, HttpServletRequest request, Model model, @RequestBody String redirectUrl ) {		
		User userLogin = userService.getUserByName(user.getUsername());
		if(user.getUsername() == null || user.getPassword() == null || userLogin == null || !userLogin.getPassword().equals(user.getPassword())) {
			model.addAttribute("error","Invalid username or password");
			return "login";
		}
		String token = JwtUtil.generateToken(signingKey, userLogin.getUsername());
		CookieUtil.create(httpServletResponse, jwtTokenCookieName, token, false, -1, domainServer);		
		/**
		 * Truyen du lieu tu server den client nhung chua biet cach
		 * 
		ObjectMapper mapper = new ObjectMapper();
		String userData = new String();
		try {
			userData = mapper.writeValueAsString(userService.getUserByName(username));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		this.user = userLogin;
//		CookieUtil.create(httpServletResponse, "email", userLogin.getEmail(), false, -1, domainServer);
//		CookieUtil.create(httpServletResponse, "role", userLogin.getRole().getName(), false, -1, domainServer);
		return "redirect:" + "http://localhost:8081/login";
	}
	
	
	/**
	 * Api get jwtToken
	 * @return jwtToken for client 
	 */
	@RequestMapping(value = "/getJwtToken", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ResponseBody
	public String getJwtToken() {
		return jwtTokenStore;
	}
	
	/**
	 * Api get userInfo
	 * @return
	 */
	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	@ResponseBody
	public User getUserInfo() {
		return this.user;
	}
	
	/**
	 * Api recevive redirectUrl
	 */
	@RequestMapping(value = "/setRedirectUrl", method = RequestMethod.POST)
	public void receiveRedirectUrl(@RequestBody String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	
}
