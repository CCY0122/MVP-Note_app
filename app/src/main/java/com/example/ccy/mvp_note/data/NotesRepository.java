package com.example.ccy.mvp_note.data;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ccy on 2017-07-12.
 * MVP之Model实现类
 * 管理数据处理
 */

public class NotesRepository implements NoteDataSource {

    private static NotesRepository INSTANCE = null;
    private Map<String,NoteBean> notesCache; //数据缓存，key为id
    private boolean cacheEnable = false; //是否可以从缓存中获取数据

    private NotesRepository(){
        //目前没有从数据库、服务端获取数据的实现类，先给缓存填充一些假数据
        notesCache = new LinkedHashMap<>();
        for (int i = 0; i < 20; i++) {
            NoteBean bean = new NoteBean(""+i,""+i,i%2==0);
            notesCache.put(bean.id,bean);
        }
    }

    public static NotesRepository getInstence(){
        if(INSTANCE == null){
            INSTANCE = new NotesRepository();
        }
        return INSTANCE;
    }

    @Override
    public void getNote(String noteId, LoadNoteCallback callback) {
        NoteBean bean = getNoteById(noteId);
        if(bean != null){
            callback.loadSuccess(bean);
        }else{
            callback.loadFailed();
        }
    }

    /**
     * 尝试从缓存中获取note
     * @param noteId
     * @return
     */
    private NoteBean getNoteById(String noteId){
        if(notesCache == null || notesCache.isEmpty()){
            return null;
        }else{
            return notesCache.get(noteId);
        }
    }

    @Override
    public void getNotes(final LoadNotesCallback callback) {

        if(notesCache != null && cacheEnable){
            callback.loadSucess(new ArrayList<NoteBean>(notesCache.values()));
        }else {
            callback.loadFailed();
        }
    }

    @Override
    public void saveNote(NoteBean note) {
        if(notesCache == null){
            notesCache = new LinkedHashMap<>();
        }
        notesCache.put(note.id,note);
    }

    @Override
    public void markNote(NoteBean note, boolean isActive) {
        if(notesCache == null){
            notesCache = new LinkedHashMap<>();
        }
        NoteBean newBean = new NoteBean(note.id,note.title,note.content,isActive);
        notesCache.put(note.id,newBean);
    }

    @Override
    public void clearCompleteNotes() {
        if(notesCache == null){
            notesCache = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, NoteBean>> iterator = notesCache.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, NoteBean> next = iterator.next();
            if(!next.getValue().isActive){
                iterator.remove();
            }
        }

    }

    @Override
    public void deleteAllNotes() {
        if(notesCache == null){
            notesCache = new LinkedHashMap<>();
        }
        notesCache.clear();
    }

    @Override
    public void deleteNote(NoteBean note) {
        if(notesCache == null){
            notesCache = new LinkedHashMap<>();
        }
        notesCache.remove(note.id);
    }

    @Override
    public void cacheEnable(boolean enable) {
//        this.cacheEnable = enable;
        cacheEnable = true; //因为还没有从数据库、服务器获取数据实现类，故暂时只能从缓存中获取
    }
}
