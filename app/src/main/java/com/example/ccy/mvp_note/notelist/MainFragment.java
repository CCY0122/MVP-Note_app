package com.example.ccy.mvp_note.notelist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ccy.mvp_note.R;
import com.example.ccy.mvp_note.data.NoteBean;
import com.example.ccy.mvp_note.editnote.EditActivity;
import com.example.ccy.mvp_note.notedetail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccy on 2017-07-11.
 */

public class MainFragment extends Fragment implements MainContract.View {
    private MainContract.Presenter presenter;  //View持有Presenter
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private TextView headerView;
    private RecyclerAdapter adapter;
    private List<NoteBean> data = new ArrayList<>();


    public static MainFragment newInstence(){
        return new MainFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_frag,container,false);

        //初始化view
        headerView = (TextView) v.findViewById(R.id.header_tv);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(data, onNoteItemClickListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(   //设置刷新时颜色动画，第一个颜色也会应用于下拉过程中的颜色
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadNotes(true,true);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addNote();
            }
        });
        setupNavigationView(navigationView);


        //使fragment参与对menu的控制（使onCreateOptionsMenu、onOptionsItemSelected有效）
        setHasOptionsMenu(true);

        return v;
    }

    private void setupNavigationView(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_filter_all:
                        presenter.setFiltering(FilterType.ALL_NOTES);
                        break;
                    case R.id.menu_filter_active:
                        presenter.setFiltering(FilterType.ACTIVE_NOTES);
                        break;
                    case R.id.menu_filter_complete:
                        presenter.setFiltering(FilterType.COMPLETED_NOTES);
                        break;
                    case R.id.menu_clear_complete:
                        presenter.clearCompleteNotes();
                        break;
                }
                presenter.loadNotes(false,false);  //参数为false，不需要从数据源重新获取数据，从缓存取出并过滤即可,也没必要显示加载条
                ((DrawerLayout)getActivity().findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.refresh:
               presenter.loadNotes(true,true);
               break;
       }
        return true;
    }

    /**
     * RecyclerView的点击事件监听
     */
    RecyclerAdapter.OnNoteItemClickListener onNoteItemClickListener = new RecyclerAdapter.OnNoteItemClickListener() {
        @Override
        public void onNoteClick(NoteBean note) {
            presenter.openNoteDetail(note);
        }

        @Override
        public void onCheckChanged(NoteBean note, boolean isChecked) {
            if(isChecked){
                presenter.makeNoteComplete(note);
            }else{
                presenter.makeNoteActive(note);
            }
        }

        @Override
        public boolean onLongClick(View v, final NoteBean note) {
            final AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("确定要删除么？");
            builder.setTitle("警告");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    presenter.deleteNote(note);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();
            return true;
        }
    };

    //以下为MainContract.View接口实现

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if(getView() == null){
            return;
        }
        //用post可以保证swipeRefreshLayout已布局完成
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showNotes(List<NoteBean> notes) {
        adapter.replaceData(notes);

    }

    @Override
    public void showLoadNotesError() {
        Snackbar.make(getView(),"加载数据失败",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAddNotesUi() {
        Intent i = new Intent(getActivity(), EditActivity.class);
        startActivity(i);
    }

    @Override
    public void showNoteDetailUi(String noteId) {
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra(DetailActivity.EXTRE_NOTE_ID,noteId);
        startActivity(i);
    }

    @Override
    public void showAllNoteTip() {
        headerView.setBackgroundColor(0x88ff0000);
        headerView.setText("全部便笺");
    }

    @Override
    public void showActiveNoteTip() {
        headerView.setBackgroundColor(0x8800ff00);
        headerView.setText("未完成的便笺");
    }

    @Override
    public void showCompletedNoteTip() {
        headerView.setBackgroundColor(0x880000ff);
        headerView.setText("已完成的便笺");
    }

    @Override
    public void showNoNotesTip() {
        headerView.setBackgroundColor(0xffffffff);
        headerView.setText("没有便笺，请创建");
    }

    @Override
    public void showNoteDeleted() {
        Snackbar.make(getView(),"成功删除该便笺",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showCompletedNotesCleared() {
        Snackbar.make(getView(),"成功清除已完成便笺",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNoteMarkedActive() {
        Snackbar.make(getView(),"成功标记为未完成",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNoteMarkedComplete() {
        Snackbar.make(getView(),"成功标记为已完成",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
