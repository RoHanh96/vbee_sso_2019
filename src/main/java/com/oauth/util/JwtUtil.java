package com.oauth.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	public static String generateToken(String signingKey, String subject) throws UnsupportedEncodingException {
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		JwtBuilder builder = Jwts.builder()
				.setSubject(subject)
				.setIssuedAt(now)
				.signWith(SignatureAlgorithm.HS256, signingKey.getBytes("UTF-8"));
		return builder.compact();
	}
	
	public static String getSubject(HttpServletRequest httpServletRequest, String jwtTokenCookieName, String signingKey) {
		String token = CookieUtil.getValue(httpServletRequest, jwtTokenCookieName);
		if(token == null) return null;
		return Jwts.parser().setSigningKey(signingKey.getBytes()).parseClaimsJws(token).getBody().getSubject();
	}
}
