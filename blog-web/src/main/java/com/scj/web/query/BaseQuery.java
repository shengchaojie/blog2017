package com.scj.web.query;

/**
 * Created by shengchaojie on 2017/5/31.
 */
public class BaseQuery {
    private Integer page;

    private Integer limit;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
