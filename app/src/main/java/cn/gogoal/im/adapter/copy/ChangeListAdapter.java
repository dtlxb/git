package cn.gogoal.im.adapter.copy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.common.NormalIntentUtils;
import hply.com.niugu.StringUtils;
import hply.com.niugu.bean.StockData;


/**
 * Created by huangxx on 2015/9/17.
 */
public class ChangeListAdapter extends MyBaseAdapter<StockData>{
    private ArrayList<StockData> list;
    public ChangeListAdapter(ArrayList<StockData> list) {
        super(list);
        this.list=list;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.stockinformation_item,parent,false);
            holder=new ViewHolder();
            holder.privce= (TextView) convertView.findViewById(R.id.tv_stock_price);
            holder.rate= (TextView) convertView.findViewById(R.id.tv_stock_rate);
            holder.code= (TextView) convertView.findViewById(R.id.tv_stock_code);
            holder.name= (TextView) convertView.findViewById(R.id.tv_stock_name);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.privce.setText(String.valueOf(StringUtils.get2Double(list.get(position).getCurrent_price()) + ""));

        holder.rate.setText(StringUtils.get2Double(list.get(position).getRate()) * 100 + "%");

        holder.code.setText(list.get(position).getStock_code());
        holder.name.setText(list.get(position).getStock_name());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalIntentUtils.go2StockDetail(parent.getContext(),list.get(position).getStock_code(),
                        list.get(position).getStock_name());
            }
        });
        return convertView;
    }
    static class ViewHolder{
        private TextView privce;
        private TextView rate;
        private TextView code;
        private TextView name;
    }
}
