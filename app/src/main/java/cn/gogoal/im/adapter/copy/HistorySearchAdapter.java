package cn.gogoal.im.adapter.copy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gogoal.im.R;
import hply.com.niugu.bean.HistorySearchData;


/**
 * Created by daiwei on 2016/1/4.
 */
public class HistorySearchAdapter extends MyBaseAdapter<HistorySearchData> {

    private ArrayList<HistorySearchData> list;

    public HistorySearchAdapter(ArrayList<HistorySearchData> list) {
        super(list);
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.historysearch_grid_item, null);
            holder = new ViewHolder();
            holder.stock_name = (TextView) convertView.findViewById(R.id.search_history_stockName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String stock_name = list.get(position).getStock_name();
        if(stock_name!=null){
            holder.stock_name.setText(stock_name);
        }else{
            holder.stock_name.setText("--");
        }

        return convertView;
    }

    private class ViewHolder {
        TextView stock_name;
    }
}
