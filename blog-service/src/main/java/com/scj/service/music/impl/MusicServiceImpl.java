package com.scj.service.music.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.scj.common.threadpool.TaskExecutePool;
import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.SingerRO;
import com.scj.service.asyntask.AlbumTask;
import com.scj.service.asyntask.SingerTask;
import com.scj.service.asyntask.SongTask;
import com.scj.service.event.CrawlEvent;
import com.scj.service.event.CrawlEventType;
import com.scj.service.music.AlbumService;
import com.scj.service.music.MusicService;
import com.scj.service.music.SingerService;
import com.scj.service.music.WebPageService;
import com.scj.service.user.UserService;
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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Service
public class MusicServiceImpl implements MusicService,ApplicationContextAware{

    private static final Logger logger = LoggerFactory.getLogger(MusicServiceImpl.class);

    private static final String BASE_URL = "http://music.163.com";
    private static final String SINGER_CATALOG_URL="http://music.163.com/discover/artist";

    private static final Integer PAGE_SIZE =1000;

    @Resource
    private SingerService singerService;

    private ApplicationContext applicationContext;

    @Resource
    private AlbumService albumService;

    @Override
    public void crawlAllSinger() {
        try {
            //爬取所有排行榜
            Document artistCatalogDoc = Jsoup.connect(SINGER_CATALOG_URL).get();
            Elements catalogs = artistCatalogDoc.select("div.blk .cat-flag");
            Map<String,String> catalogMap =new HashMap<>();
            for (Element cat : catalogs) {
                catalogMap.put(cat.html(),BASE_URL+cat.attr("href"));
                logger.info(cat.html() + " " + cat.attr("href"));
            }
            List<CatalogItem> catalogItems =new ArrayList<>();
            int i =1;
            for(Map.Entry<String,String> entry:catalogMap.entrySet()) {
                String catalogUrl = entry.getValue();
                //爬对应排行下面的自分类 热门 A B C D..
                Document itemDoc = org.jsoup.Jsoup.connect(catalogUrl).get();
                Elements items =itemDoc.select("ul#initial-selector>li>a");
                for(Element item :items){
                    if("热门".equals(item.html())){
                        continue;
                    }
                    catalogItems.add(new CatalogItem(item.html(),BASE_URL+item.attr("href")));
                    if(catalogItems.size()>=PAGE_SIZE){
                        SingerTask singerTask =applicationContext.getBean(SingerTask.class);
                        singerTask.setName("singer"+i++);
                        singerTask.setCatalogItems(ImmutableList.copyOf(catalogItems));
                        singerTask.doTask();
                        catalogItems.clear();
                    }
                }
            }
            //开启单线程爬取
            if(!CollectionUtils.isEmpty(catalogItems)){
                logger.info("开始启动线程,一共有{}个类别",catalogItems.size());
                SingerTask singerTask =applicationContext.getBean(SingerTask.class);
                singerTask.setName("singer");
                singerTask.setCatalogItems(catalogItems);
                singerTask.doTask();
            }
        }catch (IOException ex){
            logger.error("爬取歌手清单出现异常",ex);
        }

    }

    @Scheduled(cron = "0 12 2 4 * ? ")
    public void startJob(){
        logger.info("定时任务开始执行...");
        crawlAllSinger();
    }

    @Override
    public void crawlSingerAlbum() {
        int total =singerService.count();
        if(total>0){
            for(int i =0;i<total/PAGE_SIZE+1;i++){
                List<SingerRO> singerROs =singerService.pageAll(i+1,PAGE_SIZE);
                /*albumTask.setSingerROs(singerROs);
                albumTask.doTask();*/
                AlbumTask task = applicationContext.getBean(AlbumTask.class);
                task.setName("page"+(i+1));
                task.setSingerROs(singerROs);
                task.doTask();
            }
            //理论上线程池有先来后到，上面的运行完了，才会执行这个线程
            applicationContext.publishEvent(new CrawlEvent(CrawlEventType.START_CRAWL_SONG));
        }

    }

    @Override
    public void crawlSongs() {
        long total =albumService.count();
        if(total>0){
            for(int i =0;i<total/PAGE_SIZE+1;i++){
                List<AlbumRO> albumROS =albumService.pageAll(i+1,PAGE_SIZE);
                SongTask songTask =applicationContext.getBean(SongTask.class);
                songTask.setName("page"+(i+1));
                songTask.setAlbumROList(albumROS);
                songTask.doTask();
            }
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
