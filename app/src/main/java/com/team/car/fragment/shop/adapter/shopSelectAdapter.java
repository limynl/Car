package com.team.car.fragment.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.car.R;


/**
 * Created by Lmy on 2017/2/12.
 * email 1434117404@qq.com
 */

public class shopSelectAdapter extends BaseAdapter {
    private String[] data = null;
    private LayoutInflater inflater;
    private int markPosition = 0;

    public shopSelectAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(String[] data){
       this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.car_shop_item_select, null);
            holder.shop_mark = convertView.findViewById(R.id.v_line_vertical);
            holder.content = (TextView)convertView.findViewById(R.id.shop_content);
            holder.shop_chesked = (ImageView) convertView.findViewById(R.id.v_line_checked);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        String s = data[position];

        holder.content.setText(s);
        if(position == markPosition){
            holder.content.setEnabled(true);
            holder.shop_mark.setVisibility(View.VISIBLE);
            holder.shop_chesked.setVisibility(View.VISIBLE);
        }else{
            holder.content.setEnabled(false);
            holder.shop_mark.setVisibility(View.GONE);
            holder.shop_chesked.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        View shop_mark;
        TextView content;
        ImageView shop_chesked;
    }

    public void checked(int markPosition){
        this.markPosition = markPosition;
        if(markPosition>=0 && markPosition < data.length){
            notifyDataSetChanged();
        }
    }

}
