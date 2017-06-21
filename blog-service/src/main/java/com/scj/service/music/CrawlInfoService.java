package com.scj.service.music;

import com.scj.common.enums.JobTypeEnum;
import com.scj.dal.ro.music.CrawlInfoRO;

import java.util.Date;

/**
 * Created by shengchaojie on 2017/6/21.
 */
public interface CrawlInfoService {
    boolean add(CrawlInfoRO crawlInfoRO);

    CrawlInfoRO get(JobTypeEnum jobTypeEnum);

    void delete(Long id);
}
