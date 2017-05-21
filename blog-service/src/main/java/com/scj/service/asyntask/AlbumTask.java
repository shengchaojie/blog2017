package com.scj.service.asyntask;

import com.scj.common.enums.WebPageEnum;
import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.SingerRO;
import com.scj.dal.ro.music.WebPageRO;
import com.scj.service.music.AlbumService;
import com.scj.service.music.WebPageService;
import org.apache.commons.lang3.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Component
@Scope("prototype")
public class AlbumTask {

    private static final Logger logger = LoggerFactory.getLogger(AlbumTask.class);

    private static final String BASE_URL = "http://music.163.com";


    private List<SingerRO> singerROs;

    private String name;

    public AlbumTask() {
    }

    public AlbumTask(List<SingerRO> singerROs) {
        this.singerROs = singerROs;
    }

    @Resource
    private AlbumService albumService;

    @Resource
    private WebPageService webPageService;

    @Async("myTaskAsyncPool")
    public void doTask() {
        logger.info(Thread.currentThread().getName()+getName());
        //System.out.println(Thread.currentThread().getName()+getName());
        for (SingerRO singerRO:singerROs){
            try {
                if(StringUtils.isEmpty(singerRO.getSingerUrl())||!singerRO.getSingerUrl().contains("artist")){
                    continue;
                }
                //缓存 先查看是否保存了这个html
                //后期可以做本地缓存 或者 其他缓存
                WebPageRO webPageRO =webPageService.findByIdAndType(singerRO.getId(),0, WebPageEnum.SINGER);
                Document document =null;
                if(webPageRO!=null){
                    document =Jsoup.parse(webPageRO.getWebpageContent());
                }else {
                    String albumUrl =singerRO.getSingerUrl().replace("artist","artist/album");
                    document = Jsoup.connect(albumUrl)
                            //.proxy("127.0.0.1",1080)
                            .get();
                    webPageRO =new WebPageRO();
                    webPageRO.setWebpageId(singerRO.getId());
                    webPageRO.setWebpageType(WebPageEnum.SINGER);
                    webPageRO.setCrawled(true);
                    webPageRO.setCrawlTime(new Date());
                    webPageRO.setWebpageContent(document.html());
                    webPageService.add(webPageRO);
                }
                /*try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                boolean isHaveNext =true;
                int index =1;
                while(isHaveNext){
                    Elements currentAlbums =document.select("ul#m-song-module>li");
                    for (Element album :currentAlbums){
                        //System.out.println(album.attr("title")+" "+BASE_URL+album.select("a.msk").attr("href"));
                        String url =BASE_URL+album.select("div>a.msk").attr("href");
                        String imgUrl =album.select("div>img").attr("src");
                        if(!url.contains("id=")){
                            continue;
                        }
                        String albumId =url.substring(url.indexOf("id=")+3);
                        if(albumService.findById(Long.parseLong(albumId))!=null){
                            continue;
                        }
                        AlbumRO albumRO =new AlbumRO();
                        albumRO.setId(Long.parseLong(albumId));
                        albumRO.setAlbumName(album.select("div").attr("title"));
                        albumRO.setAlbumUrl(url);
                        albumRO.setSingerId(singerRO.getId());
                        albumRO.setImgUrl(imgUrl);
                        albumRO.setCrawlTime(new Date());
                        String createTimeString =album.select("p>span.s-fc3").html();
                        albumRO.setCreateTime(DateTime.parse(createTimeString, DateTimeFormat.forPattern("yyyy.MM.dd")).toDate());
                        albumService.add(albumRO);

                    }
                    Elements nextPageEle =document.select("div.u-page>.znxt");
                    isHaveNext = nextPageEle.size() > 0 && (!nextPageEle.get(0).attr("href").contains("javascript"));
                    if(!isHaveNext){
                        break;
                    }
                    webPageRO =webPageService.findByIdAndType(singerRO.getId(),index, WebPageEnum.SINGER);
                    if(webPageRO!=null){
                        document =Jsoup.parse(webPageRO.getWebpageContent());
                    }else {
                        String albumUrl =BASE_URL+nextPageEle.get(0).attr("href");
                        document = Jsoup.connect(albumUrl)
                                //.proxy("127.0.0.1",1080)
                                .get();
                        webPageRO =new WebPageRO();
                        webPageRO.setWebpageId(singerRO.getId());
                        webPageRO.setWebpageType(WebPageEnum.SINGER);
                        webPageRO.setCrawled(true);
                        webPageRO.setCrawlTime(new Date());
                        webPageRO.setWebpageIndex(index);
                        webPageRO.setWebpageContent(document.html());
                        webPageService.add(webPageRO);
                    }
                    index++;
                    /*try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }

            }catch (IOException ex){
                logger.error("获取歌手专辑io出现异常,休息半小时",ex);
                try {
                    Thread.sleep(1800000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<SingerRO> getSingerROs() {
        return singerROs;
    }

    public void setSingerROs(List<SingerRO> singerROs) {
        this.singerROs = singerROs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
