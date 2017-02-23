package com.team.car.widgets.dialog1;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.View;

public class SlideTop extends BaseEffects{
    @SuppressLint("NewApi")
	@Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "translationY", -300, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)
        );
    }
}
