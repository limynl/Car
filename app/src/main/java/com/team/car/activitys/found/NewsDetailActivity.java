package com.team.car.activitys.found;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.team.car.R;
import com.team.car.adapter.found.SharePopBaseAdapter;
import com.team.car.entity.found.SharePopBean;
import com.team.car.utils.MarqueeText;
import com.team.car.widgets.dialogview.SVProgressHUD;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Lmy on 2017/2/13.
 * email 1434117404@qq.com
 */

public class NewsDetailActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    private ImageView back;
    private ImageView newsShare;
    private PullToRefreshWebView showDetailNews;
    private WebView webView;
    private MarqueeText contentTitle;

    private String detailContentUrl;
    private String detailContentTitle;

    private PopupWindow pw;
    private View popView;
    private GridView gv;
    private TextView carName;
    private String[] names = {"QQ", "空间", "新浪", "微信", "朋友圈"};
    private int[] iconId = {R.drawable.ssdk_oks_classic_qq, R.drawable.ssdk_oks_classic_qzone,
                            R.drawable.ssdk_oks_classic_sinaweibo, R.drawable.ssdk_oks_classic_wechat,
                            R.drawable.ssdk_oks_classic_wechatmoments};
    private List<SharePopBean> shareBeanList = new ArrayList<SharePopBean>();
    private SharePopBaseAdapter shareBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_news_detail);
        ShareSDK.initSDK(this);
        Log.e(TAG, "新闻详情页面创建了");
        SVProgressHUD.showWithStatus(NewsDetailActivity.this,"加载中...");
        initView();
        setData();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.fount_detail_content_back);
        newsShare = (ImageView) findViewById(R.id.found_news_share);
        showDetailNews = (PullToRefreshWebView) findViewById(R.id.show_detail_news);
        webView = showDetailNews.getRefreshableView();
        contentTitle = (MarqueeText) findViewById(R.id.found_detail_content_title);

        shareBeanList.add(new SharePopBean(iconId[0], names[0]));
        shareBeanList.add(new SharePopBean(iconId[1], names[1]));
        shareBeanList.add(new SharePopBean(iconId[2], names[2]));
        shareBeanList.add(new SharePopBean(iconId[3], names[3]));
        shareBeanList.add(new SharePopBean(iconId[4], names[4]));
        popView=getLayoutInflater().inflate(R.layout.share_grid,null);
        gv=(GridView)popView.findViewById(R.id.share_grid);
        shareBaseAdapter = new SharePopBaseAdapter(NewsDetailActivity.this, shareBeanList);
        gv.setAdapter(shareBaseAdapter);

        back.setOnClickListener(this);
        newsShare.setOnClickListener(this);
        gv.setOnItemClickListener(this);
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

    public PopupWindow getPopWindow(View view){
        PopupWindow popupWindow=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha((float) 0.5);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((float) 1.0);
            }
        });
        return popupWindow;
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fount_detail_content_back:
                NewsDetailActivity.this.finish();
                break;
            case R.id.found_news_share:{
                /*OnekeyShare oks = new OnekeyShare();
                oks.setText(detailContentTitle);
                oks.setTitleUrl(detailContentUrl);
                oks.setUrl(detailContentUrl);
                oks.show(this);*/

                pw = getPopWindow(popView);
            }
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = shareBeanList.get(position).getName();
        if(TextUtils.equals(name, "QQ")){
            Platform qq = ShareSDK.getPlatform(NewsDetailActivity.this, QQ.NAME);
            QQ.ShareParams sp = new QQ.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setTitle(detailContentTitle);
            sp.setTitleUrl(detailContentUrl);
            qq.share(sp);
            pw.dismiss();
        }else if (TextUtils.equals(name, "空间")){
            Platform qZone = ShareSDK.getPlatform(NewsDetailActivity.this, QZone.NAME);
            QZone.ShareParams sp = new QZone.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setTitle(detailContentTitle);
            sp.setTitleUrl(detailContentUrl);
            qZone.share(sp);
            pw.dismiss();
        }else if(TextUtils.equals(name, "新浪")){
            SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
            sp.setText("测试分享的文本");
            sp.setImagePath("http://139.199.23.142:8080/TestShowMessage1/logo.png");

            Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
            weibo.share(sp);
//            Platform sina = ShareSDK.getPlatform(NewsDetailActivity.this, SinaWeibo.NAME);
//            SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
//            sp.setShareType(Platform.SHARE_WEBPAGE);
//            sp.setText(detailContentTitle);

//            sp.setTitle(detailContentTitle);
//            sp.setTitleUrl(detailContentUrl);
            /*sp.setText(detailContentTitle);
            sp.setImagePath("http://139.199.23.142:8080/TestShowMessage1/logo.png");*/
//            sina.share(sp);
            pw.dismiss();
        }else if (TextUtils.equals(name, "微信")){
            Platform weChat = ShareSDK.getPlatform(NewsDetailActivity.this, Wechat.NAME);
            Wechat.ShareParams sp = new Wechat.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setTitle(detailContentTitle);
            sp.setTitleUrl(detailContentUrl);
            weChat.share(sp);
            pw.dismiss();
        } else if (TextUtils.equals(name, "朋友圈")){
            Platform moments = ShareSDK.getPlatform(NewsDetailActivity.this, WechatMoments.NAME);
            WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setTitle(detailContentTitle);
            sp.setTitleUrl(detailContentUrl);
            moments.share(sp);
            pw.dismiss();
        }
    }
}
