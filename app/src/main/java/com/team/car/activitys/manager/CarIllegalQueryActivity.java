package com.team.car.activitys.manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.widgets.ToastUtil;

/**
 * Created by Lmy on 2017/2/26.
 * email 1434117404@qq.com
 */

public class CarIllegalQueryActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private static final String TAG = CarIllegalQueryActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private ImageView back;
    private FrameLayout pop;
    private ImageView oneHelp;
    private ImageView twoHelp;
    private Button closeGraph;

    private RelativeLayout location;
    private TextView cityLocation;

    private PopupWindow pw;
    private View popView;
    private GridView gv;
    private ArrayAdapter<String> adapter;
    private String[] names = {
            "京","津","沪","渝","冀","豫","云","辽",
            "黑","湘","皖","鲁","新","苏","浙","赣",
            "鄂","桂","甘","晋","蒙","陕","吉","闽",
            "粤","贵","青","藏","川","宁","琼"};

    private TextView carNumberProvince;
    private EditText carNumber;

    private Button btnCarQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_illegal_query);
        initView();//初始化视图
        setPopup();//设置PopupWindow

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.car_illegal_query_back);
        pop = (FrameLayout) findViewById(R.id.pop);
        oneHelp = (ImageView) findViewById(R.id.car_illegal_help_one);
        twoHelp = (ImageView) findViewById(R.id.car_illegal_help_two);
        closeGraph = (Button) findViewById(R.id.close_graph);

        location = (RelativeLayout) findViewById(R.id.query_location);
        cityLocation = (TextView) findViewById(R.id.city_location);
        carNumberProvince = (TextView) findViewById(R.id.choose_car_number);
        carNumber = (EditText) findViewById(R.id.car_number);
        btnCarQuery = (Button) findViewById(R.id.btn_car_query);

        back.setOnClickListener(this);
        oneHelp.setOnClickListener(this);
        twoHelp.setOnClickListener(this);
        closeGraph.setOnClickListener(this);
        location.setOnClickListener(this);
        carNumberProvince.setOnClickListener(this);
        btnCarQuery.setOnClickListener(this);
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
     * GridView点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String  province = adapter.getItem(position);
        carNumberProvince.setText(province);
        pw.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_illegal_query_back:{
                this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            break;
            case R.id.choose_car_number:{
                pw = getPopWindow(popView);
            }
            break;
            case R.id.car_illegal_help_one:{
                pop.setVisibility(View.VISIBLE);
            }
            break;
            case R.id.car_illegal_help_two:{
                pop.setVisibility(View.VISIBLE);
            }
            break;
            case R.id.close_graph:{
                pop.setVisibility(View.GONE);
            }
            break;
            case R.id.query_location:{
                Intent intent = new Intent(CarIllegalQueryActivity.this, CarQueryProvinceActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            break;
            case R.id.btn_car_query:{
                Intent intent = new Intent(CarIllegalQueryActivity.this, CarIllegalQueryResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("carNumber", carNumberProvince.getText().toString() + carNumber.getText().toString());
                bundle.putString("carLocation", cityLocation.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            String nameProvince = bundle.getString("nameProvince");
            String nameCity = bundle.getString("nameCity");
            cityLocation.setText(nameProvince + " " + nameCity);
        }
    }
}
