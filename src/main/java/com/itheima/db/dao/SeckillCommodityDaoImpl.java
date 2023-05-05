package com.itheima.db.dao;

import com.itheima.db.mappers.SeckillCommodityMapper;
import com.itheima.db.po.SeckillCommodity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

// import javax.annotation.Resource;

@Repository
public class SeckillCommodityDaoImpl implements SeckillCommodityDao {

    @Resource
    private SeckillCommodityMapper seckillCommodityMapper;

    @Override
    public SeckillCommodity querySeckillCommodityById(long commodityId) {
        return seckillCommodityMapper.selectByPrimaryKey(commodityId);
    }
}
