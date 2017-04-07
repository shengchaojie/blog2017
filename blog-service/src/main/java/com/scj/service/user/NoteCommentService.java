package com.scj.service.user;


import com.scj.service.vo.NoteCommentVO;

import java.util.List;

/**
 * Created by shengcj on 2016/11/16.
 */
public interface NoteCommentService {
    /**
     * 查询所有评论
     * @param noteId
     * @return
     */
    List<NoteCommentVO> queryAllNoteComment(Integer noteId);

    /**
     * 对文章新增评论
     * @param noteCommentVO
     */
    NoteCommentVO addNoteComment(NoteCommentVO noteCommentVO, Integer userId, Integer noteId);

    /**
     * 回复评论
     */
    List<NoteCommentVO> replyNoteComment(NoteCommentVO noteCommentVO, Integer userId, Integer noteId, Integer targetCommentId);

    /**
     * 根据id获取评论
     * @param id
     * @return
     */
    NoteCommentVO queryNoteCommentById(Integer id);

    /**
     * 删除评论
     */
    void deleteNoteComment(Integer id);
}
