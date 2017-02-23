package com.team.car.activitys.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.entity.user.UserBean;
import com.team.car.utils.CommonAdapter;
import com.team.car.utils.CropOption;
import com.team.car.utils.ViewHolder;
import com.team.car.widgets.ToastUtil;
import com.team.car.widgets.timechooses.CustomDatePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static vi.com.gdi.bgl.android.java.EnvDrawText.buffer;

/**
 * 用户个人中心展示与修改
 * Created by Lmy on 2017/1/20.
 * email 1434117404@qq.com
 */

public class UserMessageActivity extends Activity implements View.OnClickListener{
    private static final String TAG = UserMessageActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();

    private ImageView back;
    private ImageView submit;
    private CircleImageView userHead;

    private EditText userNick;
    private TextView usrGender;
    private TextView userBirthday;
    private EditText userAddress;

    private RelativeLayout userGenderSelect;
    private RelativeLayout userBirthdaySelect;

    private CustomDatePicker customDatePicker;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//获取系统时间
    private String temp = sdf.format(new Date()).split(" ")[0];//暂存系统时间，用于初始化

    private final int PHOTO_PICKED_FROM_CAMERA = 1; // 用来标识头像来自系统拍照
    private final int PHOTO_PICKED_FROM_FILE = 2; // 用来标识从相册获取头像
    private final int CROP_FROM_CAMERA = 3;//剪切图片
    private Uri imgUri; // 用于存储图片

    private Dialog dialog;//弹框
    private Button chooseFromCamera;//选择按钮一
    private Button chooseFromPhoto;//选择按钮二
    private Button cancelDialog;//取消按钮
    private View viewDialog;//弹框视图

    private byte[] bitmapByte;//用户头像的byte数组
    private String userHeadPhoto;//用户的头像字节数据流

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        initView();//初始化视图
        initDatePicker();//时间选择器
    }

    /**
     * 初始化视图
     */
    private void initView() {
        back = (ImageView) findViewById(R.id.user_message_back);
        submit = (ImageView) findViewById(R.id.user_message_submit);
        userHead = (CircleImageView) findViewById(R.id.user_message_head);
        userNick = (EditText) findViewById(R.id.user_message_nick);
        usrGender = (TextView) findViewById(R.id.user_message_gender);
        userBirthday = (TextView) findViewById(R.id.user_message_birthday);
        userAddress = (EditText) findViewById(R.id.user_message_address);

        userGenderSelect = (RelativeLayout) findViewById(R.id.user_gender_select);
        userBirthdaySelect = (RelativeLayout) findViewById(R.id.user_birthday_select);

        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        userHead.setOnClickListener(this);
        userGenderSelect.setOnClickListener(this);
        userBirthdaySelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_message_back:{//返回主页
                UserMessageActivity.this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            break;
            case R.id.user_message_submit:{//填写完成提交
                UserBean userBean = new UserBean();
//                userBean.setUserId();//进行用户的身份标识，传到服务器寻找当前的用户，进行匹配
                userBean.setNickName(userNick.getText().toString());
                userBean.setSex(usrGender.getText().toString());
                userBean.setBirthDay(userBirthday.getText().toString());
                userBean.setUserAddress(userAddress.getText().toString());
                saveUserMessage(userBean);
            }
            break;
            case R.id.user_message_head:{//头像选取
                showDialog("拍照", "从相册获取");
                chooseFromCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getIconFromCamera();//拍照获取图片
                        dialog.dismiss();
                    }
                });

                chooseFromPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getIconFromPhoto();//从相册获取图片
                        dialog.dismiss();
                    }
                });

                cancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastUtil.Long(UserMessageActivity.this, "取消干啥啊...").show();
                        dialog.dismiss();
                    }
                });
                dialog.show();//显示对话框主题
            }
            break;
            case R.id.user_gender_select:{//性别选择
                showDialog("男", "女");
                chooseFromCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        usrGender.setText("男");
                        dialog.dismiss();
                    }
                });

                chooseFromPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        usrGender.setText("女");
                        dialog.dismiss();
                    }
                });

                cancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();//显示对话框主题
            }
            break;
            case R.id.user_birthday_select:{//生日选择
                customDatePicker.show(temp);
            }
            break;
        }
    }

    /**
     * 用户进行生日选择
     */
    private void initDatePicker() {
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                userBirthday.setText(time.split(" ")[0]);
            }
        }, "1900-01-01 00:00", sdf.format(new Date())); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(true); // 不允许循环滚动
    }

    /**
     * 弹框显示
     */
    private void showDialog(String value1, String value2) {
        viewDialog = View.inflate(this,R.layout.photo_choose_dialog, null);
        chooseFromCamera = (Button) viewDialog.findViewById(R.id.choose_one);
        chooseFromCamera.setText(value1);
        chooseFromPhoto = (Button) viewDialog.findViewById(R.id.choose_two);
        chooseFromPhoto.setText(value2);
        cancelDialog = (Button) viewDialog.findViewById(R.id.cancel);

        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(viewDialog, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
    }

    /**
     * 从相册获取图片
     */
    private void getIconFromPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_PICKED_FROM_FILE);
    }

    /**
     * 拍照获取图片
     */
    private void getIconFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imgUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"avatar_"+String.valueOf(System.currentTimeMillis())+".png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
        startActivityForResult(intent,PHOTO_PICKED_FROM_CAMERA);
    }

    /**
     * 剪切图片
     */
    private void doCrop(){
        final ArrayList<CropOption> cropOptions = new ArrayList<>();
        final Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent,0);
        int size = list.size();
        if (size == 0){
            toastUtil.Short(UserMessageActivity.this, "当前不支持裁剪图片!").show();
            return;
        }
        intent.setData(imgUri);
        intent.putExtra("outputX",300);
        intent.putExtra("outputY",300);
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("scale",true);
        intent.putExtra("return-data",true);
        //只有一张
        if (size == 1){
            Intent intent1 = new Intent(intent);
            ResolveInfo res = list.get(0);
            intent1.setComponent(new ComponentName(res.activityInfo.packageName,res.activityInfo.name));
            startActivityForResult(intent1,CROP_FROM_CAMERA);
            }else {
            for (ResolveInfo res : list) {
                CropOption co = new CropOption();
                co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                co.appIntent = new Intent(intent);
                co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName,res.activityInfo.name));
                cropOptions.add(co);
            }
            CommonAdapter<CropOption> adapter = new CommonAdapter<CropOption>(this,cropOptions,R.layout.layout_crop_selector) {
                @Override
                public void convert(ViewHolder holder, CropOption item) {
                    holder.setImageDrawable(R.id.iv_icon,item.icon);
                    holder.setText(R.id.tv_name,item.title);
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("choose a app");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(cropOptions.get(which).appIntent,CROP_FROM_CAMERA);
                    }
                });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (imgUri != null){
                        getContentResolver().delete(imgUri,null,null);
                        imgUri = null;
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        switch (requestCode) {
            case PHOTO_PICKED_FROM_CAMERA:
                doCrop();
                break;
            case PHOTO_PICKED_FROM_FILE:
                imgUri = data.getData();
                doCrop();
                break;
            case CROP_FROM_CAMERA:
                if (data != null){
                    setCropImg(data);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 处理剪切后的图片，将其显示到头像处并转换为字节数据流
     * @param picData
     */
    private void setCropImg(Intent picData){
        Bundle bundle = picData.getExtras();
        if (bundle != null){
            Bitmap userHeadBitmap = bundle.getParcelable("data");
            userHead.setImageBitmap(userHeadBitmap);
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                userHeadBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);//将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
                bitmapByte = baos.toByteArray();
                userHeadPhoto = Base64.encodeToString(bitmapByte, 0, buffer.length,Base64.DEFAULT);//将图片的字节流数据加密成base64字符输出
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存用户的数据，将其上传到服务器，并将其更换到侧滑栏进行显示
     */
    private void saveUserMessage(UserBean userBean){
        //以下数据仅仅用于测试
        Intent intent = new Intent(UserMessageActivity.this, UserMainActivity.class);
        Bundle bundle = new Bundle();
        Log.e(TAG, "saveUserMessage: 用户的昵称为：" + userBean.getNickName());
        bundle.putString("userNick", userBean.getNickName());
        Log.e(TAG, "saveUserMessage: 用户的Bitmap数组长度为：" + bitmapByte.length);
        bundle.putByteArray("userHead", bitmapByte);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        UserMessageActivity.this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


        /*String url  = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("userNick", userBean.getNickName());
        map.put("userSex", userBean.getSex());
        map.put("userBirthday", userBean.getBirthDay());
        map.put("userAddress", userBean.getUserAddress());
        VolleyRequestUtil.RequestPost(UserMessageActivity.this, url, "updateUseMessage", map, new VolleyListenerInterface(UserMessageActivity.this, VolleyListenerInterface.mSuccessListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onSuccess(String result) {
                //这里接受服务器返回来的用户头像地址


            }

            @Override
            public void onError(VolleyError error) {
                toastUtil.Short(UserMessageActivity.this, "服务器出现异常了！").show();
            }
        });*/
    }
}
