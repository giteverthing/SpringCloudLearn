package com.microservice.ruohan.config;

import com.microservice.ruohan.handler.MyAckReciver;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MessageListenerConfig {
    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private MyAckReciver myAckReciver;//消息接收处理类
    @Autowired
    private Environment env;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);

        // 设置一个队列
        //container.setQueueNames("TestDirectQueue");
        // 设置多个队列
        container.setQueueNames("TestDirectQueue", "fanout.A", "fanout.B");
        //如果同时设置多个如下： 前提是队列都是必须已经创建存在的
        //  container.setQueueNames("TestDirectQueue","TestDirectQueue2","TestDirectQueue3");


        //另一种设置队列的方法,如果使用这种情况,那么要设置多个,就使用addQueues
        //container.setQueues(new Queue("TestDirectQueue",true));
        //container.addQueues(new Queue("TestDirectQueue2",true));
        //container.addQueues(new Queue("TestDirectQueue3",true));
        container.setMessageListener(myAckReciver);

        //并发配置
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setPrefetchCount(5);
//        container.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.concurrency", Integer.class));
//        container.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.max-concurrency", Integer.class));
//        container.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.prefetch", Integer.class));

        //消息确认机制
        // RabbitMQ默认是自动确认，这里改为手动确认消息
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }
}
