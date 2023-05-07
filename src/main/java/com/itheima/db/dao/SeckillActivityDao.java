package com.itheima.db.dao;

import com.itheima.db.po.SeckillActivity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SeckillActivityDao {

    public List<SeckillActivity> querySeckillActivitysByStatus(int activityStatus);

    public void inertSeckillActivity(SeckillActivity seckillActivity);

    public SeckillActivity querySeckillActivityById(long activityId);

    public void updateSeckillActivity(SeckillActivity seckillActivity);

    public boolean lockStock(Long seckillActivityId);

    boolean deductStock(Long seckillActivityId);

    void revertStock(Long seckillActivityId);
}
