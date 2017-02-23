package com.team.car.adapter.shop;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.team.car.R;
import com.team.car.entity.shop.ShopMessageBean;

import java.util.List;

/**
 * Created by Lmy on 2017/2/22.
 * email 1434117404@qq.com
 */

public class ShopMessageAdapter extends BaseAdapter {
    private Context context;
    private List<ShopMessageBean> list;

    public ShopMessageAdapter(Context context, List<ShopMessageBean> list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.shop_detail_item, null);
            viewHolder.shopIcon = (ImageView) convertView.findViewById(R.id.shop_icon);
            viewHolder.shopName = (TextView) convertView.findViewById(R.id.shop_name);
            viewHolder.shopAddress = (TextView) convertView.findViewById(R.id.shop_address);
            viewHolder.shopDistance= (TextView) convertView.findViewById(R.id.shop_distance);
            viewHolder.shopType = (TextView) convertView.findViewById(R.id.shop_type);
            viewHolder.shopPrice = (TextView) convertView.findViewById(R.id.shop_price);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ShopMessageBean bean = list.get(position);
        String iconUrl = "http://139.199.23.142:8080/TestShowMessage1/carImages/car" + (int)(1 + Math.random() * 20) + ".jpg";
        Glide.with(context)
                .load(iconUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.shopIcon);
        if (!TextUtils.isEmpty(bean.getShopName())){
            viewHolder.shopName.setText(bean.getShopName());
        }
        if (!TextUtils.isEmpty(bean.getShopAddress())){
            viewHolder.shopAddress.setText(bean.getShopAddress());
        }
        if (!TextUtils.isEmpty(bean.getShopDistance())){
            viewHolder.shopDistance.setText(bean.getShopDistance() + "米");
        }
        if (!TextUtils.isEmpty(bean.getShopType())){
            viewHolder.shopType.setText(bean.getShopType());
        }
        if (!TextUtils.isEmpty(bean.getShopPrice())){
            viewHolder.shopPrice.setText(bean.getShopPrice());
        }
        return convertView;
    }

    class ViewHolder{
        public ImageView shopIcon;
        public TextView shopName;
        public TextView shopAddress;
        public TextView shopDistance;
        public TextView shopType;
        public TextView shopPrice;
    }
}