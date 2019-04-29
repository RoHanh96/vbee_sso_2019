package com.oauth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.oauth.util.AppUtil;
import com.oauth.util.CookieUtil;
import com.oauth.util.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {
	private static final String jwtTokenCookieName = "JWT-TOKEN";
	private static final String signingKey = "signingKey";
	private static final String domainClient = "demo1.com";
	private static final String domainServer = "localhost";
	private static final String tokenApi = "http://localhost:8081/getJwtToken";
	private static final String sendRedirectApi = "http://localhost:8081/setRedirectUrl";
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {
			RestTemplate rest1 = new RestTemplate();
			String getJwtTokenFromServer = rest1.getForObject(tokenApi, String.class);
			System.out.println("jwtFromServer: " + getJwtTokenFromServer);
			if(getJwtTokenFromServer != null) CookieUtil.create(response, jwtTokenCookieName, getJwtTokenFromServer, false, -1, domainClient);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		String username = JwtUtil.getSubject(request, jwtTokenCookieName, signingKey);
		if(username ==  null) {
			RestTemplate rest = new RestTemplate();			
			String authService = this.getFilterConfig().getInitParameter("services.auth");
			String redirectUrl = request.getRequestURL().toString();
			rest.postForObject(sendRedirectApi, redirectUrl, String.class);
//			CookieUtil.create(response, "clientRedirectURL", request.getRequestURL().toString(), false, -1, domainServer);
//			CookieUtil.getValue(request, "clientRedirectURL");
//			response.sendRedirect(authService + "?redirect=" + request.getRequestURL());
			response.sendRedirect(authService);
			
		}else {
			System.out.println("qua filter");
			request.setAttribute("username", username);
			filterChain.doFilter(request, response);
		}
	}
	
	
}
