package com.scj.service.user;



import com.scj.service.vo.NoteVO;
import com.scj.service.vo.NoteTagVO;

import java.util.List;

/**
 * Created by shengcj on 2016/9/14.
 */
public interface NoteService {
    Integer addNoteTag(String tagName, Integer userId);
    void deleteNoteTag(Integer tagId);
    void modifyNoteTagName(Integer tagId, String newTagName);

    /**
     * 查询所有标签，用于笔记展示页面，所有人都能看到
     */
    List<NoteTagVO> queryAllTag();
    List<NoteTagVO> queryTag(Integer userId);

    void addNote(String title, String content, Integer userId, String tagIds);
    void deleteNote(Integer noteId);

    /**
     * 查询文章，根据标签，没有标签返回空
     * @param tagIds
     * @return
     */
    List<NoteVO> queryNote(List<Integer> tagIds);
    /*Page<NoteVO> queryNote(List<Integer> tagIds, Pageable pageable);*/

    List<NoteVO> queryNote(Integer userId, List<Integer> tagIds);

    NoteVO queryNoteById(Integer id);

    List<NoteVO> queryAllNote();

    /*Page<NoteVO> queryAllNote(Pageable pageable);*/


}
