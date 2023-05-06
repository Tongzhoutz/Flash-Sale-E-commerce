package com.itheima.mq;


import com.alibaba.fastjson.JSON;
import com.itheima.db.dao.OrderDao;
import com.itheima.db.dao.SeckillActivityDao;
import com.itheima.db.po.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
@RocketMQMessageListener(topic = "seckill_order", consumerGroup = "seckill_order_group")
public class OrderConsumer implements RocketMQListener<MessageExt> {

    @Resource
    private OrderDao orderDao;

    @Resource
    private SeckillActivityDao seckillActivityDao;
    @Override
    @Transactional
    public void onMessage(MessageExt messageExt) {

        // 1. parse new order
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("Received new order info: " + message);
        Order order = JSON.parseObject(message, Order.class);
        order.setCreateTime(new Date());

        // 2. inventory deduction
        boolean lockStockResult = seckillActivityDao.lockStock(order.getSeckillActivityId());
        if (lockStockResult) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(0);
        }

        orderDao.insertOrder(order);
    }
}
