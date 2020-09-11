package com.microservice.ruohanapp;

import com.microservice.ruohanapp.config.RibbonPolicyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@RibbonClient(name = "ruohan-consumer", configuration = RibbonPolicyConfig.class)
public class RuoHanAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuoHanAppApplication.class, args);
    }

}
