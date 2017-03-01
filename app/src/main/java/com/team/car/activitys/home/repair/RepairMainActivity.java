package com.team.car.activitys.home.repair;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.team.car.R;
import com.team.car.fragment.repair.OrderFragment;
import com.team.car.fragment.repair.PeiJianFragment;
import com.team.car.fragment.repair.WeiXiuFragment;
import com.team.car.widgets.ToastUtil;

/**
 * Created by Lmy on 2017/2/25.
 * email 1434117404@qq.com
 */

public class RepairMainActivity extends FragmentActivity implements View.OnClickListener{
    private static final String TAG = RepairMainActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private ImageView back;

    private String[] tabNames = {"维修商", "配件商"};
    private int[] tabIcons = {R.drawable.tab_discover, R.drawable.tab_mine};
    private SpaceNavigationView mTab;
    private WeiXiuFragment wxFragment;
    private PeiJianFragment pjFragment;
    private OrderFragment orderFragment;
    private Fragment mFragment;
    private final int CONTENT_ID = R.id.repair_main_content;
    private FragmentManager fg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_main);
        fg = getSupportFragmentManager();

        // 初次加载的时候显示首页布局
        wxFragment = new WeiXiuFragment();
        fg.beginTransaction().add(CONTENT_ID, wxFragment).commit();
        bindView();//初始化布局控件
        // 下面是开源底部导航栏
        for (int i = 0; i < tabNames.length; i++) {
            mTab.addSpaceItem(new SpaceItem(tabNames[i], tabIcons[i]));
        }
        mTab.showIconOnly();
        mTab.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                toastUtil.Short(RepairMainActivity.this, "点击了中间的按钮").show();
                gotoCenterFragment();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                toastUtil.Short(RepairMainActivity.this, "你点击了"+itemName+"("+itemIndex+")").show();
                gotoOtherFragment(itemName);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                toastUtil.Short(RepairMainActivity.this, "重复点击").show();
                gotoOtherFragment(itemName);
            }
        });

        back.setOnClickListener(this);
    }

    /**
     * 初始化视图
     */
    private void bindView() {
        back = (ImageView) findViewById(R.id.car_repair_back);
        mTab = (SpaceNavigationView) findViewById(R.id.repair_main_tab);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mTab.onSaveInstanceState(outState);
    }

    private void swithFragment(Fragment fragment) {
        if (mFragment != fragment) {
            if (!fragment.isAdded()) {
                fg.beginTransaction().hide(mFragment).add(CONTENT_ID, fragment).commit();
            } else {
                fg.beginTransaction().hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }

    /**
     * 进入中间的订单中心
     */
    private void gotoCenterFragment() {
        hideFragment();
        if (orderFragment == null) {
            orderFragment = new OrderFragment();
            fg.beginTransaction().add(CONTENT_ID, orderFragment).commit();
        } else {
            fg.beginTransaction().show(orderFragment).commit();
        }
    }

    /**
     * 点击tab显示相应的界面
     * @param itemName
     */
    private void gotoOtherFragment(String itemName) {
        hideFragment();
        switch (itemName) {
            case "维修商":
                if (wxFragment == null) {
                    wxFragment = new WeiXiuFragment();
                    fg.beginTransaction().add(CONTENT_ID, wxFragment).commit();
                } else {
                    fg.beginTransaction().show(wxFragment).commit();
                }
                break;
            case "配件商":
                if (pjFragment == null) {
                    pjFragment = new PeiJianFragment();
                    fg.beginTransaction().add(CONTENT_ID, pjFragment).commit();
                } else {
                    fg.beginTransaction().show(pjFragment).commit();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏所有Fragment
     */
    private void hideFragment() {
        if (wxFragment != null) {
            fg.beginTransaction().hide(wxFragment).commit();
        }
        if (pjFragment != null) {
            fg.beginTransaction().hide(pjFragment).commit();
        }
        if (orderFragment != null) {
            fg.beginTransaction().hide(orderFragment).commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_repair_back:{
                this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            break;
        }
    }

    /**
     * 手机系统返回键
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
