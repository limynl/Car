package com.team.car.widgets.dialog1;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.view.View;

@SuppressLint("NewApi")
public abstract  class BaseEffects {
	String index ;
    private static final int DURATION = 1 * 700;
    protected long mDuration =DURATION ;
    private AnimatorSet mAnimatorSet;{
        mAnimatorSet = new AnimatorSet();
    }
    protected abstract void setupAnimation(View view);
    public void start(View view) {
        reset(view);
        setupAnimation(view);
        mAnimatorSet.start();
    }
    public void reset(View view) {
    }
    public AnimatorSet getAnimatorSet() {
        return mAnimatorSet;
    }
    public void setDuration(long duration) {
        this.mDuration = duration;
    }
}
