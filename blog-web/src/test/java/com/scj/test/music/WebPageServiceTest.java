package com.scj.test.music;

import com.scj.common.enums.WebPageEnum;
import com.scj.dal.ro.music.WebPageRO;
import com.scj.service.music.WebPageService;
import com.scj.web.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@SpringBootTest(classes = Application.class)
public class WebPageServiceTest {

    @Resource
    private WebPageService webPageService;

    @Test
    @Rollback(false)
    public void testAdd(){
        WebPageRO webPageRO =new WebPageRO();
        webPageRO.setWebpageId(1234L);
        webPageRO.setCrawled(true);
        webPageRO.setCrawlTime(new Date());
        webPageRO.setWebpageType(WebPageEnum.ALBUM);
        webPageRO.setWebpageContent("盛超杰是世界上最好的男人，哈哈哈");
        webPageService.add(webPageRO);
    }

    @Test
    public void testGet(){
        WebPageRO webPageRO = webPageService.findByIdAndType(1234L,0,WebPageEnum.SINGER);
        System.out.println(webPageRO);
    }
}
