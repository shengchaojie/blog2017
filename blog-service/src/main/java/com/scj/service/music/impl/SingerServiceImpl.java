package com.scj.service.music.impl;

import com.github.pagehelper.PageHelper;
import com.scj.dal.mapper.music.SingerMapper;
import com.scj.dal.ro.music.SingerRO;
import com.scj.service.music.SingerService;
import com.scj.service.query.SingerQuery;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
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

    @Override
    public SingerRO getSingerNameById(Long id) {
        return singerMapper.selectByPrimaryKey(id);
    }

    @Override
    public int count(SingerQuery singerQuery) {
        Example example = getExample(singerQuery);
        return singerMapper.selectCountByExample(example);
    }

    private Example getExample(SingerQuery singerQuery) {
        Example example =new Example(SingerRO.class);
        Example.Criteria criteria = example.createCriteria();
        if(singerQuery.getStartCrawlTime()!=null){
            criteria.andGreaterThanOrEqualTo("crawlTime",singerQuery.getStartCrawlTime());
        }
        if(singerQuery.getEndCrawlTime()!=null){
            criteria.andLessThan("crawlTime",singerQuery.getEndCrawlTime());
        }
        return example;
    }

    @Override
    public List<SingerRO> pageAll(SingerQuery singerQuery, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        Example example = getExample(singerQuery);
        return singerMapper.selectByExample(example);
    }


    @Override
    public int updateCrawlTime(Long id, Date date) {
        SingerRO singerRO =new SingerRO();
        singerRO.setId(id);
        singerRO.setCrawlTime(date);
        return singerMapper.updateByPrimaryKeySelective(singerRO);
    }

    @Override
    public List<SingerRO> getSingerByName(String name) {
        Example example =new Example(SingerRO.class);
        example.createCriteria().andEqualTo("singerName",name);
        return singerMapper.selectByExample(example);
    }
}
