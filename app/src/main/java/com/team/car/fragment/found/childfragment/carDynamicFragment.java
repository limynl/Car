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

public class carDynamicFragment extends Fragment {
    private static final String TAG  = carDynamicFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();
    private TextView view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "carDynamicFragment创建了");
        view = new TextView(getContext());
        view.setText("车动态");
        view.setTextColor(Color.parseColor("#7ADFB8"));
        view.setTextSize(20);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
