package com.team.car.activitys.user;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mob.tools.utils.UIHandler;
import com.team.car.R;
import com.team.car.widgets.ToastUtil;
import com.team.car.widgets.dialogview.SVProgressHUD;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by Lmy on 2017/1/15.
 * email 1434117404@qq.com
 */

public class LoginActivity extends Activity implements View.OnClickListener,PlatformActionListener,Handler.Callback{
    private ToastUtil toastUtil = new ToastUtil();
    private TextView mBtnLogin;
    private TextView register;

    private Button btnSinaLogin, btnQQLogin, btnWechatLogin;
    private TextView mThirdLoginResult;

    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;
    private Platform mPf;

    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;

    public static IWXAPI api;
    final private Context context = LoginActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
        //Mob平台授权,初始化SDK
        ShareSDK.initSDK(this);

        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance("1105819277",context.getApplicationContext());

        //微信平台注册
        regToWx();

        btnSinaLogin = (Button)findViewById(R.id.btn_sina_login);//微博登录
        btnQQLogin = (Button)findViewById(R.id.btn_qq_login);//qq登录
        btnWechatLogin = (Button)findViewById(R.id.btn_wechat_login);//微信登录
        mThirdLoginResult = (TextView)findViewById(R.id.test_result);//测试结果显示
        btnSinaLogin.setOnClickListener(this);
        btnQQLogin.setOnClickListener(this);
        btnWechatLogin.setOnClickListener(this);
    }

    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        register = (TextView) findViewById(R.id.register);
        mBtnLogin.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //登录
            case R.id.main_btn_login:{
                SVProgressHUD.showWithStatus(context, "登录中...");
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(8000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                Timer timer=new Timer();
                timer.schedule(new wait(), 5000);
                SVProgressHUD.isCancel(this, true);
                startActivity(new Intent(context, UserMainActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);//淡入淡出效果
            }
            break;
            //注册
            case R.id.register:
            {
                showDialog();
            }
            break;
            //第三方新浪微博登录
            case R.id.btn_sina_login:
                thirdSinaLogin();
                break;
            //退出新浪微博登录
            /*mPf = ShareSDK.getPlatform(MainActivity.this, SinaWeibo.NAME);
            //如果要删除授权信息，重新授权
            mPf.getDb().removeAccount();
            mThirdLoginResult.setText("退出登录");*/
            //第三方QQ登录
            case R.id.btn_qq_login:
                thirdQQLogin();
                break;
            //第三方微信登录
            case R.id.btn_wechat_login:
                if(!api.isWXAppInstalled()){
                    toastUtil.Short(LoginActivity.this, "请安装微信客户端之后再进行登录").show();
                    return;
                }
                getCode();
                break;
        }
    }
    /**新浪微博授权，获取用户注册信息*/
    private void thirdSinaLogin() {
        //初始化新浪平台
        Platform pf = ShareSDK.getPlatform(context, SinaWeibo.NAME);
        pf.SSOSetting(true);
        //设置监听
        pf.setPlatformActionListener(LoginActivity.this);
        //出现授权界面，获取登陆用户的信息，如果没有授权，会先授权，然后获取用户信息
        pf.authorize();
    }
    /** 新浪微博授权成功回调页面 */
    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    /** 授权失败 */
    @Override
    public void onError(Platform platform, int action, Throwable t) {
        t.printStackTrace();
        t.getMessage();
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    /** 取消授权 */
    @Override
    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_TOAST: {
                String text = String.valueOf(msg.obj);
                toastUtil.Short(LoginActivity.this, text).show();
            }
            break;
            case MSG_ACTION_CCALLBACK: {
                switch (msg.arg1) {
                    case 1: {
                        // 成功, successful notification
                        //http://open.weibo.com/wiki/2/users/show 新浪微博返回结果字段说明
                        Platform pf = ShareSDK.getPlatform(context, SinaWeibo.NAME);
                        //常用返回字段
                        Log.e("sharesdk use_id", pf.getDb().getUserId()); //获取用户id
                        Log.e("sharesdk use_name", pf.getDb().getUserName());//获取用户名称
                        Log.e("sharesdk use_icon", pf.getDb().getUserIcon());//获取用户头像
                        Log.e("sharesdk use_gender", pf.getDb().getUserGender());//获取用户性别
                        Log.e("sharesdk use_gender", pf.getDb().getPlatformNname());//获取平台名称
                        mThirdLoginResult.setText(pf.getDb().getUserId());
                    }
                    break;
                    case 2: {
                        mThirdLoginResult.setText("登录失败");
                    }
                    break;
                    case 3: {
                        // 取消, cancel notification
                        mThirdLoginResult.setText("取消授权");
                    }
                    break;
                }
            }
            break;
            case MSG_CANCEL_NOTIFY: {
                NotificationManager nm = (NotificationManager) msg.obj;
                if (nm != null) {
                    nm.cancel(msg.arg1);
                }
            }
            break;
        }
        return false;
    }



    /**QQ登录授权，获取用户信息*/
    public void thirdQQLogin(){
        mIUiListener = new BaseUiListener();
        if(!mTencent.isSessionValid()){//判断是否登录过
            mTencent.login(LoginActivity.this,"all", mIUiListener);//all表示获取所有权限
        }else{//登录过注销之后再登录
            mTencent.logout(LoginActivity.this);
            mTencent.login(LoginActivity.this,"all", mIUiListener);
        }
    }

    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
//            Toast.makeText(TestLoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(),qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        JSONObject jsonString = (JSONObject)response;
                        try {
                            //常用字段
                            String name = jsonString.getString("nickname");//获取用户名
                            String iconQQ = jsonString.getString("figureurl_qq_1");//获取QQ用户的头像
                            String iconQZ = jsonString.getString("figureurl_1");//获取QQ空间头像
                            String province = jsonString.getString("province");//获取地址
                            String gender = jsonString.getString("gender");//获取性别
                            mThirdLoginResult.setText(name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        toastUtil.Short(LoginActivity.this, "登录成功").show();
                        Log.e("Mainactivity","登录成功"+response.toString());
                        /*Intent intent = new Intent(TestLoginActivity.this, TestExampleAsyncActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                    @Override
                    public void onError(UiError uiError) {
                        Log.e("Mainactivity","登录失败"+uiError.toString());
                    }
                    @Override
                    public void onCancel() {
                        Log.e("Mainactivity","登录取消");
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Log.e("MainActivity", "onError: " + uiError.toString());
            toastUtil.Short(LoginActivity.this, "授权失败").show();
        }

        @Override
        public void onCancel() {
            toastUtil.Short(LoginActivity.this, "授权取消").show();
        }
    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /** 第三方微信登录  */
    private void regToWx(){
        // 通过WXAPIFactory工厂,获得IWXAPI的实例
        api = WXAPIFactory.createWXAPI(LoginActivity.this, "wx4868b35061f87885", true);
        // 将应用的appid注册到微信
        api.registerApp("wx4868b35061f87885");
    }

    private void getCode(){
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "carjob_wx_login";
        api.sendReq(req);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    private void showDialog() {
        View view = View.inflate(this,R.layout.photo_choose_dialog, null);
        Button user_choose = (Button) view.findViewById(R.id.choose_one);
        user_choose.setText("用户注册");
        Button business_choose = (Button) view.findViewById(R.id.choose_two);
        business_choose.setText("商家注册");
        Button cancel = (Button) view.findViewById(R.id.cancel);

        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = window.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);

        //用户注册
        user_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastUtil.Long(LoginActivity.this, "用户注册").show();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("user_register", "1");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                dialog.dismiss();
            }
        });

        //商家注册
        business_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastUtil.Long(LoginActivity.this, "商家注册").show();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("business_register", "2");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                dialog.dismiss();
            }
        });

        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastUtil.Long(LoginActivity.this, "你点击了取消").show();
                dialog.dismiss();
            }
        });
        dialog.show();//显示对话框主题
    }

    class wait extends TimerTask {
        @Override
        public void run() {
            finish();
        }
    }
}
