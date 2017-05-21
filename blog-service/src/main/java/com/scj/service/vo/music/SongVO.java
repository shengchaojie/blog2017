package com.scj.service.vo.music;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public class SongVO {
    private String songName;

    private String songUrl;

    private String songDownloadUrl;

    private String imgUrl;

    private long commentCount;

    private String singerName;

    private String albumName;

    private Date crawlTime;

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

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Date getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Date crawlTime) {
        this.crawlTime = crawlTime;
    }
}
