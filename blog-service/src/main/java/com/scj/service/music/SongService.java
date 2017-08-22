package com.scj.service.music;

import com.scj.dal.ro.music.SongRO;

import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public interface SongService {
    SongRO findById(Long id);

    List<SongRO> findBySingerId(Long singerId);

    List<SongRO> pageAll(int page,int pageSize);

    int add(SongRO songRO);

    int batchAdd(List<SongRO> songROList);

    long count();

    int updateSongCommentCount(Long id,Long count);

    int updateSongDownloadUrl(Long id,String url);

    int updateSongSingerId(Long id,Long singerId);
}
