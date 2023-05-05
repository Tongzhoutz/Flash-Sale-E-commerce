package com.itheima.service;

import com.itheima.db.dao.SeckillActivityDao;
import com.itheima.db.po.SeckillActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillOverSellService {

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    public String processSeckill(long activityId) {
       SeckillActivity activity = seckillActivityDao.querySeckillActivityById(activityId);
       int availableStock = activity.getAvailableStock();
       String result;
       if (availableStock > 0) {
           result =  "Congratulations!";
           System.out.println(result);
           availableStock -= 1;
           activity.setActivityStatus(availableStock);
           // write in db
           seckillActivityDao.updateSeckillActivity(activity);
       } else {
           result = "Sorry, No Item Left!";
           System.out.println(result);
       }
       return result;

    }
}
