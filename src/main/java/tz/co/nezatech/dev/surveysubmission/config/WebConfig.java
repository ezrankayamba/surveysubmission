package tz.co.nezatech.dev.surveysubmission.config;


import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver() {
			@Override
		     public boolean isMultipart(HttpServletRequest request) {
				String method = request.getMethod().toLowerCase();
		        if (!Arrays.asList("put", "post").contains(method)) {
		           return false;
		        }
		        String contentType = request.getContentType();
		        return (contentType != null &&contentType.toLowerCase().startsWith("multipart/"));
			}
		};
	}
}
