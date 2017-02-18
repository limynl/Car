package com.team.car.adapter.car;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.team.car.R;
import com.team.car.entity.car.CarBean;
import com.team.car.widgets.ToastUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lmy on 2017/2/16.
 * email 1434117404@qq.com
 */

public class ShowCarAdapter extends BaseAdapter {
    private ToastUtil toastUtil = new ToastUtil();
    private Context context;
    private List<CarBean> data;
    btnClickListener listener;

    public ShowCarAdapter(Context context, List<CarBean> data) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.show_car_item, null);
            viewHolder.carIcon = (CircleImageView) convertView.findViewById(R.id.show_car_icon);
            viewHolder.carNumber = (TextView) convertView.findViewById(R.id.show_car_number);
            viewHolder.carBrandMessage = (TextView) convertView.findViewById(R.id.show_car_brand_message);
            viewHolder.deleteCarMessage = (Button) convertView.findViewById(R.id.show_delete_car);
            viewHolder.editCarMessage = (Button) convertView.findViewById(R.id.show_edit_car);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CarBean carBean = data.get(position);
        if (carBean != null) {
            Glide.with(context)
                    .load(carBean.getCarIconUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.carIcon);
            viewHolder.carNumber.setText(carBean.getCarNumber());
            String carBrandMessage = carBean.getCarBrand() + carBean.getCarSpecificModel();
            viewHolder.carBrandMessage.setText(carBrandMessage);
        }else{
            toastUtil.Short(context, "用户车辆信息是空的").show();
        }

        viewHolder.deleteCarMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnDeleteClick(position);
            }
        });

        viewHolder.editCarMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnEditClick(position);
            }
        });


        return convertView;
    }

    class ViewHolder{
        public CircleImageView carIcon;
        public TextView carNumber;
        public TextView carBrandMessage;
        public Button deleteCarMessage;
        public Button editCarMessage;
    }

    public interface btnClickListener{
        public void btnDeleteClick(int position);

        public void btnEditClick(int position);
    }

    public void setBtnClickListener(btnClickListener btnListener) {
        this.listener = btnListener;
    }


}
