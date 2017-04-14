package cn.gogoal.im.adapter.stock;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockDetailMarketIndexActivity;
import cn.gogoal.im.bean.stock.StockMarketBean;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;

/**
 * author wangjd on 2017/3/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description:轮播适配器
 */

public class MyStockMarketAdapter extends BaseAdapter {

    private List<StockMarketBean.DataBean.HangqingBean> datas;

    private Context context;
    public MyStockMarketAdapter(Context context,List<StockMarketBean.DataBean.HangqingBean> datas) {
        this.datas=datas;
        this.context=context;
    }

    @Override
    public int getCount() {
        return datas==null?0:datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mystock_market,parent,false);
            holder.tvMarketName= (TextView) convertView.findViewById(R.id.tv_mystock_market_name);
            holder.tvMarketPrice= (TextView) convertView.findViewById(R.id.tv_mystock_market_price);
            holder.tvMarketPriceChange$Rate= (TextView) convertView.findViewById(R.id.tv_mystock_market_pricechange$rate);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.tvMarketName.setText(String.valueOf(datas.get(position).getName().charAt(0)));
        holder.tvMarketPrice.setText(StringUtils.saveSignificand(datas.get(position).getPrice(),2));
        holder.tvMarketPriceChange$Rate.setText(StringUtils.saveSignificand(datas.get(position).getPrice_change(),2)
                +"\u3000"+ StockUtils.plusMinus(datas.get(position).getPrice_change_rate(),true));

        int rateColor = StockUtils.getStockRateColor(datas.get(position).getPrice_change_rate());
        holder.tvMarketPrice.setTextColor(ContextCompat.getColor(parent.getContext(),rateColor));
        holder.tvMarketPriceChange$Rate.setTextColor(ContextCompat.getColor(parent.getContext(),rateColor));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StockDetailMarketIndexActivity.class);
                intent.putExtra("stockName", datas.get(position).getName());
                intent.putExtra("stockCode", datas.get(position).getFullcode());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder{
        private TextView tvMarketName;
        private TextView tvMarketPrice;
        private TextView tvMarketPriceChange$Rate;
    }
}
