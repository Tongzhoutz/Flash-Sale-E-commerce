package com.itheima.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisServiceTest {

    @Autowired
    private RedisService service;

    @Test
    void setValue() {
        service.setValue("stock:19", 10L);
    }

    @Test
    void getValue() {
        String stock = service.getValue("stock:19");
        System.out.println(stock);
    }

    @Test
    void stockDeductValidation() {
        boolean result = service.stockDeductValidation("stock:19");
        System.out.println("result: " + result);
        String stock = service.getValue("stock:19");
        System.out.println("stock:" + stock);
    }

    @Test
    public void revertStock() {
        String stock = service.getValue("stock:19");
        System.out.println("回滚前的库存: " + stock);

        service.revertStock("stock:19");

        stock = service.getValue("stock:19");
        System.out.println("回滚后的库存: " + stock);
    }
}