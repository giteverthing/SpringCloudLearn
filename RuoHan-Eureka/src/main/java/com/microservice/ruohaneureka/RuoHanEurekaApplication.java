package com.microservice.ruohaneureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RuoHanEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuoHanEurekaApplication.class, args);
    }

}
