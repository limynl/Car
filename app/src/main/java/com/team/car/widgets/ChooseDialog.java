package com.team.car.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.team.car.R;

/**
 * Created by Lmy on 2017/1/19.
 * email 1434117404@qq.com
 */

public class ChooseDialog {
    public static void Initial(final Context context, String text_one, String text_two){
        View view = View.inflate(context, R.layout.photo_choose_dialog, null);
        Button choose_one = (Button) view.findViewById(R.id.choose_one);
        choose_one.setText("用户注册");
        Button choose_two = (Button) view.findViewById(R.id.choose_two);
        choose_two.setText("商家注册");
        Button cancel = (Button) view.findViewById(R.id.cancel);

        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
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

        //条目一
        choose_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseOne();
            }
        });

        //条目二
        choose_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();//显示对话框主题
    }

    private static void chooseOne() {

    }


}
