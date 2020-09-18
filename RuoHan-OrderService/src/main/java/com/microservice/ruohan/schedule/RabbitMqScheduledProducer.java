package com.microservice.ruohan.schedule;

import com.microservice.ruohan.config.DirectRabbitConfig;
import com.microservice.ruohan.config.TopicRabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@EnableScheduling
public class RabbitMqScheduledProducer {
    private Logger logger = LoggerFactory.getLogger(RabbitMqScheduledProducer.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Scheduled(cron = "0/5 * 15 * * *")
//    @Scheduled(fixedRate = 5)
    public void produceDirectMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message,hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(DirectRabbitConfig.EXCHANGE_NAME, DirectRabbitConfig.ROUTE_KEY, map);

        logger.info("produceDirectMessage...");
    }

    //    @Scheduled(fixedRate = 10)
    public void produceTopicMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message:B I Z 1!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(TopicRabbitConfig.EXCHANGE_NAME, TopicRabbitConfig.ROUTE_KEY1, map);

        logger.info("produceTopicMessage...");
    }

    //    @Scheduled(fixedRate = 10)
    public void produceTopicMessage2() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message:B I Z 2!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(TopicRabbitConfig.EXCHANGE_NAME, TopicRabbitConfig.ROUTE_KEY2, map);

        logger.info("produceTopicMessage2...");
    }

//    @Scheduled(fixedRate = 10)
    public void produceFanoutMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message:testFanoutMessage!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("fanoutExchange", null, map);

        logger.info("produceFanoutMessage...");
    }
}
