package com.example.ccy.mvp_note.editnote;

import android.support.annotation.Nullable;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;

import com.example.ccy.mvp_note.data.NoteBean;
import com.example.ccy.mvp_note.data.NoteDataSource;
import com.example.ccy.mvp_note.data.NotesRepository;

/**
 * Created by ccy on 2017-07-13.
 */

public class EditPresenter implements EditContract.Presenter {

    private EditContract.View editView; //Presenter持有View
    private NotesRepository notesRepository; //MVP的Model，管理数据处理
    private String loadNoteId;
    private boolean isNewNote;


    public EditPresenter(NotesRepository notesRepository, EditContract.View editView, @Nullable String noteId){
        this.notesRepository = notesRepository;
        this.editView = editView;
        this.loadNoteId = noteId;
        if(TextUtils.isEmpty(loadNoteId)){
           isNewNote = true;
        }
        this.editView.setPresenter(this);
    }



    //以下为EditContract.Presenter接口实现
    @Override
    public void start() {
        loadNote();
    }

    @Override
    public void loadNote() {
        if(isNewNote){
            return;  //新建的便笺不用加载
        }
        notesRepository.getNote(loadNoteId, new NoteDataSource.LoadNoteCallback() {
            @Override
            public void loadSuccess(NoteBean note) {

                if(editView.isActive()){
                    editView.setTitle(note.title);
                    editView.setContent(note.content);
                }
            }

            @Override
            public void loadFailed() {
                if(editView.isActive()){
                    editView.showError();
                }
            }
        });
    }

    @Override
    public void saveNote(String title, String content) {
        if(TextUtils.isEmpty(title) && TextUtils.isEmpty(content)){
            editView.showEmptyError();
            return;
        }

        if(isNewNote){
            createNote(title,content);
        }else{
            updateNote(loadNoteId,title,content);
        }
    }

    private void updateNote(String loadNoteId, String title, String content) {
        NoteBean bean = new NoteBean(loadNoteId,title,content,true);
        notesRepository.updateNote(bean);
        editView.showNoteList();
    }

    private void createNote(String title, String content) {
        NoteBean bean = new NoteBean(title,content,true);
        notesRepository.saveNote(bean);
        editView.showNoteList();
    }

    @Override
    public void deleteNote() {
        if(isNewNote){
            editView.showNoteList(); //如果是新建的便笺，直接返回主界面即可
        }else{
            notesRepository.deleteNote(loadNoteId);
            editView.showNoteList();
        }
    }

}
