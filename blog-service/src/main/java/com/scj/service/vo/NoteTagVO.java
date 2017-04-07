package com.scj.service.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by shengcj on 2016/9/1.
 */

public class NoteTagVO {

    private Integer id;

    private String tagName;

    private UserVO userVO;

    private Date createTime;

    private Date updateTime;

    private Set<NoteVO> notes =new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Set<NoteVO> getNotes() {
        return notes;
    }

    public void setNotes(Set<NoteVO> notes) {
        this.notes = notes;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
