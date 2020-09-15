package com.microservice.ruohan.handler;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义手动确认消息消费者
 * 手动确认模式需要实现 ChannelAwareMessageListener
 */
@Component
public class MyAckReciver implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliverTag = message.getMessageProperties().getDeliveryTag();
        try {
            //因为传递消息的时候用的map传递,所以将Map从Message内取出需要做些处理
            String msg = message.toString();
            //可以点进Message里面看源码,单引号直接的数据就是我们的map消息数据
            String[] msgArray = msg.split("'");
            Map<String, String> msgMap = mapStringToMap(msgArray[1].trim(), 3);
            String messageId = msgMap.get("messageId");
            String messageData = msgMap.get("messageData");
            String createTime = msgMap.get("createTime");
            //监听的好几个队列都想变成手动确认模式，而且处理的消息业务逻辑不一样。
            String queueName = message.getMessageProperties().getConsumerQueue();
            System.out.println("消费的消息来自的队列名为：" + queueName);
            System.out.println("消息成功消费到  messageId:" + messageId + "  messageData:" + messageData + "  createTime:" + createTime);
            System.out.println("执行" + queueName + "中的消息的业务处理流程......");
            channel.basicAck(deliverTag, true);
            //channel.basicReject(deliveryTag, true);//为true会重新放回队列
        } catch (Exception e) {
            channel.basicReject(deliverTag, false);
            e.printStackTrace();
        }
    }

    //{key=value,key=value,key=value} 格式转换成map
    private Map<String, String> mapStringToMap(String str, int entryNum) {
        str = str.substring(1, str.length() - 1);
        String[] strs = str.split(",", entryNum);
        Map<String, String> map = new HashMap<>();
        for (String pair : strs) {
            String key = pair.split("=")[0].trim();
            String value = pair.split("=")[1];
            map.put(key, value);
        }

        return map;
    }
}
