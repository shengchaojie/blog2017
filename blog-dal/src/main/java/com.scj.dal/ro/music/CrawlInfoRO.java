package com.scj.dal.ro.music;

import com.scj.common.enums.JobTypeEnum;
import com.scj.dal.ro.BaseRO;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by shengchaojie on 2017/6/21.
 */
@Table(name ="crawl_info")
public class CrawlInfoRO extends BaseRO{

    private JobTypeEnum jobType;

    private Date crawlTime;

    private Long validDuration;

    private Boolean deleted;

    public JobTypeEnum getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeEnum jobType) {
        this.jobType = jobType;
    }

    public Date getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Date crawlTime) {
        this.crawlTime = crawlTime;
    }

    public Long getValidDuration() {
        return validDuration;
    }

    public void setValidDuration(Long validDuration) {
        this.validDuration = validDuration;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
