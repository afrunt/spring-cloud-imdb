package com.afrunt.scimdb.crawler;

import com.afrunt.imdb.client.IMDbClient;
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
@EnableScheduling
@EnableAsync
@EnableFeignClients
@EnableHystrix
@EnableCircuitBreaker
@SpringBootApplication
public class CrawlerServiceApp {

    @Bean
    public IMDbClient createImDbClient() {
        return new IMDbClient();
    }

    public static void main(String[] args) {
        SpringApplication.run(CrawlerServiceApp.class, args);
    }
}
