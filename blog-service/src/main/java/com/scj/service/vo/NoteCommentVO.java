package com.scj.service.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shengcj on 2016/9/1.
 * 先把帖子功能完成 这个后期增加
 */
public class NoteCommentVO {

    private Integer id;

    private String content;

    private Integer order;

    private Date createTime;

    private UserVO userVO =new UserVO();

    private NoteVO note =new NoteVO();

    private NoteCommentVO targetComment;

    private List<NoteCommentVO> childComment =new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }

    public NoteVO getNote() {
        return note;
    }

    public void setNote(NoteVO note) {
        this.note = note;
    }

    public NoteCommentVO getTargetComment() {
        return targetComment;
    }

    public void setTargetComment(NoteCommentVO targetComment) {
        this.targetComment = targetComment;
    }

    public List<NoteCommentVO> getChildComment() {
        return childComment;
    }

    public void setChildComment(List<NoteCommentVO> childComment) {
        this.childComment = childComment;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
