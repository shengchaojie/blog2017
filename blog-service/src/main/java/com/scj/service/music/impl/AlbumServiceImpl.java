package com.scj.service.music.impl;

import com.github.pagehelper.PageHelper;
import com.scj.dal.mapper.music.AlbumMapper;
import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.SingerRO;
import com.scj.service.music.AlbumService;
import com.scj.service.query.AlbumQuery;
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
public class AlbumServiceImpl implements AlbumService{

    @Resource
    private AlbumMapper albumMapper;

    @Override
    public int add(AlbumRO albumRO) {
        return albumMapper.insert(albumRO);
    }

    @Override
    public int batchAdd(List<AlbumRO> albumROList) {
        return albumMapper.insertListWithId(albumROList);
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

    @Override
    public long count() {
        return albumMapper.selectCount(new AlbumRO());
    }

    @Override
    public AlbumRO getAlbumById(Long id) {
        return albumMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateCrawlTime(Long id, Date crawlTime) {
        AlbumRO albumRO =new AlbumRO();
        albumRO.setId(id);
        albumRO.setCrawlTime(crawlTime);
        return albumMapper.updateByPrimaryKeySelective(albumRO);
    }

    @Override
    public long count(AlbumQuery albumQuery) {
        return albumMapper.selectCountByExample(getExample(albumQuery));
    }

    @Override
    public List<AlbumRO> pageAll(int page, int pageSize, AlbumQuery albumQuery) {
        PageHelper.startPage(page,pageSize);
        return albumMapper.selectByExample(getExample(albumQuery));
    }

    private Example getExample(AlbumQuery albumQuery) {
        Example example =new Example(AlbumRO.class);
        Example.Criteria criteria = example.createCriteria();
        if(albumQuery.getStartCrawlTime()!=null){
            criteria.andGreaterThanOrEqualTo("crawlTime",albumQuery.getStartCrawlTime());
        }
        if(albumQuery.getEndCrawlTime()!=null){
            criteria.andLessThan("crawlTime",albumQuery.getEndCrawlTime());
        }
        return example;
    }
}
