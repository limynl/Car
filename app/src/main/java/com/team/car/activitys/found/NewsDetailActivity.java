package com.team.car.activitys.found;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.team.car.R;
import com.team.car.utils.MarqueeText;
import com.team.car.widgets.dialogview.SVProgressHUD;

/**
 * Created by Lmy on 2017/2/13.
 * email 1434117404@qq.com
 */

public class NewsDetailActivity extends Activity implements View.OnClickListener{
    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    private ImageView back;
    private PullToRefreshWebView showDetailNews;
    private WebView webView;
    private MarqueeText contentTitle;

    private String detailContentUrl;
    private String detailContentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_news_detail);
        Log.e(TAG, "新闻详情页面创建了");
        SVProgressHUD.showWithStatus(NewsDetailActivity.this,"加载中...");
        initView();
        setData();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.fount_detail_content_back);
        showDetailNews = (PullToRefreshWebView) findViewById(R.id.show_detail_news);
        webView = showDetailNews.getRefreshableView();
        contentTitle = (MarqueeText) findViewById(R.id.found_detail_content_title);
        back.setOnClickListener(this);
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        detailContentUrl = bundle.getString("detailContentDetailUrl");
        detailContentTitle = bundle.getString("detailContentTitle");

        Log.e(TAG, "得到的数据为：" + detailContentUrl + " -- " + detailContentTitle);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置支持javaScript
        webSettings.setUseWideViewPort(true);//设置双击变大变小
        webSettings.setBuiltInZoomControls(true);//设置缩放按钮
        //禁止从当前网页跳转到系统的浏览器中
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                SVProgressHUD.isCancel(NewsDetailActivity.this, true);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                SVProgressHUD.isCancel(NewsDetailActivity.this, true);
                return true;
            }
        });
        webView.loadUrl(detailContentUrl);
        contentTitle.setText(detailContentTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fount_detail_content_back:
                NewsDetailActivity.this.finish();
                break;
        }
    }
}
