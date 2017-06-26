package com.scj.service.asyntask;

import com.google.common.collect.ImmutableList;
import com.scj.service.event.CrawlEvent;
import com.scj.service.event.CrawlEventType;
import com.scj.service.music.impl.MusicServiceImpl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by shengchaojie on 2017/6/22.
 */
@Component
@Scope("prototype")
public class  CatalogTask extends BaseTask{

    private static final String SINGER_CATALOG_URL="http://music.163.com/discover/artist";

    private static final Integer PAGE_SIZE =2;

    private static final Logger logger = LoggerFactory.getLogger(CatalogTask.class);

    private CountDownLatch countDownLatch;

    public CatalogTask() {
    }

    public CatalogTask(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Resource
    private MusicTaskScheduler musicTaskScheduler;

    @Resource
    private ApplicationContext applicationContext;

    @Override
    @Async("myTaskAsyncPool")
    public void doTask() {
        try {
            //爬取所有排行榜
            Document artistCatalogDoc = Jsoup.connect(SINGER_CATALOG_URL).get();
            Elements catalogs = artistCatalogDoc.select("div.blk .cat-flag");
            Map<String,String> catalogMap =new HashMap<>();
            for (Element cat : catalogs) {
                catalogMap.put(cat.html(),BASE_URL+cat.attr("href"));
                logger.info(cat.html() + " " + cat.attr("href"));
            }
            List<MusicServiceImpl.CatalogItem> catalogItems =new ArrayList<>();
            for(Map.Entry<String,String> entry:catalogMap.entrySet()) {
                String catalogUrl = entry.getValue();
                //爬对应排行下面的自分类 热门 A B C D..
                Document itemDoc = org.jsoup.Jsoup.connect(catalogUrl).get();
                Elements items =itemDoc.select("ul#initial-selector>li>a");
                for(Element item :items){
                    if("热门".equals(item.html())){
                        continue;
                    }
                    catalogItems.add(new MusicServiceImpl.CatalogItem(item.html(),BASE_URL+item.attr("href")));
                    //每PAGE_SIZE个catalog为一个任务
                    if(catalogItems.size()>=PAGE_SIZE){
                        musicTaskScheduler.putCatalogItems(ImmutableList.copyOf(catalogItems));
                        catalogItems.clear();
                    }
                }
                if(debug){
                    break;
                }
            }
            //这边开启爬取歌手的事件
            //applicationContext.publishEvent(new CrawlEvent(CrawlEventType.START_CRAWL_SINGER));
        }catch (IOException ex){
            logger.error("爬取歌手清单出现异常",ex);
        }
        countDownLatch.countDown();
    }
}
