package com.team.car.adapter.found;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.activitys.found.NewsDetailActivity;
import com.team.car.entity.found.ShareBean;

import java.util.List;

/**
 * Created by Lmy on 2017/2/16.
 * email 1434117404@qq.com
 */

public class ShareBaseAdapter extends BaseAdapter {
    private Context context;
    private List<ShareBean> list;

    public ShareBaseAdapter(NewsDetailActivity context, List<ShareBean> list) {
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
            convertView  = View.inflate(context, R.layout.grid_item, null);
            viewHolder.shareIcon = (ImageView) convertView.findViewById(R.id.share_icon);
            viewHolder.shareName = (TextView) convertView.findViewById(R.id.share_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ShareBean shareBean = list.get(position);
        viewHolder.shareIcon.setImageResource(shareBean.getIconId());
        viewHolder.shareName.setText(shareBean.getName());
        return convertView;
    }

    class ViewHolder{
        public ImageView shareIcon;
        public TextView shareName;
    }
}
