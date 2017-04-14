package hply.com.niugu.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hply.com.niugu.R;
import hply.com.niugu.StringUtils;
import hply.com.niugu.bean.TimeDetialData;

/**
 * Created by wangjd on 2016/7/29 0029.
 */
public class MingXiAdapter extends BaseAdapter {
    List<TimeDetialData> lists;

    public MingXiAdapter(List<TimeDetialData> lists) {
        this.lists = lists;
    }

    @Override
    public int getCount() {

        return lists == null ? 0 : lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timedetial_lsv, parent, false);
            holder.tvDealTime = (TextView) convertView.findViewById(R.id.tv_deal_time);
            holder.tvDealPrice = (TextView) convertView.findViewById(R.id.tv_deal_price);
            holder.tvDealVolume = (TextView) convertView.findViewById(R.id.tv_deal_volume);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvDealTime.setText(StringUtils.getDateFormat("HH:mm",lists.get(position).getUpdate_time()));
        String price= StringUtils.save2Significand(lists.get(position).getPrice());
        holder.tvDealPrice.setText(price=="0.00"?"--":price);
        holder.tvDealVolume.setText(String.valueOf(lists.get(position).getVolume()));

        switch (lists.get(position).getTransaction_type()) {
            case 1:
                holder.tvDealVolume.setTextColor(Color.parseColor("#1ebf61"));
                break;
            case 2:
                holder.tvDealVolume.setTextColor(Color.parseColor("#f14958"));
                break;
            case 3:
                holder.tvDealVolume.setTextColor(Color.parseColor("#858585"));
                break;
        }
        if (lists.get(position).getLast_price_change() > 0) {
            holder.tvDealPrice.setTextColor(Color.parseColor("#F34957"));
        } else {
            holder.tvDealPrice.setTextColor(Color.parseColor("#1ebf61"));
        }
        convertView.setFocusable(false);
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    class ViewHolder {
        TextView tvDealTime, tvDealPrice, tvDealVolume;
    }
}