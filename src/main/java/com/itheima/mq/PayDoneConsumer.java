package com.itheima.mq;

import com.alibaba.fastjson.JSON;
import com.itheima.db.dao.SeckillActivityDao;
import com.itheima.db.po.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@Transactional
@RocketMQMessageListener(topic = "pay_done", consumerGroup = "pay_done_group")
public class PayDoneConsumer {

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Override
    public void onMessage(MessageExt messageExt) {
        // 1. parse order info
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("接受到创建订单请求: " + message);

        Order order = JSON.parseObject(message, Order.class);

        // 2. deduct inventory
        seckillActivityDao.deductStock(order.getSeckillActivityId());
    }

}
