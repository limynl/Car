package com.team.car.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lmy on 2017/1/28.
 * email 1434117404@qq.com
 */

public class LocationServices extends Service{
    private static final String TAG = LocationServices.class.getSimpleName();
    public LatLng latlng;//定位点信息
    private String strLocationProvince;//定位点的省份
    private String strLocationCity;//定位点的城市
    private String strLocationDistrict;//定位点的区县
    private String strLocationStreet;//定位点的街道信息
    private String strLocationStreetNumber;//定位点的街道号码
    private String strLocationAddrStr;//定位点的详细地址(包括国家和以上省市区等信息)
    private LocationClient mLocationClient =null;//定位客户端
    public MyLocationListener mMyLocationListener = new MyLocationListener();
    private boolean flag = true;
    private LocationBinder binder;
    private TimerTask task = null;
    private Timer timer = null;
    private boolean isStop = false;
    public static final String ACTION_UPDATE_LOCATION = "com.team.car.location.UPDATE_LOCATION";
    private static final int updateLocation = 1;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case updateLocation:
                    updateLocation();
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.setLocOption(setLocationClientOption());
        mLocationClient.registerLocationListener(mMyLocationListener);
        mLocationClient.start();
//        if (!isStop) {
//            startTimer();
//        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new LocationBinder();
        return binder;
    }

    public void updateLocation() {
        if (!isStop) {
            startTimer();
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_LOCATION);
        intent.putExtra(ACTION_UPDATE_LOCATION, strLocationDistrict);
        intent.putExtra("detailLocation", strLocationAddrStr);
        sendBroadcast(intent);
        handler.sendEmptyMessageDelayed(updateLocation, 10000);
    }

    private void startTimer() {
        isStop = true;//定时器启动后，修改标识，关闭定时器的开关
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {

                @Override
                public void run() {
                    do {
                        try {
                            mLocationClient.start();
                            Thread.sleep(1000 * 3);//3秒后再次执行
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (isStop);
                }
            };
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        isStop = false;//重新打开定时器开关
    }

    /**
     * 定位客户端参数设定
     * @return
     */
    private LocationClientOption setLocationClientOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setScanSpan(1000);//每隔1秒发起一次定位
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setOpenGps(true);//是否打开gps
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到该描述，不设置则在4G情况下会默认定位到“天安门广场”
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要，不设置则拿不到定位点的省市区信息
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        /*可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        该参数若不设置，则在4G状态下，会出现定位失败，将直接定位到天安门广场 */
        return option;
    }
    /**
     *
     * 定位监听器
     * @author User
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location==null) {
                return;
            }
            //获得定位信息的经度与纬度信息
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latlng = new LatLng(lat, lng);
            //定位点地址信息做非空判断
            if ("".equals(location.getProvince())) {
                strLocationProvince = "未知省";
            }else {
                strLocationProvince = location.getProvince();
            }
            if ("".equals(location.getCity())) {
                strLocationCity = "未知市";
            }else {
                strLocationCity = location.getCity();
            }
            if ("".equals(location.getDistrict())) {
                strLocationDistrict = "未知区";
            }else {
                strLocationDistrict = location.getDistrict();
            }
            if ("".equals(location.getStreet())) {
                strLocationStreet = "未知街道";
            }else {
                strLocationStreet = location.getStreet();
            }
            if ("".equals(location.getStreetNumber())) {
                strLocationStreetNumber = "";
            }else {
                strLocationStreetNumber =location.getStreetNumber();
            }
            if ("".equals(location.getAddrStr())) {
                strLocationAddrStr = "";
            }else {
                strLocationAddrStr =location.getAddrStr();
            }
            //定位成功后对获取的数据依据需求自定义处理
            Log.e(TAG, "latlng.lat="+lat);
            Log.e(TAG, "latlng.lng="+lng);
            Log.e(TAG, "省份="+strLocationProvince);//省份
            Log.e(TAG, "城市="+strLocationCity);//城市
            Log.e(TAG, "区县="+strLocationDistrict);//区县
            Log.e(TAG, "街道="+strLocationStreet);//街道
            Log.e(TAG, "街道号码="+strLocationStreetNumber);//街道号码
            Log.e(TAG, "详细地址"+strLocationAddrStr);//详细地址

            handler.sendEmptyMessage(updateLocation);//发送消息进行反复定位

            // 到此定位成功，没有必要反复定位 ，应该停止客户端再发送定位请求
            if (mLocationClient.isStarted()) {
                Log.e(TAG, "mLocationClient.isStarted()==>mLocationClient.stop()");
                mLocationClient.stop();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient!=null) {
            mLocationClient.stop();//停止定位服务
        }
        flag = false;
        if (isStop) {
            Log.i("tag", "定时器服务停止");
            stopTimer();
        }
    }

    public class LocationBinder extends Binder {
        public void stopLocation(){
            flag = false;
        }
        public void startLocation(){
            flag = true;
        }
    }


}
