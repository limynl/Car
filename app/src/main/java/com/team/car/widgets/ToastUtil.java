package com.team.car.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team.car.R;

/**
 * Created by Lmy on 2017/1/15.
 * email 1434117404@qq.com
 */

public class ToastUtil {

    private Toast toast;
    private LinearLayout toastView;

   //修改原布局的Toast
    public ToastUtil() {

    }

    //完全自定义布局Toast
    public ToastUtil(Context context, View view, int duration){
        toast=new Toast(context);
        toast.setView(view);
        toast.setDuration(duration);
    }


    //设置Toast字体及背景颜色
    public ToastUtil setToastBackground() {
        View view = toast.getView();
        view.setBackgroundColor(Color.TRANSPARENT);
        if(view!=null){
            TextView message=((TextView) view.findViewById(android.R.id.message));
            message.setBackgroundResource(R.drawable.toast_radius);
            message.setTextColor(Color.WHITE);
            message.setPadding(20,20,20,20);
        }
        return this;
    }

    /**
     * 短时间显示Toast
     */
    public  ToastUtil Short(Context context, CharSequence message){
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message, Toast.LENGTH_SHORT);
            setToastBackground();
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_SHORT);
            setToastBackground();
        }
        return this;
    }

    //短时间显示Toast
    public ToastUtil Short(Context context, int message) {
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message, Toast.LENGTH_SHORT);
            setToastBackground();
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_SHORT);
            setToastBackground();
        }
        return this;
    }

    //长时间显示Toast
    public ToastUtil Long(Context context, CharSequence message){
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message, Toast.LENGTH_LONG);
            setToastBackground();
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_LONG);
            setToastBackground();
        }
        return this;
    }

    //长时间显示Toast
    public ToastUtil Long(Context context, int message) {
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message, Toast.LENGTH_LONG);
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        return this;
    }

    //自定义显示Toast时间
    public ToastUtil Indefinite(Context context, CharSequence message, int duration) {
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message,duration);
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(duration);
        }
        return this;
    }

    //自定义显示Toast时间
    public ToastUtil Indefinite(Context context, int message, int duration) {
        if(toast==null||(toastView!=null&&toastView.getChildCount()>1)){
            toast= Toast.makeText(context, message,duration);
            toastView=null;
        }else{
            toast.setText(message);
            toast.setDuration(duration);
        }
        return this;
    }

    //显示Toast
    public ToastUtil show (){
        toast.show();
        return this;
    }

    //获取Toast
    public Toast getToast(){
        return toast;
    }
}
