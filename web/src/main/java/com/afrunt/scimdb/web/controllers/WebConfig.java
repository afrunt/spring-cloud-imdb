package com.afrunt.scimdb.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Andrii Frunt
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    CommonDataInterceptor commonDataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonDataInterceptor)
                .excludePathPatterns("/error**");
    }
}
