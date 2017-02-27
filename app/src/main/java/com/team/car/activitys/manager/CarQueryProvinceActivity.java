package com.team.car.activitys.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.team.car.R;
import com.team.car.adapter.manager.ProvinceAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lmy on 2017/2/27.
 * email 1434117404@qq.com
 */

public class CarQueryProvinceActivity extends Activity implements View.OnClickListener{
    private ImageView back;
    private ListView listView;
    private ProvinceAdapter adapter;
    private List<String> list = new ArrayList<String>();
    private String[] name = {"安徽", "北京", "福建", "甘肃", "广东", "广西", "贵州", "海南",
            "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "辽宁", "宁夏", "青海", "山东", "山西", "陕西",
            "上海", "四川", "天津", "云南", "浙江", "重庆"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_query_province_list);
        back = (ImageView) findViewById(R.id.choose_city_back);
        listView = (ListView) findViewById(R.id.province_list);
        for (int i = 0; i < name.length; i++) {
            list.add(name[i]);
        }
        adapter = new ProvinceAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = list.get(position);
                Intent intent = new Intent(CarQueryProvinceActivity.this, CarQueryDetailCityActivity.class);
                intent.putExtra("nameProvince", name);
                startActivityForResult(intent, 2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose_city_back:{
                this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            Bundle bundle = data.getExtras();
            Intent intent = new Intent(CarQueryProvinceActivity.this, CarIllegalQueryActivity.class);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            CarQueryProvinceActivity.this.finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
