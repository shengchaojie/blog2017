package com.scj.service.music.impl;

import com.scj.common.enums.JobTypeEnum;
import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.CrawlInfoRO;
import com.scj.dal.ro.music.SingerRO;
import com.scj.service.asyntask.*;
import com.scj.service.event.CrawlEvent;
import com.scj.service.event.CrawlEventType;
import com.scj.service.music.AlbumService;
import com.scj.service.music.CrawlInfoService;
import com.scj.service.music.MusicService;
import com.scj.service.music.SingerService;
import com.scj.service.query.AlbumQuery;
import com.scj.service.query.SingerQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Service
public class MusicServiceImpl implements MusicService,ApplicationContextAware{

    private static final Logger logger = LoggerFactory.getLogger(MusicServiceImpl.class);

    private ApplicationContext applicationContext;

    @Resource
    private CrawlInfoService crawlInfoService;

    @Resource
    private SingerService singerService;

    @Resource
    private MusicTaskScheduler musicTaskScheduler;

    @Resource
    private AlbumService albumService;

    @Value("${music.task.singer.thread}")
    private Integer singerTaskThreadSize;

    @Value("${music.task.singer.shard}")
    private Integer singerTaskShardSize;

    @Value("${music.task.album.thread}")
    private Integer albumTaskThreadSize;

    @Value("${music.task.album.shard}")
    private Integer albumTaskShardSize;

    @Override
    public void crawlCatalog() {
        CrawlInfoRO crawlInfoRO =crawlInfoService.get(JobTypeEnum.SINGER);
        if(isNeedAllCrawled(crawlInfoRO)){
            CountDownLatch countDownLatch =new CountDownLatch(1);
            CatalogTask catalogTask =applicationContext.getBean(CatalogTask.class,countDownLatch);
            catalogTask.doTask();
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        applicationContext.publishEvent(new CrawlEvent(CrawlEventType.START_CRAWL_SINGER));
    }

    @Override
    public void crawlAllSinger() {
        //判断是否需要进行歌手的爬取
        //这边都是全量的
        CrawlInfoRO crawlInfoRO =crawlInfoService.get(JobTypeEnum.SINGER);
        if(isNeedAllCrawled(crawlInfoRO)){
            //如果是过期的话 删除过期的
            if(crawlInfoRO!=null){
                crawlInfoService.delete(crawlInfoRO.getId());
            }
            crawlInfoService.add(JobTypeEnum.SINGER,new Date(),30L);
            CyclicBarrier cyclicBarrier =new CyclicBarrier(singerTaskThreadSize+1);
            for(int i =0;i<singerTaskThreadSize;i++){
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
        }
        applicationContext.publishEvent(new CrawlEvent(CrawlEventType.START_CRAWL_ALBUM));
    }

    public void startJob(){
        logger.info("定时任务开始执行...");
        crawlAllSinger();
    }



    @Override
    public void crawlSingerAlbum() {
        //这里需要对上次的爬取事件进行判断，如果超过间隔，重新爬
        CrawlInfoRO crawlInfoRO =crawlInfoService.get(JobTypeEnum.SINGER);
        if(isNeedAllCrawled(crawlInfoRO)){
            //把需要重新爬取的数据加入
            long count =singerService.count();
            if(count>0){
                long size =(count+singerTaskShardSize-1)/singerTaskShardSize;
                for (int i=0;i<size;i++){
                    List<SingerRO> singerROList =singerService.pageAll(i+1,1000);
                    musicTaskScheduler.putSingerItems(singerROList);
                }
            }
        }else{
            //扫描需要爬取的数据 时间小于当前爬取任务时间
            // 加入到scheduler
            SingerQuery singerQuery =new SingerQuery();
            singerQuery.setEndCrawlTime(crawlInfoRO.getCrawlTime());
            long count =singerService.count(singerQuery);
            if(count>0){
                long size =(count+singerTaskShardSize-1)/singerTaskShardSize;
                for(int i=0;i<size;i++){
                    musicTaskScheduler.putSingerItems(singerService.pageAll(singerQuery,i+1,singerTaskShardSize));
                }
            }
        }
        CyclicBarrier cyclicBarrier =new CyclicBarrier(singerTaskThreadSize+1);
        for(int i =0;i<singerTaskThreadSize;i++){
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
        CrawlInfoRO crawlInfoRO =crawlInfoService.get(JobTypeEnum.ALBUM);
        if(isNeedAllCrawled(crawlInfoRO)){
            if(crawlInfoRO!=null){
                crawlInfoService.delete(crawlInfoRO.getId());
            }
            crawlInfoService.add(JobTypeEnum.ALBUM,new Date(),30L);
            //把需要重新爬取的数据加入
            long count =albumService.count();
            if(count>0){
                long size =(count+albumTaskShardSize-1)/albumTaskShardSize;
                for (int i=0;i<size;i++){
                    List<AlbumRO> albumROList =albumService.pageAll(i+1,1000);
                    musicTaskScheduler.putAlbumItems(albumROList);
                }
            }
        }else{
            //扫描需要爬取的数据 时间小于当前爬取任务时间
            // 加入到scheduler
            AlbumQuery albumQuery =new AlbumQuery();
            albumQuery.setEndCrawlTime(crawlInfoRO.getCrawlTime());
            long count =albumService.count(albumQuery);
            if(count>0){
                long size =(count+albumTaskShardSize-1)/albumTaskShardSize;
                for(int i=0;i<size;i++){
                    musicTaskScheduler.putAlbumItems(albumService.pageAll(i+1,albumTaskShardSize,albumQuery));
                }
            }
        }
        for(int i =0;i<albumTaskThreadSize;i++){
            SongTask songTask =applicationContext.getBean(SongTask.class,"SongTask"+i);
            songTask.doTask();
        }
    }

    private boolean isNeedAllCrawled(CrawlInfoRO crawlInfoRO) {
        boolean isNeedAllCrawled =false;
        if(crawlInfoRO==null){
            isNeedAllCrawled =true;
        }else{
            long endTime =crawlInfoRO.getCrawlTime().getTime()+crawlInfoRO.getValidDuration()*24*60*60*1000;
            //当前时间已经超过数据的有效时间
            if(endTime-new Date().getTime()<0){
                isNeedAllCrawled =true;
            }
        }
        return isNeedAllCrawled;
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
