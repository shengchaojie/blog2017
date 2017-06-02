package com.scj.common;

import java.util.List;

/**
 * Created by shengchaojie on 2017/6/1.
 */
public class Page<T> {
    private List<T> list ;

    private Long total;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Page() {
    }

    public Page(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }
}
