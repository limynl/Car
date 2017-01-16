package com.team.car.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.fragment.BaseFragment;
import com.team.car.fragment.home.homeFragment;
import com.team.car.fragment.manage.manageFragment;
import com.team.car.fragment.found.foundFragment;
import com.team.car.fragment.shop.shopFragment;
import com.team.car.widgets.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lmy on 2017/1/15.
 * email 1434117404@qq.com
 */

public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    private ToastUtil toastUtil = new ToastUtil();
    private CoordinatorLayout right;
    private NavigationView left;
    private boolean isDrawer=false;

    private RadioGroup rg_main;
    private List<BaseFragment> baseFragment;
    private Fragment content; //上次的界面，上下文对象
    private int position; //选中的Fragment的对应的位置


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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //菜单与主界面一起滑动
        move(drawer);

        //设置菜单列表图标颜色为指定颜色
        navigationView.setItemIconTintList(null);

        //以下可以设置菜单列表中的信息
        //获取头部的信息
        View headerView = navigationView.getHeaderView(0);
        TextView customText = (TextView)headerView.findViewById(R.id.custom_text_view);
        ImageView userHead = (ImageView)headerView.findViewById(R.id.user_head);
        ImageView weather = (ImageView)headerView.findViewById(R.id.weather);
        userHead.setImageResource(R.mipmap.head);
        userHead.setOnClickListener(this);
        weather.setOnClickListener(this);

        //初始化主布局
        initView(); //初始化
        initFragment(); //初始化Fragment
        setListener(); //设置RadioGroup的监听
    }

    //菜单列表头部点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_head:
            {
                toastUtil.Short(MainActivity.this, "我的个人中心").show();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
                break;
            case R.id.weather:{
                toastUtil.Short(MainActivity.this, "天气预报").show();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
                break;
        }
    }

    //菜单列表条目点击事件
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.add_car) {
            toastUtil.Long(MainActivity.this, "添加爱车").show();
        } else if (id == R.id.integral) {
            toastUtil.Long(MainActivity.this, "积分查询").show();
        } else if (id == R.id.share) {
            toastUtil.Long(MainActivity.this, "分享App").show();
        } else if (id == R.id.online_complaint) {
            toastUtil.Long(MainActivity.this, "在线投诉").show();
        }else if (id == R.id.setting){
            toastUtil.Long(MainActivity.this, "设置").show();
        }

        //点击条目侧滑关闭
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //按返回键侧滑关闭
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 初始化
     */
    private void initView() {
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
    }

    /**
     * 设置监听
     */
    private void setListener(){
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_main.check(R.id.rb_home); //实现进入主界面时就在初始界面(默认为个人中心)
    }


    /**
     * 点击相应的选项进入相应的fragment
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId)
            {
                case R.id.rb_home:
                    position = 0;
                    break;
                case R.id.rb_manager:
                    position = 1;
                    break;
                case R.id.rb_shangjia:
                    position = 2;
                    break;
                case R.id.rb_found:
                    position = 3;
                    break;
                default:
                    position = 0;
                    break;
            }
            //根据位置得到对应的Fragment
           /* BaseFragment fragment = getFragment();
            switchFragment(fragment);*/
            BaseFragment to = getFragment();
            switchFragment(content,to);
        }
    }

    /**
     * 用于切换时，不会被重复加载
     * @param from
     * @param to
     */
    private void switchFragment(Fragment from, Fragment to)
    {
        if(from != to)
        {
            //实现切换
            content = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //判断是否被添加
            if(!to.isAdded())
            {
                //to没被添加，from隐藏,添加to
                if(from != null)
                {
                    ft.hide(from);
                }
                if(to != null)
                {
                    ft.add(R.id.fl_content,to).commit();
                }
            }else{
                //to已经被添加，from隐藏
                if(from != null)
                {
                    ft.hide(from);
                }
                //显示to
                if(to != null)
                {
                    ft.show(to).commit();
                }
            }
        }
    }

    /**
     * 根据位值得到对应的Fragment
     * @return
     */
    private BaseFragment getFragment()
    {
        BaseFragment fragment = baseFragment.get(position);
        return fragment;
    }

    private void initFragment()
    {
        baseFragment = new ArrayList<BaseFragment>();
        baseFragment.add(new homeFragment());
        baseFragment.add(new manageFragment());
        baseFragment.add(new foundFragment());
        baseFragment.add(new shopFragment());
    }

    /**
        主界面与菜单一起滑动
     */
    private void move(DrawerLayout drawer) {
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
