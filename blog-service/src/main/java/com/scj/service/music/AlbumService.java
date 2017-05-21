package com.scj.service.music;

import com.scj.dal.ro.music.AlbumRO;

import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public interface AlbumService {
    int add(AlbumRO albumRO);

    List<AlbumRO> findAll();

    List<AlbumRO> pageAll(int page,int pageSize);

    AlbumRO findById(Long id);
}
