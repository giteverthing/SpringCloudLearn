package com.microservice.ruohan.handler;

import com.microservice.ruohan.config.TopicRabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = TopicRabbitConfig.ROUTE_KEY1)
public class TopicBiz1Receiver {
    @RabbitHandler
    public void process(Map map) {
        System.out.println("TopicBiz1Receiver消费者收到消息:" + map.toString());
    }
}
