package com.vbee.springbootmongodbnewspapersrestapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@EnableMongoAuditing
public class WebConfig extends WebMvcConfigurerAdapter{
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
	
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/data/**").addResourceLocations("/WEB-INF/static/data/").setCachePeriod(31556926);
		registry.addResourceHandler("/dist/**").addResourceLocations("/WEB-INF/static/dist/").setCachePeriod(31556926);
		registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/static/js/").setCachePeriod(31556926);
		registry.addResourceHandler("/vendor/**").addResourceLocations("/WEB-INF/static/vendor/").setCachePeriod(31556926);
		registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/static/css/").setCachePeriod(31556926);
		registry.addResourceHandler("/img/**").addResourceLocations("/WEB-INF/static/img/").setCachePeriod(31556926);
	}
}
