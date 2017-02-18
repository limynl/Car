package com.team.car.utils.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.team.car.app.MyApplication;

import java.util.Map;

/**
 * Volley中Get和Post请求工具类
 * Created by Lmy on 2017/1/18.
 * email 1434117404@qq.com
 */

public class VolleyRequestUtil {
    public static StringRequest stringRequest;
    public static Context context;

    /**
     *
     * Get请求
     * @param context 当前上下文
     * @param url 请求的网络地址
     * @param tag 当前请求的标签
     * @param volleyListenerInterface 回调接口
     */
    public static void RequestGet(Context context, String url, String tag, VolleyListenerInterface volleyListenerInterface){
        //清除请求队列中的已有的该请求
        MyApplication.getRequestQueue().cancelAll(tag);
        //创建当前请求，获取返回结果
        stringRequest = new StringRequest(Request.Method.GET, url, volleyListenerInterface.responseListener(), volleyListenerInterface.errorListener());
        //给当前请求添加标记
        stringRequest.setTag(tag);
        //将请求添加到请求队列中
        MyApplication.getRequestQueue().add(stringRequest);
        //重启当前请求队列
        MyApplication.getRequestQueue().start();
    }

    /**
     * Post请求
     * @param context 当前上下文
     * @param url 请求的网络地址
     * @param tag 当前请求的标签
     * @param map 请求的数据集合
     * @param volleyListenerInterface 回调接口
     */
    public static void RequestPost(Context context, String url, String tag, final Map<String, String> map, VolleyListenerInterface volleyListenerInterface){
        MyApplication.getRequestQueue().cancelAll(tag);
        //创建当前请求，并向服务器传递参数
        stringRequest = new StringRequest(Request.Method.POST, url, volleyListenerInterface.responseListener(), volleyListenerInterface.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        stringRequest.setTag(tag);
        MyApplication.getRequestQueue().add(stringRequest);
        MyApplication.getRequestQueue().start();
    }
}
