package com.scj.web.controller;

import com.scj.common.Page;
import com.scj.common.ResponseResult;
import com.scj.common.exception.StatusCode;
import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.SingerRO;
import com.scj.dal.ro.music.SongRO;
import com.scj.service.music.AlbumService;
import com.scj.service.music.SingerService;
import com.scj.service.music.SongService;
import com.scj.service.vo.music.SongVO;
import com.scj.web.query.SongQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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

    @Resource
    private SongService songService;

    @Resource
    private SingerService singerService;

    @Resource
    private AlbumService albumService;

    @RequestMapping(value = "/page",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult<Page<SongVO>> getSongByPage(@RequestBody SongQuery songQuery){
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
                        songVO.setCreateTime(sdf.format(albumRO.getCreateTime()));
                    }
                }
                SingerRO singerRO =singerService.getSingerNameById(songRO.getSingerId());
                songVO.setSingerName(singerRO== null?"":singerRO.getSingerName());
                result.add(songVO);
            }
        }
        return new ResponseResult<>(StatusCode.OK,new Page<>(result,total));
    }
}
