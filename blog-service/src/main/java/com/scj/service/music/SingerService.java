package com.scj.service.music;

import com.scj.dal.ro.music.SingerRO;
import com.scj.service.query.SingerQuery;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public interface SingerService {

    SingerRO findById(Long id);

    List<SingerRO> findAll();

    List<SingerRO> pageAll(int page,int pageSize);

    int add(SingerRO singerRO);

    int batchAdd(List<SingerRO> singerROS);

    int count();

    SingerRO getSingerNameById(Long id);

    int count(SingerQuery singerQuery);

    List<SingerRO> pageAll(SingerQuery singerQuery,int page,int pageSize);

    int updateCrawlTime(Long id, Date date);

}
