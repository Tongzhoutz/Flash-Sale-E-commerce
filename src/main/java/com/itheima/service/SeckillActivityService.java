package com.itheima.service;

import com.alibaba.fastjson.JSON;
import com.itheima.db.dao.OrderDao;
import com.itheima.db.dao.SeckillActivityDao;
import com.itheima.db.po.Order;
import com.itheima.db.po.SeckillActivity;
import com.itheima.mq.RocketMQService;
import com.itheima.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class SeckillActivityService {

    @Resource
    private RedisService service;

    @Resource
    private SeckillActivityDao seckillActivityDao;

    @Resource
    private RocketMQService rocketMQService;

    @Resource
    private OrderDao orderDao;
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
        rocketMQService.sendDelayMessage("pay_check", JSON.toJSONString(order), 2);
        log.info("sent delayed order!");
        return order;

    }

    public void payOrderProcess(String orderNo) {
        Order order = orderDao.queryOrder(orderNo);
        boolean deductStockResult = seckillActivityDao.deductStock(order.getSeckillActivityId());

        if (deductStockResult) {
            order.setPayTime(new Date());
            // 0: no inventory and invalid order
            // 1: created order, awaiting payment
            // 2: paid order
            order.setOrderStatus(2);
            orderDao.updateOrder(order);
        }
    }
}

