package com.scj.service.asyntask;

import com.scj.dal.ro.music.SingerRO;
import com.scj.service.event.CrawlEvent;
import com.scj.service.event.CrawlEventType;
import com.scj.service.music.SingerService;
import com.scj.service.music.impl.MusicServiceImpl;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
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
 * Created by shengchaojie on 2017/5/23.
 */
@Component
@Scope("prototype")
public class SingerTask extends BaseTask{

    private static final Logger logger = LoggerFactory.getLogger(SingerTask.class);

    @Resource
    private SingerService singerService;

    @Resource
    private MusicTaskScheduler musicTaskScheduler;

    private CyclicBarrier barrier;

    public SingerTask() {
    }

    public SingerTask(String name,CyclicBarrier barrier) {
        super(name);
        this.barrier=barrier;
    }

    @Override
    @Async("myTaskAsyncPool")
    public void doTask() {
        logger.info("线程:{},爬取歌手任务开始",getName());
        List<MusicServiceImpl.CatalogItem> catalogItems = null;
        while ((catalogItems = musicTaskScheduler.takeCatalogItems()) != null) {
            for (MusicServiceImpl.CatalogItem catalogItem : catalogItems) {
                try {
                    Document itemDoc = org.jsoup.Jsoup.connect(catalogItem.getUrl()).get();
                    Elements singerItems = itemDoc.select("ul#m-artist-box li a.nm.nm-icn");
                    List<SingerRO> singerROS = new ArrayList<>();
                    for (Element singerItem : singerItems) {
                        String url = singerItem.attr("href").trim();
                        String singerId = null;
                        if (url.contains("id=")) {
                            singerId = url.substring(url.indexOf("id=") + 3);
                        } else {
                            logger.info("解析歌手id出现错误,url:{}", url);
                            continue;
                        }
                        String singerName = singerItem.html();
                        //插入前判断是否存在
                        if (singerService.findById(Long.parseLong(singerId)) != null) {
                            logger.info("歌手id:{}已经存在于数据库", singerId);
                            //这边的数据很固定 不做更新处理
                            continue;
                        }

                        //插入数据到数据库
                        SingerRO singerRO = new SingerRO();
                        singerRO.setId(Long.parseLong(singerId));
                        singerRO.setSingerName(singerName);
                        singerRO.setSingerUrl(BASE_URL + url);
                        singerRO.setFirstLetter(catalogItem.getFirstLetter());
                        singerRO.setCrawlTime(new Date());
                        singerROS.add(singerRO);
                    }
                    if (!CollectionUtils.isEmpty(singerROS)) {
                        singerService.batchAdd(singerROS);
                        logger.info("爬取{}首歌手",singerROS.size());
                    }
                    Thread.sleep(sleepTime);
                } catch (IOException ex) {
                    logger.error("爬取歌手出现异常,url:{}", catalogItem.getUrl(), ex);
                } catch (InterruptedException e) {
                    logger.error("爬取歌手线程休眠被打断",e);
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
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        logger.info("线程:{}爬取歌手任务结束,开启爬专辑任务",getName());
    }

}
