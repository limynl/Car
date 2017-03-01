package com.team.car.fragment.repair;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.car.R;

/**
 * Created by Lmy on 2017/2/25.
 * email 1434117404@qq.com
 */

public class WeiXiuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weixiu,container,false);
        return view;
    }
}
