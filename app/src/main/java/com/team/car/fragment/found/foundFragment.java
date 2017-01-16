package com.team.car.fragment.found;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.team.car.fragment.BaseFragment;

/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class foundFragment extends BaseFragment {
    @Override
    protected View initView() {
        TextView textView = new TextView(getContext());
        textView.setText("我是周边新鲜事");
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        return textView;
    }
}
