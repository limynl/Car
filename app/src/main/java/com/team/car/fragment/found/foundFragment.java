package com.team.car.fragment.found;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.team.car.R;
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
    private ImageView sendMood;//发动态
    private RadioButton newThing;//新鲜事
    private RadioButton carDynamic;//车动态

    private View view;

    /**
     * 初始化主布局
     * @return 返回需要加载的视图
     */
    @Override
    protected View initView() {
        Log.e(TAG, "foundFragment创建了");
        view = View.inflate(context, R.layout.activity_found, null);
        sendMood = (ImageView)view.findViewById(R.id.found_send_mood);
        newThing = (RadioButton)view.findViewById(R.id.found_new_things);
        carDynamic = (RadioButton)view.findViewById(R.id.found_car_dynamic);
        return view;
    }

    @Override
    public void initDate() {
        super.initDate();
        initial();//初始化布局
        initFragment();//初始化fragment
        setListener();//设置监听



        sendMood.setOnClickListener(this);
    }

    /**
     * 控件的点击事件
     * @param v ：选中的视图
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.found_send_mood://发表动态
                toastUtil.Short(getContext(), "点击我发表动态^_^").show();
                break;
        }
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
//        mFragments.add(new FragmentPage1());
//        mFragments.add(new FragmentPage2());
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
                default:
                    position = 0;
                    newThing.getBackground().setAlpha(100);
                    break;
            }
            Fragment toFragment = mFragments.get(position);
            switchFragment (content, toFragment);
        }
    }

    /**
     * 转换fragment
     * @param fromFragment：上一个fragment
     * @param toFragment：要显示的下一个fragment
     */
    private void switchFragment(Fragment fromFragment, Fragment toFragment) {
        if(fromFragment != toFragment) {
            //实现切换
            content = toFragment;
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            //判断是否被添加
            if(!toFragment.isAdded()) {
                //to没被添加，from隐藏,添加to
                if(fromFragment != null) {
                    ft.hide(fromFragment);
                }
                if(toFragment != null) {
                    ft.add(R.id.found_fl_content,toFragment).commit();
                }
            }else{
                //to已经被添加，from隐藏
                if(fromFragment != null) {
                    ft.hide(fromFragment);
                }
                //显示to
                if(toFragment != null) {
                    ft.show(toFragment).commit();
                }
            }
        }
    }

}
