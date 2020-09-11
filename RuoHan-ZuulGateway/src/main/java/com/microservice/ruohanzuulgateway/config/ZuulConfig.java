package com.microservice.ruohanzuulgateway.config;

import com.microservice.ruohanzuulgateway.filter.PreRequestLogFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulConfig {
    @Bean
    public PreRequestLogFilter preRequestLogFilter() {
        return new PreRequestLogFilter();
    }
}
