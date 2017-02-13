package com.team.car.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Lmy on 2017/2/13.
 * email 1434117404@qq.com
 */

public class MarqueeText extends TextView{

    public MarqueeText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeText(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}