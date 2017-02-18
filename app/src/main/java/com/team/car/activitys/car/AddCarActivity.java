package com.team.car.activitys.car;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.widgets.ToastUtil;
import com.team.car.widgets.timechooses.CustomDatePicker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lmy on 2017/2/17.
 * email 1434117404@qq.com
 */

public class AddCarActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private static final String TAG = AddCarActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private ImageView addCarBack;
    private ImageView addCarFinish;

    private TextView addCarProvince;//车牌的省份
    private EditText addCarNum;//车牌号
    private EditText addCarFrameNum;//车架号
    private EditText addCarEngineNum;//发动机号
    private TextView addCarRegisterData;//注册日期
    private TextView subAddCarBrand;
    private TextView addCarBrand;//车品牌
    private TextView addCarSpecificModel;//具体车型

    private RelativeLayout addCarSelectTime;//选择日期
    private RelativeLayout addCarSelectBrand;//选择品牌
    private RelativeLayout addCarSelectModel;//选择具体车型

    private PopupWindow pw;
    private View popView;
    private GridView gv;
    private ArrayAdapter<String> adapter;
    private String[] names = {
            "京","津","沪","渝","冀","豫","云","辽",
            "黑","湘","皖","鲁","新","苏","浙","赣",
            "鄂","桂","甘","晋","蒙","陕","吉","闽",
            "粤","贵","青","藏","川","宁","琼"};

    private CustomDatePicker customDatePicker;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//获取系统时间
    private String temp = sdf.format(new Date()).split(" ")[0];//暂存系统时间，用于初始化

    private List<String> listModel;//具体车型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_car);
        initView();//初始化视图控件
        setPopup();//设置PopupWindow
        initDataPicker();//初始化时间选择器
    }

    /**
     * 初始化视图控件
     */
    private void initView() {
        addCarBack = (ImageView) findViewById(R.id.add_car_back);
        addCarFinish = (ImageView) findViewById(R.id.add_car_finish);

        addCarProvince = (TextView) findViewById(R.id.add_car_province);
        addCarNum = (EditText) findViewById(R.id.add_car_num);
        addCarFrameNum = (EditText) findViewById(R.id.add_car_frame_num);
        addCarEngineNum = (EditText) findViewById(R.id.add_car_engine_num);
        addCarRegisterData = (TextView) findViewById(R.id.add_car_register_data);
        subAddCarBrand = (TextView) findViewById(R.id.sub_add_car_specific_model);
        addCarBrand = (TextView) findViewById(R.id.add_car_brand);
        addCarSpecificModel = (TextView) findViewById(R.id.add_car_specific_model);

        addCarSelectTime = (RelativeLayout) findViewById(R.id.add_car_select_time);
        addCarSelectBrand = (RelativeLayout) findViewById(R.id.add_car_select_brand);
        addCarSelectModel = (RelativeLayout) findViewById(R.id.add_car_select_model);

        addCarBack.setOnClickListener(this);
        addCarFinish.setOnClickListener(this);
        addCarProvince.setOnClickListener(this);
        addCarSelectTime.setOnClickListener(this);
        addCarSelectBrand.setOnClickListener(this);
        addCarSelectModel.setOnClickListener(this);
    }

    /**
     * PopupWindow设置
     */
    private void setPopup() {
        popView=getLayoutInflater().inflate(R.layout.add_car_grid,null);
        gv=(GridView)popView.findViewById(R.id.gv);
        adapter = new ArrayAdapter<String>(this, R.layout.add_car_grid_item, names);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
    }

    /**
     * 设置点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_car_back:{//返回键
                AddCarActivity.this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            break;
            case R.id.add_car_finish:{//添加完成
                toastUtil.Short(AddCarActivity.this, "添加完成").show();
                //以下为将填写好的数据存储到后台服务器数据库进行保存，并且添加到我的爱车列表

            }
            break;
            case R.id.add_car_province:{//车牌号省份选择
                pw = getPopWindow(popView);
            }
            break;
            case R.id.add_car_select_time:{//注册日期选择
                customDatePicker.show(temp);
            }
            break;
            case R.id.add_car_select_brand:{//车品牌选择
                Intent intent = new Intent(AddCarActivity.this, CarBrandSelectActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            break;
            case R.id.add_car_select_model:{//具体车型选择
                if (TextUtils.isEmpty(addCarBrand.getText().toString())) {
                    toastUtil.Short(AddCarActivity.this, "请先选择品牌车系").show();
                }else{
                    Intent intent =  new Intent(AddCarActivity.this, CarBrandSelectThreeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", (Serializable) listModel);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 2);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
            break;
        }
    }

    /**
     * GridView点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String  province = adapter.getItem(position);
        addCarProvince.setText(province);
        pw.dismiss();
    }

    /**
     * 设置PopupWindow显示
     * @param view 要加载的视图
     * @return 返回一个PopupWindow
     */
    public PopupWindow getPopWindow(View view){
        PopupWindow popupWindow=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        return popupWindow;
    }

    /**
     * 初始化时间选择器
     */
    private void initDataPicker() {
        customDatePicker = new CustomDatePicker(AddCarActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                addCarRegisterData.setText(time.split(" ")[0]);
            }
        },"1900-01-01 00:00", sdf.format(new Date()));
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2 && data != null){
            Bundle bundle = data.getExtras();
            subAddCarBrand.setVisibility(View.GONE);
            addCarBrand.setText(bundle.getString("brands"));
            listModel = bundle.getStringArrayList("model");
            Log.e(TAG, "onActivityResult: 该车的品牌车系为" + bundle.getString("brands"));
            Log.e(TAG, "onActivityResult: 具体车型为" + listModel.toString());
            toastUtil.Short(AddCarActivity.this, "接受值成功！");
        }else if (requestCode == 2 && resultCode == RESULT_OK){
            addCarSpecificModel.setText(data.getStringExtra("specificModel"));
        }else{
            toastUtil.Short(AddCarActivity.this, "接受值失败！");
        }
    }
}















