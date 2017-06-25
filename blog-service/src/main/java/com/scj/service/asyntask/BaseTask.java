package com.scj.service.asyntask;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by shengchaojie on 2017/5/23.
 */
public abstract class BaseTask {

    private String name;

    public BaseTask() {
    }

    @Value("${music.sleepTime}")
    protected Long sleepTime;

    @Value("${music.debug}")
    protected Boolean debug;

    public BaseTask(String name) {
        this.name = name;
    }

    public static final String BASE_URL = "http://music.163.com";

    public abstract void doTask();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
