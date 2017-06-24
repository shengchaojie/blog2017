package com.scj.service.query;

import java.util.Date;

/**
 * Created by shengchaojie on 2017/6/24.
 */
public class SingerQuery {

    private Date startCrawlTime;

    private Date endCrawlTime;

    public Date getStartCrawlTime() {
        return startCrawlTime;
    }

    public void setStartCrawlTime(Date startCrawlTime) {
        this.startCrawlTime = startCrawlTime;
    }

    public Date getEndCrawlTime() {
        return endCrawlTime;
    }

    public void setEndCrawlTime(Date endCrawlTime) {
        this.endCrawlTime = endCrawlTime;
    }
}
