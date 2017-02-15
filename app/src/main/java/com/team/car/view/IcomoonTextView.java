package com.team.car.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.team.car.utils.FontCustomHelper;


/**
 * 引用特殊字体的 TextView
 * Created by Lmy on 2017/1/20.
 * email 1434117404@qq.com
 */

public class IcomoonTextView extends TextView {

	public IcomoonTextView(Context context) {
		super(context);
	}

	public IcomoonTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IcomoonTextView(Context context, AttributeSet attrs, int defSyle) {
		super(context, attrs, defSyle);
	}
	
	
	@Override
	public Typeface getTypeface() {
		return FontCustomHelper.getInstance().getTypeface(getContext());
	}
	
	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(FontCustomHelper.getInstance().getTypeface(getContext()));
	}
	
}
