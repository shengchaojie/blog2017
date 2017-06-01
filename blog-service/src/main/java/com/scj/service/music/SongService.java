package com.scj.service.music;

import com.scj.dal.ro.music.SongRO;

import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public interface SongService {
    SongRO findById(Long id);

    List<SongRO> pageAll(int page,int pageSize);

    int add(SongRO songRO);

    int batchAdd(List<SongRO> songROList);

    long count();
}
