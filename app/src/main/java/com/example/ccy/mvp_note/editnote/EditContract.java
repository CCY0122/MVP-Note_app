package com.example.ccy.mvp_note.editnote;

import com.example.ccy.mvp_note.BasePresenter;
import com.example.ccy.mvp_note.BaseView;

/**
 * Created by ccy on 2017-07-13.
 */

public class EditContract {

    interface View extends BaseView<Presenter>{

        void showNoteList(); //显示便笺列表（即返回主界面）

        void setTitle(String title);

        void setContent(String content);

        void showError();

        void showEmptyError();

        boolean isActive();

    }

    interface Presenter extends BasePresenter{

        void loadNote();

        void saveNote(String title,String content);

        void deleteNote();
    }
}
