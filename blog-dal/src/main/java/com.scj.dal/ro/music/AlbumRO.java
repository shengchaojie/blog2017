package com.scj.dal.ro.music;

import com.scj.dal.ro.BaseRO;
import com.sun.javafx.print.PrintHelper;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Table(name = "music_album")
public class AlbumRO extends BaseRO{

    private String albumName;

    private String albumUrl;

    private String imgUrl;

    private Long singerId;

    private Date createTime;

    private Date crawlTime;

    private Date updateTime;

    private String publishTime;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getSingerId() {
        return singerId;
    }

    public void setSingerId(Long singerId) {
        this.singerId = singerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Date crawlTime) {
        this.crawlTime = crawlTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
