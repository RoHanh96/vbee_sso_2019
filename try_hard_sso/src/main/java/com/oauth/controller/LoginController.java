package com.oauth.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oauth.entity.User;
import com.oauth.services.UserServiceImpl;
import com.oauth.util.AppUtil;
import com.oauth.util.CookieUtil;
import com.oauth.util.JwtUtil;
import com.oauth.util.RefererUtil;
import com.oauth.util.SessionUtil;
import com.oauth.util.StringUtil;

@Controller
public class LoginController {
	
	private static final String jwtTokenCookieName = "JWT-TOKEN";
	private static final String jwtTokenSessionName = "JWT-TOKEN-SESSION";
	private static final String signingKey = "signingKey";
	private static final String domainServer = "springsso.herokuapp.com";
	private static final String roleAdmin = "ROLE_ADMIN";
	private static final String logoutCookie = "check_logout";
	
	@Autowired
	private UserServiceImpl userService;
	
	private String jwtTokenStore;
	private String jwtTokenStoreSession;
	private boolean checkLogout = false;
	private String randomToken;
	private HashSet<String> listUrl = new HashSet<String>();
	
	public LoginController() {
		
	}
	
	@RequestMapping("/")
	public String home() {
		return "redirect:/login";
	}
	
	@RequestMapping(value = "/list_user", method = RequestMethod.GET)
	public String listUser(Model model, HttpServletRequest request) {
//		System.out.println("COOKIE " + CookieUtil.getValue(request, jwtTokenCookieName));
		if(!(LoginController.getUserRole(request).equals(roleAdmin))) {
			return "access_denied";
		}
		List<User> users = userService.getAllUser();
		String userData = JwtUtil.getSubject(request, jwtTokenCookieName, signingKey);
		User userLogined = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			userLogined = mapper.readValue(userData, User.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("userLogined", userLogined);
		model.addAttribute("listUser", users);
		return "list_user";
	}
	
	@RequestMapping(value = "/login")
	public String login(Model model, HttpServletRequest request, @RequestParam(value = "callbackUrl", required = false) String redirectUrl, HttpServletResponse response) {
		if(this.checkLogout == true) {
			try {
				Cookie cookie = WebUtils.getCookie(request, jwtTokenCookieName);
				System.out.println("COOKIE " + cookie.getValue());
				cookie.setValue(null);
				cookie.setPath("/");
				cookie.setHttpOnly(true);
				cookie.setMaxAge(0);
				cookie.setDomain(domainServer);
				response.addCookie(cookie);
//				CookieUtil.clear(response, jwtTokenCookieName);
				System.out.println("da xoa cookie");
				this.checkLogout = false;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		System.out.println("COOKIE AFTER " + CookieUtil.getValue(request, jwtTokenCookieName));
		if(CookieUtil.getValue(request, jwtTokenCookieName) != null && SessionUtil.getAttribute(request, jwtTokenSessionName).toString().equals(CookieUtil.getValue(request, jwtTokenCookieName))) {
			this.jwtTokenStore = CookieUtil.getValue(request, jwtTokenCookieName);
			System.out.println("token store: " + this.jwtTokenStore);
			try {
				this.jwtTokenStoreSession = SessionUtil.getAttribute(request, jwtTokenSessionName).toString();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			randomToken = StringUtil.randomAlphaNumeric(10);
//			this.addDomainUrl(redirectUrl);
			if(LoginController.getUserRole(request).equals(roleAdmin)) {
				return "redirect:/list_user";
			}
			return "redirect:" + redirectUrl + "?token=" + randomToken;
		}
		model.addAttribute("userFormLogin", new User());
		System.out.println("cookie at 8081" + CookieUtil.getValue(request, jwtTokenCookieName));
		return "login";
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(HttpServletResponse httpServletResponse, @ModelAttribute("userFormLogin") User user, HttpServletRequest request, Model model, @RequestParam(value = "callbackUrl", required = false) String callbackUrl) {
		User userLogin = userService.getUserByName(user.getUsername());
		if(user.getUsername() == null || user.getPassword() == null || userLogin == null || !userLogin.getPassword().equals(user.getPassword())) {
			model.addAttribute("error","Invalid username or password");
			if(callbackUrl != null) {
				return "redirect:" + "/login" + "?callbackUrl=" + callbackUrl;
			}
			else return "redirect:" + "/login";
		}
		ObjectMapper mapper = new ObjectMapper();
		String userData = new String();
		userLogin.setPassword("");
		try {
			userData = mapper.writeValueAsString(userLogin);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("userData " + userData);
		String token = null;
		try {
			token = JwtUtil.generateToken(signingKey, userData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CookieUtil.create(httpServletResponse, jwtTokenCookieName, token, false, -1, domainServer);
		SessionUtil.setAtribute(request, jwtTokenSessionName, token);
//		this.checkLogout = false;
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		if(userLogin.getRole().getName().equals("ROLE_ADMIN")) {
			return "redirect:" + "/list_user";
		}
		else return "redirect:" + "/login" + "?callbackUrl=" + callbackUrl;
	}
	
	@RequestMapping(value = "/getJwtToken/{token}", method = RequestMethod.GET)
	@ResponseBody
	public String getJwtToken(@PathVariable("token") String token) {
		try {
			if(this.randomToken.equals(token) && this.jwtTokenStore.equals(this.jwtTokenStoreSession)) {
				this.randomToken = null;
				return jwtTokenStore;
			}
			return null;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	@RequestMapping(value = "/getJwtToken", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> testGetJwtToken(@RequestParam(value= "token") String token) {
		System.out.println("test node call api");
		try {
			System.out.println("token" + token);
			System.out.println("random token" + this.randomToken);
			System.out.println("token store" + this.jwtTokenStore);
			if(this.randomToken.equals(token) && this.jwtTokenStore.equals(this.jwtTokenStoreSession)) {
				this.randomToken = null;
				return new ResponseEntity<String>(this.jwtTokenStore, HttpStatus.OK);
			}
			return null;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	
	@RequestMapping(value = "/setJwtToken", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> setJwtToken(@RequestBody String tokenRefresh) {
		if(tokenRefresh != null) {
			this.jwtTokenStore = null;
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
		
	@RequestMapping(value = "/clearCookie", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> clearCookieFromLogout(@RequestBody boolean checkLogout) {
		this.checkLogout = checkLogout;
		System.out.println("checkLogout:" + this.checkLogout);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value="clearCookieJs", method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> clearCookieNodeJS(@RequestBody String checkLogout){
		if(checkLogout.contains("1")) {
			this.checkLogout = true;
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	@RequestMapping(value = "/getCheckLogoutStatus", method = RequestMethod.GET)
	@ResponseBody
	public String getCheckLogoutStatus() {
		if(this.checkLogout) {
			return "1";
		}
		return "0";
	}
	
	@RequestMapping(value = "/doLogout", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> doLogout(@RequestBody String userId){
		System.out.println("123" + userId);
		RestTemplate rest = new RestTemplate();
		for(String s : listUrl) {
			System.out.println("url: " + s+"/doLogout");
			rest.postForObject(s+"/doLogout", userId, String.class);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	
	private String getDomainUrl(String url) throws MalformedURLException {
		URL uri;
		String domain = null;
		uri = new URL(url);
		domain = uri.getProtocol() + "://" + uri.getHost();
		return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
	
	//Ham xu li domain URL
	private void addDomainUrl(String url) {
		String domain = null;
		try {
			domain = getDomainUrl(url);
			System.out.println("domain" + domain);
			if(!domain.equals(null)) {
				listUrl.add(getDomainUrl(domain));
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	//Ham authorization
	public  static String getUserRole(HttpServletRequest request) {
		String userData = JwtUtil.getSubject(request, jwtTokenCookieName, signingKey);
		System.out.println("bsajash" + userData);
		if(userData == null) return null;
		ObjectMapper mapper = new ObjectMapper();
		User user = null;
		try {
			user = mapper.readValue(userData, User.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(user!=null) {
			return user.getRole().getName();
		}
		return null;
	}
}
