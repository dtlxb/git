package cn.gogoal.im.adapter.copy;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.common.copy.NewsUtils;
import hply.com.niugu.bean.StockDetailResearchData;


/**
 * Created by daiwei on 2015/10/8.
 */
public class StockDetailResearchAdapter extends MyBaseAdapter<StockDetailResearchData> {

    private ArrayList<StockDetailResearchData> list;

    public StockDetailResearchAdapter(ArrayList<StockDetailResearchData> list) {
        super(list);
        this.list=list;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.stockdetailnews_item,null);
            holder.big_event_tittle_tv= (TextView) convertView.findViewById(R.id.big_event_tittle_tv);
            holder.big_event_date= (TextView) convertView.findViewById(R.id.big_event_date);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        if(NewsUtils.isAlreadyRead(list.get(position).getGuid())){
            holder.big_event_tittle_tv.setTextColor(ContextCompat.getColor(parent.getContext(),R.color.text_color_light));
        }else{
            holder.big_event_tittle_tv.setTextColor(ContextCompat.getColor(parent.getContext(),R.color.text_color_normal));
        }
        holder.big_event_tittle_tv.setText(list.get(position).getReport_title());
        holder.big_event_date.setText(list.get(position).getOrgan_name()+" "+list.get(position).getCreate_date());

        return convertView;
    }
    class ViewHolder{
        TextView big_event_tittle_tv,big_event_date;
    }
}
