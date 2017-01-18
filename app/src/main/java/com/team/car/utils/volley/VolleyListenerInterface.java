package com.team.car.utils.volley;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Volley请求监听事件
 * Created by Lmy on 2017/1/18.
 * email 1434117404@qq.com
 */

public abstract class VolleyListenerInterface {
    public Context context;
    public static Response.Listener<String> mSuccessListener;
    public static Response.ErrorListener mErrorListener;

    /**
     * 无参构造函数
     */
    public VolleyListenerInterface(){

    }

    /**
     * 有参构造函数
     * @param context
     * @param mSuccessListener
     * @param mErrorListener
     */
    public VolleyListenerInterface(Context context, Response.Listener<String> mSuccessListener, Response.ErrorListener mErrorListener){
        this.context = context;
        this.mSuccessListener = mSuccessListener;
        this.mErrorListener = mErrorListener;
    }

    /**
     * 请求成功时的回调函数
     * @param result
     */
    public abstract void onSuccess(String result);

    /**
     * 请求失败时的回调函数
     * @param error
     */
    public abstract void onError(VolleyError error);

    /**
     * 创建请求成功的事件监听
     * @return
     */
    public Response.Listener<String> responseListener(){
        mSuccessListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                onSuccess(s);
            }
        };
        return mSuccessListener;
    }

    /**
     * 创建请求失败时的事件监听
     * @return
     */
    public Response.ErrorListener errorListener(){
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onError(volleyError);
            }
        };
        return mErrorListener;
    }








}
