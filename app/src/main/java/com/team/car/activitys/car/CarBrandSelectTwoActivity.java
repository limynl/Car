package com.team.car.activitys.car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.team.car.R;
import com.team.car.adapter.car.CarBrandSelectTwoAdapter;
import com.team.car.entity.car.CarBrandSelectBean;
import com.team.car.widgets.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 汽车车型选择
 * Created by Lmy on 2017/2/18.
 * email 1434117404@qq.com
 */

public class CarBrandSelectTwoActivity extends Activity implements View.OnClickListener{
    private static final String TAG = CarBrandSelectTwoActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private ImageView carBrandBack;
    private ListView listView;
    private CarBrandSelectTwoAdapter adapter;

    private List<String> listModel;//存放该品牌汽车的具体车型，并将其传到起始界面，用于用户进一步选择车型
    private int location;//上一个界面点击的位置，当前界面用与获取到该brand的具体信息
    private String brand;//上一个Activity传过来的brand的一部份，当前界面进行拼接，组成该车的 品牌车系

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand_select_two);
        initView();
        setData();

    }

    /**
     * 初始化视图
     */
    private void initView() {
        carBrandBack = (ImageView) findViewById(R.id.car_brand_select_two_back);
        listView = (ListView) findViewById(R.id.car_brand_select_two);

        carBrandBack.setOnClickListener(this);
    }

    /**
     * 设置数据
     */
    private void setData() {
        final Bundle bundle = this.getIntent().getExtras();
        location = bundle.getInt("position");
        brand = bundle.getString("brand");
        String url = "http://api.jisuapi.com/car/carlist?appkey=d6c1207126bca7d5&parentid=" + location;

        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<CarBrandSelectBean> list = new ArrayList<CarBrandSelectBean>();
                List<String> listTwo = new ArrayList<String>();//暂存具体车型
                JSONObject jsonObject = null;
                CarBrandSelectBean bean = null;
                try {
                    jsonObject = new JSONObject(result);
                    JSONArray jsonArray1  = jsonObject.optJSONArray("result");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        jsonObject = jsonArray1.optJSONObject(i);
                        JSONArray jsonArray2 = jsonObject.optJSONArray("carlist");
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            jsonObject = jsonArray2.optJSONObject(j);
                            bean = new CarBrandSelectBean();
                            bean.setCarBrand(jsonObject.optString("fullname"));
                            bean.setName(jsonObject.optString("name"));
                            bean.setCarLogo(jsonObject.optString("logo"));
                            JSONArray jsonArray3 = jsonObject.optJSONArray("list");
                            if (jsonArray3 == null) {//防止空指针异常，因为有些汽车没有list字段
                                listTwo.add("此车没有更多信息！");
                            }else{
                                Log.e(TAG, "onSuccess: list" + jsonArray3.length());
                                for (int k = 0; k < jsonArray3.length(); k++) {
                                    jsonObject = jsonArray3.optJSONObject(k);
                                    listTwo.add(jsonObject.optString("name"));
                                }
                            }
                            bean.setList(listTwo);
                            list.add(bean);
                        }
                    }
                    adapter = new CarBrandSelectTwoAdapter(CarBrandSelectTwoActivity.this, list);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
                toastUtil.Short(CarBrandSelectTwoActivity.this, "获取数据失败").show();
            }

            @Override
            public void onCancelled(CancelledException cex) {}

            @Override
            public void onFinished() {}
        });

        //设置listView的监听，并将数据传回起始界面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                brand = brand + " " + ((CarBrandSelectBean) adapter.getItem(position)).getName();
                listModel = ((CarBrandSelectBean) adapter.getItem(position)).getList();
                Intent intent = new Intent(CarBrandSelectTwoActivity.this, AddCarActivity.class);
                Bundle b = new Bundle();
                b.putString("brands", brand);
                b.putSerializable("model", (Serializable) listModel);
                intent.putExtras(b);
                setResult(3, intent);
                CarBrandSelectTwoActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_brand_select_two_back:{
                this.finish();
            }
        }
    }
}
