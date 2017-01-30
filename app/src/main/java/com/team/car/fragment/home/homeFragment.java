package com.team.car.fragment.home;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.fragment.BaseFragment;
import com.team.car.services.LocationServices;
import com.team.car.widgets.ToastUtil;

/**
 * 注：这个fragment要用到上下文对象时，使用父类中的——context
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class homeFragment extends BaseFragment {
    private static final String TAG = homeFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private TextView location;//保存当前位置
    private LocationReceive locationReceive;
    private LocationServices.LocationBinder locationBinder;

    //创建一个服务连接
    private ServiceConnection locationConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            locationBinder = (LocationServices.LocationBinder) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            toastUtil.Short(context, "位置定位失败").show();
        }
    };

    @Override
    protected View initView() {
        Log.e(TAG, "homeFragment创建了");

        View view = View.inflate(context, R.layout.activity_home, null);
        location = (TextView)view.findViewById(R.id.main_location);
        return view;
    }

    @Override
    public void initDate() {
        super.initDate();
        initialLocation();//初始化地理信息
    }

    private void initialLocation() {
        locationReceive = new LocationReceive();//注册广播
        IntentFilter intentFilter = new IntentFilter();//添加过滤器
        intentFilter.addAction(LocationServices.ACTION_UPDATE_LOCATION);
        context.registerReceiver(locationReceive, intentFilter);
        Intent locationIntent = new Intent(context, LocationServices.class);
        context.bindService(locationIntent, locationConn, Service.BIND_AUTO_CREATE);//绑定服务
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationBinder.stopLocation();
        context.unbindService(locationConn);
        context.unregisterReceiver(locationReceive);
    }

    /**
     * 定义一个广播接收器，用于接受当前的地理信息
     */
    class LocationReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(LocationServices.ACTION_UPDATE_LOCATION)){
                location.setText(intent.getStringExtra(LocationServices.ACTION_UPDATE_LOCATION));
                toastUtil.Long(context, intent.getStringExtra("detailLocation"));
            }
        }
    }
}


























/*
//以下为测试Volley框架代码
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "http://139.199.23.142:8080/TestShowMessage1/lmy/RegisterServlet";


                String url = null;
                Map<String, String> map = new HashMap<>();
                map.put("title", "zhangsan");
                map.put("image", "ImageView");
                map.put("breifMessage", "lisi");
                map.put("detailMessage", "wangwu");
                VolleyRequestUtil.RequestPost(context, url, "tag", map, new VolleyListenerInterface(context, VolleyListenerInterface.mSuccessListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        try{
                            JSONObject jsonObject = new JSONObject(result);
                            String flag = jsonObject.getString("flag");
                            toastUtil.Long(context, "返回结果:" + result).show();
                            Log.e("TAG", "onSuccess: " + result);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        toastUtil.Short(context, error.toString()).show();
                    }
                });
            }
        });




* */
