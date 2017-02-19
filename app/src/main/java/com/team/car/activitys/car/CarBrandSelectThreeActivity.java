package com.team.car.activitys.car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.team.car.R;
import com.team.car.widgets.ToastUtil;

import java.util.List;

/**
 * 汽车具体车型选择
 * Created by Lmy on 2017/2/18.
 * email 1434117404@qq.com
 */

public class CarBrandSelectThreeActivity extends Activity implements View.OnClickListener{
    private static final String TAG = CarBrandSelectThreeActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private ImageView back;
    private ListView listView;

    private List<String> listModel;//详细车型数据
    private ArrayAdapter<String> adapter;
    private String[] specificModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand_select_three);
        initView();//初始化视图
        setData();//设置数据

    }

    /**
     * 初始化是图
     */
    private void initView() {
        back = (ImageView) findViewById(R.id.car_brand_select_three_back);
        listView = (ListView) findViewById(R.id.car_brand_select_three);

        back.setOnClickListener(this);
    }

    /**
     * 设置数据
     */
    private void setData() {
        Bundle bundle = getIntent().getExtras();
        listModel = bundle.getStringArrayList("model");
        specificModel = new String[listModel.size()];
        for (int i = 0; i < listModel.size(); i++) {
            specificModel[i] = listModel.get(i);
        }
        adapter = new ArrayAdapter<String>(CarBrandSelectThreeActivity.this, android.R.layout.simple_list_item_1, specificModel);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String model = adapter.getItem(position);
                Intent intent = new Intent(CarBrandSelectThreeActivity.this, AddCarActivity.class);
                intent.putExtra("specificModel", model);
                setResult(RESULT_OK, intent);
                CarBrandSelectThreeActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    /**
     * 返回添加界面
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_brand_select_three_back:{
                this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            break;
        }
    }
}
