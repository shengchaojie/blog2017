package com.scj.service.music.impl;

import com.scj.dal.ro.music.SingerRO;
import com.scj.service.asyntask.AlbumTask;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private static final Integer THREAD_POOL_SIZE =3;
    private static final Integer PAGE_SIZE =100;

    @Resource
    private SingerService singerService;

    private ApplicationContext applicationContext;

    @Override
    @Transactional
    public void crawlAllSinger() {
        try {
            //爬取所有排行榜
            Document artistCatalogDoc = Jsoup.connect(SINGER_CATALOG_URL).get();
            Elements catalogs = artistCatalogDoc.select("div.blk .cat-flag");
            Map<String,String> catalogMap =new HashMap<>();
            for (Element cat : catalogs) {
                catalogMap.put(cat.html(),BASE_URL+cat.attr("href"));
                System.out.println(cat.html() + " " + cat.attr("href"));
            }
            for(Map.Entry<String,String> entry:catalogMap.entrySet()) {
                String catalogUrl = entry.getValue();
                //爬对应排行下面的自分类 热门 A B C D..
                List<CatalogItem> catalogItems =new ArrayList<>();
                Document itemDoc = org.jsoup.Jsoup.connect(catalogUrl).get();
                Elements items =itemDoc.select("ul#initial-selector>li>a");
                for(Element item :items){
                    catalogItems.add(new CatalogItem(item.html(),BASE_URL+item.attr("href")));
                }
                for (CatalogItem item :catalogItems){
                    if("热门".equals(item.getFirstLetter())){
                        continue;
                    }

                    itemDoc = org.jsoup.Jsoup.connect(item.getUrl()).get();
                    Elements singerItems =itemDoc.select("ul#m-artist-box li a.nm.nm-icn");
                    for (Element singerItem:singerItems){
                        String url =singerItem.attr("href").trim();
                        String singerId =null;
                        if(url.contains("id=")){
                            singerId=url.substring(url.indexOf("id=")+3);
                        }else {
                            continue;
                        }
                        String singerName =singerItem.html();
                        //插入前判断是否存在
                        if(singerService.findById(Long.parseLong(singerId))!=null){
                            logger.info("歌手id:{}已经存在于数据库");
                            //这边的数据很固定 不做更新处理
                            continue;
                        }

                        //插入数据到数据库
                        SingerRO singerRO =new SingerRO();
                        singerRO.setId(Long.parseLong(singerId));
                        singerRO.setSingerName(singerName);
                        singerRO.setSingerUrl(BASE_URL+url);
                        singerRO.setFirstLetter(item.getFirstLetter());
                        singerRO.setCrawlTime(new Date());
                        singerService.add(singerRO);
                    }
                }
            }
        }catch (IOException ex){
            logger.error("爬取歌手清单出现异常",ex);
        }

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
        }

    }

    public static void main(String[] args) {
        System.out.println("/artist?id=562".substring("/artist?id=562".indexOf("id=")+3));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext =applicationContext;
    }

    private static class CatalogItem{

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
