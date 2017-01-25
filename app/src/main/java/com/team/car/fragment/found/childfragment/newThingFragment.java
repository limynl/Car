package com.team.car.fragment.found.childfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team.car.widgets.ToastUtil;

/**
 * Created by Lmy on 2017/1/25.
 * email 1434117404@qq.com
 */

public class newThingFragment extends Fragment {
    private static final String TAG = newThingFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();
    private TextView view;

    /**
     * 初始化要加载的视图
     * @return 返回需要加载的视图
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "newThingFragment创建了");
        view = new TextView(getContext());
        view.setText("周围新鲜事");
        view.setTextColor(Color.parseColor("#7ADFB8"));
        view.setTextSize(20);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
