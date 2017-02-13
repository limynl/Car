package com.team.car.fragment.found.childfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.team.car.R;
import com.team.car.activitys.found.NewsDetailActivity;
import com.team.car.entity.TestContentNewsBean;
import com.team.car.widgets.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lmy on 2017/1/25.
 * email 1434117404@qq.com
 */

public class newThingFragment extends Fragment {
    private static final String TAG = newThingFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private String channel = URLEncoder.encode("头条","UTF-8");
    private int startIndex = 0, numCount = 6;
    private String url = "http://api.jisuapi.com/news/get?channel=" + channel + "&start=" + startIndex +"&num="+ numCount +"&appkey=d6c1207126bca7d5";
    private Context context;
    private ViewPager viewPager;
    private TextView title;
    private LinearLayout ll_point_group;
    private ListView listView;
    private MyTestContentNewsAdapter adapter;
    private ImageOptions imageOptions;//配置listView中的图片

    //得到“头条中的所有数据”
    private List<TestContentNewsBean> allDataList = new ArrayList<TestContentNewsBean>();

    //顶部轮播图数据集合
    private List<TestContentNewsBean> list = new ArrayList<TestContentNewsBean>();

    //新闻列表的数据集合
    private List<TestContentNewsBean> contentList = new ArrayList<TestContentNewsBean>();

    //用于计数
    private static int count = 6;
    private PullToRefreshListView mPullRefreshListView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    Toast.makeText(context, "没有更多", Toast.LENGTH_SHORT).show();
                    mPullRefreshListView.onRefreshComplete();
                    break;
                case 2:
                    mPullRefreshListView.onRefreshComplete();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public newThingFragment() throws UnsupportedEncodingException {
        imageOptions = new ImageOptions.Builder()
                .setSize(org.xutils.common.util.DensityUtil.dip2px(110), org.xutils.common.util.DensityUtil.dip2px(80))
                .setRadius(org.xutils.common.util.DensityUtil.dip2px(5))
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setPlaceholderScaleType(ImageView.ScaleType.FIT_XY)// 加载中或错误图片的ScaleType
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.news_pic_default)
                .setFailureDrawableId(R.mipmap.news_pic_default)
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    /**
     * 初始化要加载的视图
     * @return 返回需要加载的视图
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "newThingFragment创建了");
        toastUtil.Short(getContext(), "test上下文对象").show();
        View view = View.inflate(context, R.layout.activity_new_thing, null);

        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        listView = mPullRefreshListView.getRefreshableView();

        //添加顶部新闻轮播图
        View topNews = View.inflate(context, R.layout.topnews, null);
        viewPager = (ViewPager) topNews.findViewById(R.id.top_news_viewpager);
        title = (TextView) topNews.findViewById(R.id.top_news_title);
        ll_point_group = (LinearLayout) topNews.findViewById(R.id.ll_point_group);
        listView.addHeaderView(topNews);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                list.clear();
                contentList.clear();
                getDataFromNet(url);//下拉刷新，相当于再次进行数据的请求
                count = 6;
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMoreData();
            }
        });

        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TestContentNewsBean news = contentList.get(position - 2);
                Bundle bundle = new Bundle();
                bundle.putString("detailContentDetailUrl", news.getContentUrl());
                bundle.putString("detailContentTitle", news.getTitle());
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
                Log.e(TAG, "onItemClick: "+news.getContentUrl());
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getAllDataList();
        Log.e(TAG, "共有数据条数：" + allDataList.size());
        getDataFromNet(url);
        showDataToTopNews();
    }

    /**
     * 为顶部轮播图添加数据
     */
    private void showDataToTopNews() {
        String testUrl = "http://api.jisuapi.com/news/get?channel=" + channel + "&start=0&num=5&appkey=d6c1207126bca7d5";
        RequestParams params = new RequestParams(testUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<TestContentNewsBean> dataFromJson = getDataFromJson(result);
                list = dataFromJson;
                viewPager.setAdapter(new MyTopNewsAdapter());
                ll_point_group.removeAllViews();//移除所有的红点
                for (int j = 0; j < list.size(); j++) {
                    ImageView imageView = new ImageView(context);
                    imageView.setBackgroundResource(R.drawable.point_selector);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(org.xutils.common.util.DensityUtil.dip2px(5), org.xutils.common.util.DensityUtil.dip2px(5));
                    if(j == 0){
                        imageView.setEnabled(true);
                    }else{
                        imageView.setEnabled(false);
                        params.leftMargin = org.xutils.common.util.DensityUtil.dip2px(8);
                    }
                    imageView.setLayoutParams(params);
                    ll_point_group.addView(imageView);
                }

                //监听页面的改变事件，设置文本变化和文本变化
                viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
                //默认显示第一个页面的文字
                title.setText(list.get(0).getTitle());
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {}
        });
    }


    /**
     * 用于初始加载和下拉刷新时加载要显示的数据
     * @param url 加载数据的地址
     */
    private void getDataFromNet(String url){
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(10000);//设置超时时间
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: "+result);
                processData(result);//解析和处理显示数据
//                listView.onRefreshFinish(true, 0);//刷新成功，隐藏下拉刷新控件，重新显示数据，更新时间
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: "+ex.toString());
//                listView.onRefreshFinish(false, 0);//刷新失败，隐藏下拉刷新控件，不更新时间，并且toast提示更新失败
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "onCancelled: "+cex.toString());
            }

            @Override
            public void onFinished() {
                Log.e(TAG, "onFinished: 请求完成");
            }
        });
    }

    private int prePosition;//上一次高亮显示的位置

    /**
     * 为list添加数据
     * @param json 要显示的json数据
     */
    private void processData(String json) {
        contentList = getDataFromJson(json);
        Log.e(TAG, "打印新闻列表的数据" + contentList.toString());
        adapter = new MyTestContentNewsAdapter();//设置listview的适配器
        listView.setAdapter(adapter);
    }

    /**
     * 为上拉加载更多动态设置数据
     */
    private void getMoreData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < 3; i++) {
                    if (count == 40){
                        handler.sendEmptyMessage(1);
                    }else{

                        contentList.add(allDataList.get(count++));
                        Log.e(TAG, "count为：" + count);
                        handler.sendEmptyMessage(2);
                    }
                }
            }
        }.start();
    }

    /**
     * 新闻的数据适配器
     */
    class MyTestContentNewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contentList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.found_news_item, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.new_icon);
                viewHolder.newTitle = (TextView) convertView.findViewById(R.id.new_title);
                viewHolder.newSrc = (TextView) convertView.findViewById(R.id.new_src);
                viewHolder.newTime = (TextView) convertView.findViewById(R.id.new_time);
                viewHolder.browseNumber = (TextView) convertView.findViewById(R.id.new_browse_number);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //显示相应的数据
            TestContentNewsBean newsBean = contentList.get(position);
            x.image().bind(viewHolder.imageView, newsBean.getImageUrl(), imageOptions);//请求图片
            viewHolder.newTitle.setText(newsBean.getTitle());
            viewHolder.newSrc.setText(newsBean.getSrc());
            String[] times = newsBean.getTime().split("-");
            viewHolder.newTime.setText(times[1] + "/" + times[2]);
            int browseNumber = (int)(100+Math.random()*(999));
            viewHolder.browseNumber.setText(browseNumber + "");
            return convertView;
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //1.设置文本
            title.setText(list.get(position).getTitle());
            //2对应页面的红点高亮
            //将之前的点变成灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //将当前点变成红色
            ll_point_group.getChildAt(position).setEnabled(true);
            //记录更新上一次的位置
            prePosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyTopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.mipmap.news_pic_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            TestContentNewsBean testBean = list.get(position);
            String imageUrl = testBean.getImageUrl();
//            x.image().bind(imageView, imageUrl);
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class ViewHolder{
        public ImageView imageView;
        public TextView newSrc;
        public TextView newTitle;
        public TextView newTime;
        public TextView browseNumber;
    }

    private void getAllDataList(){
        String testUrl = "http://api.jisuapi.com/news/get?channel=" + channel + "&start=0&num=40&appkey=d6c1207126bca7d5";
        RequestParams params = new RequestParams(testUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonNews = null;
                TestContentNewsBean newsBean = null;
                try {
                    jsonNews = new JSONObject(result);
                    jsonNews = jsonNews.getJSONObject("result");
                    JSONArray jsonArrayNews = jsonNews.getJSONArray("list");
                    for (int s = 0; s < jsonArrayNews.length(); s++) {
                        jsonNews = jsonArrayNews.getJSONObject(s);
                        newsBean = new TestContentNewsBean();
                        newsBean.setImageUrl(jsonNews.getString("pic"));
                        newsBean.setTitle(jsonNews.getString("title"));
                        newsBean.setSrc(jsonNews.getString("src"));
                        newsBean.setTime(jsonNews.getString("time"));
                        newsBean.setContentUrl(jsonNews.getString("url"));
                        allDataList.add(newsBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {}
            @Override
            public void onCancelled(CancelledException cex) {}
            @Override
            public void onFinished() {}
        });
    }

    //用于解析数据
    private List<TestContentNewsBean> getDataFromJson(String json){
        List<TestContentNewsBean> dataList = new ArrayList<TestContentNewsBean>();
        JSONObject jsonNews = null;
        TestContentNewsBean newsBean = null;
        try {
            jsonNews = new JSONObject(json);
            jsonNews = jsonNews.optJSONObject("result");
            JSONArray jsonArrayNews = jsonNews.optJSONArray("list");
            for (int s = 0; s < jsonArrayNews.length(); s++) {
                jsonNews = jsonArrayNews.optJSONObject(s);
                newsBean = new TestContentNewsBean();
                newsBean.setImageUrl(jsonNews.optString("pic"));
                newsBean.setTitle(jsonNews.optString("title"));
                newsBean.setSrc(jsonNews.optString("src"));
                newsBean.setTime(jsonNews.optString("time"));
                newsBean.setContentUrl(jsonNews.optString("url"));
                dataList.add(newsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }



}
