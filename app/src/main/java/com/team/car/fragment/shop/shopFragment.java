package com.team.car.fragment.shop;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.team.car.R;
import com.team.car.adapter.shop.ShopMessageAdapter;
import com.team.car.base.fragment.BaseFragment;
import com.team.car.entity.shop.ShopMessageBean;
import com.team.car.widgets.ToastUtil;
import com.zxl.library.DropDownMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class shopFragment extends BaseFragment {
    private static final String TAG = shopFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private DropDownMenu mDropDownMenu;
    private String[] headers= {"类别", "排序", "地区"};
    private String[] category = {"汽车维修", "汽车保养"};
    private String[] sort= {"默认", "距离", "订单", "价格"};
    private String[] region= {"全市", "锦江区", "青羊区", "金牛区", "武侯区", "成华区", "高新区", "龙泉驿区", "青白江区", "新都区", "温江区", "金堂县", "双流县"};

    private View menuView;//下拉检索视图
    private View mainView;//主布局视图

    private ListView listView;
    private ShopMessageAdapter adapter;
    private List<ShopMessageBean> shopList = new ArrayList<ShopMessageBean>();
    private String channel = URLEncoder.encode("汽车维修","UTF-8");
    private int page = 1;
    private String cityName = URLEncoder.encode("成都","UTF-8");//城市名-地区
    private String type = URLEncoder.encode("汽车维修","UTF-8");//种类
    private float locationLat;//纬度
    private float locationLng;//经度

    /*

    http://api.chuxingdata.com/surrounding/local?page=1&pagesize=20&cityName=成都&radius=10000&lat=104.43&lat=30.85&keyWord=汽车维修&appkey=bb7e380d901a599e

    */

    public shopFragment() throws UnsupportedEncodingException {
    }

    @Override
    protected View initView() {
        menuView = LayoutInflater.from(context).inflate(R.layout.drop_down_view, null);
        mainView = LayoutInflater.from(context).inflate(R.layout.activity_shop, null);
        return menuView;
    }

    @Override
    public void initDate() {
        super.initDate();
        listView = (ListView) mainView.findViewById(R.id.shop_listview);


        mDropDownMenu = (DropDownMenu) menuView.findViewById(R.id.dropDownMenu);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), setData(), mainView);
        mDropDownMenu.addMenuSelectListener(new DropDownMenu.OnDefultMenuSelectListener() {
            @Override
            public void onSelectDefaultMenu(int index, int pos, String clickstr) {//index:点击的tab索引，pos：单项菜单中点击的位置索引，clickstr：点击位置的字符串
                toastUtil.Long(context, "我是 : " + clickstr).show();
                Log.e(TAG, "index:" + index);
                Log.e(TAG, "pos:" + pos);
                Log.e(TAG, "clickstr:" + clickstr);

                setShopMessageData();

            }
        });

        setShopMessageData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toastUtil.Short(context, shopList.get(position).getShopName()).show();
            }
        });

    }

    /**
     * 设置类型和数据源：
     * DropDownMenu.KEY对应类型（DropDownMenu中的常量，参考上述核心源码）
     * DropDownMenu.VALUE对应数据源：key不是TYPE_CUSTOM则传递string[],key是TYPE_CUSTOM类型则传递对应view
     */
    private List<HashMap<String, Object>> setData() {
        List<HashMap<String, Object>> viewDatas = new ArrayList<>();
        HashMap<String, Object> categoryMap = new HashMap<String, Object>();
        categoryMap.put(DropDownMenu.KEY, DropDownMenu.TYPE_LIST_CITY);
        categoryMap.put(DropDownMenu.VALUE, category);
        categoryMap.put(DropDownMenu.SELECT_POSITION,0);
        viewDatas.add(categoryMap);

        HashMap<String, Object> sortMap = new HashMap<String, Object>();
        sortMap.put(DropDownMenu.KEY, DropDownMenu.TYPE_LIST_CITY);
        sortMap.put(DropDownMenu.VALUE, sort);
        sortMap.put(DropDownMenu.SELECT_POSITION,0);
        viewDatas.add(sortMap);

        HashMap<String, Object> regionMap = new HashMap<String, Object>();
        regionMap.put(DropDownMenu.KEY, DropDownMenu.TYPE_LIST_CITY);
        regionMap.put(DropDownMenu.VALUE, region);
        regionMap.put(DropDownMenu.SELECT_POSITION,0);
        viewDatas.add(regionMap);

        return viewDatas;
    }

    /**
     * 设置商店条目显示数据
     */
    private void setShopMessageData() {
        String shopUrl = "http://api.chuxingdata.com/surrounding/local?page=" + page + "&pagesize=20&cityName=" + cityName + "&radius=10000&lat=104.43&lat=30.85&keyWord=" + type + "&appkey=bb7e380d901a599e";
        RequestParams params = new RequestParams(shopUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    ShopMessageBean bean = null;
                    JSONObject jsonObject = new JSONObject(result);
                    jsonObject = jsonObject.optJSONObject("result");
                    JSONArray jsonArray = jsonObject.optJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.optJSONObject(i);
                        bean = new ShopMessageBean();
                        JSONObject jsonObject1 = jsonObject.optJSONObject("additionalInformation");
                        bean.setShopPrice(jsonObject1.optString("price"));
                        bean.setShopType(jsonObject1.optString("tag"));
                        bean.setShopTelephone(jsonObject1.optString("telephone"));

                        bean.setShopName(jsonObject.optString("name"));
                        bean.setShopAddress(jsonObject.optString("address"));
                        bean.setShopCityName(jsonObject.optString("cityName"));
                        bean.setShopDistance(jsonObject.optString("distance"));
                        bean.setDistrict(jsonObject.optString("district"));

                        JSONObject jsonObject2 = jsonObject.optJSONObject("location");
                        bean.setShopLocationLat(jsonObject2.optString("lat"));
                        bean.setShopLocationLng(jsonObject2.optString("lng"));
                        shopList.add(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new ShopMessageAdapter(context, shopList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {}

            @Override
            public void onFinished() {}
        });
    }

}
