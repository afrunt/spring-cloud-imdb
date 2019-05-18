package com.afrunt.scimdb.titlebasics;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * @author Andrii Frunt
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication
@EnableHystrix
@EnableCircuitBreaker
@EnableElasticsearchRepositories(basePackages = "com.afrunt.scimdb.titlebasics.repository")
public class TitleBasicsServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(TitleBasicsServiceApp.class, args);
    }

    @Bean
    public MapperFactory createMapperFactory() {
        return customize(new DefaultMapperFactory.Builder().build());
    }

    @Bean
    @Autowired
    public MapperFacade createMapperFacade(MapperFactory mapperFactory) {
        return mapperFactory.getMapperFacade();
    }

    @Bean
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

    private MapperFactory customize(MapperFactory factory) {
        return factory;
    }
}
