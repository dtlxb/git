package cn.gogoal.im.adapter.copy;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.StockUtils;
import hply.com.niugu.bean.HotSearchStockData;


/**
 * Created by daiwei on 2016/1/5.
 */
public class HotSearchAdapter extends MyBaseAdapter<HotSearchStockData> {

    private ArrayList<String> array;
    private ArrayList<HotSearchStockData> list;
    private boolean showAddBtn;

    public HotSearchAdapter(ArrayList<HotSearchStockData> list, boolean showAddBtn) {
        super(list);
        this.showAddBtn = showAddBtn;
        this.list = list;
        array = StockUtils.getMyStockCodeList();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotsearch_list_item, null);
            holder = new ViewHolder();
            holder.stock_number = (TextView) convertView.findViewById(R.id.stock_number);
            holder.stock_name = (TextView) convertView.findViewById(R.id.stock_name);
            holder.stock_code = (TextView) convertView.findViewById(R.id.stock_code);
            holder.stock_add = (TextView) convertView.findViewById(R.id.search_stock_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (showAddBtn) {
            holder.stock_add.setVisibility(View.VISIBLE);
        } else {
            holder.stock_add.setVisibility(View.INVISIBLE);
        }

        final int num = position + 1;
        holder.stock_number.setText(num + "");
        if (position == 0) {
            holder.stock_number.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.white));
            holder.stock_number.setBackgroundResource(R.drawable.hotsearch_list_item_number1);
        } else if (position == 1) {
            holder.stock_number.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.white));
            holder.stock_number.setBackgroundResource(R.drawable.hotsearch_list_item_number2);
        } else if (position == 2) {
            holder.stock_number.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.white));
            holder.stock_number.setBackgroundResource(R.drawable.hotsearch_list_item_number3);
        } else {
            holder.stock_number.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.text_color_normal));
            holder.stock_number.setBackgroundResource(R.drawable.hotsearch_list_item_number);
        }

        holder.stock_name.setText(list.get(position).getStock_name());
        holder.stock_code.setText(list.get(position).getStock_code());

        final String stockId = list.get(position).getStock_code().toString();
        if (array != null) {
            if (handleSearchStock(stockId)) {
                holder.stock_add.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                holder.stock_add.setText("已添加");
                holder.stock_add.setClickable(false);
            } else {
                holder.stock_add.setText("");
                holder.stock_add.setCompoundDrawablesWithIntrinsicBounds(
                        parent.getContext().getResources()
                        .getDrawable(R.drawable.add_icon), null, null, null);
                holder.stock_add.setClickable(true);

                holder.stock_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StockUtils.addMyStock(list.get(position).getStock_code(), new Impl<Boolean>() {
                            @Override
                            public void response(int code, Boolean data) {
                                if (data){
                                    holder.stock_add.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                    holder.stock_add.setText("已添加");
                                    holder.stock_add.setClickable(false);
                                }
                            }
                        });
                    }
                });
            }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView stock_number, stock_name, stock_code, stock_add;
    }

    private boolean handleSearchStock(String stockCode) {
        boolean flag = false;
        for (String stockCode1 : array) {
            if (stockCode.equals(stockCode1)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

//    private void InitList(final Context context, final String stock_name, final String stock_code) {
//        final Map<String, String> param = new HashMap<String, String>();
//        /*params token=4967b285a82244d296b807a8fea9bc77&
//        * params group_id=22&
//        * params stock_code=600001&
//        * params stock_class=333&
//        * params source=54&
//        * params group_class=1*/
//        param.put("token", UserUtils.getToken());
//        param.put("group_id", "0");
//        param.put("stock_code", stock_code);
//        param.put("stock_class", "0");
//        param.put("source", "9");
//        param.put("group_class", "1");
//
//        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
//
//            @Override
//            public void onSuccess(String responseInfo) {
//                JSONObject result = JSONObject.parseObject(responseInfo);
//                int data = (int) result.get("code");
//                if (data == 0) {
//                    JSONObject singlestock = new JSONObject();
//                    singlestock.put("stock_name", stock_name);
//                    singlestock.put("stock_code", stock_code);
//                    singlestock.put("stock_type", 1);
//                    singlestock.put("price", 0);
//                    singlestock.put("change_rate", 0);
//                    StockUtils.addStock2MyStock(singlestock);
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
//            }
//        };
//        new GGOKHTTP(param, GGOKHTTP.MYSTOCK_ADD, ggHttpInterface).startGet();
//    }
}
