package com.itheima.db.mappers;

import com.itheima.db.po.SeckillActivity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SeckillActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillActivity row);

    int insertSelective(SeckillActivity row);

    SeckillActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillActivity row);

    int updateByPrimaryKey(SeckillActivity row);

    List<SeckillActivity> querySeckillActivitysByStatus(int activityStatus);

    int lockStock(Long id);
}