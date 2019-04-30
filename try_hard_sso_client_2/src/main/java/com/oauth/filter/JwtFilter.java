package com.oauth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.oauth.util.AppUtil;
import com.oauth.util.CookieUtil;
import com.oauth.util.Encryptor;
import com.oauth.util.JwtUtil;
import com.oauth.util.SessionUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {
	private static final String jwtTokenSSOName = "JWT-TOKEN-SERVER-SSO";
	private static final String jwtTokenSessionName = "JWT-TOKEN-SEVER-SESSION";
	private static final String domainClient = "demo2.com";
	private static final String tokenApi = "http://localhost:8081/getJwtToken";
	private static final String sendRedirectApi = "http://localhost:8081/setRedirectUrl";
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//Check logout 
		if(CookieUtil.getValue(request, jwtTokenSessionName) == null) {
			try {
				RestTemplate rest1 = new RestTemplate();
				String getJwtTokenFromServer = rest1.getForObject(tokenApi, String.class);
				if(getJwtTokenFromServer != null) {
					CookieUtil.create(response, jwtTokenSSOName, getJwtTokenFromServer, false, -1, domainClient);
					CookieUtil.create(response, jwtTokenSessionName, encodeJwtToken(getJwtTokenFromServer), false, -1, domainClient);
					SessionUtil.setAtribute(request, jwtTokenSessionName, encodeJwtToken(getJwtTokenFromServer));
					System.out.println("Dang nhap thanh cong");
					filterChain.doFilter(request, response);
				}
				else {
					RestTemplate rest = new RestTemplate();			
					String authService = this.getFilterConfig().getInitParameter("services.auth");
					String redirectUrl = request.getRequestURL().toString();
					System.out.println("Lan dang nhap dau tien ");
					rest.postForObject(sendRedirectApi, redirectUrl, String.class);
					response.sendRedirect(authService);
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
			}
			
		}
		else {
			if(CookieUtil.getValue(request, jwtTokenSessionName).equals(SessionUtil.getAttribute(request, jwtTokenSessionName))) {
				System.out.println("Lan dang nhap thu 2. Khong request len server. Dang nhap thanh cong.");
				filterChain.doFilter(request, response);
			}
		}
	}
	
	private String encodeJwtToken(String token) {
		//Must be 16 bytes long
		String key = "abcdabcdabcdabcd";
		
		//Must be 16 bytes long
		String initVector = "aaaabbbbccccdddd";
		return Encryptor.encrypt(key, initVector, token);
	}
	
	
}
