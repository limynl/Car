package com.team.car.adapter.car;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.team.car.R;
import com.team.car.entity.car.CarBrandSelectBean;

import java.util.List;

/**
 * 汽车品牌选择适配器
 * Created by Lmy on 2017/2/2.
 * email 1434117404@qq.com
 */

public class CarBrandSelectAdapter extends BaseAdapter implements AbsListView.OnScrollListener,SectionIndexer{
    private List<CarBrandSelectBean> data;
    private LayoutInflater inflater;
    private int mStart, mEnd;//ListView中可见的起始项与最后一项
    private Context context;

    public CarBrandSelectAdapter(Context context, List<CarBrandSelectBean> list){
        data = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.car_brand_item, null);
            viewHolder.carLogo = (ImageView) convertView.findViewById(R.id.car_brand_logo);
            viewHolder.carBrand = (TextView) convertView.findViewById(R.id.car_brand);
            viewHolder.cataLog = (TextView) convertView.findViewById(R.id.car_brand_catalog);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CarBrandSelectBean itemBean = data.get(position);

        String url = itemBean.getCarLogo();
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.carLogo);
        viewHolder.carBrand.setText(itemBean.getCarBrand());

        //// 获取首字母的assii值
        int section = getSectionForPosition(position);
        //通过首字母的assii值来判断是否显示字母
        int positionForSelection = getPositionForSection(section);

        viewHolder.cataLog.setOnClickListener(null);

        if(position == getPositionForSection(section)){
            viewHolder.cataLog.setVisibility(View.VISIBLE);
            viewHolder.cataLog.setText(itemBean.getCarInitial());
        }else{
            viewHolder.cataLog.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
    }

    class ViewHolder{
        public ImageView carLogo;//汽车logo
        public TextView carBrand;//汽车品牌
        public TextView cataLog;//首字母
    }

    public int getSectionForPosition(int position) {
        return data.get(position).getCarInitial().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = data.get(i).getCarInitial();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

}
