package com.vbee.springbootmongodbnewspapersrestapi.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbee.springbootmongodbnewspapersrestapi.model.Config;
import com.vbee.springbootmongodbnewspapersrestapi.model.User;
import com.vbee.springbootmongodbnewspapersrestapi.ulti.CookieUtil;
import com.vbee.springbootmongodbnewspapersrestapi.ulti.Encryptor;
import com.vbee.springbootmongodbnewspapersrestapi.ulti.SessionUtil;

import io.jsonwebtoken.Jwts;

@Component
public class JwtFilter extends OncePerRequestFilter {
	private static final String jwtTokenSessionName = "access-token";
	private static final String domainClient = "demo3.com";
	private static final String signingKey = "signingKey";
	
	@Autowired
	Config config;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(CookieUtil.getValue(request, jwtTokenSessionName) == null || SessionUtil.getAttribute(request, "access-token") == null) 
		{
			try {
				RestTemplate rest1 = new RestTemplate();
				String token = null;
				String getJwtTokenFromServer = null;
				try {
					token = request.getParameter("token");
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				if(token != null) {
					UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(config.getSsoUrl()+"getJwtToken").queryParam("token",  token);
					getJwtTokenFromServer = rest1.getForObject(uriBuilder.toUriString(), String.class);
				}
				if(getJwtTokenFromServer != null ) {
					String tokenRefresh = getJwtTokenFromServer;
					rest1.postForObject(config.getSsoUrl()+"/setJwtToken", tokenRefresh, String.class);
					CookieUtil.create(response, jwtTokenSessionName, encodeJwtToken(getJwtTokenFromServer), false, -1, domainClient);
					SessionUtil.setAtribute(request, jwtTokenSessionName, encodeJwtToken(getJwtTokenFromServer));
					String userJson = Jwts.parser().setSigningKey(signingKey.getBytes()).parseClaimsJws(getJwtTokenFromServer).getBody().getSubject();
					ObjectMapper mapper = new ObjectMapper();
					User user = mapper.readValue(userJson, User.class);
					SessionUtil.setAtribute(request, "userId", user.getId());
					SessionUtil.setAtribute(request, "userName", user.getUsername());
					System.out.println("userId in session: " + SessionUtil.getAttribute(request, "userId"));
					System.out.println("Dang nhap thanh cong");
					filterChain.doFilter(request, response);
					
				}
				else {
					String authService = this.getFilterConfig().getInitParameter("services.auth");
					String redirectUrl = request.getRequestURL().toString();
					System.out.println("Lan dang nhap dau tien ");
					response.sendRedirect(authService + "?callbackUrl=" + redirectUrl);
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("abc" + e.toString());
			}
			
		}
		else {
			if(CookieUtil.getValue(request, jwtTokenSessionName).equals(SessionUtil.getAttribute(request, jwtTokenSessionName))) {
				System.out.println("Lan dang nhap thu 2. Khong request len server. Dang nhap thanh cong");
				filterChain.doFilter(request, response);
			}
			//Khi client A logout thi cookie trong client B van con
			else {
				
			}
		}
	}
	
	/**
	 * Hàm convert token nhận được từ server sang token ở client theo giải thuật nhất định (md5)
	 * @param token
	 * @return
	 */
	private String encodeJwtToken(String token) {
		//Must be 16 bytes long
		String key = "abcdabcdabcdabce";
		
		//Must be 16 bytes long
		String initVector = "aaaabbbbccccdddd";
		return Encryptor.encrypt(key, initVector, token);
	}
	
	
}
