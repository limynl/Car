package com.team.car.activitys.weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.Weather;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Hashon;
import com.team.car.R;
import com.team.car.widgets.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lmy on 2017/1/24.
 * email 1434117404@qq.com
 */

public class WeatherActivity extends Activity implements APICallback,View.OnClickListener {
    private static final String TAG = WeatherActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();
    private String ip;
    private TextView tvCity;
    private TextView tvUpdateTime;
    private TextView tvTemperature;
    private TextView tvWind;
    private TextView tvDate;
    private TextView tvCurrentTemperature;
    private TextView tvCurrentWeather;
    private ImageView weatherIcon;

    public static Weather context;

    private LinearLayout weatherBg;
    private LinearLayout titleBarLayout;
    private LinearLayout currentWeatherLayout;
    private ScrollView scrollView;

    private ListView weatherForcastList;

    private ArrayList<HashMap<String, Object>> weeks;
    private ArrayList<HashMap<String, Object>> results;
    private ArrayList<MyWeather> weathers;

    private Intent intent;
    private String city;
    private String currentWeather;

    private Time time;

    public final static int SELECT_CITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_weather);
        LinearLayout tqLinList = (LinearLayout) findViewById(R.id.tq_lin_list);
        tqLinList.getBackground().setAlpha(153);
        init();
        MobAPI.initSDK(this, "1a0c862622d16");//120b650027878  1a0c862622d16
        Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
        api.getSupportedCities(this);
        time = new Time();
        intent = getIntent();
        city = intent.getStringExtra("city");
        if(city == null){
            new Thread(){
                public void run() {
                    ip = null;
                    try {
                        NetworkHelper network = new NetworkHelper();
                        ArrayList<KVPair<String>> values = new ArrayList<KVPair<String>>();
                        values.add(new KVPair<String>("ie", "utf-8"));
                        String resp = network.httpGet("http://pv.sohu.com/cityjson", values, null, null);
                        resp = resp.replace("var returnCitySN = {", "{").replace("};", "}");
                        ip = (String) (new Hashon().fromJson(resp).get("cip"));
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }finally {
                        Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
                        api.queryByIPAddress(ip, WeatherActivity.this);
                    }
                }

            }.start();
        }else {
            api.queryByCityName(city, WeatherActivity.this);
        }
    }

    private void initWeather(){
        HashMap<String,Object> today = weeks.get(0);
        MyWeather todayWeather = new MyWeather(com.mob.tools.utils.R.toString(today.get("week")), currentWeather, com.mob.tools.utils.R.toString(today.get("temperature")));
        weathers.add(todayWeather);
        for(int i = 1;i < weeks.size(); i++){
            HashMap<String,Object> week = weeks.get(i);
            MyWeather weather = new MyWeather(com.mob.tools.utils.R.toString(week.get("week")),
                    com.mob.tools.utils.R.toString(week.get("dayTime")),
                    com.mob.tools.utils.R.toString(week.get("temperature")));
            weathers.add(weather);
        }
    }

    private void onWeatherDetailsGot(Map<String, Object> result){
        results = (ArrayList<HashMap<String, Object>>) result.get("result");
        HashMap<String, Object> weather = results.get(0);

        weathers = new ArrayList<MyWeather>();

        weeks = (ArrayList<HashMap<String,Object>>) weather.get("future");
        HashMap<String, Object> week = weeks.get(0);
        tvCity.setText(com.mob.tools.utils.R.toString(weather.get("city")));
        tvCurrentTemperature.setText(com.mob.tools.utils.R.toString(weather.get("temperature")));
        currentWeather = com.mob.tools.utils.R.toString(weather.get("weather"));
        tvCurrentWeather.setText(currentWeather);
        String time = com.mob.tools.utils.R.toString(weather.get("updateTime"));
        String date = "今日 ";
        String upTime = time.substring(8, 10) + ":" + time.substring(10, 12);
        tvUpdateTime.setText(date + " " + upTime + " 更新");
        tvTemperature.setText(com.mob.tools.utils.R.toString(week.get("temperature")));
        tvWind.setText(com.mob.tools.utils.R.toString(weather.get("wind")));
        tvDate.setText(time.substring(4, 6) + "/" + time.substring(6, 8) + " " + com.mob.tools.utils.R.toString(weather.get("week")));
        initWeather();
        WeatherAdapter adapter = new WeatherAdapter(WeatherActivity.this, R.layout.weather_forecast_item, weathers);
        weatherForcastList.setAdapter(adapter);
        updateWeatherImage();
    }

    /**
     * 更新背景图片和天气图标
     */
    private void updateWeatherImage() {
        time.setToNow();
        if (currentWeather.contains("晴")) {
            weatherBg.setBackgroundResource(R.mipmap.tq);
            weatherIcon.setImageResource(R.mipmap.tq_07);
        } else if (currentWeather.contains("多云")) {
            weatherBg.setBackgroundResource(R.mipmap.tq);
            weatherIcon.setImageResource(R.mipmap.dtq_15);
        } else if (currentWeather.contains("阴")) {
            weatherBg.setBackgroundResource(R.mipmap.tq);
            weatherIcon.setImageResource(R.mipmap.dtq_30);
        } else if (currentWeather.contains("雷")) {
            weatherBg.setBackgroundResource(R.mipmap.tq_lei);
            weatherIcon.setImageResource(R.mipmap.dtq_28);
        } else if (currentWeather.contains("雨")) {
            weatherBg.setBackgroundResource(R.mipmap.tq_yu);
            if (currentWeather.contains("小雨")) {
                weatherIcon.setImageResource(R.mipmap.dtq_23);
            } else if (currentWeather.contains("中雨")) {
                weatherIcon.setImageResource(R.mipmap.dtq_23);
            } else if (currentWeather.contains("大雨")) {
                weatherIcon.setImageResource(R.mipmap.dtq_25);
            } else if (currentWeather.contains("暴雨")) {
                weatherIcon.setImageResource(R.mipmap.dtq_25);
            } else if (currentWeather.contains("雨夹雪")) {
                weatherIcon.setImageResource(R.mipmap.dtq_25);
            } else if (currentWeather.contains("冻雨")) {
                weatherIcon.setImageResource(R.mipmap.dtq_25);
            } else {
                weatherIcon.setImageResource(R.mipmap.dtq_23);
            }
        } else if (currentWeather.contains("雪") || currentWeather.contains("冰雹")) {
            weatherBg.setBackgroundResource(R.mipmap.tq_yu);
            if (currentWeather.contains("小雪")) {
                weatherIcon.setImageResource(R.mipmap.dtq_23);
            } else if (currentWeather.contains("中雪")) {
                weatherIcon
                        .setImageResource(R.mipmap.dtq_23);
            } else if (currentWeather.contains("大雪")) {
                weatherIcon.setImageResource(R.mipmap.dtq_25);
            } else if (currentWeather.contains("暴雪")) {
                weatherIcon.setImageResource(R.mipmap.dtq_25);
            } else if (currentWeather.contains("冰雹")) {
                weatherIcon.setImageResource(R.mipmap.dtq_25);
            } else {
                weatherIcon.setImageResource(R.mipmap.dtq_23);
            }
        } else if (currentWeather.contains("雾")) {
            weatherBg.setBackgroundResource(R.mipmap.tq_yu);
            weatherIcon.setImageResource(R.mipmap.dtq_30);
        } else if (currentWeather.contains("霾")) {
            weatherBg.setBackgroundResource(R.mipmap.tq_yu);
            weatherIcon.setImageResource(R.mipmap.dtq_30);
        } else if (currentWeather.contains("沙尘暴") || currentWeather.contains("浮尘") || currentWeather.contains("扬沙")) {
            weatherBg.setBackgroundResource(R.mipmap.tq_yu);
            weatherIcon.setImageResource(R.mipmap.dtq_30);
        } else {
            weatherBg.setBackgroundResource(R.mipmap.tq);
            weatherIcon.setImageResource(R.mipmap.tq_07);
        }
    }

    private void init(){
        tvCity = (TextView) findViewById(R.id.city);
        tvUpdateTime = (TextView) findViewById(R.id.update_time);
        tvTemperature = (TextView) findViewById(R.id.temperature);
        tvCurrentTemperature = (TextView) findViewById(R.id.current_temperature);
        tvCurrentWeather = (TextView) findViewById(R.id.current_weather);

        weatherIcon = (ImageView) findViewById(R.id.weather_icon);

        tvWind = (TextView) findViewById(R.id.wind);
        tvDate = (TextView) findViewById(R.id.date);
        weatherBg = (LinearLayout) findViewById(R.id.weather_bg);
        weatherForcastList = (ListView) findViewById(R.id.weather_forecast_list);
        tvCity.setOnClickListener(this);
    }

    @Override
    public void onSuccess(API api, int action, Map<String, Object> result) {
        switch (action) {
            case Weather.ACTION_IP: onWeatherDetailsGot(result); break;
            case Weather.ACTION_QUERY: onWeatherDetailsGot(result); break;
        }
    }

    @Override
    public void onError(API api, int action, Throwable details) {
        details.printStackTrace();
        toastUtil.Short(WeatherActivity.this, "天气预报定位失败！").show();
        Log.e(TAG, "onError: "+details.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.city:
                intent = new Intent();
                intent.setClass(WeatherActivity.this, SelectCity.class);
                WeatherActivity.this.startActivityForResult(intent, SELECT_CITY);
                break;
        }
    }

    /**
     * 设置布局的高度（铺满屏幕）,设置通知栏相关
     */
    private void setCurrentWeatherLayoutHight() {
        // 通知栏高度
        int statusBarHeight = 0;
        try {
            statusBarHeight = getResources().getDimensionPixelSize(
                    Integer.parseInt(Class
                            .forName("com.android.internal.R$dimen")
                            .getField("status_bar_height")
                            .get(Class.forName("com.android.internal.R$dimen")
                                    .newInstance()).toString()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // 屏幕高度
        int displayHeight = ((WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getHeight();
        // title bar LinearLayout高度
        titleBarLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int titleBarHeight = titleBarLayout.getMeasuredHeight();

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) currentWeatherLayout
                .getLayoutParams();
        linearParams.height = displayHeight - statusBarHeight - titleBarHeight;
        currentWeatherLayout.setLayoutParams(linearParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    city = data.getStringExtra("city");
                    if(!city.equals("自动定位")){
                        Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
                        api.queryByCityName(city, this);
                    }else{
                        //Toast.makeText(WeatherActivity.this, city, Toast.LENGTH_SHORT).show();
                        new Thread(){
                            public void run() {
                                ip = null;
                                try {
                                    NetworkHelper network = new NetworkHelper();
                                    ArrayList<KVPair<String>> values = new ArrayList<KVPair<String>>();
                                    values.add(new KVPair<String>("ie", "utf-8"));
                                    String resp = network.httpGet("http://pv.sohu.com/cityjson", values, null, null);
                                    resp = resp.replace("var returnCitySN = {", "{").replace("};", "}");
                                    ip = (String) (new Hashon().fromJson(resp).get("cip"));
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                }finally {
                                    Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
                                    api.queryByIPAddress(ip, WeatherActivity.this);
                                }
                            }
                        }.start();
                    }
                }
                break;
            default:
        }
    }
}
