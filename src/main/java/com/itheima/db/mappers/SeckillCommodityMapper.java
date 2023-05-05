package com.itheima.db.mappers;

import com.itheima.db.po.SeckillCommodity;
import org.apache.ibatis.annotations.Mapper;

public interface SeckillCommodityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillCommodity row);

    int insertSelective(SeckillCommodity row);

    SeckillCommodity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillCommodity row);

    int updateByPrimaryKey(SeckillCommodity row);
}