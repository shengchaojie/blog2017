package com.scj.service.music.impl;

import com.github.pagehelper.PageHelper;
import com.scj.dal.mapper.music.AlbumMapper;
import com.scj.dal.ro.music.AlbumRO;
import com.scj.service.music.AlbumService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Service
public class AlbumServiceImpl implements AlbumService{

    @Resource
    private AlbumMapper albumMapper;

    @Override
    public int add(AlbumRO albumRO) {
        return albumMapper.insert(albumRO);
    }

    @Override
    public List<AlbumRO> findAll() {
        return albumMapper.selectAll();
    }

    @Override
    public List<AlbumRO> pageAll(int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return albumMapper.selectAll();
    }

    @Override
    public AlbumRO findById(Long id) {
        return albumMapper.selectByPrimaryKey(id);
    }
}
