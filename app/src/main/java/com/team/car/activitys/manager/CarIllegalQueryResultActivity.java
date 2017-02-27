package com.team.car.activitys.manager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.adapter.manager.IllegalResultAdapter;
import com.team.car.entity.manager.CarIllegalBean;
import com.team.car.widgets.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class CarIllegalQueryResultActivity extends Activity implements View.OnClickListener{
    private static final String TAG = CarIllegalQueryResultActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private ImageView back;
    private TextView carNumber;
    private TextView carLocation;

    private ListView listView;
    private IllegalResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_illegal_query_result);
        initView();//初始化控件
        initData();//设置数据
    }

    /**
     * 初始化视图
     */
    private void initView() {
        back = (ImageView) findViewById(R.id.car_query_result_back);
        listView = (ListView) findViewById(R.id.car_illegal_result_listview);
        carNumber = (TextView) findViewById(R.id.query_car_number);
        carLocation = (TextView) findViewById(R.id.query_car_location);
    }

    /**
     * 设置数据
     */
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        String number = bundle.getString("carNumber");
        String location = bundle.getString("carLocation");
        carNumber.setText(number);
        carLocation.setText(location);
        adapter = new IllegalResultAdapter(this, getData());
        listView.setAdapter(adapter);

        back.setOnClickListener(this);

    }

    /**
     * 构造一些假数据，用户测试
     */
    private List<CarIllegalBean> getData(){
        List<CarIllegalBean> testList = new ArrayList<>();
        testList.add(new CarIllegalBean("2015-02-14 12:00:06", "计三分， 罚款100元", "四川成都", "随意停车"));
        testList.add(new CarIllegalBean("2016-10-01 09:30:32", "计两分， 罚款200元", "四川成都", "闯红灯"));
        return testList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_query_result_back:{
                this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            break;
        }
    }
}
