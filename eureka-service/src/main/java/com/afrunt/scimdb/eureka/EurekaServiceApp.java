package com.afrunt.scimdb.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Andrii Frunt
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceApp.class, args);
    }
}
