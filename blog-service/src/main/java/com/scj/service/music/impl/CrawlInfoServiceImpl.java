package com.scj.service.music.impl;

import com.scj.common.enums.JobTypeEnum;
import com.scj.dal.mapper.music.CrawlInfoMapper;
import com.scj.dal.ro.music.CrawlInfoRO;
import com.scj.service.music.CrawlInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by shengchaojie on 2017/6/21.
 */
@Service
public class CrawlInfoServiceImpl implements CrawlInfoService{

    @Resource
    private CrawlInfoMapper crawlInfoMapper;

    @Override
    public boolean add(CrawlInfoRO crawlInfoRO) {
        return crawlInfoMapper.insert(crawlInfoRO) >0;
    }

    @Override
    public CrawlInfoRO get(JobTypeEnum jobTypeEnum) {
        Example example =new Example(CrawlInfoRO.class);
        example.createCriteria()
                .andEqualTo("deleted",0)
                .andEqualTo("jobType",jobTypeEnum);
        List<CrawlInfoRO> crawlInfoROList = crawlInfoMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(crawlInfoROList)){
            return null;
        }
        return crawlInfoROList.get(0);
    }

    @Override
    public void delete(Long id) {
        CrawlInfoRO crawlInfoRO =new CrawlInfoRO();
        crawlInfoRO.setId(id);
        crawlInfoRO.setDeleted(true);
        crawlInfoMapper.updateByPrimaryKeySelective(crawlInfoRO);
    }
}
