package com.scj.test.music;

import com.scj.common.enums.JobTypeEnum;
import com.scj.dal.ro.music.CrawlInfoRO;
import com.scj.service.music.CrawlInfoService;
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
 * Created by shengchaojie on 2017/6/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@SpringBootTest(classes = Application.class)
public class CrawlInfoTest {

    @Resource
    private CrawlInfoService crawlInfoService;

    @Test
    @Rollback(false)
    public void testAdd(){
        CrawlInfoRO crawlInfoRO =new CrawlInfoRO();
        crawlInfoRO.setJobType(JobTypeEnum.SINGER);
        crawlInfoRO.setValidDuration(30L);
        crawlInfoRO.setDeleted(false);
        crawlInfoRO.setCrawlTime(new Date());
        crawlInfoService.add(crawlInfoRO);
    }

    @Test
    public void testGet(){
        CrawlInfoRO infoRO = crawlInfoService.get(JobTypeEnum.SINGER);
    }

    @Test
    @Rollback(false)
    public void testDelete(){
        crawlInfoService.delete(4L);
    }
}
