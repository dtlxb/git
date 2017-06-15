package cn.gogoal.im.adapter;

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
public class AnalysisRightAdapter extends MyBaseAdapter {

    private ArrayList<JSONArray> contList;
    private Context context;

    public AnalysisRightAdapter(Context context, ArrayList list) {
        super(list);
        this.context = context;
        this.contList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_analysis_right, parent, false);
            holder = new ViewHolder();
            holder.linearAnalysis = (LinearLayout) convertView.findViewById(R.id.linearAnalysis);
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
            holder.linearAnalysis.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_1));
        } else {
            holder.linearAnalysis.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_2));
        }

        if (contList.get(position).size() == 0) {
            holder.textReport1.setText("--");
            holder.textReport2.setText("--");
            holder.textReport3.setText("--");
            holder.textReport4.setText("--");
            holder.textReport5.setText("--");
        } else {
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
        private LinearLayout linearAnalysis;
        private TextView textReport1, textReport2, textReport3, textReport4, textReport5;
    }
}
