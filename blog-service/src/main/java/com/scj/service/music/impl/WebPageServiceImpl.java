package com.scj.service.music.impl;

import com.scj.common.enums.WebPageEnum;
import com.scj.dal.mapper.music.WebPageMapper;
import com.scj.dal.ro.music.WebPageRO;
import com.scj.service.music.WebPageService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Service
public class WebPageServiceImpl implements WebPageService{

    @Resource
    private WebPageMapper webPageMapper;

    @Override
    public WebPageRO findByIdAndType(Long id, Integer index, WebPageEnum webPageType) {
        Example example =new Example(WebPageRO.class);
        example.createCriteria().andEqualTo("webpageId",id)
                .andEqualTo("webpageType",webPageType.ordinal())
                .andEqualTo("webpageIndex",index);
        List<WebPageRO> webPageROs = webPageMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(webPageROs)){
            return null;
        }
        return webPageROs.get(0);
    }

    @Override
    public int add(WebPageRO webPageRO) {
        return webPageMapper.insert(webPageRO);
    }
}
