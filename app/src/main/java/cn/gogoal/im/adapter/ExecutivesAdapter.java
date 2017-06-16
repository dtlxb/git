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
import cn.gogoal.im.bean.ExecutivesData;

/**
 * Created by dave.
 * Date: 2017/6/16.
 * Desc: description
 */
public class ExecutivesAdapter extends MyBaseAdapter<ExecutivesData> {

    private ArrayList<ExecutivesData> contList;
    private Context context;

    public ExecutivesAdapter(Context context, ArrayList<ExecutivesData> list) {
        super(list);
        this.context = context;
        this.contList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_holder_research, parent, false);
            holder = new ViewHolder();
            holder.linearAnalysis = (LinearLayout) convertView.findViewById(R.id.linearAnalysis);
            holder.textHolder1 = (TextView) convertView.findViewById(R.id.textHolder1);
            holder.textHolder2 = (TextView) convertView.findViewById(R.id.textHolder2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 0) {
            holder.linearAnalysis.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_1));
        } else {
            holder.linearAnalysis.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_2));
        }

        holder.textHolder1.setText(contList.get(position).getDegree());
        holder.textHolder2.setText(contList.get(position).getDuty());

        return convertView;
    }

    class ViewHolder {
        private LinearLayout linearAnalysis;
        private TextView textHolder1, textHolder2;
    }
}
