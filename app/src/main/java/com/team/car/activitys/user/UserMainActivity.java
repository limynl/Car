package com.team.car.activitys.user;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.team.car.R;
import com.team.car.utils.NetReceiver;
import com.team.car.activitys.weather.WeatherActivity;
import com.team.car.services.WeatherService;
import com.team.car.base.fragment.BaseFragment;
import com.team.car.fragment.found.foundFragment;
import com.team.car.fragment.home.homeFragment;
import com.team.car.fragment.manager.manageFragment;
import com.team.car.fragment.shop.shopFragment;
import com.team.car.widgets.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Lmy on 2017/1/15.
 * email 1434117404@qq.com
 */

public class UserMainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    private static final String TAG = UserMainActivity.class.getSimpleName();
    private View headerView;//侧滑头布局
    private ToastUtil toastUtil = new ToastUtil();
    private CoordinatorLayout right;
    private NavigationView left;
    private boolean isDrawer=false;

    private RadioGroup rg_main;//底部四个按钮
    private List<BaseFragment> baseFragment;
    private Fragment content; //上次的界面内容，上下文对象
    private int position; //选中的Fragment的对应的位置

    private Intent weatherIntent;
    private TextView tvWeather;
    private TextView tvCity;
    private ProgressReceiver progressReceiver;//广播接收器
    private String city = null, weather;//存储城市和天气信息
    private String ip;//通过ip获取天气
    private Intent intent;//用于跳转
    private NetReceiver mReceiver;//广播接收器
    private IntentFilter mFilter;//过滤器
    private WeatherService.WeatherBinder binder;
    public final static int INTENT_SETCARINFO = 1;

    private GifImageView btnSstq;//显示gif

    private static Boolean isExit = false;//是否退出应用程序

    //创建一个服务连接,用于更新天气
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (WeatherService.WeatherBinder) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            toastUtil.Short(UserMainActivity.this, "天气部分出现错误!").show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        right = (CoordinatorLayout) findViewById(R.id.right);
        left = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);//设置菜单列表图标颜色为图片的本来颜色

        //以下可以设置侧滑部分的信息
        //获取头部的信息
        headerView = navigationView.getHeaderView(0);
        TextView customText = (TextView)headerView.findViewById(R.id.custom_text_view);//获取用户名或昵称
        ImageView userHead = (ImageView)headerView.findViewById(R.id.user_head);//获取用户头像
        btnSstq = (GifImageView)headerView.findViewById(R.id.btnSstq);//获取天气图标
        btnSstq.setImageResource(R.mipmap.test_gif);
        userHead.setImageResource(R.mipmap.head);
        userHead.setOnClickListener(this);
        btnSstq.setOnClickListener(this);

        //初始化主布局
        initView(); //初始化
        initFragment(); //初始化Fragment
        setListener(); //设置RadioGroup的监听
        initialWeather();//初始化天气
    }

    /**
     * 侧滑中头部点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_head:
            {
                toastUtil.Short(UserMainActivity.this, "个人中心").show();
                startActivity(new Intent(UserMainActivity.this, UserMessageActivity.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
                break;
            case R.id.btnSstq:{
                toastUtil.Short(UserMainActivity.this, "天气预报").show();
                Intent intent = new Intent(UserMainActivity.this, WeatherActivity.class);
                intent.putExtra("city", city);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);//淡入淡出效果
            }
                break;
        }
    }

    /**
     * 菜单列表条目点击事件
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_car) {
            toastUtil.Long(UserMainActivity.this, "添加爱车").show();
        } else if (id == R.id.integral) {
            toastUtil.Long(UserMainActivity.this, "我的资产").show();
        } else if (id == R.id.share) {
            toastUtil.Long(UserMainActivity.this, "分享App").show();
        } else if (id == R.id.online_complaint) {
            toastUtil.Long(UserMainActivity.this, "在线客服").show();
            ConsultSource source = new ConsultSource(null, null, null);
            if(!Unicorn.isServiceAvailable()){
                Toast.makeText(UserMainActivity.this, "客服接口有问题，请稍后再试", Toast.LENGTH_SHORT).show();
            }
            Unicorn.openServiceActivity(UserMainActivity.this, "车应用客服", source);
        }else if (id == R.id.setting){
            toastUtil.Long(UserMainActivity.this, "设置").show();
            Intent intent = new Intent(UserMainActivity.this, SettingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }

        //点击条目侧滑关闭
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }

    /**
     * 按返回键侧滑关闭
     */
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
        rg_main.check(R.id.rb_home); //实现进入主界面时就在初始界面(默认为首页)
    }

    /**
     * 点击相应的选项进入相应的fragment，点击顺序就是initFragment（）中初始化添加的顺序
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
                default://默认为首页
                    position = 0;
                    break;
            }
            BaseFragment to = baseFragment.get(position);
            switchFragment(content,to);
        }
    }

    /**
     * 用于切换时，不会被重复加载
     * @param from
     * @param to
     */
    private void switchFragment(Fragment from, Fragment to) {
        if(from != to) {
            content = to;//记录上一个fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(!to.isAdded()) {//判断要显示的Fragment是否被添加
                //如果没有添加，则将上一个fragment隐藏，再添加要显示的fragment
                if(from != null) {//先进行判空操作，是不是第一次进入主界面
                    ft.hide(from);
                }
                if(to != null) {
                    ft.add(R.id.fl_content,to).commit();
                }
            }else{
                //如果要显示的界面添加过，则将上一个界面隐藏，将下一个要显示的显示出来即可
                if(from != null) {
                    ft.hide(from);
                }
                if(to != null) {
                    ft.show(to).commit();
                }
            }
        }
    }

    /**
     * 初始化Fragment(即底部的四个tab选项)
     */
    private void initFragment() {
        baseFragment = new ArrayList<BaseFragment>();
        baseFragment.add(new homeFragment());
        baseFragment.add(new manageFragment());
        baseFragment.add(new shopFragment());
        baseFragment.add(new foundFragment());
    }


    /**
     *初始化跟天气相关控件
     */
    private void initialWeather() {
        tvWeather = (TextView) headerView.findViewById(R.id.weather_text);
        tvCity = (TextView) headerView.findViewById(R.id.city_text);
        mReceiver = new NetReceiver();//网络接受
        mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        UserMainActivity.this.registerReceiver(mReceiver, mFilter);
        registerMyReceiver();
        weatherIntent = new Intent(UserMainActivity.this, WeatherService.class);
        bindService(weatherIntent, conn, Service.BIND_AUTO_CREATE);
    }

    private void registerMyReceiver() {
        progressReceiver = new ProgressReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WeatherService.ACTION_UPDATE_WEATHER);
        intentFilter.addAction(WeatherService.ACTION_UPDATE_CITY);
        UserMainActivity.this.registerReceiver(progressReceiver, intentFilter);
    }

    class ProgressReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WeatherService.ACTION_UPDATE_WEATHER.equals(action)) {//接受天气预报的城市
                weather = intent.getStringExtra(WeatherService.ACTION_UPDATE_WEATHER);
                tvWeather.setText(weather);
            } else if (WeatherService.ACTION_UPDATE_CITY.equals(action)) {//接受相应的天气
                city = intent.getStringExtra(WeatherService.ACTION_UPDATE_CITY);
                tvCity.setText(city);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //服务解除绑定
        binder.stopWeather();
        unbindService(conn);

        //广播解除绑定
        UserMainActivity.this.unregisterReceiver(mReceiver);
        UserMainActivity.this.unregisterReceiver(progressReceiver);
    }

    /**
     * 手机返回键监听，连续按两次退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Timer timer = null;
            if(isExit == false){
                isExit = true;
                toastUtil.Short(UserMainActivity.this, "再按一次退出应用程序^_^").show();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            }else{
                UserMainActivity.this.finish();
                System.exit(0);
            }
        }
        return false;
    }
}
