package com.example.ccy.mvp_note.editnote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ccy.mvp_note.R;

/**
 * Created by ccy on 2017-07-13.
 */

public class EditFragment extends Fragment implements EditContract.View {

    private EditContract.Presenter presenter; //View持有Presenter
    private EditText mTitle;
    private EditText mContent;
    private FloatingActionButton fab;


    public static EditFragment getInstence(){
        return  new EditFragment();
    }



    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_frag,container,false);
        mTitle = (EditText) v.findViewById(R.id.edit_title);
        mContent = (EditText) v.findViewById(R.id.edit_content);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveNote(mTitle.getText().toString(),mContent.getText().toString());
            }
        });

        setHasOptionsMenu(true);

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_note:
                presenter.deleteNote();
                break;
        }
        return true;
    }

    //以下为EditContract.View接口实现


    @Override
    public void setPresenter(EditContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoteList() {
        getActivity().finish();
    }

    public void setTitle(String title) {
        mTitle.setText(title+"");
    }

    @Override
    public void setContent(String content) {
        mContent.setText(content+"");
    }

    @Override
    public void showError() {
        Snackbar.make(getView(),"加载失败",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyError() {
        Snackbar.make(getView(),"标题和内容不能全空",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
