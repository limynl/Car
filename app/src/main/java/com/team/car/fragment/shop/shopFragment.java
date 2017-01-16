package com.team.car.fragment.shop;

import android.view.View;
import android.widget.TextView;

import com.team.car.fragment.BaseFragment;

/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class shopFragment extends BaseFragment {
    @Override
    protected View initView() {
        TextView textView = new TextView(getContext());
        textView.setText("我是商店");
        textView.setTextSize(25);
        return textView;
    }
}
