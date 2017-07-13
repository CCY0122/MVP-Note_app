package com.example.ccy.mvp_note.editnote;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.ccy.mvp_note.R;
import com.example.ccy.mvp_note.data.Injection;
import com.example.ccy.mvp_note.data.NotesRepository;
import com.example.ccy.mvp_note.data.local.NotesLocalDataSource;
import com.example.ccy.mvp_note.util.ActivityUtils;

/**
 * Created by ccy on 2017-07-12.
 */

public class EditActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "NOTE_ID";
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_act);
        //5.0以上使布局延伸到状态栏的方法
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        //设置toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("编辑便笺");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        String noteId = getIntent().getStringExtra(EXTRA_NOTE_ID); //为空则代表是新建的便笺

        //创建fragment (V)
        EditFragment editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        if(editFragment == null){
            editFragment = EditFragment.getInstence();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),editFragment,R.id.fragment_content);
        }

        //创建Presenter  (P)
        EditPresenter presenter = new EditPresenter(Injection.provideRespository(this),editFragment,noteId);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
