package com.itheima.mq;

import com.alibaba.fastjson.JSON;
import com.itheima.db.dao.OrderDao;
import com.itheima.db.dao.SeckillActivityDao;
import com.itheima.db.po.Order;
import com.itheima.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RocketMQMessageListener(topic = "pay_check", consumerGroup = "pay_check_group")
public class PayStatusCheckListener implements RocketMQListener<MessageExt> {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Resource
    private RedisService redisService;

    @Override
    @Transactional
    public void onMessage(MessageExt messageExt) {
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("接收到订单支付状态校验消息: " + message);
        Order order = JSON.parseObject(message, Order.class);

        // 1. query order
        Order orderInfo = orderDao.queryOrder(order.getOrderNo());

        if (orderInfo == null){
            log.info("Invalid Order " + order.getOrderNo());
            return;
        }

        // 2. check payment
        if (orderInfo.getOrderStatus() != 2) {
            // 3. close order
            log.info("Unpaid order, order No.: " + orderInfo.getOrderNo());
            orderInfo.setOrderStatus(99);
            orderDao.updateOrder(orderInfo);

            // 4. restore inventory
            seckillActivityDao.revertStock(order.getSeckillActivityId());
            redisService.revertStock("stock:" + order.getSeckillActivityId());
            // 5. remove user from paid list
            redisService.removeLimitMember(order.getSeckillActivityId(), order.getUserId());
        }

    }
}
