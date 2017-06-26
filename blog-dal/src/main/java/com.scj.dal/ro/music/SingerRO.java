package com.scj.dal.ro.music;

import com.scj.dal.ro.BaseRO;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Table(name = "music_singer")
public class SingerRO extends BaseRO{

    private String singerName;

    private String singerUrl;

    private String imgUrl;

    private String firstLetter;

    private Date crawlTime;

    private Date createTime;

    private Date updateTime;

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSingerUrl() {
        return singerUrl;
    }

    public void setSingerUrl(String singerUrl) {
        this.singerUrl = singerUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
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
