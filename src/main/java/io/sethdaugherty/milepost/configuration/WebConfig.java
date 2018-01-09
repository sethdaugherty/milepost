package io.sethdaugherty.milepost.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	@Value("${upload.location}")
	private String uploadLocation;
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	// TODO: Fix this
    	String uploadLocation = "C:\\Users\\Seth\\workspace\\wanderingmaps\\";
    	registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + uploadLocation);
    	super.addResourceHandlers(registry);
    }

}