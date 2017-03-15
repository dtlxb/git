package cn.gogoal.im.adapter.stock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.market.StockMarketBanner;

/**
 * author wangjd on 2017/3/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description:轮播适配器
 */

public class AdViewAdapter extends BaseAdapter {

    private List<StockMarketBanner.DataBeanX> dataBeanXes;

    public AdViewAdapter(List<StockMarketBanner.DataBeanX> dataBeanXes) {
        this.dataBeanXes = dataBeanXes;
    }

    @Override
    public int getCount() {
        return dataBeanXes==null?0:dataBeanXes.size();
    }

    @Override
    public Object getItem(int position) {
        return dataBeanXes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market_banner_ad,parent,false);
            holder.tvBanner= (TextView) convertView.findViewById(R.id.item_market_banner_ad);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tvBanner.setText(dataBeanXes.get(position).getData().getNewstitle());
        return convertView;
    }

    private class ViewHolder{
        private TextView tvBanner;
    }
}
