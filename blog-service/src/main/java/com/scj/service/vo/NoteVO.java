package com.scj.service.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by shengcj on 2016/9/1.
 */
public class NoteVO {

    private Integer id;

    private String title;

    private String content;

    private Date createTime;

    //新版本的mysql 只能有一个列 采用这个属性
    private Date updateTime;

    private UserVO userVO;

    private Set<NoteTagVO> noteTagVOS =new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }

    public Set<NoteTagVO> getNoteTagVOS() {
        return noteTagVOS;
    }

    public void setNoteTagVOS(Set<NoteTagVO> noteTagVOS) {
        this.noteTagVOS = noteTagVOS;
    }
}
