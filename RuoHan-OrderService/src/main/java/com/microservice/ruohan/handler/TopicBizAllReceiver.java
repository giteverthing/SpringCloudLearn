package com.microservice.ruohan.handler;

import com.microservice.ruohan.config.TopicRabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = TopicRabbitConfig.ROUTE_KEY2)
public class TopicBizAllReceiver {
    @RabbitHandler
    public void process(Map map) {
        System.out.println("TopicBizAllReceiver消费者收到消息:" + map.toString());
    }
}
