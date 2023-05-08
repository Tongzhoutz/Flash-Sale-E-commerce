package com.itheima.service;

import com.itheima.db.dao.SeckillActivityDao;
import com.itheima.db.dao.SeckillCommodityDao;
import com.itheima.db.po.SeckillActivity;
import com.itheima.db.po.SeckillCommodity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

@Slf4j
@Service
public class ActivityHtmlPageService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private SeckillCommodityDao seckillCommodityDao;

    public void createActivityHtml(long seckillActivityId) {
        PrintWriter writer = null;

        try {
            SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
            SeckillCommodity seckillCommodity = seckillCommodityDao.querySeckillCommodityById(seckillActivity.getCommodityId());

            // Retrieve page info
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("seckillActivity", seckillActivity);
            resultMap.put("seckillCommodity", seckillCommodity);
            resultMap.put("seckillPrice", seckillActivity.getSeckillPrice());
            resultMap.put("oldPrice", seckillActivity.getOldPrice());
            resultMap.put("commodityId", seckillActivity.getCommodityId());
            resultMap.put("commodityName", seckillCommodity.getCommodityName());
            resultMap.put("commodityDesc", seckillCommodity.getCommodityDesc());

            // create thymeleaf context
            Context context = new Context();
            context.setVariables(resultMap);


            // output flow
            File file = new File("src/main/resources/templates/" + "seckill_item_" + seckillActivityId + ".html");
            writer = new PrintWriter(file);

            templateEngine.process("seckill_item", context, writer);

        } catch (Exception e) {
            log.error(e.toString());
            log.error("Static page failed: " + seckillActivityId);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }
}
