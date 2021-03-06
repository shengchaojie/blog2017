package com.scj.service.music;

import com.scj.dal.ro.music.AlbumRO;
import com.scj.service.query.AlbumQuery;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public interface AlbumService {
    int add(AlbumRO albumRO);

    int batchAdd(List<AlbumRO> albumROList);

    List<AlbumRO> findAll();

    List<AlbumRO> pageAll(int page,int pageSize);

    AlbumRO findById(Long id);

    long count();

    AlbumRO getAlbumById(Long id);

    int updateCrawlTime(Long id , Date crawlTime);

    long count(AlbumQuery albumQuery);

    List<AlbumRO> pageAll(int page,int pageSize,AlbumQuery albumQuery);
}
