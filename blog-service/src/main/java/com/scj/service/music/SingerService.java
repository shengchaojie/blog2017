package com.scj.service.music;

import com.scj.dal.ro.music.SingerRO;

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
}
