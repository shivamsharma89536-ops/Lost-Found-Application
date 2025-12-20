package com.Android.Android_App.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // Allow all endpoints
                .allowedOrigins("*")      // Allow all origins (for testing)
                .allowedMethods("*")      // Allow all methods (GET, POST, etc.)
                .allowedHeaders("*");     // Allow all headers
    }
}