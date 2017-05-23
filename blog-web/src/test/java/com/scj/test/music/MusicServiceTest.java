package com.scj.test.music;

import com.scj.service.music.MusicService;
import com.scj.service.music.SingerService;
import com.scj.web.Application;
import org.jsoup.Jsoup;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@SpringBootTest(classes = Application.class)
public class MusicServiceTest {

    @Resource
    private MusicService musicService;

    @Test
    @Rollback(false)
    public void testCrawlAllSinger() {
        musicService.crawlAllSinger();

        while(true) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testCrawlAlbums(){
        musicService.crawlSingerAlbum();

        while(true) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testCrawlSongs(){
        musicService.crawlSongs();

        while(true) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
