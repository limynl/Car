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
import com.team.car.entity.car.CarBean;
import com.team.car.widgets.ToastUtil;
import com.team.car.widgets.timechooses.CustomDatePicker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 添加我的爱车
 * Created by Lmy on 2017/2/17.
 * email 1434117404@qq.com
 */

public class AddCarActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private static final String TAG = AddCarActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private ImageView addCarBack;
    private ImageView addCarFinish;
    private TextView addCarTitle;

    private TextView addCarProvince;//车牌的省份
    private EditText addCarNum;//车牌号
    private EditText addCarFrameNum;//车架号
    private EditText addCarEngineNum;//发动机号
    private TextView addCarRegisterData;//注册日期
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
    private String iconUrl;//汽车logo

    private int flag;//判断标志，判断是添加汽车还是编辑汽车
    private CarBean updateCar;//要编辑的车的信息
    private int position;//要编辑车的编号

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
        addCarTitle = (TextView) findViewById(R.id.add_car_title);

        addCarProvince = (TextView) findViewById(R.id.add_car_province);
        addCarNum = (EditText) findViewById(R.id.add_car_num);
        addCarFrameNum = (EditText) findViewById(R.id.add_car_frame_num);
        addCarEngineNum = (EditText) findViewById(R.id.add_car_engine_num);
        addCarRegisterData = (TextView) findViewById(R.id.add_car_register_data);
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


        Bundle bundle = getIntent().getExtras();
        flag = bundle.getInt("flag");
        if (flag == 2){
            addCarTitle.setText("编辑爱车");
            updateCar = (CarBean) bundle.getSerializable("updateCar");
            position = bundle.getInt("position");
            Log.e(TAG, "initView: " + updateCar);
            if (updateCar != null) {
                iconUrl = updateCar.getCarIconUrl();
                addCarProvince.setText(updateCar.getCarNumber().substring(0, 1));
                addCarNum.setText(updateCar.getCarNumber().substring(1));
                addCarFrameNum.setText(updateCar.getCarFrameNum());
                addCarEngineNum.setText(updateCar.getCarEngineNum());
                addCarRegisterData.setText(updateCar.getCarRegistrationDate());
                addCarBrand.setText(updateCar.getCarBrand());
                addCarSpecificModel.setText(updateCar.getCarSpecificModel());
            }
        }
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
                //以下为将填写好的数据存储到后台服务器数据库进行保存，并且添加到我的爱车列表
                CarBean carBean = new CarBean();
                carBean.setCarIconUrl(iconUrl);
                carBean.setCarNumber(addCarProvince.getText().toString() + addCarNum.getText().toString());
                carBean.setCarFrameNum(addCarFrameNum.getText().toString());
                carBean.setCarEngineNum(addCarEngineNum.getText().toString());
                carBean.setCarRegistrationDate(addCarRegisterData.getText().toString());
                carBean.setCarBrand(addCarBrand.getText().toString());
                carBean.setCarSpecificModel(addCarSpecificModel.getText().toString());
                saveData(carBean);//保存数据
                toastUtil.Short(AddCarActivity.this, "添加完成").show();

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

    /**
     * 将用户输入的信息保存到服务器
     */
    private void saveData(final CarBean carBean) {
        //这是假数据，仅仅用于测试
        Intent intent = new Intent(AddCarActivity.this, ShowCarActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("carBean", carBean);
        if(flag == 2){
            bundle.putInt("position", position);//如果是编辑汽车，还要将车辆的原先位置，再次传回去
        }
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        AddCarActivity.this.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);



       /* String url = null;//连接服务器的地址
        if(!TextUtils.isEmpty(addCarNum.getText().toString())){
            if (!TextUtils.isEmpty(addCarFrameNum.getText().toString())){
                if (!TextUtils.isEmpty(addCarEngineNum.getText().toString())){
                    if (!TextUtils.isEmpty(addCarRegisterData.getText().toString())){
                        if (!TextUtils.isEmpty(addCarBrand.getText().toString())){
                            if (!TextUtils.isEmpty(addCarSpecificModel.getText().toString())){
                                Map<String, String> map = new HashMap<>();
                                map.put("carNumber", carBean.getCarNumber());
                                map.put("addCarFrameNum", carBean.getCarFrameNum());
                                map.put("addCarEngineNum", carBean.getCarEngineNum());
                                map.put("addCarRegisterData", carBean.getCarRegistrationDate());
                                map.put("addCarBrand", carBean.getCarBrand());
                                map.put("addCarSpecificModel", carBean.getCarSpecificModel());
                                VolleyRequestUtil.RequestPost(AddCarActivity.this, url, "addCar", map, new VolleyListenerInterface(AddCarActivity.this, VolleyListenerInterface.mSuccessListener, VolleyListenerInterface.mErrorListener ) {
                                    @Override
                                    public void onSuccess(String result) {
                                        //这里进行判断一下是否成功,成功就进行跳转，显示到我的爱车列表

                                        toastUtil.Short(AddCarActivity.this, "添加成功").show();
                                        Intent intent = new Intent(AddCarActivity.this, ShowCarActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("carBean", (Serializable) carBean);
                                        intent.putExtras(bundle);
                                        setResult(RESULT_OK, intent);
                                        AddCarActivity.this.finish();
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        Log.e(TAG, "onError: " + error.toString());
                                        toastUtil.Short(AddCarActivity.this, "服务器异常").show();
                                    }
                                });
                            }else{
                                toastUtil.Short(AddCarActivity.this, "具体车型不能为空！").show();
                            }
                        }else{
                            toastUtil.Short(AddCarActivity.this, "品牌车系不能为空！").show();
                        }
                    }else{
                        toastUtil.Short(AddCarActivity.this, "注册日期不能为空！").show();
                    }
                }else{
                    toastUtil.Short(AddCarActivity.this, "发动机号不能为空！").show();
                }
            }else{
                toastUtil.Short(AddCarActivity.this, "车架号不能为空！").show();
            }
        }else{
            toastUtil.Short(AddCarActivity.this, "车牌号不能为空！").show();
        }*/
    }


    /**
     * 接受其他Activity传过来的值
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2 && data != null){//品牌选择Activity传过来的值
            Bundle bundle = data.getExtras();
            addCarBrand.setText(bundle.getString("brands"));
            listModel = bundle.getStringArrayList("model");
            iconUrl = bundle.getString("iconUrl");
            Log.e(TAG, "onActivityResult: 该车的品牌车系为" + bundle.getString("brands"));
            Log.e(TAG, "onActivityResult: 具体车型为" + listModel.toString());
            toastUtil.Short(AddCarActivity.this, "接受值成功！");
        }else if (requestCode == 2 && resultCode == RESULT_OK){//具体车型Activity传过来的值
            addCarSpecificModel.setText(data.getStringExtra("specificModel"));
        }else{
            toastUtil.Short(AddCarActivity.this, "接受值失败！");
        }
    }
}















