package com.example.ccy.mvp_note.notelist;

import android.util.Log;

import com.example.ccy.mvp_note.data.NoteBean;
import com.example.ccy.mvp_note.data.NoteDataSource;
import com.example.ccy.mvp_note.data.NotesRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccy on 2017-07-12.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View notesView; //Presenter持有View
    private NotesRepository notesRepository; //MVP的Model，管理数据处理
    private FilterType filterType = FilterType.ALL_NOTES; //当前过滤条件
    private boolean isFirstLoad = true;


    public MainPresenter(NotesRepository notesRepository, MainContract.View notesView) {
        this.notesView = notesView;
        this.notesRepository = notesRepository;
        notesView.setPresenter(this);
    }


    //以下为MainContract.Presenter接口实现

    @Override
    public void start() {
        if (isFirstLoad) {
            loadNotes(true, true);  //第一次打开界面时从数据源获取数据
            isFirstLoad = false;
        } else {
            loadNotes(false, true);
        }
    }

    @Override
    public void loadNotes(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            notesView.setLoadingIndicator(true);
        }
        notesRepository.cacheEnable(forceUpdate);

        notesRepository.getNotes(new NoteDataSource.LoadNotesCallback() {
            @Override
            public void loadSucess(List<NoteBean> notes) {
                if (showLoadingUI) {
                    notesView.setLoadingIndicator(false);
                }
                List<NoteBean> notesToShow = new ArrayList<NoteBean>();

                //根据当前过滤条件来过滤数据
                for (NoteBean bean : notes) {
                    switch (filterType) {
                        case ALL_NOTES:
                            notesToShow.add(bean);
                            break;
                        case ACTIVE_NOTES:
                            if (bean.isActive) {
                                notesToShow.add(bean);
                            }
                            break;
                        case COMPLETED_NOTES:
                            if (!bean.isActive) {
                                notesToShow.add(bean);
                            }
                            break;
                    }
                }
                //即将显示数据了，先判断一下持有的View还在不在前台
                if (!notesView.isActive()) {
                    return; //没必要显示了
                }

                switch (filterType) {
                    case ALL_NOTES:
                        notesView.showAllNoteTip();
                        break;
                    case ACTIVE_NOTES:
                        notesView.showActiveNoteTip();
                        break;
                    case COMPLETED_NOTES:
                        notesView.showCompletedNoteTip();
                        break;
                }
                if (notesToShow.isEmpty()) {
                    notesView.showNoNotesTip();
                    notesView.showNotes(notesToShow);
                } else {
                    notesView.showNotes(notesToShow);
                }

            }

            @Override
            public void loadFailed() {
                if (!notesView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    notesView.setLoadingIndicator(false);
                }
                notesView.showLoadNotesError();
            }
        });
    }

    @Override
    public void addNote() {
        notesView.showAddNotesUi();
    }

    @Override
    public void deleteNote(NoteBean bean) {
        notesRepository.deleteNote(bean);
        notesView.showNoteDeleted();
    }

    @Override
    public void openNoteDetail(NoteBean bean) {
        notesView.showNoteDetailUi(bean.id);
    }

    @Override
    public void makeNoteComplete(NoteBean bean) {
        notesRepository.markNote(bean, false);
        notesView.showNoteMarkedComplete();
    }

    @Override
    public void makeNoteActive(NoteBean bean) {
        notesRepository.markNote(bean, true);
        notesView.showNoteMarkedActive();
    }

    @Override
    public void clearCompleteNotes() {
        notesRepository.clearCompleteNotes();
        notesView.showCompletedNotesCleared();
        loadNotes(false, false);
    }

    @Override
    public void setFiltering(FilterType type) {
        this.filterType = type;
    }
}
