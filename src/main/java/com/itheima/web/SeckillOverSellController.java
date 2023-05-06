package com.itheima.web;

import com.itheima.service.SeckillActivityService;
import com.itheima.service.SeckillOverSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SeckillOverSellController {

    // @Resource
    // private SeckillOverSellService seckillOverSellService;

    private SeckillActivityService seckillActivityService;
/*
    @ResponseBody
    @RequestMapping("/seckill/{seckillActivityId}")
    public String seckill(@PathVariable long seckillActivityId) {
        return seckillOverSellService.processSeckill(seckillActivityId);
    }
*/

    @ResponseBody
    @RequestMapping("/seckill/{seckillActivityId}")
    public String seckillCommodity(@PathVariable long seckillActivityId) {
        boolean stockValidateResult = seckillActivityService.seckillStockValidator(seckillActivityId);
        return stockValidateResult ? "Congratulations! You got it!" : "Sorry! Sold Out!";
    }


}
