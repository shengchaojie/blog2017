package com.scj.service.music.impl;

import com.github.pagehelper.PageHelper;
import com.scj.dal.mapper.music.SingerMapper;
import com.scj.dal.ro.music.SingerRO;
import com.scj.service.music.SingerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Service
public class SingerServiceImpl implements SingerService{

    @Resource
    private SingerMapper singerMapper;

    @Override
    public SingerRO findById(Long id) {
        return singerMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SingerRO> findAll() {
        return singerMapper.selectAll();
    }

    @Override
    public List<SingerRO> pageAll(int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return singerMapper.selectAll();
    }

    @Override
    public int add(SingerRO singerRO) {
         return singerMapper.insertSelective(singerRO);
    }

    @Override
    public int batchAdd(List<SingerRO> singerROS) {
        return singerMapper.insertListWithId(singerROS);
    }

    @Override
    public int count() {
        return singerMapper.selectCount(new SingerRO());
    }

}
