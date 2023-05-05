package com.itheima.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SeckillActivityService {

    @Resource
    private RedisService service;


    public boolean seckillStockValidator(long activityId){
        String key = "stock:" + activityId;
        return service.stockDeductValidation(key);
    }
}

