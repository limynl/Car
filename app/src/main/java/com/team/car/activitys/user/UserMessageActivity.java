package com.team.car.activitys.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.car.R;
import com.team.car.activitys.MainActivity;
import com.team.car.widgets.CleanableEditText;
import com.team.car.widgets.ToastUtil;
import com.team.car.widgets.dialogview.SVProgressHUD;
import com.team.car.widgets.timechooses.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserMessageActivity extends Activity {
    @Bind(R.id.user_head)
    ImageView userHead;//头像
    @Bind(R.id.user_message_nick)
    CleanableEditText userMessageNick;//昵称
    @Bind(R.id.user_message_name)
    CleanableEditText userMessageName;//姓名
    @Bind(R.id.user_message_number)
    CleanableEditText userMessageNumber;//电话号码
    @Bind(R.id.user_message_gender)
    TextView userMessageGender;//性别
    @Bind(R.id.user_message_age)
    TextView userMessageAge;//年龄
    @Bind(R.id.user_message_submit)
    Button userMessageSubmit;//提交
    ToastUtil toastUtil = new ToastUtil();
    private CustomDatePicker customDatePicker1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//获取系统时间
    String temp = sdf.format(new Date()).split(" ")[0];//暂存系统时间，用于初始化

    String gender;//用户性别
    int userAge = 0;//用户年龄


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        ButterKnife.bind(this);
        initDatePicker();
    }

    @OnClick({R.id.user_head, R.id.user_message_back, R.id.choose_gender, R.id.choose_age, R.id.user_message_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_head:{
                showDialog();
            }
                break;
            case R.id.user_message_back:{
                startActivity(new Intent(UserMessageActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.in_from_left_two, R.anim.out_from_right_two);//左右滑动效果
            }
                break;
            case R.id.choose_gender:{

            }
                break;
            case R.id.choose_age:{
                customDatePicker1.show(temp);
            }
                break;
            case R.id.user_message_submit:{
                SVProgressHUD.showWithStatus(UserMessageActivity.this,"请稍等...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                SVProgressHUD.isCancel(UserMessageActivity.this, true);
                startActivity(new Intent(UserMessageActivity.this, MainActivity.class));
                toastUtil.Short(UserMessageActivity.this, "保存成功").show();
                finish();
                overridePendingTransition(R.anim.in_from_left_two, R.anim.out_from_right_two);//左右滑动效果
            }
                break;
        }
    }

    //将用户的出生年月日转换为对应的年龄
    private void initDatePicker() {
        final int[] age = new int[1];
        final String[] nowTimes = sdf.format(new Date()).split(" ")[0].split("-");
        Log.e("UserMessageActivity", "系统时间: " + nowTimes[0] + "-" + nowTimes[1] + "-" + nowTimes[2]);
//        userMessageAge.setText(sdf.format(new Date()).split(" ")[0]);
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                temp = time.split(" ")[0];
                userMessageAge.setText(time.split(" ")[0]);
                String[] currentTime = time.split(" ")[0].split("-");
                Log.e("UserMessageActivity", "选中的时间: " + currentTime[0] + "-" + currentTime[1] + "-" + currentTime[2]);
                age[0] = Integer.parseInt(nowTimes[0]) - Integer.parseInt(currentTime[0]);
                if(Integer.parseInt(nowTimes[0]) >= 2001){
                    if(nowTimes[1].equals(currentTime[1] )&& nowTimes[2].equals(currentTime[2])){
                        userMessageAge.setText(String.valueOf(age[0]));
                        userAge = age[0];
                    }if(!nowTimes[1].equals(currentTime[1])){
                        if(Integer.parseInt(nowTimes[1]) > Integer.parseInt(currentTime[1])){
                            userMessageAge.setText(String.valueOf(age[0] + 1));
                            userAge = age[0] + 1;
                        }else{
                            userMessageAge.setText(String.valueOf(age[0]));
                            userAge = age[0];
                        }
                    }if(nowTimes[1].equals(currentTime[1]) && !nowTimes[2].equals(currentTime[2])){
                        if(Integer.parseInt(nowTimes[2]) > Integer.parseInt(currentTime[2])){
                            userMessageAge.setText(String.valueOf(age[0] + 1));
                            userAge = age[0] + 1;
                        }else{
                            userMessageAge.setText(String.valueOf(age[0]));
                            userAge = age[0];
                        }
                    }
                }else{
                    age[0] = age[0] - 1;
                    if(nowTimes[1].equals(currentTime[1] )&& nowTimes[2].equals(currentTime[2])){
                        userMessageAge.setText(String.valueOf(age[0]));
                        userAge = age[0];
                    }if(!nowTimes[1].equals(currentTime[1])){
                        if(Integer.parseInt(nowTimes[1]) > Integer.parseInt(currentTime[1])){
                            userMessageAge.setText(String.valueOf(age[0] + 1));
                            userAge = age[0] + 1;
                        }else{
                            userMessageAge.setText(String.valueOf(age[0]));
                            userAge = age[0];
                        }
                    }if(nowTimes[1].equals(currentTime[1]) && !nowTimes[2].equals(currentTime[2])){
                        if(Integer.parseInt(nowTimes[2]) > Integer.parseInt(currentTime[2])){
                            userMessageAge.setText(String.valueOf(age[0] + 1));
                            userAge = age[0] + 1;
                        }else{
                            userMessageAge.setText(String.valueOf(age[0]));
                            userAge = age[0];
                        }
                    }
                }
            }
        }, "1900-01-01 00:00", sdf.format(new Date())); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(true); // 不允许循环滚动
    }


    private void showDialog() {
        View view = View.inflate(this,R.layout.photo_choose_dialog, null);
        Button user_choose = (Button) view.findViewById(R.id.choose_one);
        user_choose.setText("拍照");
        Button business_choose = (Button) view.findViewById(R.id.choose_two);
        business_choose.setText("从图库选取");
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
                getImageFromCamera();
                dialog.dismiss();
            }
        });

        //商家注册
        business_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
                dialog.dismiss();
            }
        });

        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastUtil.Long(UserMessageActivity.this, "你点击了取消").show();
                dialog.dismiss();
            }
        });
        dialog.show();//显示对话框主题
    }

    protected void getImageFromCamera() {//拍照获取图片
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera, 2);
        }
        else {
            toastUtil.Short(UserMessageActivity.this, "请确认已经插入SD卡").show();
        }
    }

    protected void getImageFromAlbum() {//从本地图库选取照片
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    //回调方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK){
                toastUtil.Short(UserMessageActivity.this, "获取照片成功").show();
                showYourPic(data);
            }else if (requestCode == 2 && resultCode == Activity.RESULT_OK){
                toastUtil.Short(UserMessageActivity.this, "拍照成功").show();
                showYourPic(data);
            }
        }
    }

    //显示图片
    private void showYourPic(Intent data) {
        Uri selectedImage = data.getData();
        String pic_path;
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        if (picturePath.equals("")){
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
            return;
        }
        pic_path = picturePath; // 保存所添加的图片的路径

        // 缩放图片, width, height 按相同比例缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
        int scale = (int) (options.outWidth / (float) 300);
        if (scale <= 0)
            scale = 1;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(picturePath, options);
//        load(bitmap);
        userHead.setImageBitmap(bitmap);
        userHead.setMaxHeight(90);
        userHead.setMaxWidth(90);
        userHead.setVisibility(ImageView.VISIBLE);
    }


}
