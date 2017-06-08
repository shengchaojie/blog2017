package com.scj.service.music.impl;

import com.github.pagehelper.PageHelper;
import com.scj.dal.mapper.music.SongMapper;
import com.scj.dal.ro.music.SongRO;
import com.scj.service.music.AlbumService;
import com.scj.service.music.SingerService;
import com.scj.service.music.SongService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Service
@CacheConfig(cacheNames = "music")
public class SongServiceImpl implements SongService{

    @Resource
    private SongMapper songMapper;

    @Override
    public SongRO findById(Long id) {
        return songMapper.selectByPrimaryKey(id);
    }

    @Cacheable
    @Override
    public List<SongRO> pageAll(int page, int pageSize) {
        PageHelper.startPage(page,pageSize);

        Example example =new Example(SongRO.class);
        example.setTableName("music_song");
        example.setOrderByClause("comment_count desc");

        return songMapper.selectByExample(example);
    }

    @Override
    public int add(SongRO songRO) {
        return songMapper.insert(songRO);
    }

    @Override
    public int batchAdd(List<SongRO> songROList) {
        return songMapper.insertListWithId(songROList);
    }

    @Override
    public long count() {
        return songMapper.selectCount(null);
    }

    @Override
    public int updateSongCommentCount(Long id,Long count) {
        SongRO songRO =new SongRO();
        songRO.setId(id);
        songRO.setCommentCount(count);
        return songMapper.updateByPrimaryKeySelective(songRO);
    }

    @Override
    public int updateSongDownloadUrl(Long id, String url) {
        SongRO songRO =new SongRO();
        songRO.setId(id);
        songRO.setSongDownloadUrl(url);
        return songMapper.updateByPrimaryKeySelective(songRO);
    }
}
