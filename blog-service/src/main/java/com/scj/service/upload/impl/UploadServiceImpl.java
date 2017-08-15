package com.scj.service.upload.impl;

import com.alibaba.druid.sql.PagerUtils;
import com.github.pagehelper.PageHelper;
import com.scj.dal.mapper.upload.UploadInfoMapper;
import com.scj.dal.ro.upload.UploadInfoRo;
import com.scj.service.upload.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by shengchaojie on 2017/8/15.
 */
@Service
public class UploadServiceImpl implements UploadService{

    @Resource
    private UploadInfoMapper uploadInfoMapper;

    @Override
    public void addUploadInfo(UploadInfoRo uploadInfoRo) {
        uploadInfoMapper.insert(uploadInfoRo);
    }

    @Override
    public List<UploadInfoRo> getUploadInfo(int page, int pageSize, String description) {
        PageHelper.startPage(page,pageSize);
        Example example =new Example(UploadInfoRo.class);
        if(StringUtils.isNotEmpty(description)){
            example.createCriteria().andLike("description",description);
        }
        return uploadInfoMapper.selectByExample(example);
    }

    @Override
    public long count(String description) {
        Example example =new Example(UploadInfoRo.class);
        if(StringUtils.isNotEmpty(description)){
            example.createCriteria().andLike("description",description);
        }
        return uploadInfoMapper.selectCountByExample(example);
    }
}
