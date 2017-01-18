package com.team.car.fragment.home;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.android.volley.VolleyError;
import com.team.car.R;
import com.team.car.fragment.BaseFragment;
import com.team.car.utils.volley.VolleyListenerInterface;
import com.team.car.utils.volley.VolleyRequestUtil;
import com.team.car.widgets.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class homeFragment extends BaseFragment {
    ToastUtil toastUtil = new ToastUtil();
    @Override
    protected View initView() {

        View view = View.inflate(context, R.layout.activity_home, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.ceshi);
        RadioButton radioButton = (RadioButton)view.findViewById(R.id.click);


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

        return view;
    }
}
