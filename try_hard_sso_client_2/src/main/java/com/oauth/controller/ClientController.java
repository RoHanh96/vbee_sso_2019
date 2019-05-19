package com.oauth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.oauth.entity.User;
import com.oauth.util.CookieUtil;
import com.oauth.util.SessionUtil;

@Controller
public class ClientController {
	private static final String jwtTokenSessionName = "access-token";
	private static final String clearCookieApi = "http://localhost:8081/clearCookie";
	public static final String domain = "demo2.com";
	
//	private String userId = null;
//	private boolean setSessionNull = false;
	
	@RequestMapping("/")
	public String home(HttpServletRequest request) {
//		if(this.setSessionNull) {
//			
//			SessionUtil.setAtribute(request, "access-token", null);
//			System.out.println("session val: " + SessionUtil.getAttribute(request, "access-token"));
//			this.setSessionNull = false;
//		}
		return "redirect:/protected-resource";
	}
	
	@RequestMapping("/protected-resource")
	public String protectedResource(Model model, HttpServletRequest request) {
		return "protected-resource";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletResponse httpServletResponse, HttpServletRequest request) {
		CookieUtil.clear(httpServletResponse, jwtTokenSessionName, domain);
		System.out.println("vao logout");
		RestTemplate rest = new RestTemplate();
		boolean checkLogout = true;
		rest.postForObject(clearCookieApi, checkLogout, String.class);
//		this.userId = SessionUtil.getAttribute(request, "userId").toString();
//		rest.postForObject("http://localhost:8081/doLogout", SessionUtil.getAttribute(request, "userId"), String.class);
		return "redirect:/";
	}
	
//	@RequestMapping(value = "/doLogout", method = RequestMethod.POST)
//	@ResponseBody
//	public ResponseEntity<String> doLogout( @RequestBody String userId){
//		if(userId.equals(this.userId)) {
//			this.setSessionNull = true;
//		}
//		return new ResponseEntity<String>(HttpStatus.OK);
//	}
	public String updateUserInfo() {
		return "updateForm";
	}
	
}
