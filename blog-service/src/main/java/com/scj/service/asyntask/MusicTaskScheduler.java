package com.scj.service.asyntask;

import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.SingerRO;
import com.scj.service.music.impl.MusicServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by shengchaojie on 2017/6/22.
 */
@Component
public class MusicTaskScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MusicTaskScheduler.class);

    private BlockingQueue<List<MusicServiceImpl.CatalogItem>> catalogQueue;

    private BlockingQueue<List<SingerRO>> singerQueue;

    private BlockingQueue<List<AlbumRO>> albumQueue;

    //private BlockingQueue<List<SongRO>> songQueue;

    public MusicTaskScheduler() {
        catalogQueue =new LinkedBlockingQueue<>();
        albumQueue =new LinkedBlockingQueue<>();
        singerQueue =new LinkedBlockingQueue<>();
        //songQueue =new LinkedBlockingQueue<>();
    }

    public boolean putCatalogItems(List<MusicServiceImpl.CatalogItem> catalogItemList){
        try {
            catalogQueue.put(catalogItemList);
        } catch (InterruptedException e) {
            logger.error("Catalog插入失败",e);
            return false;
        }
        return true;
    }

    /**
     * 存在元素返回，不存在直接返回null
     * @return
     */
    public List<MusicServiceImpl.CatalogItem> takeCatalogItems(){
        return catalogQueue.poll();
    }

    public boolean putSingerItems(List<SingerRO> singerROList){
        try {
            singerQueue.put(singerROList);
        } catch (InterruptedException e) {
            logger.error("Singer插入失败",e);
            return false;
        }
        return true;
    }

    public List<SingerRO> takeSingerItems(){
        return singerQueue.poll();
    }

    public boolean putAlbumItems(List<AlbumRO> albumROList){
        try {
            albumQueue.put(albumROList);
        } catch (InterruptedException e) {
            logger.error("Album插入失败",e);
            return false;
        }
        return true;
    }

    public List<AlbumRO> takeAlbumItems(){
        return albumQueue.poll();
    }


}
