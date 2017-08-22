package com.scj.common.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.HashMap;

/**
 * 网易云音乐cookie存储
 * 用户名,对应的cookie
 * 直接做内存缓存其实也行
 * 定期起线程做备份和清除硬盘缓存也行
 * Created by shengchaojie on 2017/8/22.
 */
@Component
public class NetEaseMusicCookieStore extends HashMap<String,BasicCookieStore>{

    private static final String CACHE_DIRECTORY ="d:/data/neteasemusic/";

    private static final String CACHE_FILENAME ="cookie.cache";

    private static final String CACHE_PATH =CACHE_DIRECTORY+CACHE_FILENAME;

    private static final Logger logger = LoggerFactory.getLogger(NetEaseMusicCookieStore.class);

    @PostConstruct
    public void init(){
        //从硬盘中读取缓存
        File file =new File(CACHE_PATH);
        if(!file.exists()){
            return;
        }
        ObjectInputStream ois =null;
        try {
            /*fis = new FileInputStream(CACHE_PATH);
            String result = IOUtils.toString(fis, UTF_8);
            NetEaseMusicCookieStore cookieStore = JSON.parseObject(result,NetEaseMusicCookieStore.class);*/
            ois =new ObjectInputStream(new FileInputStream(file));
            NetEaseMusicCookieStore cookieStore=(NetEaseMusicCookieStore)ois.readObject();
            putAll(cookieStore);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(ois);
        }
    }

    @PreDestroy
    public void destroy(){
        //把数据缓存到硬盘
        File file =new File(CACHE_DIRECTORY);
        if(!file.exists()){
            file.mkdirs();
        }
        ObjectOutputStream oos =null;
        try {
            //IOUtils.write(JSON.toJSONString(this),new FileOutputStream(CACHE_PATH), UTF_8 );
            oos =new ObjectOutputStream(new FileOutputStream(CACHE_PATH));
            oos.writeObject(this);
        } catch (IOException e) {
            logger.error("IO发生异常");
        }finally {
            IOUtils.closeQuietly(oos);
        }
    }
}
