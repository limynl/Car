package com.team.car.activitys.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.widgets.CleanableEditText;
import com.team.car.widgets.ToastUtil;
import com.team.car.widgets.dialogview.SVProgressHUD;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.R.getStringRes;

/**
 * Created by Lmy on 2017/1/15.
 * email 1434117404@qq.com
 */

public class RegisterActivity extends Activity implements View.OnClickListener{
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private ImageView topbar_left;
    private ToastUtil toastUtil = new ToastUtil();

    private CleanableEditText phoneNumber;//手机号
    private CleanableEditText code;//验证码
    private Button getCord;//获取验证码按钮
    private TextView saveCord;//验证按钮
    private String iPhone;
    private String iCord;
    private int time = 60;//两次获取验证码的时间间隔
    private boolean flag = true;//验证码是否正确标记
    private int category;//注册的类别
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        //获得注册的类别，用于判断是用户注册还是商家注册
        Intent fIntent = getIntent();
        Bundle bundle = fIntent.getExtras();
        int category = Integer.parseInt(bundle.getString("user_register"));
        Log.e(TAG, "注册类别: "+flag);
        init();//初始化控件
        //initSDK():初始化SDK，单例，可以多次调用；任何方法调用前，必须先初始化，相当于SDK入口
        SMSSDK.initSDK(RegisterActivity.this,"1a442baece8ba","8060130de4ada3e60349f2c342b08ad3");
        //操作回调，有四个回调方法，且都不在UI线程下，如果要在其中执行UI操作，应使用Handler发送一个消息给UI线程处理
        EventHandler eh=new EventHandler(){
            /*
            afterEvent在操作结束时触发
            int result：操作结果，SMSSDK.RESULT_COMPLETE表示操作成功；SMSSDK.RESULT_ERROR表示操作失败
            Object data：事件操作结果，其具体取值根据参数result而定
             */
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //registerEventHandler(EventHandler handler)注册回调接口，
        //用来往SMSSDK中注册一个事件接收器，SMSSDK允许开发者注册任意数量的接收器，所有接收器都会在事件被触发时收到消息。
        SMSSDK.registerEventHandler(eh);
    }
    private void init() {
        phoneNumber = (CleanableEditText) findViewById(R.id.phone_number);
        code = (CleanableEditText) findViewById(R.id.code);
        getCord = (Button) findViewById(R.id.getCord);
        saveCord = (TextView) findViewById(R.id.btn_register);
        topbar_left = (ImageView) findViewById(R.id.topbar_left);
        getCord.setOnClickListener(this);
        saveCord.setOnClickListener(this);
        topbar_left.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getCord:
                if(!TextUtils.isEmpty(phoneNumber.getText().toString().trim())){
                    if(phoneNumber.getText().toString().trim().length()==11){
                        iPhone = phoneNumber.getText().toString().trim();
                        //用于向服务器请求发送验证码的服务，需要接受传递国家代号和接受验证码的手机号码，支持此服务的国家代码在getSupportedCountries中获取
                        SMSSDK.getVerificationCode("86",iPhone);
                        code.requestFocus();
                    }else{
                        toastUtil.Short(RegisterActivity.this, "请输入完整电话号码").show();
                        phoneNumber.requestFocus();
                    }
                }else{
                    toastUtil.Short(RegisterActivity.this, "请输入您的电话号码").show();
                    phoneNumber.requestFocus();
                }
                break;
            case R.id.btn_register:
                if(!TextUtils.isEmpty(code.getText().toString().trim())){
                    if(!TextUtils.isEmpty(code.getText().toString().trim())){
                        if(code.getText().toString().trim().length()==4){
                            SVProgressHUD.showWithStatus(RegisterActivity.this,"Loading...");
                            iCord = code.getText().toString().trim();
                            //用于向服务器接受提交接收到的验证码，验证成功后会通过EventHandler返回国家代码和电话号码
                            SMSSDK.submitVerificationCode("86", iPhone, iCord);
                            flag = false;
                        }else{
                            toastUtil.Short(RegisterActivity.this, "请输入完整验证码").show();
                            code.requestFocus();
                        }
                    }else{
                        toastUtil.Short(RegisterActivity.this, "请输入验证码").show();
                        code.requestFocus();
                    }
                }else{
                    toastUtil.Short(RegisterActivity.this, "请输入电话号码").show();
                    phoneNumber.requestFocus();
                }
                break;
            case R.id.topbar_left:
            {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.in_from_left_two, R.anim.out_from_right_two);//左右滑动效果
            }
            default:
                break;
        }
    }
    //验证码送成功后提示文字
    private void reminderText() {
        handlerText.sendEmptyMessageDelayed(1, 0);
    }

    //总共创建两个Handler对象，一个对象来改变获取验证码按钮的可点击属性，防止获取验证码过于频繁
    //另一个对象获取信息后，通过返回的结果来判断是否验证成功

    Handler handlerText =new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what==1){
                if(time>0){
                    getCord.setText(time+"秒后可重新发送");
                    time--;
                    handlerText.sendEmptyMessageDelayed(1, 1000);
                }else{
                    getCord.setText("重新发送");
                    time = 60;
                }
            }else{//重置获取验证码
                code.setText("");
                getCord.setText("重新发送");
                time = 60;
            }
        }
    };
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event="+event);
            if (result == SMSSDK.RESULT_COMPLETE) {//回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
                    handlerText.sendEmptyMessage(2);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    SVProgressHUD.isCancel(RegisterActivity.this, true);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    //这里可以添加用户


                    toastUtil.Short(RegisterActivity.this, "注册成功").show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                    overridePendingTransition(R.anim.in_from_left_two, R.anim.out_from_right_two);//左右滑动效果
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){//服务器验证码发送成功
                    reminderText();
                    toastUtil.Short(RegisterActivity.this, "验证码已经发送").show();
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
                    toastUtil.Short(RegisterActivity.this, "获取国家列表成功").show();
                }
            } else {
                if(flag){
                    toastUtil.Short(RegisterActivity.this, "验证码获取失败，请重新获取").show();
                    phoneNumber.requestFocus();
                }else{
                    //当result=SMSSDK.RESULT_ERROR，data的类型的Throwable，如果服务器有返回错误码，那么这个Throwable的message就存放着服务器返回的json数据，可从中读取相关信息
                    ((Throwable) data).printStackTrace();
                    int resId = getStringRes(RegisterActivity.this, "smssdk_network_error");
                    toastUtil.Short(RegisterActivity.this, "验证码错误").show();
                    code.selectAll();
                    if (resId > 0) {
                        toastUtil.Short(RegisterActivity.this, resId).show();
                    }
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterAllEventHandler()：反注册函数，实现反注册，且必须和registerEventHandler配套使用，否则可能造成内存泄漏
        SMSSDK.unregisterAllEventHandler();
    }


}
