package com.team.car.fragment.home;


import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.team.car.fragment.BaseFragment;

/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class homeFragment extends BaseFragment {
    @Override
    protected View initView() {
        TextView textView = new TextView(getContext());
        textView.setText("我是主页");
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        return textView;
    }
}
