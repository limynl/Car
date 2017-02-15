package com.team.car.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.ninegrid.NineGridView;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.qiyukf.unicorn.api.YSFOptions;
import com.team.car.utils.imageloader.GlideImageLoader;
import com.team.car.utils.imageloader.PicassoImageLoader;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 *
 * Created by Lmy on 2017/1/18.
 * email 1434117404@qq.com
 */

public class MyApplication extends Application{
    private static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication myApplication;
    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化全局对象
         myApplication = this;

        // 在使用 SDK 各组之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);

        //xUtils初始化
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        //建立Volley请求队列
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //初始化网易七鱼在线客服
        Unicorn.init(this, "47e5ebd5bddb6f9bfde7cbaebbee47be", options(), new UnicornImageLoader() {
            @Nullable
            @Override
            public Bitmap loadImageSync(String uri, int width, int height) {
                return null;
            }

            @Override
            public void loadImage(String uri, int width, int height, ImageLoaderListener listener) {
            }
        });

        // 九宫格图片展示及加载方式初始化
        NineGridView.setImageLoader(new PicassoImageLoader());

        //系统图片多选ImagePicker初始化
        initImagePicker();

    }

    /**
     * 获取Application Context
     */
    public static Context getAppContext() {
        return myApplication != null ? myApplication.getApplicationContext() : null;
    }

    /**
     * 设置Volley网络请求队列接口
     * @return
     */
    public static RequestQueue getRequestQueue(){
        return requestQueue;
    }

    /**
     * 如果返回值为null，则全部使用默认参数。
     * @return
     */
    private YSFOptions options() {
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        options.savePowerConfig = new SavePowerConfig();
        return options;
    }

    /**
     * 系统图片多选ImagePicker初始化
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

}
