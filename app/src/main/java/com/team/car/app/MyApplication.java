package com.team.car.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 *
 * Created by Lmy on 2017/1/18.
 * email 1434117404@qq.com
 */

public class MyApplication extends Application{
    private static final String TAG = MyApplication.class.getSimpleName();
    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        //建立Volley请求队列
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    //设置Volley网络请求队列接口
    public static RequestQueue getRequestQueue(){
        return requestQueue;
    }
}
