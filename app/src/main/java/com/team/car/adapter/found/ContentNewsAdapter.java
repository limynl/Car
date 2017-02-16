package com.team.car.adapter.found;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.car.R;
import com.team.car.entity.found.TestContentNewsBean;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 发现栏目中新鲜事的新闻条目适配器
 * Created by Lmy on 2017/2/13.
 * email 1434117404@qq.com
 */

public class ContentNewsAdapter extends BaseAdapter {
    private static final String TAG = ContentNewsAdapter.class.getSimpleName();
    private Context context;
    private List<TestContentNewsBean> contentList;
    private ImageOptions imageOptions;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);//获取系统时间

    public ContentNewsAdapter(Context context, List<TestContentNewsBean> contentList, ImageOptions imageOptions){
        this.context = context;
        this.contentList = contentList;
        this.imageOptions = imageOptions;
    }

    @Override
    public int getCount() {
        return contentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.found_news_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.new_icon);
            viewHolder.newTitle = (TextView) convertView.findViewById(R.id.new_title);
            viewHolder.newSrc = (TextView) convertView.findViewById(R.id.new_src);
            viewHolder.newTime = (TextView) convertView.findViewById(R.id.new_time);
            viewHolder.browseNumber = (TextView) convertView.findViewById(R.id.new_browse_number);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //显示相应的数据
        TestContentNewsBean newsBean = contentList.get(position);
        x.image().bind(viewHolder.imageView, newsBean.getImageUrl(), imageOptions);//请求图片
        if(!TextUtils.isEmpty(newsBean.getTitle())){
            viewHolder.newTitle.setText(newsBean.getTitle());
        }
        if(!TextUtils.isEmpty(newsBean.getSrc())){
            viewHolder.newSrc.setText(newsBean.getSrc());
        }
        String[] times = new String[4];
        if(!TextUtils.isEmpty(newsBean.getTime())){
            times = newsBean.getTime().split(" ")[0].split("-");
        }else{
            times = sdf.format(new Date()).split(" ")[0].split("-");
        }
        viewHolder.newTime.setText(times[1] + "/" + times[2]);
        int browseNumber = (int)(100+Math.random()*(999));
        viewHolder.browseNumber.setText(browseNumber + "");
        return convertView;
    }

    class ViewHolder{
        public ImageView imageView;
        public TextView newSrc;
        public TextView newTitle;
        public TextView newTime;
        public TextView browseNumber;
    }

}
