package com.team.car.fragment.manager;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.team.car.R;
import com.team.car.activitys.manager.CarIllegalQueryActivity;
import com.team.car.base.fragment.BaseFragment;
import com.team.car.widgets.ToastUtil;



/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class manageFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = manageFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private View view;//主视图
    private RelativeLayout carQuery;//违章查询

    @Override
    protected View initView() {
        view = View.inflate(context, R.layout.activity_manage, null);//加载主布局
        bindView();//初始化视图
        return view;
    }

    private void bindView() {
        carQuery = (RelativeLayout) view.findViewById(R.id.car_illegal_query);

        carQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_illegal_query:{
                Intent intent = new Intent(context, CarIllegalQueryActivity.class);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }
}
