package com.scj.dal.ro.music;

import com.scj.dal.ro.BaseRO;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Table(name = "music_song",schema = "blog")
public class SongRO extends BaseRO{

    private String songName;

    private String songUrl;

    private String songDownloadUrl;

    private String imgUrl;

    private Long commentCount;

    private Long singerId;

    private Long albumId;

    private Date crawlTime;

    private Date createTime;

    private Date updateTime;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getSongDownloadUrl() {
        return songDownloadUrl;
    }

    public void setSongDownloadUrl(String songDownloadUrl) {
        this.songDownloadUrl = songDownloadUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getSingerId() {
        return singerId;
    }

    public void setSingerId(Long singerId) {
        this.singerId = singerId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public Date getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Date crawlTime) {
        this.crawlTime = crawlTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
