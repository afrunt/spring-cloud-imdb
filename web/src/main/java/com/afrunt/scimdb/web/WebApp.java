package com.afrunt.scimdb.web;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Andrii Frunt
 */
@EnableFeignClients
@EnableAsync
@EnableScheduling
@EnableHystrix
@EnableCircuitBreaker
@SpringBootApplication
public class WebApp {

    @Bean
    public MapperFactory createMapperFactory() {
        return new DefaultMapperFactory.Builder().build();
    }

    @Bean
    @Autowired
    public MapperFacade createMapperFacade(MapperFactory mapperFactory) {
        return mapperFactory.getMapperFacade();
    }


    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }
}
