package cn.gogoal.im.adapter.copy;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.common.copy.NewsUtils;
import hply.com.niugu.bean.StockDecisionData;


/**
 * Created by daiwei on 2015/9/30.
 */
public class StockDetailOverviewAdapter extends MyBaseAdapter<StockDecisionData> {

    private ArrayList<StockDecisionData> list;

    public StockDetailOverviewAdapter(ArrayList<StockDecisionData> list) {
        super(list);
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final HoldView holdView;
        if (convertView == null) {
            holdView = new HoldView();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stockdetailoverview_item, null);
            holdView.stock_new_title = (TextView) convertView.findViewById(R.id.stock_new_title);
            holdView.stock_new_content = (TextView) convertView.findViewById(R.id.stock_new_content);
            holdView.stock_new_name = (TextView) convertView.findViewById(R.id.stock_new_name);
            holdView.stock_new_author = (TextView) convertView.findViewById(R.id.stock_new_author);
            holdView.stock_new_time = (TextView) convertView.findViewById(R.id.stock_new_time);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }

        if(NewsUtils.isAlreadyRead(list.get(position).getGuid())){
            holdView.stock_new_title.setTextColor(ContextCompat.getColor(parent.getContext(),R.color.text_color_light));
            holdView.stock_new_content.setTextColor(ContextCompat.getColor(parent.getContext(),R.color.text_color_light));
        }else{
            holdView.stock_new_title.setTextColor(ContextCompat.getColor(parent.getContext(),R.color.decision_title));
            holdView.stock_new_content.setTextColor(ContextCompat.getColor(parent.getContext(),R.color.decision_news));
        }
        holdView.stock_new_title.setText(list.get(position).getReport_title());
        holdView.stock_new_content.setText(list.get(position).getReport_summary().replaceAll("\\s", ""));
        holdView.stock_new_name.setText(list.get(position).getOrgan_name() + " ");

        StringBuffer authorNames = new StringBuffer();
        for (int i = 0; i < list.get(position).getAuthors().size(); i++) {
            authorNames.append(list.get(position).getAuthors().get(i).getAuthor_name() + " ");
        }
        holdView.stock_new_author.setText(authorNames.toString());

        holdView.stock_new_time.setText(list.get(position).getCreate_date());

        return convertView;
    }

    class HoldView {
        TextView stock_new_title, stock_new_content, stock_new_name, stock_new_author, stock_new_time;
    }
}