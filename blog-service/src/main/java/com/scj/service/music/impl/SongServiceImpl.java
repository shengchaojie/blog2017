package com.scj.service.music.impl;

import com.github.pagehelper.PageHelper;
import com.scj.dal.mapper.music.SongMapper;
import com.scj.dal.ro.music.SongRO;
import com.scj.service.music.SongService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Service
public class SongServiceImpl implements SongService{

    @Resource
    private SongMapper songMapper;

    @Override
    public SongRO findById(Long id) {
        return songMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SongRO> pageAll(int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return songMapper.selectAll();
    }

    @Override
    public int add(SongRO songRO) {
        return songMapper.insert(songRO);
    }

    @Override
    public int batchAdd(List<SongRO> songROList) {
        return songMapper.insertListWithId(songROList);
    }
}
