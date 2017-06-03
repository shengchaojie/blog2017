package com.scj.service.event;

import com.scj.service.asyntask.AlbumTask;
import com.scj.service.asyntask.SingerTask;
import com.scj.service.asyntask.SongTask;
import com.scj.service.music.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/6/4 0004.
 */
@Component
public class CrawlEventListener implements ApplicationListener<CrawlEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CrawlEventListener.class);

    @Resource
    private MusicService musicService;

    @Override
    public void onApplicationEvent(CrawlEvent crawlEvent) {
        switch (crawlEvent.getCrawlEventType()){
            case START_CRAWL_SINGER:
                logger.info("接收到事件,开启爬歌手任务");
                musicService.crawlAllSinger();
                break;
            case START_CRAWL_ALBUM:
                logger.info("接收到事件,开启爬专辑任务");
                musicService.crawlSingerAlbum();
                break;
            case START_CRAWL_SONG:
                logger.info("接收到事件,开启爬歌曲任务");
                musicService.crawlSongs();
                break;
            default:
                break;
        }
    }
}
