package com.itheima.db.dao;

import com.itheima.db.po.Order;

public interface OrderDao {

    void insertOrder(Order order);
    Order queryOrder(String orderNo);

    void updateOrder(Order order);
}
