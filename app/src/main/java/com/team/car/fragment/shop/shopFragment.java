package com.team.car.fragment.shop;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.team.car.R;
import com.team.car.adapter.shop.ShopMessageAdapter;
import com.team.car.base.fragment.BaseFragment;
import com.team.car.entity.shop.ShopMessageBean;
import com.team.car.view.listview.XListView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



/**
 * Created by Lmy on 2017/1/16.
 * email 1434117404@qq.com
 */

public class shopFragment extends BaseFragment implements XListView.IXListViewListener{
    private static final String TAG = shopFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private DropDownMenu mDropDownMenu;
    private String[] headers= {"类别", "排序", "地区"};
    private String[] category = {"汽车维修", "汽车保养"};
    private String[] sort= {"默认", "距离", "订单", "价格"};
    private String[] region= {"全市", "锦江区", "青羊区", "金牛区", "武侯区", "成华区", "高新区", "龙泉驿区", "青白江区", "新都区", "温江区", "金堂县", "双流县"};

    private View menuView;//下拉检索视图
    private View mainView;//主布局视图
    private Handler handler;

    private XListView listView;
    private ShopMessageAdapter adapter;
    private List<ShopMessageBean> shopList = new ArrayList<ShopMessageBean>();
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

    /**
     * 加载视图
     * @return 让下拉检索视图作为主布局
     */
    @Override
    protected View initView() {
        menuView = LayoutInflater.from(context).inflate(R.layout.drop_down_view, null);
        mainView = LayoutInflater.from(context).inflate(R.layout.activity_shop, null);
        return menuView;
    }

    /**
     * 加载所有数据
     */
    @Override
    public void initDate() {
        super.initDate();
        listView = (XListView) mainView.findViewById(R.id.shop_listview);
        handler = new Handler();
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this);

        mDropDownMenu = (DropDownMenu) menuView.findViewById(R.id.dropDownMenu);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), setData(), mainView);
        mDropDownMenu.addMenuSelectListener(new DropDownMenu.OnDefultMenuSelectListener() {
            @Override
            public void onSelectDefaultMenu(int index, int pos, String clickstr) {//index:点击的tab索引，pos：单项菜单中点击的位置索引，clickstr：点击位置的字符串
                toastUtil.Long(context, "我是 : " + clickstr).show();
                Log.e(TAG, "index:" + index);
                Log.e(TAG, "pos:" + pos);
                Log.e(TAG, "clickstr:" + clickstr);
                shopList.clear();
                setShopMessageData(page, cityName, type, 2);
                Log.e(TAG, "onSelectDefaultMenu: 考察这块执行了吗");
            }
        });

        setShopMessageData(page, cityName, type, 1);//默认加载第一页数据
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toastUtil.Short(context, shopList.get(position - 1).getShopName()).show();
                //以下将用户选中的数据传到详情页面，进行展示
                ShopMessageBean bean = shopList.get(position - 1);
            }
        });
    }

    /**
     * 设置DropDownMenu类型和数据源：
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
    private void setShopMessageData(int page, String cityName, String type, final int flag) {
//        String shopUrl = "http://api.chuxingdata.com/surrounding/local?page=" + page + "&pagesize=20&cityName=" + cityName + "&radius=10000&lat=104.43&lat=30.85&keyWord=" + type + "&appkey=bb7e380d901a599e";
        String shopUrl = "http://139.199.23.142:8080/TestShowMessage1/shop.txt";
        RequestParams params = new RequestParams(shopUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<ShopMessageBean> listTest;
                if(flag == 2){//表示加载更多
                    listTest = new ArrayList<ShopMessageBean>();
                    listTest = getDataFromJson(result);
                    shopList.addAll(listTest);
                    adapter.notifyDataSetChanged();
                    Log.e(TAG, "onSuccess: 筛选部分执行了" + result.toString());
                    return;
                }
                shopList = getDataFromJson(result);
                adapter = new ShopMessageAdapter(context, shopList);
                listView.setAdapter(adapter);
                Log.e(TAG, "onSuccess: 数据刷新完成");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求服务器部分：" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {}

            @Override
            public void onFinished() {}
        });
    }

    private List<ShopMessageBean> getDataFromJson(String json) {
        List<ShopMessageBean> list = new ArrayList<ShopMessageBean>();
        ShopMessageBean bean = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            list.add(bean);
        }
        return list;
    }

    /**
     * 刷新完成时执行的动作
     */
    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = simpleDateFormat.format(date);
        listView.setRefreshTime(str);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        shopList.clear();//先将list清空
        setShopMessageData(page, cityName, type, 1);
        onLoad();//刷新完成时调用
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMore() {
        page += 1;
        setShopMessageData(page, cityName, type, 2);
        onLoad();//刷新完成时调用
    }
}
