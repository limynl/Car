package com.team.car.activitys.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.team.car.R;
import com.team.car.activitys.MainActivity;
import com.team.car.widgets.SlideSwitch;
import com.team.car.widgets.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lmy on 2017/1/18.
 * email 1434117404@qq.com
 */

public class SettingActivity extends Activity {
    @Bind(R.id.setting_account_bind)
    ImageView settingAccountBind;//账号绑定
    @Bind(R.id.setting_slide_switch)
    SlideSwitch settingSlideSwitch;//消息推送
    @Bind(R.id.setting_version_update)
    ImageView settingVersionUpdate;//版本更新
    @Bind(R.id.setting_feedback)
    ImageView settingFeedback;//意见反馈
    @Bind(R.id.setting_retreat)
    Button settingRetreat;//退出账号
    private ToastUtil toastUtil = new ToastUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        //消息推送点击事件
        settingSlideSwitch.setOnStateChangedListener(new SlideSwitch.OnStateChangedListener() {
            @Override
            public void onStateChanged(boolean state) {
                if (state == true) {
                    toastUtil.Long(SettingActivity.this, "开关打开").show();
                } else {
                    toastUtil.Long(SettingActivity.this, "开关关闭").show();
                }
            }
        });


    }

    @OnClick({R.id.setting_back, R.id.setting_account_bind, R.id.setting_version_update, R.id.setting_feedback, R.id.setting_retreat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_back:
            {
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.in_from_left_two, R.anim.out_from_right_two);//左右滑动效果
            }
                break;
            case R.id.setting_account_bind:
            {
                toastUtil.Long(SettingActivity.this, "账号绑定").show();
            }
                break;
            case R.id.setting_version_update:
            {
                toastUtil.Long(SettingActivity.this, "版本更新").show();
            }
                break;
            case R.id.setting_feedback:
            {
                toastUtil.Long(SettingActivity.this, "意见反馈").show();
            }
                break;
            case R.id.setting_retreat:
            {
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
                break;
        }
    }
}
