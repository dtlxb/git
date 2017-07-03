package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.copy.MyBaseAdapter;
import cn.gogoal.im.bean.f10.EquityRightData;

/**
 * Created by dave.
 * Date: 2017/6/14.
 * Desc: description
 */
public class EquityRightAdapter extends MyBaseAdapter {

    private ArrayList<EquityRightData> contList;
    private Context context;

    public EquityRightAdapter(Context context, ArrayList<EquityRightData> list) {
        super(list);
        this.context = context;
        this.contList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_equity_right, parent, false);
            holder = new ViewHolder();
            holder.linearEquity = (LinearLayout) convertView.findViewById(R.id.linearEquity);
            holder.textReport1 = (TextView) convertView.findViewById(R.id.textReport1);
            holder.textReport2 = (TextView) convertView.findViewById(R.id.textReport2);
            holder.textReport3 = (TextView) convertView.findViewById(R.id.textReport3);
            holder.textReport4 = (TextView) convertView.findViewById(R.id.textReport4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 0) {
            holder.linearEquity.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_1));
        } else {
            holder.linearEquity.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_2));
        }

        holder.textReport1.setText(contList.get(position).getStock_total() != null ?
                contList.get(position).getStock_total() : "--");
        holder.textReport2.setText(contList.get(position).getTradable_stock() != null ?
                contList.get(position).getTradable_stock() : "--");
        holder.textReport3.setText(contList.get(position).getTradable_A_stock() != null ?
                contList.get(position).getTradable_A_stock() : "--");
        holder.textReport4.setText(contList.get(position).getChange_reason() != null ?
                contList.get(position).getChange_reason() : "--");

        return convertView;
    }

    class ViewHolder {
        private LinearLayout linearEquity;
        private TextView textReport1, textReport2, textReport3, textReport4;
    }
}
