package com.team.car.fragment.manager;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.team.car.fragment.BaseFragment;

/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class manageFragment extends BaseFragment {
    private static final String TAG = manageFragment.class.getSimpleName();
    @Override
    protected View initView() {
        Log.e(TAG, "manageFragment创建了");
        TextView textView = new TextView(getContext());
        textView.setText("我是管家");
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        return textView;
    }
}
