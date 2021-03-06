package com.microservice.ruohan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication()//exclude = {DataSourceAutoConfiguration.class}
@MapperScan("com.microservice.ruohan.repository")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableElasticsearchRepositories(basePackages = "com.microservice.ruohan.repository")
@EnableCaching
public class RuoHanApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(RuoHanApplication.class, args);
    }

}
