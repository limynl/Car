package com.team.car.activitys.car;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.adapter.car.CarBrandSelectAdapter;
import com.team.car.entity.car.CarBrandSelectBean;
import com.team.car.utils.PinyinComparator;
import com.team.car.utils.SideBar;
import com.team.car.widgets.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 汽车品牌选择
 * Created by Lmy on 2017/2/18.
 * email 1434117404@qq.com
 */

public class CarBrandSelectActivity extends Activity implements View.OnClickListener{
    private static final String TAG = CarBrandSelectActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private static final String URL  = "http://api.jisuapi.com/car/brand?appkey=d6c1207126bca7d5";
    private static List<CarBrandSelectBean> itemList = new ArrayList<CarBrandSelectBean>();
    private ListView sortListView;
    private static CarBrandSelectAdapter adapter;

    private SideBar sideBar;
    private TextView dialog;

    private ImageView back;

    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    /**
     * 分组的布局
     */
    private LinearLayout titleLayout;

    /**
     * 分组上显示的字母
     */
    private TextView title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand_select);
        sortListView = (ListView) findViewById(R.id.car_brand_list);
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(URL);
        initViews();
    }

    private void initViews() {
        back = (ImageView) findViewById(R.id.car_brand_select_back);
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        title = (TextView) findViewById(R.id.title);

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidebar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        back.setOnClickListener(this);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                toastUtil.Short(CarBrandSelectActivity.this, ((CarBrandSelectBean) adapter.getItem(position)).getCarBrand() + "=" + position).show();
                Intent intent = new Intent(CarBrandSelectActivity.this, CarBrandSelectTwoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", (position + 1));
                bundle.putString("brand", ((CarBrandSelectBean) adapter.getItem(position)).getCarBrand());
                bundle.putString("iconUrl", ((CarBrandSelectBean) adapter.getItem(position)).getCarLogo());
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // 根据a-z进行排序源数据
        Log.e(TAG, "initViews: "+itemList);
        Collections.sort(itemList, pinyinComparator);

        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //字母连续断层使不能置顶，例如  D （空） F使D到F阶段不存在置顶
                int section;
                try{
                    section = adapter.getSectionForPosition(firstVisibleItem);
                }catch (Exception e){
                    return ;
                }
                int nextSecPosition = adapter.getPositionForSection(section + 1);
                //解决断层置顶
                for (int i = 1; i < 30; i++) {
                    //26个英文字母充分循环
                    if (nextSecPosition == -1) {
                        //继续累加
                        int data = section + 1 + i;
                        nextSecPosition = adapter.getPositionForSection(data);
                    } else {
                        break;
                    }
                }

                if (firstVisibleItem != lastFirstVisibleItem) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
                    params.topMargin = 0;
                    titleLayout.setLayoutParams(params);
                    title.setText(String.valueOf((char) section));
                }
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = titleLayout.getHeight();
                        int bottom = childView.getBottom();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            titleLayout.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                titleLayout.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_brand_select_back:{
                this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            break;
        }
    }


    class MyAsyncTask extends AsyncTask<String, Void, List<CarBrandSelectBean>> {
        public MyAsyncTask(){

        }
        @Override
        protected List<CarBrandSelectBean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<CarBrandSelectBean> list) {
            super.onPostExecute(list);
            itemList = list;
            adapter = new CarBrandSelectAdapter(CarBrandSelectActivity.this, list);
            sortListView.setAdapter(adapter);
        }

        private List<CarBrandSelectBean> getJsonData(String url) {
            List<CarBrandSelectBean> list = new ArrayList<CarBrandSelectBean>();
            try {
                //此句功能与url.openConnection().getInputStream()相同，返回类型为InputStream
                String jsonString = readStream(new URL(url).openStream());
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                CarBrandSelectBean itemBean = null;
                for (int i = 0; i < 217; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    itemBean = new CarBrandSelectBean();
                    itemBean.setCarLogo(jsonObject.getString("logo"));
                    itemBean.setCarBrand(jsonObject.getString("name"));
                    itemBean.setCarInitial(jsonObject.getString("initial"));
                    list.add(itemBean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e(TAG, "getJsonData: "+list);
            Log.e(TAG, "返回字符串的大小" + list.size());
            return list;
        }

        //网络请求，获取json字符串
        private String readStream(InputStream is){
            InputStreamReader isr;
            String result = "";
            String line = "";
            try {
                isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                while((line = br.readLine()) != null){
                    result += line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 3 && data != null){
            setResult(2, data);
            finish();
        }
    }
}
