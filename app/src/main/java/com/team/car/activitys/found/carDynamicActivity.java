package com.team.car.activitys.found;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.team.car.R;
import com.team.car.adapter.found.ImagePickerAdapter;
import com.team.car.utils.BitmapUtil;
import com.team.car.utils.ScreenUtil;
import com.team.car.view.WavyLineView;
import com.team.car.widgets.ToastUtil;
import com.team.car.widgets.dialog1.Effectstype;
import com.team.car.widgets.dialog1.NiftyDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lmy on 2017/2/14.
 * email 1434117404@qq.com
 */

public class carDynamicActivity extends Activity implements ImagePickerAdapter.OnRecyclerViewItemClickListener, View.OnClickListener{
    private static final String TAG = carDynamicActivity.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();
    private Effectstype effect;


    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数

    private ImageView back;
    private ImageView submmit;
    private String mFrom;
    private WavyLineView mWavyLine;
    private EditText mEditText;
    private int infoType;
    private List<File> mFiles;
    private List<String> mSmallUrls;
    private boolean isUploadPics;
    private int reqWidth = 0;
    private int reqHeight = 0;
    private Point point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_send_dynamic);
        effect = Effectstype.Slidetop;

        updatePixel();
        bindView();
    }

    private void updatePixel() {
        point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        Log.e(TAG,"宽:"+point.x+",高："+point.y + "hahha.............");
        reqWidth = point.x ;
        reqHeight = point.y ;
    }

    private void bindView() {
        mFiles = new ArrayList<>();
        mSmallUrls = new ArrayList<>();

        back = (ImageView) findViewById(R.id.car_dynamic_back);
        submmit = (ImageView) findViewById(R.id.car_dynamic_finish);
        submmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryDecodeSmallImg2();
            }
        });

        back.setOnClickListener(this);


        // 波浪线设置
        mWavyLine = (WavyLineView) findViewById(R.id.release_wavyLine);
        int initStrokeWidth = 1;
        int initAmplitude = 5;
        float initPeriod = (float) (2 * Math.PI / 60);
        mWavyLine.setPeriod(initPeriod);
        mWavyLine.setAmplitude(initAmplitude);
        mWavyLine.setStrokeWidth(ScreenUtil.dp2px(initStrokeWidth));


        mEditText = (EditText) findViewById(R.id.car_dynamic_content);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }


    private void showDialog(){
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle("发表动态")
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage("确定发表")
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#009688")
                .isCancelableOnTouchOutside(false)
                .withDuration(700)
                .withEffect(effect)
                .withButton1Text("确定")
                .withButton2Text("取消")
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastUtil.Short(carDynamicActivity.this, "确定").show();
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastUtil.Short(carDynamicActivity.this, "取消").show();
                        dialogBuilder.dismiss();
                    }
                })
                .show();
    }

    /**
     * 压缩图片为小图片
     */
    private void tryDecodeSmallImg2() {
        showDialog();
//        showLoading(this);显示弹窗
        for (int i = 0; i < selImageList.size(); i++) {
            Log.e(TAG,"第"+i+"个图片宽:"+selImageList.get(i).width);
            Log.e(TAG,"第"+i+"个图片高:"+selImageList.get(i).height);
            String filePath = selImageList.get(i).path;

            /* 这里假设只对 200k 以上 并且 宽高小的像素 > 400的图片进行裁剪*/
            reqHeight = selImageList.get(i).height;
            reqWidth = selImageList.get(i).width;
            int minSize = Math.min(reqHeight,reqWidth);
            int size = (int) (selImageList.get(i).size/1024);//当前图片的大小
            Log.e(TAG,"图片size:"+size+"KB");
            while (minSize > 350 && size >= 200){
                reqWidth /= 2;
                reqHeight /= 2;
                minSize = Math.min(reqHeight,reqWidth);
            }

            if (reqWidth == 0 || reqHeight == 0){ //拍照返回的宽高为0，这里避免异常
                reqWidth = 390;
                reqHeight = 520;
            }
            Log.e(TAG,"第"+i+"个图片压缩后宽："+reqWidth);
            Log.e(TAG,"第"+i+"个图片压缩后高："+reqHeight);
            // 对图片压缩尺寸为原来的八分之一
            Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(filePath,reqWidth,reqHeight);
            Log.e(TAG,"第"+i+"个图片大小:"+bitmap.getByteCount()/1024+"kb");
            saveBitmapFile(bitmap,filePath);
        }
        Toast.makeText(this, "图片上传成功", Toast.LENGTH_SHORT).show();
//        uploadPic();  // 图片压缩完毕开始上传图片

    }


    //向服务器发送图片
    public void  load(Bitmap photodata) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
            photodata.compress(Bitmap.CompressFormat.PNG, 100, baos);
            baos.close();
            byte[] buffer = baos.toByteArray();
            System.out.println("图片的大小："+buffer.length);

            //将图片的字节流数据加密成base64字符输出
            String photo = Base64.encodeToString(buffer, 0, buffer.length,Base64.DEFAULT);
            String url = "http://139.199.23.142:8080/TestShowMessage1/lmy/ShowPicture";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 把bitmap转换为file
     * @param bitmap    bitmap源
     * @param filePath  文件路径
     */
    public void saveBitmapFile(Bitmap bitmap,String filePath) {
        if(bitmap==null){
            return;
        }
        Log.e(TAG,"文件路径:"+filePath);
        File file = new File(filePath);
        try {
            FileOutputStream bos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            mFiles.add(file);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"失败："+e.getMessage());
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
           case IMAGE_ITEM_ADD:
                new MaterialDialog.Builder(this)
                        .items(R.array.release)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                if (text.equals("相片")){
                                    Log.e(TAG, "onSelection: " + "成功打开相册");
                                    ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                    Intent intent = new Intent(carDynamicActivity.this, com.lzy.imagepicker.ui.ImageGridActivity.class);
                                    startActivityForResult(intent, REQUEST_CODE_SELECT);
                                }
                            }
                        }).show();
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                mFiles.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                selImageList.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFiles != null){
            mFiles.clear();
            mFiles = null;
        }
        if (mSmallUrls != null){
            mSmallUrls.clear();
            mSmallUrls = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_dynamic_back:{
                this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        }
    }
}
