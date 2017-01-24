package com.team.car.activitys.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team.car.R;

import java.util.List;

/**
 * Created by Lmy on 2017/1/24.
 * email 1434117404@qq.com
 */

public class CityAdapter extends ArrayAdapter<City> {

    private int resourceId;

    public CityAdapter(Context context, int resource, List<City> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City city = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView cityName = (TextView) view.findViewById(R.id.city);
        cityName.setText(city.getName());
        return view;
    }
}
