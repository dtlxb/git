package cn.gogoal.im.adapter.copy;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UserUtils;
import hply.com.niugu.R;
import hply.com.niugu.bean.SearchStockData;


/**
 * Created by daiwei on 2015/9/17.
 * 搜索股票的适配器
 */
public class SearchStockAdapter extends MyBaseAdapter<SearchStockData> {

    private final Set<String> myStockSet;

    private ArrayList<SearchStockData> list;
    private SearchView editText;

    public SearchStockAdapter(ArrayList<SearchStockData> list, SearchView editText) {
        super(list);
        myStockSet = StockUtils.getMyStockSet();
        this.list = list;
        this.editText=editText;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchstock_list_item, null);
            holder = new ViewHolder();
            holder.stock_name = (TextView) convertView.findViewById(R.id.stock_name);
            holder.stock_code = (TextView) convertView.findViewById(R.id.stock_code);
            holder.stock_add = (TextView) convertView.findViewById(R.id.search_stock_add);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int index;
        String search=editText.getQuery().toString();
        ForegroundColorSpan span=new ForegroundColorSpan(parent.getContext().getResources().getColor(R.color.search_stock_key));
        SpannableStringBuilder builder=new SpannableStringBuilder(list.get(position).getStock_name());
        index=list.get(position).getStock_name().indexOf(search);
        if(index!=-1){
            builder.setSpan(span,index,index+search.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.stock_name.setText(builder);
            holder.stock_code.setText(list.get(position).getStock_code());
        }else{
            holder.stock_name.setText(list.get(position).getStock_name());

            builder=new SpannableStringBuilder(list.get(position).getStock_code());
            index=list.get(position).getStock_code().indexOf(search);
            if(index!=-1){
                builder.setSpan(span,index,index+search.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.stock_code.setText(builder);
            }else{
                holder.stock_code.setText(list.get(position).getStock_code());
            }
        }

        final String stockId = list.get(position).getStock_code().toString();
        if (myStockSet != null) {
            if (handleSearchStock(stockId)) {
                holder.stock_add.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                holder.stock_add.setText("已添加");
                holder.stock_add.setClickable(false);
            } else {
                holder.stock_add.setText("");
                holder.stock_add.setCompoundDrawablesWithIntrinsicBounds(parent.getContext().getResources()
                        .getDrawable(R.drawable.add_icon), null, null, null);
                holder.stock_add.setClickable(true);

                holder.stock_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!UserUtils.isLogin()) {
                            JSONObject singlestock = new JSONObject();
                            singlestock.put("stock_name", list.get(position).getStock_name());
                            singlestock.put("stock_code", list.get(position).getStock_code());
                            singlestock.put("stock_type", 1);
                            singlestock.put("price", 0);
                            singlestock.put("change_rate", 0);
                            StockUtils.addStock2MyStock(singlestock);

                            holder.stock_add.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            holder.stock_add.setText("已添加");
                            holder.stock_add.setClickable(false);

                        } else {
                            //登录时
                            InitList(v.getContext(),list.get(position).getStock_name(), list.get(position).getStock_code());
                            holder.stock_add.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            holder.stock_add.setText("已添加");
                            holder.stock_add.setClickable(false);
                        }
                    }
                });
            }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView stock_name;
        TextView stock_code;
        TextView stock_add;
    }

    private boolean handleSearchStock(String stockCode) {

        boolean flag = false;
        for (String stockCode1 : myStockSet) {
            if (stockCode.equals(stockCode1)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private void InitList(final Context context, final String stock_name, final String stock_code) {
        final Map<String, String> param = new HashMap<String, String>();
        /*params token=4967b285a82244d296b807a8fea9bc77&
        * params group_id=22&
        * params stock_code=600001&
        * params stock_class=333&
        * params source=54&
        * params group_class=1*/
        param.put("token", UserUtils.getToken());
        param.put("group_id", "0");
        param.put("stock_code", stock_code);
        param.put("stock_class", "0");
        param.put("source", "9");
        param.put("group_class", "1");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {

            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                int data = (int) result.get("code");
                if (data == 0) {
                    JSONObject singlestock = new JSONObject();
                    singlestock.put("stock_name", stock_name);
                    singlestock.put("stock_code", stock_code);
                    singlestock.put("stock_type", 1);
                    singlestock.put("price", 0);
                    singlestock.put("change_rate", 0);
                    StockUtils.addStock2MyStock(singlestock);
                }
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
            }
        };
        new GGOKHTTP(param, GGOKHTTP.MYSTOCK_ADD, ggHttpInterface).startGet();
    }

}
