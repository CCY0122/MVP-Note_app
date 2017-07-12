package com.example.ccy.mvp_note.data;

import java.util.List;

/**
 * Created by ccy on 2017-07-12.
 * MVP之Model
 * 简单起见，目前只有获取数据有回调
 * 实际上删除、修改等数据操作也应该有回调
 */

public interface NoteDataSource {

    /**
     * 获取单个数据的回调
     */
    interface LoadNoteCallback{
        void loadSuccess(NoteBean note);
        void loadFailed();
    }

    /**
     * 获取全部数据的回调
     */
    interface LoadNotesCallback{
        void loadSucess(List<NoteBean> notes);
        void loadFailed();
    }


    void getNote(String noteId,LoadNoteCallback callback); //通过id获取指定数据

    void getNotes(LoadNotesCallback callback); //获取所有数据

    void saveNote(NoteBean note);

    void markNote(NoteBean note,boolean isActive); //标记便笺完成状态

    void clearCompleteNotes();

    void deleteAllNotes();

    void deleteNote(NoteBean note);

    void cacheEnable(boolean enable); //缓存是否可用（如果有)

}
