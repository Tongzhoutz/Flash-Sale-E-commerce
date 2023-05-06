package com.itheima.db.mappers;

import com.itheima.db.po.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order row);

    int insertSelective(Order row);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order row);

    int updateByPrimaryKey(Order row);

    Order selectByOrderNo(String orderNo);
}