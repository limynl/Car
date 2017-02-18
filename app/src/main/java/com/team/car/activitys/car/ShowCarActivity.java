package com.team.car.activitys.car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.team.car.R;
import com.team.car.adapter.car.ShowCarAdapter;
import com.team.car.entity.car.CarBean;
import com.team.car.utils.volley.VolleyListenerInterface;
import com.team.car.utils.volley.VolleyRequestUtil;
import com.team.car.widgets.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Lmy on 2017/1/15.
 * email 1434117404@qq.com
 */
public class ShowCarActivity extends Activity implements View.OnClickListener{
    private static final String TAG = ShowCarActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private ImageView showCarBack;
    private ImageView showCarAdd;
    private ListView listView;

    private ShowCarAdapter showCarAdapter;
    private List<CarBean> list = new ArrayList<CarBean>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_show_car);
        initView();//初始化布局
        getAllCarData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        /*
        toastUtil.Short(ShowCarActivity.this, "添加我的爱车").show();
                Intent intent = new Intent(ShowCarActivity.this, AddCarActivity.class);
                startActivity(intent);
                ShowCarActivity.this.finish();
                overridePendingTransition(R.anim.in_from_left_two, R.anim.out_from_right_two);//左右滑动开启界面

        * */
        showCarBack = (ImageView) findViewById(R.id.show_car_back);
        showCarAdd = (ImageView) findViewById(R.id.show_car_add);

        listView = (ListView) findViewById(R.id.show_car_listview);

        showCarBack.setOnClickListener(this);
        showCarAdd.setOnClickListener(this);
    }

    /**
     * 设置点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_car_back:{
                ShowCarActivity.this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
                break;
            case R.id.show_car_add:{
                toastUtil.Short(ShowCarActivity.this, "添加爱车").show();
                Intent intent = new Intent(ShowCarActivity.this, AddCarActivity.class);
                startActivity(intent);
                ShowCarActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            break;
        }
    }

    /**
     * 初始化适配器
     */
    private void setData(List<CarBean> carBeanList) {
        showCarAdapter = new ShowCarAdapter(ShowCarActivity.this, carBeanList);
        showCarAdapter.setBtnClickListener(new ShowCarAdapter.btnClickListener() {
            @Override
            public void btnDeleteClick(int position) {
                //显示弹框防止用户误操作
                deleteCar(position);//
                toastUtil.Short(ShowCarActivity.this, "删除成功").show();
            }

            @Override
            public void btnEditClick(int position) {
                toastUtil.Short(ShowCarActivity.this, "编辑成功").show();
            }
        });
        listView.setAdapter(showCarAdapter);
    }

    /**
     * 得到当前用户所有的车辆
     */
    public void getAllCarData() {
        //首先模拟一些假数据，用于测试
        for (int i = 0; i < 2; i++) {
            CarBean carBean = new CarBean();
            carBean.setCarIconUrl("http://139.199.23.142:8080/TestShowMessage1/test.png");
            carBean.setCarNumber("川A12345");
            carBean.setCarBrand("奥迪 Q5");
            carBean.setCarSpecificModel("2015款 40 TFSI 豪华型");
            list.add(carBean);
        }
        setData(list);
    }

    int location;//记录要删除的位置
    /**
     * 删除用户指定的车辆信息，并更改服务器中的数据
     * @param position
     */
    private void deleteCar(int position) {
        location = position;
        String carDeleteUrl = null;//访问服务器的地址

        Map<String, String> map = new HashMap<>();
        map.put("userId", "用户的ID");
        map.put("carId", "要删除的车信息ID");
        VolleyRequestUtil.RequestPost(ShowCarActivity.this, carDeleteUrl, "tag", map, new VolleyListenerInterface(ShowCarActivity.this, VolleyListenerInterface.mSuccessListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onSuccess(String result) {
                try{
                    if (result != null) {
                        JSONObject jsonObject = new JSONObject(result);
                        String flag = jsonObject.getString("flag");
                        toastUtil.Short(ShowCarActivity.this, "删除成功:" + result).show();
                        Log.e(TAG, "onSuccess: " + result);
                    }else {
                        toastUtil.Short(ShowCarActivity.this, "删除失败！").show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {
                toastUtil.Short(ShowCarActivity.this, "删除失败" + error.toString()).show();
            }
        });
    }
}
