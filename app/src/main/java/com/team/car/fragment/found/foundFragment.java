package com.team.car.fragment.found;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.activitys.user.SettingActivity;
import com.team.car.fragment.BaseFragment;
import com.team.car.fragment.found.childfragment.carDynamicFragment;
import com.team.car.fragment.found.childfragment.newThingFragment;
import com.team.car.widgets.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class foundFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = foundFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();
    private RadioGroup rg_found;
    private List<Fragment> mFragments;//两个fragment的集合
    private Fragment content;//存储上一次fragment
    private int position;//标记fragment选中状态
    private ImageView foundMore;//更多
    private RadioButton newThing;//新鲜事
    private RadioButton carDynamic;//车动态

    private PopupWindow popupWindow ;
    private View mPopupWindowView;
    private TextView sendMood;
    private TextView sendHelp;

    private View view;//主视图

    /**
     * 初始化主布局
     * @return 返回需要加载的视图
     */
    @Override
    protected View initView() {
        Log.e(TAG, "foundFragment创建了");
        view = View.inflate(context, R.layout.activity_found, null);//加载主布局
        mPopupWindowView = LayoutInflater.from(context).inflate(R.layout.found_menu_select, null);//加载弹框
        //主布局中相应控件的初始化
        foundMore = (ImageView)view.findViewById(R.id.found_more);
        newThing = (RadioButton)view.findViewById(R.id.found_new_things);
        carDynamic = (RadioButton)view.findViewById(R.id.found_car_dynamic);

        //弹框中相应控件的初始化
        sendMood = (TextView) mPopupWindowView.findViewById(R.id.found_send_mood);
        sendMood.getBackground().setAlpha(100);
        sendHelp = (TextView) mPopupWindowView.findViewById(R.id.found_send_help);
        sendHelp.getBackground().setAlpha(100);

        return view;
    }

    @Override
    public void initDate() {
        super.initDate();
        initial();//初始化布局
        initFragment();//初始化fragment
        setListener();//设置监听
        initPopupWindow();//初始化弹出窗体

        foundMore.setOnClickListener(this);//监听更多
        sendMood.setOnClickListener(this);//监听发动态
        sendHelp.setOnClickListener(this);//监听求救
    }

    /**
     * 控件的点击事件
     * @param v ：选中的视图
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.found_more://发表动态
                toastUtil.Short(getContext(), "点击我查看更多^_^").show();
                showPopupWindow();
                break;
            case R.id.found_send_mood:
                toastUtil.Short(context, "发动态").show();
                startActivity(new Intent(context, SettingActivity.class));
                popupWindow.dismiss();
                break;
            case R.id.found_send_help:
                toastUtil.Short(context, "求救").show();
                popupWindow.dismiss();
                break;
        }
    }

    /**
     * 显示popupWindow
     */
    private void showPopupWindow(){
        if(!popupWindow.isShowing()){
            popupWindow.showAsDropDown(foundMore, foundMore.getLayoutParams().width/2, 31);
        }else{
            popupWindow.dismiss();
        }
    }

    /**
     * 初始化PopupWindow
     * 注意：popupWindow.setBackgroundDrawable()这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景；使用该方法点击窗体之外，才可关闭窗体
     *如果将setBackgroundDrawable设为null，那么将不会有任何效果，dismiss将失效
     */
    private void initPopupWindow(){
        popupWindow = new PopupWindow(mPopupWindowView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);//绑定视图
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);//点击外围也将失效
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.mipmap.clear));
		popupWindow.setAnimationStyle(R.style.popupwindow);//设置进入和退出效果
        popupWindow.update();
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
    }

    /**
     * 初始化布局控件
     */
    private void initial() {
        rg_found = (RadioGroup)view.findViewById(R.id.rg_found);
    }

    /**
     * 初始化fragment，添加要显示的fragment到list集合中
     */
    private void initFragment(){
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new newThingFragment());
        mFragments.add(new carDynamicFragment());
    }

    /**
     *设置fragment状态改变监听，并且初始化第一次要显示的fragment
     */
    private void setListener() {
        rg_found.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_found.check(R.id.found_new_things);//必须放在setOnCheckedChangeListener的后面，因为先要有点击，然后才能被选中
    }

    /**
     * 切换fragment
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.found_new_things://新鲜事
                    position = 0;
                    newThing.getBackground().setAlpha(100);
                    break;
                case R.id.found_car_dynamic://车动态
                    position = 1;
                    carDynamic.getBackground().setAlpha(100);
                    break;
                default://默认被选中的
                    position = 0;
                    newThing.getBackground().setAlpha(100);
                    break;
            }
            Fragment toFragment = mFragments.get(position);
            switchFragment (content, toFragment);
        }
    }

    /**
     * 转换fragment，其中对fragment进行了优化，防止每次切换fragment都要重新加载
     * @param fromFragment：上一个fragment
     * @param toFragment：要显示的下一个fragment
     */
    private void switchFragment(Fragment fromFragment, Fragment toFragment) {
        if(fromFragment != toFragment) {
            content = toFragment;//存储上一个Fragment
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            if(!toFragment.isAdded()) {//判断要显示的Fragment是否被添加
                //toFragment没被添加，先将fromFragment隐藏,再添加toFragment
                if(fromFragment != null) {//先进行判空操作(前一个fragment是否被添加)，防止空指针异常
                    ft.hide(fromFragment);
                }
                if(toFragment != null) {
                    ft.add(R.id.found_fl_content,toFragment).commit();
                }
            }else{
                //toFragment已经被添加，直接将fromFragment隐藏
                if(fromFragment != null) {
                    ft.hide(fromFragment);
                }
                if(toFragment != null) {//显示toFragment
                    ft.show(toFragment).commit();
                }
            }
        }
    }
}
