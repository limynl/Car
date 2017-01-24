package com.team.car.activitys.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.team.car.R;

import java.util.ArrayList;

/**
 * Created by Lmy on 2017/1/24.
 * email 1434117404@qq.com
 */

public class SelectCity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private String[] citys = {"自动定位", "北京", "上海", "广州", "深圳", "厦门", "天津", "重庆", "珠海", "苏州", "石家庄", "太原", "呼和浩特", "沈阳",
            "长春", "哈尔滨", "南京", "杭州", "合肥", "福州", "南昌", "济南", "郑州", "武汉", "长沙",
            "南宁", "海口", "成都", "贵阳", "昆明", "拉萨", "西安", "兰州", "银川", "西宁", "乌鲁木齐"};
    private ImageView back;
    private GridView cityList;
    private Intent intent;
    private EditText inputCity;
    private Button search;

    private String city;

    private ArrayList<City> cityArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        init();
        initCity();
        CityAdapter adapter = new CityAdapter(SelectCity.this, R.layout.city_item, cityArrayList);
        cityList.setAdapter(adapter);
        final Intent intent = new Intent(SelectCity.this, WeatherService.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        back = (ImageView) findViewById(R.id.back);
        cityList = (GridView) findViewById(R.id.city_list);
        inputCity = (EditText) findViewById(R.id.input_city);
        search = (Button) findViewById(R.id.search);
        inputCity.addTextChangedListener(new Watcher());
        cityList.setOnItemClickListener(this);
        back.setOnClickListener(this);
        search.setOnClickListener(this);
    }

    private void initCity() {
        for (int i = 0; i < citys.length; i++) {
            City city = new City(citys[i]);
            cityArrayList.add(city);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        city = citys[position];
        intent = new Intent();
        intent.putExtra("city", city);
        SelectCity.this.setResult(RESULT_OK, intent);
        SelectCity.this.finish();
    }

    class Watcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (inputCity.getText().toString().length() == 0) {
                search.setVisibility(View.GONE);
            } else {
                search.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                back();
                break;
            case R.id.search:
                city = inputCity.getText().toString();
                intent = new Intent();
                intent.putExtra("city", city);
                SelectCity.this.setResult(RESULT_OK, intent);
                SelectCity.this.finish();
                break;
            default:
                break;
        }
    }

    /**
     * finish Activity前判断是否结束主Activity
     */
    private void back() {
        SelectCity.this.finish();
    }
}
