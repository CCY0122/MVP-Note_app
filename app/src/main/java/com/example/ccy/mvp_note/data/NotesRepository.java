package com.example.ccy.mvp_note.data;

import android.os.SystemClock;
import android.util.Log;

import com.example.ccy.mvp_note.data.local.NoteDbHelper;
import com.example.ccy.mvp_note.data.local.NotesLocalDataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ccy on 2017-07-12.
 * MVP之Model实现类
 * 管理数据处理
 * 单例
 */

public class NotesRepository implements NoteDataSource {

    private static NotesRepository INSTANCE = null;
    private Map<String,NoteBean> notesCache; //数据缓存，key为id
    private boolean cacheEnable = false; //是否可以从缓存中获取数据

    private NoteDataSource notesLocalDataSource;//从数据库获取数据的Model

    private NotesRepository(NoteDataSource notesLocalDataSource){
        this.notesLocalDataSource = notesLocalDataSource;
    }

    public static NotesRepository getInstence(NoteDataSource notesLocalDataSource){
        if(INSTANCE == null){
            INSTANCE = new NotesRepository(notesLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getNote(final String noteId, final LoadNoteCallback callback) {

        if(notesCache != null && cacheEnable){  //直接从缓存中取
            NoteBean bean =getNoteFromCacheById(noteId);
            if(bean != null){
                callback.loadSuccess(notesCache.get(noteId));
                return;
            }
        }
        notesLocalDataSource.getNote(noteId, new LoadNoteCallback() {
            @Override
            public void loadSuccess(NoteBean note) {
               if(notesCache == null){
                   notesCache = new LinkedHashMap<String, NoteBean>();
               }
               notesCache.put(noteId,note);
                callback.loadSuccess(note);
            }

            @Override
            public void loadFailed() {
                callback.loadFailed();
            }
        });

    }

    /**
     * 尝试从缓存中获取note
     * @param noteId
     * @return
     */
    private NoteBean getNoteFromCacheById(String noteId){
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
            return;
        }

        notesLocalDataSource.getNotes(new LoadNotesCallback() {
            @Override
            public void loadSucess(List<NoteBean> notes) {
                refreshCache(notes);
                callback.loadSucess(notes);
            }

            @Override
            public void loadFailed() {
                callback.loadFailed();
            }
        });
    }

    private void refreshCache(List<NoteBean> notes) {
        if(notesCache == null){
            notesCache = new LinkedHashMap<>();
        }
        notesCache.clear();
        for (NoteBean bean : notes){
            notesCache.put(bean.id,bean);
        }
        cacheEnable = true;
    }

    @Override
    public void saveNote(NoteBean note) {
        notesLocalDataSource.saveNote(note);
        if(notesCache == null){
            notesCache = new LinkedHashMap<>();
        }
        notesCache.put(note.id,note);
    }

    @Override
    public void updateNote(NoteBean note) {
        notesLocalDataSource.updateNote(note);
        if(notesCache == null){
            notesCache = new LinkedHashMap<>();
        }
        notesCache.put(note.id,note);
    }

    @Override
    public void markNote(NoteBean note, boolean isActive) {
        notesLocalDataSource.markNote(note,isActive);
        if(notesCache == null){
            notesCache = new LinkedHashMap<>();
        }
        NoteBean newBean = new NoteBean(note.id,note.title,note.content,isActive);
        notesCache.put(note.id,newBean);
    }

    @Override
    public void clearCompleteNotes() {
        notesLocalDataSource.clearCompleteNotes();
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
        notesLocalDataSource.deleteAllNotes();
        if(notesCache == null){
            notesCache = new LinkedHashMap<>();
        }
        notesCache.clear();
    }

    @Override
    public void deleteNote(String noteId) {
        notesLocalDataSource.deleteNote(noteId);
        if(notesCache == null){
            notesCache = new LinkedHashMap<>();
        }
        notesCache.remove(noteId);
    }

    @Override
    public void cacheEnable(boolean enable) {
        this.cacheEnable = enable;
    }
}
