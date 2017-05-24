package com.scj.service.queue;

import com.scj.dal.ro.music.AlbumRO;
import com.scj.dal.ro.music.SingerRO;
import com.scj.service.music.impl.MusicServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by shengchaojie on 2017/5/24.
 */
public class MusicCrawlerQueue {

    private static final Logger logger = LoggerFactory.getLogger(MusicCrawlerQueue.class);

    private BlockingQueue<SingerRO> singerBlockingQueue;

    private BlockingQueue<List<AlbumRO>> albumBlockingQueue;

    public MusicCrawlerQueue(BlockingQueue<SingerRO> singerBlockingQueue, BlockingQueue<List<AlbumRO>> albumBlockingQueue) {
        this.singerBlockingQueue = singerBlockingQueue;
        this.albumBlockingQueue = albumBlockingQueue;
    }

    public MusicCrawlerQueue() {
        this.singerBlockingQueue = new LinkedBlockingQueue<>();
        this.albumBlockingQueue = new LinkedBlockingQueue<>();
    }

    public static class SingerThread extends Thread {

    }
}
