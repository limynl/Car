package com.team.car.adapter.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team.car.R;

import java.util.List;

/**
 * Created by Lmy on 2017/2/27.
 * email 1434117404@qq.com
 */

public class ProvinceAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;

    public ProvinceAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.province_item, null);

        TextView provinceName = (TextView) view.findViewById(R.id.province_name);

        String name = data.get(position);
        provinceName.setText(name);
        return view;
    }
}
