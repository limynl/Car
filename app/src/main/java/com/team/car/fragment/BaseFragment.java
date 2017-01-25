package com.team.car.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public abstract class BaseFragment extends Fragment {
    protected Context context;//上下文对象

    /**
     * 创建Fragment时回调，只执行一次
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    /**
     * 将layout布局文件转换成View对象,每次创建都会绘制Fragment的View组件时回调该方法
     * @param inflater Fragment需要加载的布局文件
     * @param container 加载layout的父ViewGroup
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 强制子类重写，实现子类特有的UI
     * @return 返回需要加载的布局
     */
    protected abstract View initView();

    /**
     * 当Fragment所在的Activity启动完成后调用
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDate();
    }

    /**
     * 当子类需要初始化数据、联网请求数据、展示数据是可以重写该方法
     */
    public void initDate() {}

}
