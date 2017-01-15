package com.team.car.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.car.R;

/**
 * Created by Lmy on 2017/1/15.
 * email 1434117404@qq.com
 */

public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    private CoordinatorLayout right;
    private NavigationView left;
    private boolean isDrawer=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//显示ActionBar
        //取消显示标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("汽车服务");
        toolbar.setTitleMarginStart(20);
//        toolbar.setLogo(R.mipmap.head);
        toolbar.setNavigationIcon(R.mipmap.head);*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        right = (CoordinatorLayout) findViewById(R.id.right);
        left = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //菜单与主界面一起滑动
        move(drawer);

        //设置菜单列表图标颜色
        navigationView.setItemIconTintList(null);

        //以下可以设置菜单列表中的信息
        //获取头部的信息
        View headerView = navigationView.getHeaderView(0);
        TextView customText = (TextView)headerView.findViewById(R.id.custom_text_view);
//        customText.setText("哈哈哈。。。我换了");
        ImageView imageView = (ImageView)headerView.findViewById(R.id.imageView);
        imageView.setImageResource(R.mipmap.head);
        imageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView:
            {
//                Intent intent = new Intent(MainActivity.this, UpDataUserActivity.class);
//                startActivity(intent);
//                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);//淡入淡出效果
            }
                break;
        }
    }

    //侧滑导航选择
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Toast.makeText(this, "我要拍照", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }else if (id == R.id.setting){
            Toast.makeText(this, "你点击了设置", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void move(DrawerLayout drawer) {
        //主界面与菜单一起滑动
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isDrawer){
                    return left.dispatchTouchEvent(motionEvent);
                }else{
                    return false;
                }
            }
        });
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer=true;
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                right.layout(left.getRight(), 0, left.getRight() + display.getWidth(), display.getHeight());
            }
            @Override
            public void onDrawerOpened(View drawerView) {}
            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer=false;
            }
            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }


}