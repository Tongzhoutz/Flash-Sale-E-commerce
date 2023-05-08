package com.itheima;

import com.itheima.db.po.SeckillActivity;
import com.itheima.service.RedisService;
import com.itheima.service.SeckillActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedisDemoTest {

    @Resource
    private RedisService redisService;

    @Resource
    SeckillActivityService seckillActivityService;

    @Test
    public void stockTest() {
        redisService.setValue("stock:19", 10L);
    }

    @Test
    public void getStockTest() {
        String stock = redisService.getValue("stock:19");
        System.out.println(stock);
    }

    @Test
    public void removeLimitMember() {
        redisService.removeLimitMember(19, 1234);
    }

    @Test
    public void pushSeckillInfoToRedisTest() {
        seckillActivityService.pushSeckillInfoToRedis(19);
        System.out.println(redisService.getValue("seckillActivity:19"));
    }
    @Test
    public void getSeckillInfoFromRedis() {
        String seckillInfo = redisService.getValue("seckillActivity:" + 19);
        System.out.println(seckillInfo);
        String seckillCommodity = redisService.getValue("seckillCommodity:"+1001);
        System.out.println(seckillCommodity);
    }

}
