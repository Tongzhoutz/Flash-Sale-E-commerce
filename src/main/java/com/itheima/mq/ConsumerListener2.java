package com.itheima.mq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RocketMQMessageListener(topic="test_delayed", consumerGroup = "consumerGroup")
public class ConsumerListener2 implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            String body = new String(messageExt.getBody(), "UTF-8");
            System.out.println("Receive message: " + body);
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();
        }

    }
}
