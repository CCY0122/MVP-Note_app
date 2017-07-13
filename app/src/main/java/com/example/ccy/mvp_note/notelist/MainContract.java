package com.example.ccy.mvp_note.notelist;

import com.example.ccy.mvp_note.BasePresenter;
import com.example.ccy.mvp_note.BaseView;
import com.example.ccy.mvp_note.data.NoteBean;

import java.util.List;

/**
 * Created by ccy on 2017-07-11.
 */

public class MainContract {

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active); //显示、隐藏加载控件

        void showNotes(List<NoteBean> notes); //显示便笺

        void showLoadNotesError();//加载便笺失败

        void showAddNotesUi();    //显示创建便笺界面

        void showNoteDetailUi(String noteId); //显示编辑便笺界面

        void showAllNoteTip();//以下4个方法对应各种状态下需显示的内容

        void showActiveNoteTip();

        void showCompletedNoteTip();

        void showNoNotesTip();

        void showNoteDeleted(); //删除了一个便笺后

        void showCompletedNotesCleared();//删除了已完成的便笺后

        void showNoteMarkedActive();//有便笺被标记为未完成后

        void showNoteMarkedComplete();//有便笺被标记为已完成后

        boolean isActive(); //用于判断当前界面是否还在前台
    }
    interface Presenter extends BasePresenter{

        /**
         *
         * @param forceUpdate 是否是更新。true则从数据源（服务器、数据库等）获取数据，false则从缓存中直接获取
         * @param showLoadingUI 是否需要显示加载框
         */
        void loadNotes(boolean forceUpdate,boolean showLoadingUI);

        void addNote(); //添加便笺

        void deleteNote(NoteBean bean); //删除便笺

        void openNoteDetail(NoteBean bean); //便笺详情

        void makeNoteComplete(NoteBean bean); // 标记便笺为已完成

        void makeNoteActive(NoteBean bean); //标记便笺为未完成

        void clearCompleteNotes(); //清除已完成便笺

        void setFiltering(FilterType type); //数据过滤

    }
}
