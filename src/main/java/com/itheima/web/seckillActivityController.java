package com.itheima.web;

import com.itheima.db.dao.OrderDao;
import com.itheima.db.dao.SeckillActivityDao;
import com.itheima.db.dao.SeckillCommodityDao;
import com.itheima.db.po.Order;
import com.itheima.db.po.SeckillActivity;
import com.itheima.db.po.SeckillCommodity;
import com.itheima.service.SeckillActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
public class seckillActivityController {

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private SeckillCommodityDao seckillCommodityDao;

    @Autowired
    private SeckillActivityService seckillActivityService;

    @Resource
    private OrderDao orderDao;

    @RequestMapping("/addSeckillActivity")
    public String addSeckillActivity(){
        return "add_activity";
    }

    @RequestMapping("/addSeckillActivityAction")
    public String addSeckillActivityAction(
            @RequestParam("name") String name,
            @RequestParam("commodityId") long commodityId,
            @RequestParam("seckillPrice") BigDecimal seckillPrice,
            @RequestParam("oldPrice") BigDecimal oldPrice,
            @RequestParam("seckillNumber") long seckillNumber,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            Map<String, Object> resultMap
    ) throws ParseException {
        startTime = startTime.substring(0, 10) + startTime.substring(11);
        endTime = endTime.substring(0, 10) + endTime.substring(11);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh:mm");
        SeckillActivity seckillActivity = new SeckillActivity();
        seckillActivity.setName(name);
        seckillActivity.setCommodityId(commodityId);
        seckillActivity.setSeckillPrice(seckillPrice);
        seckillActivity.setOldPrice(oldPrice);
        seckillActivity.setTotalStock(seckillNumber);
        seckillActivity.setAvailableStock(new Integer("" + seckillNumber));
        seckillActivity.setLockStock(0L);
        seckillActivity.setActivityStatus(1);
        seckillActivity.setStartTime(format.parse(startTime));
        seckillActivity.setEndTime(format.parse(endTime));
        seckillActivityDao.inertSeckillActivity(seckillActivity);
        resultMap.put("seckillActivity", seckillActivity);
        return "add_success";

    }


    @RequestMapping("/seckills")
    public String activityList(
             Map<String, Object> resultMap
    ) {
        List<SeckillActivity> seckillActivities = seckillActivityDao.querySeckillActivitysByStatus(1);
        resultMap.put("seckillActivities", seckillActivities);
        return "seckill_activity";
    }

    @RequestMapping("/item/{seckillActivityId}")
    public String itemPage(
            @PathVariable("seckillActivityId") long seckillActivityId,
            Map<String, Object> resultMap
                           ) {
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        SeckillCommodity seckillCommodity = seckillCommodityDao.querySeckillCommodityById(seckillActivity.getCommodityId());
        resultMap.put("seckillActivity", seckillActivity);
        resultMap.put("seckillCommodity", seckillCommodity);
        resultMap.put("seckillPrice", seckillActivity.getSeckillPrice());
        resultMap.put("oldPrice", seckillActivity.getOldPrice());
        resultMap.put("commodityId", seckillActivity.getCommodityId());
        resultMap.put("commodityName", seckillCommodity.getCommodityName());
        resultMap.put("commodityDesc", seckillCommodity.getCommodityDesc());
        return "seckill_item";
    }

    @RequestMapping("/seckill/buy/{userId}/{seckillActivityId}")
    public ModelAndView seckillCommodity(
        @PathVariable long userId,
        @PathVariable long seckillActivityId
    ){
        boolean stockValidationResult = false;

        ModelAndView modelAndView = new ModelAndView();

        try {
            stockValidationResult = seckillActivityService.seckillStockValidator(seckillActivityId);
            // System.out.println("helll" + stockValidationResult);
            if (stockValidationResult) {
                System.out.println("到了吗?");
                Order order = seckillActivityService.createOrder(seckillActivityId, userId);
                System.out.println("order"+ order);
                modelAndView.addObject("resultInfo", "You got it, order is generating..., order ID" + order.getOrderNo() );
                modelAndView.addObject("orderNo", order.getOrderNo());
            } else {
                modelAndView.addObject("resultInfo", "Sorry, insufficient level of inventory");
            }
        } catch (Exception exception) {
            log.error("Unusual things are detected: ", exception.toString());
            modelAndView.addObject("resultInfo", "Failed");
        }
        modelAndView.setViewName("seckill_result");
        return modelAndView;
    }

    @RequestMapping("/seckill/orderQuery/{orderNo}")
    public ModelAndView orderQuery(
            @PathVariable String orderNo
    ){
        log.info("Order query, order number: " + orderNo);
        Order order = orderDao.queryOrder(orderNo);
        ModelAndView modelAndView = new ModelAndView();

        if (order != null){
            modelAndView.setViewName("order");
            modelAndView.addObject(order);
            SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(order.getSeckillActivityId());
            modelAndView.addObject("seckillActivity", seckillActivity);
        } else {
            modelAndView.setViewName("order_wait");
        }
        return modelAndView;
    }
}
