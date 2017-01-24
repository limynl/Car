package com.team.car.activitys.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Created by Lmy on 2017/1/24.
 * email 1434117404@qq.com
 */

public class NetReceiver extends BroadcastReceiver {
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
                }
                else if(isConnectedTypeMobile&&NetUtils.getConnectedType(context)==0){
                    this.setNetType("mobilenet");
                }
                //Toast.makeText(context, "已经连接网络", Toast.LENGTH_LONG).show();
            } else {
//	        	Toast.makeText(context, "已经断开网络", Toast.LENGTH_LONG).show();
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
