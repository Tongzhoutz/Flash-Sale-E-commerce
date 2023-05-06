package com.itheima.service;

import com.alibaba.fastjson.JSON;
import com.itheima.db.dao.SeckillActivityDao;
import com.itheima.db.po.Order;
import com.itheima.db.po.SeckillActivity;
import com.itheima.mq.RocketMQService;
import com.itheima.util.SnowFlake;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SeckillActivityService {

    @Resource
    private RedisService service;

    @Resource
    private SeckillActivityDao seckillActivityDao;

    @Resource
    private RocketMQService rocketMQService;
    private SnowFlake snowFlake = new SnowFlake(1,1);


    public boolean seckillStockValidator(long activityId){
        String key = "stock:" + activityId;
        return service.stockDeductValidation(key);
    }

    public Order createOrder(long seckillActivityId, long userId) throws Exception {
        SeckillActivity activity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        Order order = new Order();

        // snowflake generating order id
        order.setOrderNo(String.valueOf(snowFlake.nextId()));
        order.setSeckillActivityId(activity.getId());
        order.setUserId(userId);
        order.setOrderAmount(activity.getSeckillPrice().longValue());

        // send order msg: producer
        rocketMQService.sendMessage("seckill_order", JSON.toJSONString(order));
        return order;

    }
}

