package com.scj.service.asyntask;

import com.alibaba.fastjson.JSONObject;
import com.scj.common.utils.NetEaseMusicAPI;
import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.SongRO;
import com.scj.service.music.SongService;
import com.scj.service.music.WebPageService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by shengchaojie on 2017/5/23.
 */
@Component
@Scope("prototype")
public class SongTask extends BaseTask{

    private static final Logger logger = LoggerFactory.getLogger(SongTask.class);

    @Resource
    private SongService songService;

    @Resource
    private WebPageService webPageService;

    @Resource
    private MusicTaskScheduler musicTaskScheduler;


    public SongTask() {
    }

    public SongTask(String name) {
        super(name);
    }

    @Override
    @Async("myTaskAsyncPool")
    public void doTask() {
        logger.info("线程{}开始爬取歌手",getName());
        List<AlbumRO> albumROList =null;
        while ((albumROList =musicTaskScheduler.takeAlbumItems())!=null) {
            for (AlbumRO albumRO : albumROList) {
                String albumUrl = albumRO.getAlbumUrl();
                try {
                    Document albumDoc = Jsoup.connect(albumUrl).get();
                    Elements songs = albumDoc.select("ul.f-hide li a");
                    List<SongRO> songROList = new ArrayList<>();
                    for (Element song : songs) {
                        //String url =BASE_URL+song.attr("href");
                        //System.out.println(song.html()+" "+url+" "+crawlAlbumSongCommentCount(url));
                        String songUrl = BASE_URL + song.attr("href");
                        Long songId = Long.parseLong(songUrl.substring(songUrl.indexOf("id=") + 3));
                        if (!songUrl.contains("id=")) {
                            logger.info("歌曲url:{}解析id失败", songUrl);
                            continue;
                        }
                        if (songService.findById(songId) != null) {
                            logger.info("歌曲id:{}已经存在,进行评论数更新", songId);
                            Long count = crawlSongCommentCount(songUrl);
                            if (count > 0) {
                                songService.updateSongCommentCount(songId, count);
                                logger.info("歌曲id:{}评论数更新为{}", songId, count);
                            }
                            continue;
                        }
                        SongRO songRO = new SongRO();
                        songRO.setId(songId);
                        songRO.setAlbumId(albumRO.getId());
                        songRO.setSingerId(albumRO.getSingerId());
                        songRO.setCrawlTime(new Date());
                        songRO.setSongDownloadUrl("");//调用其他接口
                        songRO.setSongUrl(songUrl);
                        songRO.setSongName(song.html());
                        crawlAlbumSongCommentCount(songRO, songUrl);//爬取歌曲评论数 和 封面url
                        songROList.add(songRO);
                    }
                    if (CollectionUtils.isEmpty(songROList)) {
                        continue;
                    }
                    songService.batchAdd(songROList);
                    logger.info("爬取了{}首歌",songROList.size());
                } catch (IOException e) {
                    logger.error("jsoup连接失败，url:{}", albumUrl, e);
                }
            }
        }
        logger.info("线程{}结束爬取歌曲",getName());
    }

    private void crawlAlbumSongCommentCount(SongRO songRO,String songDetailUrl){
        try {
            Document songDetailDoc = Jsoup.connect(songDetailUrl).get();
            //拿封面
            Elements coverElement =songDetailDoc.select("div.u-cover img");
            String imgUrl = coverElement.attr("data-src");
            if(!StringUtils.isEmpty(imgUrl)){
                songRO.setImgUrl(imgUrl);
            }else {
                songRO.setImgUrl("");
            }
            //拿评论 通过接口
            Elements countElement = songDetailDoc.select("#comment-box");
            if(countElement.size()==0){
                songRO.setCommentCount(0L);
                logger.info("歌曲id:{}查询不到评论次数",songRO.getId());
                return;
            }
            String dataTId =countElement.get(0).attr("data-tid");
            String response = NetEaseMusicAPI.sendPostRequest("http://music.163.com/weapi/v1/resource/comments/"+dataTId+"?csrf_token=",
                    NetEaseMusicAPI.encryptedRequest(NetEaseMusicAPI.noLoginJson));
            if(!StringUtils.isEmpty(response)){
                JSONObject jsonObject= (JSONObject) JSONObject.parse(response);
                if(jsonObject!=null&&jsonObject.containsKey("total")){
                    songRO.setCommentCount(Long.parseLong(jsonObject.get("total").toString()));
                }
            }else {
                songRO.setCommentCount(0L);
            }
            //拿评论 通过页面抓取 无效 是通过接口渲染出来的
            /*Elements countElement = songDetailDoc.select("div.u-title.u-title-1 span.sub.s-fc3 span.j-flag");
            String count =countElement.html();
            songRO.setCommentCount(Long.parseLong(count));*/
        } catch (IOException e) {
            logger.error("爬虫框架出现异常",e);
        }
    }

    private Long crawlSongCommentCount(String songDetailUrl){
        try {
            Document songDetailDoc = Jsoup.connect(songDetailUrl).get();
            Elements countElement = songDetailDoc.select("#comment-box");
            if(countElement.size()==0){
                logger.info("歌曲url:{}查询不到评论次数",songDetailUrl);
                return 0L;
            }
            String dataTId =countElement.get(0).attr("data-tid");
            String response = NetEaseMusicAPI.sendPostRequest("http://music.163.com/weapi/v1/resource/comments/"+dataTId+"?csrf_token=",
                    NetEaseMusicAPI.encryptedRequest(NetEaseMusicAPI.noLoginJson));
            if(!StringUtils.isEmpty(response)){
                JSONObject jsonObject= (JSONObject) JSONObject.parse(response);
                if(jsonObject!=null&&jsonObject.containsKey("total")){
                    return Long.parseLong(jsonObject.get("total").toString());
                }
            }
        } catch (IOException e) {
            logger.error("爬虫框架出现异常",e);
        }
        return 0L;
    }
}
