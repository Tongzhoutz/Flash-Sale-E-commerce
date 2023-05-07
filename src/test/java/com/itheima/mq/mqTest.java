package com.itheima.mq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
public class mqTest {

    @Resource
    private RocketMQService service;
    @Test
    public void sendMQTest() throws Exception {
        service.sendMessage("test", "Hello, world!" + new Date().toString());
    }

    @Test
    public void sendDelayedMQTest() throws Exception {
        service.sendDelayMessage("test_delayed", "hello, word Delayed!" + new Date().toString(), 5);
    }
}
