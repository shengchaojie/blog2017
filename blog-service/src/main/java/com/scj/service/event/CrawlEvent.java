package com.scj.service.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Administrator on 2017/6/4 0004.
 */
public class CrawlEvent extends ApplicationEvent{

    private CrawlEventType crawlEventType;

    public CrawlEvent(CrawlEventType crawlEventType) {
        super(2);
        this.crawlEventType =crawlEventType;
    }

    public CrawlEventType getCrawlEventType() {
        return crawlEventType;
    }

    public void setCrawlEventType(CrawlEventType crawlEventType) {
        this.crawlEventType = crawlEventType;
    }
}
