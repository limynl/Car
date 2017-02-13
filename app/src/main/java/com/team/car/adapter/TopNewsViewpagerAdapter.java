package com.team.car.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.team.car.R;
import com.team.car.entity.TestContentNewsBean;

import java.util.List;

/**
 * 发现栏目的新鲜事中顶部轮播图的适配器
 * Created by Lmy on 2017/2/13.
 * email 1434117404@qq.com
 */

public class TopNewsViewpagerAdapter extends PagerAdapter {
    private Context context;
    private List<TestContentNewsBean> list;

    public TopNewsViewpagerAdapter(Context context, List<TestContentNewsBean> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(R.mipmap.news_pic_default);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView);
        TestContentNewsBean testBean = list.get(position);
        String imageUrl = testBean.getImageUrl();
//            x.image().bind(imageView, imageUrl);
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
