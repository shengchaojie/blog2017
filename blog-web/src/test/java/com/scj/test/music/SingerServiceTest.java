package com.scj.test.music;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.scj.dal.ro.music.SingerRO;
import com.scj.service.music.SingerService;
import com.scj.service.music.SongService;
import com.scj.service.query.SingerQuery;
import com.scj.web.Application;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@SpringBootTest(classes = Application.class)
public class SingerServiceTest {

    @Resource
    private SingerService singerService;

    @Resource
    private SongService songService;

    @Value("${test.field}")
    private String testField;

    @Test
    @Rollback(false)
    public void testAdd(){
        //System.out.println(System.getProperty("file.encoding"));

        //for (int i =1;i<100;i++){
            SingerRO singerRO =new SingerRO();
            singerRO.setId(1234568L);
            singerRO.setSingerName("盛超杰");
            singerRO.setFirstLetter("S");
            singerRO.setImgUrl(null);
            singerRO.setCrawlTime(new Date());
            singerRO.setSingerUrl("singerurl");
            singerService.batchAdd(Lists.newArrayList(singerRO));
        //}

        //Assert.assertEquals(1,singerService.add(singerRO));
    }

    @Test
    public void testFindById(){
        Assert.assertNotNull(singerService.findById(123456L));
    }

    @Test
    public void testSelectAllAndPage(){
        PageHelper.startPage(2,10);
        List<SingerRO> singerROs =singerService.findAll();
        System.out.println(JSON.toJSONString(singerROs));
    }

    @Test
    public void testCount(){
        System.out.println(singerService.count());
    }


    @Test
    public void testSetCommentCount(){
        songService.updateSongCommentCount(59867L,111L);
    }

    @Test
    public void testCountByExample(){
        SingerQuery singerQuery =new SingerQuery();
        singerQuery.setStartCrawlTime(DateTime.parse("2017-05-23 21:16:05", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
        System.out.println(singerService.count(singerQuery));
    }

    @Test
    public void testProperties(){
        System.out.println(testField);
    }

}
