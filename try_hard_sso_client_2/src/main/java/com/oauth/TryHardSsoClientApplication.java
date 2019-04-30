package com.oauth;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.oauth.filter.JwtFilter;

@SpringBootApplication
public class TryHardSsoClientApplication extends SpringBootServletInitializer {
	
	@Value("${services.auth}")
	private String authService;
	
	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.setInitParameters(Collections.singletonMap("services.auth", authService));
		registrationBean.addUrlPatterns("/protected-resource");
		return registrationBean;
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(TryHardSsoClientApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TryHardSsoClientApplication.class, args);
	}

}
