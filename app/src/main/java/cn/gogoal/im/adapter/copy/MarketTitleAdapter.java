package cn.gogoal.im.adapter.copy;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import hply.com.niugu.bean.StockData;

import static cn.gogoal.im.common.StringUtils.pareseStringDouble;


/**
 * Created by huangxx on 2015/9/17.
 */
public class MarketTitleAdapter extends MyBaseAdapter<StockData> {

    public MarketTitleAdapter(ArrayList<StockData> list) {
        super(list);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stockinformation_item, parent, false);
            holder = new ViewHolder();
            holder.price = (TextView) convertView.findViewById(R.id.tv_stock_price);
            holder.rate = (TextView) convertView.findViewById(R.id.tv_stock_rate);
            holder.code = (TextView) convertView.findViewById(R.id.tv_stock_code);
            holder.name = (TextView) convertView.findViewById(R.id.tv_stock_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //================停牌新处理================2016/08 wangjd
        if (list.get(position).getStock_type() != 1) {
            holder.price.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.gray_light));
            holder.price.setText("停牌");

            String price = cn.gogoal.im.common.StringUtils.save2Significand(list.get(position).getCurrent_price());
            holder.price.setText(String.valueOf(price));

        } else {
            holder.price.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.text_color_normal));
//            holder.price.setText(String.valueOf(StringUtils.get2Double(list.get(position).getCurrent_price())));
            holder.price.setText(pareseStringDouble((list.get(position).getCurrent_price()),2));

            try {
                holder.price.setText(String.valueOf(pareseStringDouble(list.get(position).getCurrent_price(),2)));
            } catch (Exception e) {
                holder.price.setText("0.00");
            }
        }

        if (pareseStringDouble(list.get(position).getRate()) < 0) {
            holder.rate.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.stock_green));
            holder.rate.setText(String.valueOf(StockUtils.plusMinus(list.get(position).getRate(),true)));
        } else if (pareseStringDouble(list.get(position).getRate()) == 0) {
            holder.rate.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.gray_light));
            holder.rate.setText(StockUtils.plusMinus(list.get(position).getRate(),true));
        } else {
            holder.rate.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.stock_red));
            holder.rate.setText(StockUtils.plusMinus(list.get(position).getRate(),true));
        }
        holder.code.setText(list.get(position).getStock_code());
        holder.name.setText(list.get(position).getStock_name());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalIntentUtils.go2StockDetail(parent.getContext(),
                        list.get(position).getStock_code(),
                        list.get(position).getStock_name());
            }
        });
        return convertView;
    }

    static class ViewHolder {
        private TextView price;
        private TextView rate;
        private TextView code;
        private TextView name;
    }
}
