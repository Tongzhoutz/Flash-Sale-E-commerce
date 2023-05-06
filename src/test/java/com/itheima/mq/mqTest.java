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
}
