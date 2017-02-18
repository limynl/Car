package com.team.car.adapter.car;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.team.car.R;
import com.team.car.entity.car.CarBrandSelectBean;

import java.util.List;

/**
 * Created by Lmy on 2017/2/18.
 * email 1434117404@qq.com
 */

public class CarBrandSelectTwoAdapter extends BaseAdapter{
    private Context context;
    private List<CarBrandSelectBean> list;

    public CarBrandSelectTwoAdapter() {
    }

    public CarBrandSelectTwoAdapter(Context context, List<CarBrandSelectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.car_brand_item_two, null);
            viewHolder.carLogo = (ImageView) convertView.findViewById(R.id.car_brand_logo_two);
            viewHolder.carBrand = (TextView) convertView.findViewById(R.id.car_brand_two);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CarBrandSelectBean carBrandSelectBean = list.get(position);
        Glide.with(context)
                .load(carBrandSelectBean.getCarLogo())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.carLogo);
        viewHolder.carBrand.setText(carBrandSelectBean.getCarBrand());
        return convertView;
    }

    class ViewHolder{
        public ImageView carLogo;
        public TextView carBrand;
    }


}
