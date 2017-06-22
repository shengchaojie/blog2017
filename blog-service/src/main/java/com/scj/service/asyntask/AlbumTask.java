package com.scj.service.asyntask;

import com.scj.common.enums.WebPageEnum;
import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.SingerRO;
import com.scj.dal.ro.music.WebPageRO;
import com.scj.service.event.CrawlEvent;
import com.scj.service.event.CrawlEventType;
import com.scj.service.music.AlbumService;
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
    private WebPageService webPageService;

    @Resource
    private MusicTaskScheduler musicTaskScheduler;

    @Async("myTaskAsyncPool")
    public void doTask() {
        logger.info("线程{}开始爬取专辑信息",getName());
        List<SingerRO> singerROList =null;
        while ((singerROList =musicTaskScheduler.takeSingerItems())!=null) {
            for (SingerRO singerRO : singerROList) {
                if (StringUtils.isEmpty(singerRO.getSingerUrl()) || !singerRO.getSingerUrl().contains("artist")) {
                    continue;
                }
                //缓存 先查看是否保存了这个html
                //可以开定时任务清空这个缓存表
                WebPageRO webPageRO = webPageService.findByIdAndType(singerRO.getId(), 0, WebPageEnum.SINGER);
                Document document = null;
                if (webPageRO != null) {
                    document = Jsoup.parse(webPageRO.getWebpageContent());
                } else {
                    String albumUrl = singerRO.getSingerUrl().replace("artist", "artist/album");
                    try {
                        document = Jsoup.connect(albumUrl)
                                //.proxy("127.0.0.1",1080)
                                .get();
                        webPageRO = new WebPageRO();
                        webPageRO.setWebpageId(singerRO.getId());
                        webPageRO.setWebpageType(WebPageEnum.SINGER);
                        webPageRO.setCrawled(true);
                        webPageRO.setCrawlTime(new Date());
                        webPageRO.setWebpageContent(document.html());
                        webPageService.add(webPageRO);
                    } catch (IOException ex) {
                        logger.error("记录下载webpage错误的url:{}", albumUrl, ex);
                    }
                }

                boolean isHaveNext = true;
                int index = 1;
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
                    webPageRO = webPageService.findByIdAndType(singerRO.getId(), index, WebPageEnum.SINGER);
                    if (webPageRO != null) {
                        document = Jsoup.parse(webPageRO.getWebpageContent());
                    } else {
                        String albumUrl = BASE_URL + nextPageEle.get(0).attr("href");
                        try {
                            document = Jsoup.connect(albumUrl)
                                    //.proxy("127.0.0.1",1080)
                                    .get();
                            webPageRO = new WebPageRO();
                            webPageRO.setWebpageId(singerRO.getId());
                            webPageRO.setWebpageType(WebPageEnum.SINGER);
                            webPageRO.setCrawled(true);
                            webPageRO.setCrawlTime(new Date());
                            webPageRO.setWebpageIndex(index);
                            webPageRO.setWebpageContent(document.html());
                            webPageService.add(webPageRO);
                        } catch (IOException ex) {
                            logger.error("记录下载webpage错误的url:{}", albumUrl, ex);
                        }
                    }
                    index++;
                }
                if(!CollectionUtils.isEmpty(cachedAlbumList)){
                    albumService.batchAdd(cachedAlbumList);
                    //这边不应该直接加入 应该在事件那边做控制 筛选出应该要爬的数据 再加入
                    //musicTaskScheduler.putAlbumItems(cachedAlbumList);
                }
                // TODO: 2017/6/23 需要更新歌手的crawltime
            }
        }
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        logger.info("线程:{}爬取专辑结束,开启爬取歌曲任务",getName());
    }
}
