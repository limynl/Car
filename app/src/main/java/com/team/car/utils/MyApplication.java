package com.team.car.utils;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Lmy on 2017/1/18.
 * email 1434117404@qq.com
 */

public class MyApplication extends Application{
    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        //建立Volley请求队列
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    //设置Volley网络请求队列接口
    public static RequestQueue getRequestQueue(){
        return requestQueue;
    }
}
