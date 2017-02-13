package com.team.car.fragment.shop;

import android.graphics.drawable.PaintDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.team.car.R;
import com.team.car.base.fragment.BaseFragment;
import com.team.car.fragment.shop.adapter.shopSelectAdapter;
import com.team.car.widgets.ToastUtil;

/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class shopFragment extends BaseFragment {
    private static final String TAG = shopFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();
    private View view;
    private RadioGroup rg_main;
    private RadioButton tab1, tab2, tab3;
    /**
     * popupWindow中的listView
     */
    private ListView listView;

    private static String[] dataZero = {"默认数据"};
    private String[] dataOne = {"车身打蜡","车身抛光", "车身封釉", "油漆面", "车辆贴膜", "汽车改装"};
    private String[] dataTwo = {"默认-智能排序", "距离-从近到远", "订单-从多到少", "价格-从低到高"};
    private String[] dataThree = {"全市", "锦江区", "青羊区", "金牛区", "武侯区", "成华区", "高新区", "龙泉驿区"};

    private shopSelectAdapter adapter;

    private PopupWindow popupWindow;

    /**
     * 弹出PopupWindow时背景变暗
     */
    private View darkView;

    /**
     * 弹出PopupWindow时，背景变暗的动画
     */
    private Animation animIn, animOut;

    /**
     * 加载主布局
     * @return
     */
    @Override
    protected View initView() {
        Log.e(TAG, "shopFragment创建了");
        view = View.inflate(context, R.layout.activity_shop, null);
        return view;
    }

    /**
     * 用于初始化数据，请求数据，展示数据
     */
    @Override
    public void initDate() {
        super.initDate();
        findView();
        initListener();
        initPopup();
    }


    /**
     * 初始化控件
     */
    private void findView() {
        tab1 = (RadioButton) view.findViewById(R.id.shop_selector_tab1);
        tab2 = (RadioButton) view.findViewById(R.id.shop_selector_tab2);
        tab3 = (RadioButton) view.findViewById(R.id.shop_selector_tab3);
        rg_main = (RadioGroup) view.findViewById(R.id.shop_rg_main);
        darkView = view.findViewById(R.id.shop_darkview);

        animIn = AnimationUtils.loadAnimation(context, R.anim.popupwindow_slide_in_from_top);
        animOut = AnimationUtils.loadAnimation(context, R.anim.popupwindow_slide_out_to_top);
    }

    /**
     * 设置RadioGroup的监听事件
     */
    private void initListener() {
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
    }

    private void initPopup() {
        View viewPop = LayoutInflater.from(context).inflate(R.layout.shop_popup_layout, null);
        listView = (ListView) viewPop.findViewById(R.id.shop_pop_listview);
        popupWindow = new PopupWindow(viewPop, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.shop_popwin_anim_style);
        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);
                rg_main.clearCheck();
//                listView.setSelection(0);
            }
        });

        adapter = new shopSelectAdapter(context);
        adapter.setData(dataZero);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = (String) adapter.getItem(position);
                tab1.setText(selectedName);
                popupWindow.dismiss();
                toastUtil.Short(context, "您选中的是：" + selectedName).show();
            }
        });
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if(group.getChildAt(i).getId() == checkedId){
                    popupWindow.dismiss();
                }
            }
            switch (checkedId){
                case R.id.shop_selector_tab1:
                    showPopupWindow();
                    adapter.setData(dataOne);
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.shop_selector_tab2:
                    showPopupWindow();
                    adapter.setData(dataTwo);
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.shop_selector_tab3:
                    showPopupWindow();
                    adapter.setData(dataThree);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 弹出弹窗
     */
    private void showPopupWindow() {
        popupWindow.showAsDropDown(view.findViewById(R.id.shop_div_line));
        popupWindow.setAnimationStyle(-1);
        //背景变暗
        darkView.startAnimation(animIn);
        darkView.setVisibility(View.VISIBLE);
    }
}
