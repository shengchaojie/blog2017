package com.scj.test.music;

import com.google.common.collect.Lists;
import com.scj.common.utils.NetEaseMusicAPI;
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

    @Resource
    private NetEaseMusicAPI netEaseMusicAPI;

    @Test
    public void testLogin(){
        //netEaseMusicAPI.login("13388611621","Scj@1992");
    }

    @Test
    public void testCreatePlaylist(){
        Integer playlistId = netEaseMusicAPI.createPlaylist("15067159492","zhanghao0","测试周杰伦");
        netEaseMusicAPI.addSongToPlayList("15067159492","zhanghao0", Lists.newArrayList(185613,185614,185615,185616,185617,185618,185619,185620,185621,185622,185623,185625,185627,185629,185631,185633,185635,185637,185639,185641,185643,185644,185645,185646,185647,185649,185651,185653,185655,185657,185658,185659,185660,185662,185664,185666,185667,185668,185670,185672,185674,185676,185678,185680,185684,185686,185692,185694,185696,185697,185698,185699,185701,185703,185705,185707,185709,185714,185716,185718,185719,185720,185721,185722,185723,185725,185727,185729,185731,185732,185733,185734,185736,185738,185740,185742,185743,185744,185807,185809,185811,185813,185815,185817,185818,185819,185820,185821,185827,185829,185831,185833,185835,185837,185838,185839,185840,185842,185844,185846,185848,185850,185852,185854,185855,185856,185857,185859,185861,185863,185865,185867,185868,185872,185874,185878,185879,185880,185882,185884,185886,185888,185890,185892,185894,185899,185901,185904,185905,185906,185908,185910,185912,185914,185916,185918,185920,185922,185924,185929,185930,185931,185932,185934,185936,185938),playlistId);
    }

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

    @Test
    @Rollback(false)
    public void testStartJob(){
        musicService.crawlCatalog();

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
