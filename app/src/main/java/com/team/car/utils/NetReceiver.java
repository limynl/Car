package com.team.car.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.team.car.widgets.ToastUtil;

/**
 * Created by Lmy on 2017/1/24.
 * email 1434117404@qq.com
 */

public class NetReceiver extends BroadcastReceiver {
    private static final String TAG = NetReceiver.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();
    private   String netType=""; //网络类型

    @Override
    public void onReceive(Context context, Intent intent) {
     String action= intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            boolean isConnected = NetUtils.isNetworkConnected(context);
            boolean isConnectedTypeMobile=NetUtils.isMobileConnected(context);
            boolean isConnectedTypeWifi=NetUtils.isWifiConnected(context);
            if (isConnected) {
                if(isConnectedTypeWifi&&NetUtils.getConnectedType(context)==1){
                    this.setNetType("wifinet");
                }else if(isConnectedTypeMobile&&NetUtils.getConnectedType(context)==0){
                    this.setNetType("mobilenet");
                }
                toastUtil.Short(context, "网络已连接").show();
            } else {
                toastUtil.Short(context, "网络已断开").show();
                this.setNetType("nonet");
            }
        }
    }
    public String getNetType() {
        return netType;
    }
    public void setNetType(String netType) {
        this.netType = netType;
    }
}
