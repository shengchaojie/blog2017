package com.scj.dal.ro.music;

import com.scj.common.enums.WebPageEnum;
import com.scj.dal.ro.BaseRO;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Table(name = "music_webpage")
public class WebPageRO extends BaseRO{
    private Long webpageId;

    private WebPageEnum webpageType;

    private String webpageContent;

    private Integer webpageIndex;

    private Boolean crawled;

    private Date crawlTime;

    public Long getWebpageId() {
        return webpageId;
    }

    public void setWebpageId(Long webpageId) {
        this.webpageId = webpageId;
    }

    public WebPageEnum getWebpageType() {
        return webpageType;
    }

    public void setWebpageType(WebPageEnum webpageType) {
        this.webpageType = webpageType;
    }

    public String getWebpageContent() {
        return webpageContent;
    }

    public void setWebpageContent(String webpageContent) {
        this.webpageContent = webpageContent;
    }

    public Boolean getCrawled() {
        return crawled;
    }

    public void setCrawled(Boolean crawled) {
        this.crawled = crawled;
    }

    public Date getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Date crawlTime) {
        this.crawlTime = crawlTime;
    }

    public Integer getWebpageIndex() {
        return webpageIndex;
    }

    public void setWebpageIndex(Integer webpageIndex) {
        this.webpageIndex = webpageIndex;
    }
}
