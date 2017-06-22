package com.scj.service.music.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.scj.common.threadpool.TaskExecutePool;
import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.SingerRO;
import com.scj.service.asyntask.AlbumTask;
import com.scj.service.asyntask.CatalogTask;
import com.scj.service.asyntask.SingerTask;
import com.scj.service.asyntask.SongTask;
import com.scj.service.event.CrawlEvent;
import com.scj.service.event.CrawlEventType;
import com.scj.service.music.AlbumService;
import com.scj.service.music.MusicService;
import com.scj.service.music.SingerService;
import com.scj.service.music.WebPageService;
import com.scj.service.user.UserService;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.xpath.internal.WhitespaceStrippingElementMatcher;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Service
public class MusicServiceImpl implements MusicService,ApplicationContextAware{

    private static final Logger logger = LoggerFactory.getLogger(MusicServiceImpl.class);

    private static final Integer SINGER_TASK_THREAD_SIZE =3;

    private ApplicationContext applicationContext;

    @Override
    public void crawlCatalog() {
        CatalogTask catalogTask =applicationContext.getBean(CatalogTask.class);
        catalogTask.doTask();
    }

    @Override
    public void crawlAllSinger() {
        //这里基本的思路是 开启多个线程(可以配置)去爬取
        //然后爬取全部完毕后，在触发下一个任务
        CyclicBarrier cyclicBarrier =new CyclicBarrier(SINGER_TASK_THREAD_SIZE+1);
        for(int i =0;i<SINGER_TASK_THREAD_SIZE;i++){
            SingerTask singerTask =applicationContext.getBean(SingerTask.class,"singer"+i,cyclicBarrier);
            singerTask.doTask();
        }
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        applicationContext.publishEvent(new CrawlEvent(CrawlEventType.START_CRAWL_ALBUM));
    }

    public void startJob(){
        logger.info("定时任务开始执行...");
        crawlAllSinger();
    }

    @Override
    public void crawlSingerAlbum() {
        CyclicBarrier cyclicBarrier =new CyclicBarrier(SINGER_TASK_THREAD_SIZE+1);
        for(int i =0;i<SINGER_TASK_THREAD_SIZE;i++){
            AlbumTask task = applicationContext.getBean(AlbumTask.class,"AlbumTask"+i,cyclicBarrier);
            task.doTask();
        }
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        applicationContext.publishEvent(new CrawlEvent(CrawlEventType.START_CRAWL_SONG));
    }

    @Override
    public void crawlSongs() {
        for(int i =0;i<SINGER_TASK_THREAD_SIZE;i++){
            SongTask songTask =applicationContext.getBean(SongTask.class,"SongTask"+i);
            songTask.doTask();
        }
    }

    public static void main(String[] args) {
        System.out.println("/artist?id=562".substring("/artist?id=562".indexOf("id=")+3));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext =applicationContext;
    }

    public static class CatalogItem{

        private String firstLetter;

        private String url;

        public CatalogItem() {
        }

        public CatalogItem(String firstLetter, String url) {
            this.firstLetter = firstLetter;
            this.url = url;
        }

        public String getFirstLetter() {
            return firstLetter;
        }

        public void setFirstLetter(String firstLetter) {
            this.firstLetter = firstLetter;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
