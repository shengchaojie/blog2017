package com.scj.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.scj.common.Page;
import com.scj.common.ResponseResult;
import com.scj.common.exception.StatusCode;
import com.scj.common.utils.NetEaseMusicAPI;
import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.SingerRO;
import com.scj.dal.ro.music.SongRO;
import com.scj.service.music.AlbumService;
import com.scj.service.music.SingerService;
import com.scj.service.music.SongService;
import com.scj.service.vo.music.SongVO;
import com.scj.web.query.SongQuery;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shengchaojie on 2017/5/31.
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/music")
public class MusicController {

    private static final Logger logger = LoggerFactory.getLogger(MusicController.class);

    @Resource
    private SongService songService;

    @Resource
    private SingerService singerService;

    @Resource
    private AlbumService albumService;

    @RequestMapping(value = "/page",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult<Page<SongVO>> getSongByPage(@RequestBody SongQuery songQuery) throws UnsupportedEncodingException {
        List<SongVO> result =new ArrayList<>();
        long total =songService.count();
        if(total<=0){
            return new ResponseResult<>(StatusCode.OK,new Page<>(result,0L));
        }
        List<SongRO> songROList =songService.pageAll(songQuery.getPage(),songQuery.getLimit());
        if(!CollectionUtils.isEmpty(songROList)){
            for(SongRO songRO:songROList){
                SongVO songVO =new SongVO();
                BeanUtils.copyProperties(songRO,songVO);
                AlbumRO albumRO =albumService.getAlbumById(songRO.getAlbumId());
                if(albumRO!=null){
                    songVO.setAlbumName(albumRO.getAlbumName());
                    if(albumRO.getCreateTime()!=null){
                        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
                        songVO.setCreateTime(sdf.format(albumRO.getPublishTime()));
                    }
                }
                SingerRO singerRO =singerService.getSingerNameById(songRO.getSingerId());
                songVO.setSingerName(singerRO== null?"":singerRO.getSingerName());
                songVO.setSongName(URLDecoder.decode(songVO.getSongName(),"utf-8"));
                result.add(songVO);
            }
        }
        return new ResponseResult<>(StatusCode.OK,new Page<>(result,total));
    }

    @RequestMapping(value = "/song/count",method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult<Long> getSongCommentCount(@RequestParam("songId")Long songId){
        String response = NetEaseMusicAPI.sendPostRequest("http://music.163.com/weapi/v1/resource/comments/R_SO_4_"+songId+"?csrf_token=",
                NetEaseMusicAPI.encryptedRequest(NetEaseMusicAPI.noLoginJson));
        if(!StringUtils.isEmpty(response)){
            JSONObject jsonObject= (JSONObject) JSONObject.parse(response);
            if(jsonObject!=null&&jsonObject.containsKey("total")){
                Long count =Long.parseLong(jsonObject.get("total").toString());
                songService.updateSongCommentCount(songId,count);
                return new ResponseResult<Long>(StatusCode.OK,count);
            }
        }
        return new ResponseResult<Long>(StatusCode.FAILED);
    }

    @RequestMapping(value = "/song/mp3Url",method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult<String> getSongMp3Url(@RequestParam("songId")Long songId){
        //歌曲url是动态改变的
        /*SongRO songRO =songService.findById(songId);
        if(songRO!=null&&!StringUtils.isEmpty(songRO.getSongDownloadUrl())){
            return new ResponseResult<>(StatusCode.OK,songRO.getSongDownloadUrl());
        }*/
        String url =NetEaseMusicAPI.getSongMp3Url(songId.toString());
        if(!StringUtils.isEmpty(url)){
            /*songService.updateSongDownloadUrl(songId,url);*/
            return new ResponseResult<>(StatusCode.OK,url);
        }
        return new ResponseResult<>(StatusCode.FAILED,"");
    }

    /**
     * 寻找爬取失败的歌手
     * @param songId
     * @return
     */
    @RequestMapping(value = "/song/getLostSinger",method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult<String> getLostSinger(@RequestParam("songId")Long songId){
        SongRO songRO =songService.findById(songId);
        if(songRO!=null&&!StringUtils.isEmpty(songRO.getSongUrl())){
            try {
                Document doc =Jsoup.connect(songRO.getSongUrl()).get();
                Elements singerEle =doc.select(".cnt p.s-fc4 span a");
                String singerUrl =singerEle.attr("href");
                if(!StringUtils.isEmpty(singerUrl)){
                    String singerId =singerUrl.substring(singerUrl.indexOf("id=")+3);
                    songService.updateSongSingerId(songId,Long.parseLong(singerId));
                }
                String singName =singerEle.html();
                if(!StringUtils.isEmpty(singName))
                    return new ResponseResult<>(StatusCode.OK,singName);
            } catch (IOException e) {
                logger.error("jsoup框架调用失败",e);
            }
        }
        return new ResponseResult<>(StatusCode.FAILED,"");
    }

}
