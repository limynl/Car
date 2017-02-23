package com.team.car.fragment.home;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.team.car.R;
import com.team.car.activitys.home.CarAddOilActivity;
import com.team.car.base.fragment.BaseFragment;
import com.team.car.widgets.ToastUtil;

/**
 * context为父类中的上下文对象
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class homeFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = homeFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private RelativeLayout carWeiXiu;
    private RelativeLayout carBy;
    private RelativeLayout carJy;


    @Override
    protected View initView() {
        Log.e(TAG, "homeFragment创建了");
        View view = View.inflate(context, R.layout.activity_home, null);
        carWeiXiu = (RelativeLayout) view.findViewById(R.id.home_car_weixiu);
        carBy = (RelativeLayout) view.findViewById(R.id.home_car_baoyang);
        carJy = (RelativeLayout) view.findViewById(R.id.home_car_jiayou);
        return view;
    }

    @Override
    public void initDate() {
        super.initDate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_car_weixiu:{//汽车故障

            }
            break;
            case R.id.home_car_baoyang:{//汽车保养

            }
            break;
            case R.id.home_car_jiayou:{//汽车加油
                Intent intent = new Intent(context, CarAddOilActivity.class);
            }
            break;
        }
    }
}

