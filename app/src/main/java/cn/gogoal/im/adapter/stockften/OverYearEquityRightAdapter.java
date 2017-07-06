package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.copy.MyBaseAdapter;

/**
 * Created by dave.
 * Date: 2017/6/14.
 * Desc: description
 */
public class OverYearEquityRightAdapter extends MyBaseAdapter {

    private ArrayList<JSONArray> contList;
    private Context context;

    public OverYearEquityRightAdapter(Context context, ArrayList<JSONArray> list) {
        super(list);
        this.context = context;
        this.contList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_over_year_equity_right, parent, false);
            holder = new ViewHolder();
            holder.linearEquity = (LinearLayout) convertView.findViewById(R.id.linearEquity);
            holder.textReport1 = (TextView) convertView.findViewById(R.id.textReport1);
            holder.textReport2 = (TextView) convertView.findViewById(R.id.textReport2);
            holder.textReport3 = (TextView) convertView.findViewById(R.id.textReport3);
            holder.textReport4 = (TextView) convertView.findViewById(R.id.textReport4);
            holder.textReport5 = (TextView) convertView.findViewById(R.id.textReport5);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 0) {
            holder.linearEquity.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_1));
        } else {
            holder.linearEquity.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_2));
        }

        if (contList.get(position).size() == 0) {
            holder.textReport1.setVisibility(View.VISIBLE);

            holder.textReport1.setText("--");
        } else if (contList.get(position).size() == 1) {
            holder.textReport1.setVisibility(View.VISIBLE);

            holder.textReport1.setText(contList.get(position).getString(0) != null ?
                    contList.get(position).getString(0) : "--");
        } else if (contList.get(position).size() == 2) {
            holder.textReport1.setVisibility(View.VISIBLE);
            holder.textReport2.setVisibility(View.VISIBLE);

            holder.textReport1.setText(contList.get(position).getString(0) != null ?
                    contList.get(position).getString(0) : "--");
            holder.textReport2.setText(contList.get(position).getString(1) != null ?
                    contList.get(position).getString(1) : "--");
        } else if (contList.get(position).size() == 3) {
            holder.textReport1.setVisibility(View.VISIBLE);
            holder.textReport2.setVisibility(View.VISIBLE);
            holder.textReport3.setVisibility(View.VISIBLE);

            holder.textReport1.setText(contList.get(position).getString(0) != null ?
                    contList.get(position).getString(0) : "--");
            holder.textReport2.setText(contList.get(position).getString(1) != null ?
                    contList.get(position).getString(1) : "--");
            holder.textReport3.setText(contList.get(position).getString(2) != null ?
                    contList.get(position).getString(2) : "--");
        } else if (contList.get(position).size() == 4) {
            holder.textReport1.setVisibility(View.VISIBLE);
            holder.textReport2.setVisibility(View.VISIBLE);
            holder.textReport3.setVisibility(View.VISIBLE);
            holder.textReport4.setVisibility(View.VISIBLE);

            holder.textReport1.setText(contList.get(position).getString(0) != null ?
                    contList.get(position).getString(0) : "--");
            holder.textReport2.setText(contList.get(position).getString(1) != null ?
                    contList.get(position).getString(1) : "--");
            holder.textReport3.setText(contList.get(position).getString(2) != null ?
                    contList.get(position).getString(2) : "--");
            holder.textReport4.setText(contList.get(position).getString(3) != null ?
                    contList.get(position).getString(3) : "--");
        } else if (contList.get(position).size() == 5) {
            holder.textReport1.setVisibility(View.VISIBLE);
            holder.textReport2.setVisibility(View.VISIBLE);
            holder.textReport3.setVisibility(View.VISIBLE);
            holder.textReport4.setVisibility(View.VISIBLE);
            holder.textReport5.setVisibility(View.VISIBLE);

            holder.textReport1.setText(contList.get(position).getString(0) != null ?
                    contList.get(position).getString(0) : "--");
            holder.textReport2.setText(contList.get(position).getString(1) != null ?
                    contList.get(position).getString(1) : "--");
            holder.textReport3.setText(contList.get(position).getString(2) != null ?
                    contList.get(position).getString(2) : "--");
            holder.textReport4.setText(contList.get(position).getString(3) != null ?
                    contList.get(position).getString(3) : "--");
            holder.textReport5.setText(contList.get(position).getString(4) != null ?
                    contList.get(position).getString(4) : "--");
        }

        return convertView;
    }

    class ViewHolder {
        private LinearLayout linearEquity;
        private TextView textReport1, textReport2, textReport3, textReport4, textReport5;
    }
}
