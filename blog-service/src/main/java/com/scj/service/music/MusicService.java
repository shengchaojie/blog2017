package com.scj.service.music;

import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public interface MusicService {

    void crawlCatalog();

    void crawlAllSinger();

    /**
     * 这个方法提供可以多线程爬
     */
    void crawlSingerAlbum();

    void crawlSongs();

    void createPlayListBySingerName(String username,String password,String playlistName,String singerName);

    void createPlayListBySongIds(String username, String password, String playlistName, List<Integer> songIds);
}
