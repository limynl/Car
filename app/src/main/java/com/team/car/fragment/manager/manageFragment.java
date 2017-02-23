package com.team.car.fragment.manager;

import android.view.View;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.team.car.R;
import com.team.car.base.fragment.BaseFragment;
import com.team.car.entity.ActivityModel;
import com.team.car.utils.ScreenUtil;
import com.team.car.view.WavyLineView;
import com.team.car.widgets.ToastUtil;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class manageFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener {
    private static final String TAG = manageFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private View view;//主视图
    private SliderLayout mSliderLayout;
    private WavyLineView mWavyLine;
    private List<ActivityModel> mActivityModels;

    @Override
    protected View initView() {
        view = View.inflate(context, R.layout.activity_manage, null);//加载主布局
        bindView();//初始化视图
        loadSlider();//加载轮播图
        return view;
    }

    private void bindView() {
        mSliderLayout = (SliderLayout) view.findViewById(R.id.discover_slider);
        mWavyLine = (WavyLineView) view.findViewById(R.id.discover_wavyLine);

        int initStrokeWidth = 2;
        int initAmplitude = 10;
        float initPeriod = (float)(2 * Math.PI / 120);
        mWavyLine.setPeriod(initPeriod);
        mWavyLine.setAmplitude(initAmplitude);
        mWavyLine.setStrokeWidth(ScreenUtil.dp2px(initStrokeWidth));

    }

    private void loadSlider() {
        mActivityModels = new ArrayList<>();
        // 加载一些假数据
        loadSomeData();
        // 以下是发现模块的自动轮播
        mSliderLayout.setClickable(true);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);//设置在底部
        for (ActivityModel model : mActivityModels) {
            TextSliderView textSliderView = new TextSliderView(this.getActivity());
            textSliderView.description(model.getTitle());
            textSliderView.image(model.getId());
            textSliderView.setOnSliderClickListener(this);
            textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
            mSliderLayout.addSlider(textSliderView);
        }
        mSliderLayout.startAutoCycle();//开始自动滑动
    }

    private void loadSomeData() {
        mActivityModels.add(new ActivityModel(R.drawable.activity1,"大手牵小手，你我共成长，快乐每一天"));
        mActivityModels.add(new ActivityModel(R.drawable.activity2,"乐享童趣"));
        mActivityModels.add(new ActivityModel(R.drawable.activity3,"走出家门，用爱沟通"));
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        toastUtil.Short(context, slider.getDescription()).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSliderLayout.removeAllSliders();
        mSliderLayout = null;
    }
}
