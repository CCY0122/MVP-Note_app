package com.example.ccy.mvp_note.notelist;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ccy.mvp_note.R;
import com.example.ccy.mvp_note.data.Injection;
import com.example.ccy.mvp_note.data.NotesRepository;
import com.example.ccy.mvp_note.util.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);
        //5.0以上使布局延伸到状态栏的方法
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        //初始化toolBar、drawerLayout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);  //设置toolbar最左侧图标(android.R.id.home),默认是一个返回箭头
        ab.setDisplayHomeAsUpEnabled(true);//设置是否显示左侧图标
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //创建fragment  (V)
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        if(mainFragment == null){
            mainFragment = MainFragment.newInstence();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mainFragment,R.id.fragment_content);
        }
        //创建Presenter  （P）
        MainPresenter mainPresenter = new MainPresenter(Injection.provideRespository(this),mainFragment);

    }


    //还须重写onCreateOptionsMenu，该方法写在fragment里
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
