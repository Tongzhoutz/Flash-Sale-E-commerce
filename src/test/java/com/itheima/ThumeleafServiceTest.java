package com.itheima;

import com.itheima.service.ActivityHtmlPageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ThumeleafServiceTest {
    @Autowired
    private ActivityHtmlPageService activityHtmlPageService;

    @Test
    public void createHtmlTest() {
        activityHtmlPageService.createActivityHtml(19);
    }
}
