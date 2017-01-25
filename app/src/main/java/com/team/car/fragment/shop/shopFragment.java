package com.team.car.fragment.shop;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.team.car.fragment.BaseFragment;

/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class shopFragment extends BaseFragment {
    private static final String TAG = shopFragment.class.getSimpleName();
    @Override
    protected View initView() {
        Log.e(TAG, "shopFragment创建了");
        TextView textView = new TextView(getContext());
        textView.setText("我是商店");
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        return textView;
    }
}
