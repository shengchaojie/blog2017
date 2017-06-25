package com.scj.service.asyntask;

import com.scj.common.enums.WebPageEnum;
import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.SingerRO;
import com.scj.dal.ro.music.WebPageRO;
import com.scj.service.event.CrawlEvent;
import com.scj.service.event.CrawlEventType;
import com.scj.service.music.AlbumService;
import com.scj.service.music.SingerService;
import com.scj.service.music.WebPageService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Component
@Scope("prototype")
public class AlbumTask extends BaseTask{

    private static final Logger logger = LoggerFactory.getLogger(AlbumTask.class);
    private static final String BASE_URL = "http://music.163.com";


    private CyclicBarrier barrier;

    public AlbumTask() {
    }

    public AlbumTask(String name ,CyclicBarrier barrier) {
        super(name);
        this.barrier =barrier;
    }

    @Resource
    private AlbumService albumService;

    @Resource
    private MusicTaskScheduler musicTaskScheduler;

    @Resource
    private SingerService singerService;

    @Async("myTaskAsyncPool")
    public void doTask() {
        logger.info("线程{}开始爬取专辑信息",getName());
        List<SingerRO> singerROList =null;
        while ((singerROList =musicTaskScheduler.takeSingerItems())!=null) {
            for (SingerRO singerRO : singerROList) {
                if (StringUtils.isEmpty(singerRO.getSingerUrl()) || !singerRO.getSingerUrl().contains("artist")) {
                    continue;
                }
                String albumUrl = singerRO.getSingerUrl().replace("artist", "artist/album");
                Document document =null;
                try {
                    document= Jsoup.connect(albumUrl).get();
                } catch (IOException e) {
                    logger.error("获取album页面错误,url:{}", albumUrl, e);
                    continue;
                }

                boolean isHaveNext = true;
                List<AlbumRO> cachedAlbumList =new ArrayList<>();
                while (isHaveNext) {
                    Elements currentAlbums = document.select("ul#m-song-module>li");
                    for (Element album : currentAlbums) {
                        String url = BASE_URL + album.select("div>a.msk").attr("href");
                        String imgUrl = album.select("div>img").attr("src");
                        if (!url.contains("id=")) {
                            logger.info("专辑url解析id失败,跳过,url:{}", url);
                            continue;
                        }
                        String albumId = url.substring(url.indexOf("id=") + 3);
                        if (albumService.findById(Long.parseLong(albumId)) != null) {
                            logger.info("专辑id:{}已经存在", albumId);
                            continue;
                        }
                        AlbumRO albumRO = new AlbumRO();
                        albumRO.setId(Long.parseLong(albumId));
                        albumRO.setAlbumName(album.select("div").attr("title"));
                        albumRO.setAlbumUrl(url);
                        albumRO.setSingerId(singerRO.getId());
                        albumRO.setImgUrl(imgUrl);
                        albumRO.setCrawlTime(new Date());
                        String createTimeString = album.select("p>span.s-fc3").html();
                        albumRO.setCreateTime(DateTime.parse(createTimeString, DateTimeFormat.forPattern("yyyy.MM.dd")).toDate());
                        cachedAlbumList.add(albumRO);
                    }
                    Elements nextPageEle = document.select("div.u-page>.znxt");
                    isHaveNext = nextPageEle.size() > 0 && (!nextPageEle.get(0).attr("href").contains("javascript"));
                    if (!isHaveNext) {
                        break;
                    }
                    albumUrl = BASE_URL + nextPageEle.get(0).attr("href");
                    try {
                        document = Jsoup.connect(albumUrl).get();
                    } catch (IOException e) {
                        logger.error("获取album页面错误,url:{}", albumUrl, e);
                        isHaveNext =false;
                        continue;
                    }

                }
                if(!CollectionUtils.isEmpty(cachedAlbumList)){
                    albumService.batchAdd(cachedAlbumList);
                }
                //需要更新歌手的crawltime
                singerService.updateCrawlTime(singerRO.getId(),new Date());
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(debug){
                    break;
                }
            }
            if(debug){
                break;
            }
        }
        try {
            logger.info("等待其他album爬取线程结束");
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        logger.info("线程:{}爬取专辑结束,开启爬取歌曲任务",getName());
    }
}
