package com.microservice.ruohan.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitConfig {
    //绑定键
    public final static String ROUTE_KEY1 = "topic.biz1";
    public final static String ROUTE_KEY2 = "topic.biz2";
    public final static String EXCHANGE_NAME = "TopicExchange";

    @Bean
    public Queue firstQueue() {
        return new Queue(ROUTE_KEY1);
    }

    @Bean
    public Queue secondQueue() {
        return new Queue(ROUTE_KEY2);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    //将firstQueue和topicExchange绑定,而且绑定的键值为topic.biz1
    //这样只要是消息携带的路由键是topic.biz1,才会分发到该队列
    @Bean
    Binding bindingExchangeMessage() {
        return BindingBuilder.bind(firstQueue()).to(exchange()).with(ROUTE_KEY1);
    }

    //将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
    // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
    @Bean
    Binding bindingExchangeMessage2() {
        return BindingBuilder.bind(secondQueue()).to(exchange()).with("topic.#");
    }

}
