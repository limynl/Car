package com.team.car.adapter.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.entity.manager.CarIllegalBean;

import java.util.List;

/**
 * Created by Lmy on 2017/2/27.
 * email 1434117404@qq.com
 */

public class IllegalResultAdapter extends BaseAdapter {
    private Context context;
    private List<CarIllegalBean> list;

    public IllegalResultAdapter(Context context, List<CarIllegalBean> list) {
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.car_illegal_query_item, null);
            viewHolder.time = (TextView) convertView.findViewById(R.id.query_time);
            viewHolder.result = (TextView) convertView.findViewById(R.id.query_result);
            viewHolder.location = (TextView) convertView.findViewById(R.id.query_location);
            viewHolder.reason = (TextView) convertView.findViewById(R.id.query_reason);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CarIllegalBean carIllegalBean = list.get(position);
        viewHolder.time.setText(carIllegalBean.getTime());
        viewHolder.result.setText(carIllegalBean.getResult());
        viewHolder.location.setText(carIllegalBean.getLocation());
        viewHolder.reason.setText(carIllegalBean.getReason());
        return convertView;
    }

    class ViewHolder{
        public TextView time;
        public TextView result;
        public TextView location;
        public TextView reason;
    }



}
