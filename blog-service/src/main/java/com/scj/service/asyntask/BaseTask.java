package com.scj.service.asyntask;

/**
 * Created by shengchaojie on 2017/5/23.
 */
public abstract class BaseTask {

    private String name;

    public BaseTask() {
    }

    public BaseTask(String name) {
        this.name = name;
    }

    public static final String BASE_URL = "http://music.163.com";

    public abstract  void doTask();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
