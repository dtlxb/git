package cn.gogoal.im.adapter;

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

/**
 * Created by dave.
 * Date: 2017/6/12.
 * Desc: description
 */
public class AnalysisLeftAdapter extends MyBaseAdapter {

    private ArrayList<String> titleList;
    private Context context;

    public AnalysisLeftAdapter(Context context, ArrayList list) {
        super(list);
        this.context = context;
        this.titleList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_analysis_left, parent, false);
            holder = new ViewHolder();
            holder.linearAnalysis = (LinearLayout) convertView.findViewById(R.id.linearAnalysis);
            holder.textTitle = (TextView) convertView.findViewById(R.id.textTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 0) {
            holder.linearAnalysis.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_1));
        } else {
            holder.linearAnalysis.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_2));
        }

        holder.textTitle.setText(titleList.get(position));

        return convertView;
    }

    class ViewHolder {
        private LinearLayout linearAnalysis;
        private TextView textTitle;
    }
}