package com.itheima.db.dao;

import com.itheima.db.po.SeckillCommodity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeckillCommodityDao {

    public SeckillCommodity querySeckillCommodityById(long commodityId);
}
