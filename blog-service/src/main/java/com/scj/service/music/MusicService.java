package com.scj.service.music;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public interface MusicService {

    void crawlAllSinger();

    /**
     * 这个方法提供可以多线程爬
     */
    void crawlSingerAlbum();

    void crawlSongs();
}
