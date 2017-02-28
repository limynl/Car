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

public class CarQueryDetailCityActivity extends Activity {
    private ImageView back;
    private ListView listView;
    private ProvinceAdapter adapter;
    private List<String> list = new ArrayList<String>();
    private String[] name = {"成都市", "自贡市", "攀枝花市", "泸州市", "德阳市", "绵阳市", "广元市", "遂宁市",
            "内江市", "乐山市", "南充市", "宜宾市", "广安市", "达州市", "眉山市", "雅安市", "巴中市"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_query_province_list);
        listView = (ListView) findViewById(R.id.province_list);
        for (int i = 0; i < name.length; i++) {
            list.add(name[i]);
        }
        adapter = new ProvinceAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name  = list.get(position);
                Intent intent = new Intent(CarQueryDetailCityActivity.this, CarQueryProvinceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nameProvince", getIntent().getStringExtra("nameProvince"));
                bundle.putString("nameCity", name);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                CarQueryDetailCityActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        back = (ImageView) findViewById(R.id.choose_city_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarQueryDetailCityActivity.this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }
}
