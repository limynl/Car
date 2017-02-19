package com.team.car.activitys.car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.team.car.R;
import com.team.car.adapter.car.ShowCarAdapter;
import com.team.car.entity.car.CarBean;
import com.team.car.widgets.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 显示我的爱车列表
 * Created by Lmy on 2017/1/15.
 * email 1434117404@qq.com
 */
public class ShowCarActivity extends Activity implements View.OnClickListener{
    private static final String TAG = ShowCarActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private ImageView showCarBack;
    private ImageView showCarAdd;
    private ImageView noCar;
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
        showCarBack = (ImageView) findViewById(R.id.show_car_back);
        showCarAdd = (ImageView) findViewById(R.id.show_car_add);
        noCar = (ImageView) findViewById(R.id.no_car);

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
                Bundle bundle = new Bundle();
                bundle.putInt("flag", 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
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
                deleteCar(position);
                if(list.size() == 0){
                    noCar.setVisibility(View.VISIBLE);
                }
                toastUtil.Short(ShowCarActivity.this, "删除成功").show();
            }

            @Override
            public void btnEditClick(int position) {
                toastUtil.Short(ShowCarActivity.this, "开始修改").show();
                Intent intent = new Intent(ShowCarActivity.this, AddCarActivity.class);
                CarBean carBean = list.get(position);
                Log.e(TAG, "btnEditClick: " + carBean.toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("updateCar", carBean);
                bundle.putInt("position", position);//该车的编号
                bundle.putInt("flag", 2);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        listView.setAdapter(showCarAdapter);
    }

    /**
     * 得到当前用户所有的车辆(通过发送用户的指定ID到服务器进行匹配，成功就返回该用户所用车辆)
     */
    public void getAllCarData() {
        //首先模拟一些假数据，用于测试
        for (int i = 0; i < 2; i++) {
            CarBean carBean = new CarBean();
            carBean.setCarIconUrl("http://139.199.23.142:8080/TestShowMessage1/test.png");
            carBean.setCarNumber("川A12345");
            carBean.setCarFrameNum("112233");
            carBean.setCarEngineNum("147147");
            carBean.setCarBrand("奥迪 Q5");
            carBean.setCarSpecificModel("2015款 40 TFSI 豪华型");
            list.add(carBean);
        }
        setData(list);
    }

    int location;//记录要删除的位置
    /**
     * 删除用户指定的车辆信息，并更改服务器中的数据和刷新本地的列表显示
     * @param position
     */
    private void deleteCar(int position) {
        location = position;
        String carDeleteUrl = null;//访问服务器的地址


        //这是假数据，只是用于测试
        list.remove(location);
        showCarAdapter.notifyDataSetChanged();//更新爱车列表


        /*Map<String, String> map = new HashMap<>();
        map.put("userId", "用户的ID");
        map.put("carId", "要删除的车信息ID");
        VolleyRequestUtil.RequestPost(ShowCarActivity.this, carDeleteUrl, "tag", map, new VolleyListenerInterface(ShowCarActivity.this, VolleyListenerInterface.mSuccessListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onSuccess(String result) {
                try{
                    if (result != null) {
                        JSONObject jsonObject = new JSONObject(result);
                        String flag = jsonObject.getString("flag");

                        list.remove(location);
                        showCarAdapter.notifyDataSetChanged();//更新爱车列表

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
        });*/
    }

    /**
     * 添加界面传回来的数据，用户更新列表
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 返回来的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){//添加界面传过来的值
            Bundle bundle = data.getExtras();
            Log.e(TAG, "onActivityResult: 接收到的值为：" + bundle.getSerializable("carBean"));
            list.add((CarBean) bundle.getSerializable("carBean"));
            if(list.size() > 0){
                noCar.setVisibility(View.GONE);
            }
            showCarAdapter.notifyDataSetChanged();//更新爱车列表
        }else if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            Bundle bundle = data.getExtras();
            int position = bundle.getInt("position");
            list.set(position, (CarBean) bundle.getSerializable("carBean"));
            showCarAdapter.notifyDataSetChanged();//更新编辑内容
        }else{
            toastUtil.Short(ShowCarActivity.this, "添加或编辑没有返回数据").show();
        }
    }
}
