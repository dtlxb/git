package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.copy.MyBaseAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.MyStockData;
import cn.gogoal.im.common.ArrayUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.XTitle;
import cn.gogoal.im.ui.widget.drag.DragSortListView;

public class MyStockEditActivity extends BaseActivity implements View.OnClickListener {

    private JSONArray stocks;
    //全选按钮
    @BindView(R.id.select_all)
    TextView select_all;
    //删除按钮
    @BindView(R.id.delete_btn)
    TextView delete_btn;
    //股票列表
    private ArrayList<Map<String, String>> stocklist;
    //选择的数目
    private int selecnum = 0;

    private MyDragAdapater myDragAdapater;
    private Context mContext = this;
    //完成按钮点击标志
    private boolean compalteFlag = true;

    @Override
    public int bindLayout() {
        return R.layout.activity_mystock_edit;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("自选股", true).addAction(new XTitle.TextAction(getString(R.string.complete)) {
            @Override
            public void actionClick(View view) {
                onComplateClick();
            }
        });
        getCachedStocks();
        initEvent();
    }

    /**
     * 得到自选股
     */
    private void getCachedStocks() {
        List<MyStockData> myStockList = getIntent().getParcelableArrayListExtra("my_stock_edit_list");
        stocks = JSONArray.parseArray(JSON.toJSONString(myStockList));
        stocklist = new ArrayList<>();
        for (int i = 0; i < stocks.size(); i++) {
            Map<String, String> stocksmap = new HashMap<String, String>();
            JSONObject stockData = (JSONObject) stocks.get(i);
            stocksmap.put("stock_name", stockData.getString("stock_name"));
            stocksmap.put("stock_code", stockData.getString("stock_code"));
            stocksmap.put("ischecked", "0");
            stocklist.add(stocksmap);

            JSONObject stock = (JSONObject) stocks.get(i);
            stock.put("ischecked", "0");
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //响应完成点击事件
        delete_btn.setOnClickListener(this);
        select_all.setOnClickListener(this);
        //响应列表控件拖拽事件
        DragSortListView list = (DragSortListView) findViewById(R.id.draglist);
        myDragAdapater = new MyDragAdapater(mContext, stocklist);
        list.setAdapter(myDragAdapater);

        list.setDropListener(onDrop);
    }

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                handleListAfterSort(from, to);
                myDragAdapater.notifyDataSetChanged();
            }
        }
    };

    /**
     * 完成按钮点击事件
     */
    private void onComplateClick() {
        WaitDialog waitDialog = WaitDialog.getInstance("请稍后", R.mipmap.login_loading, true);
        if (!compalteFlag) {
            waitDialog.show(getSupportFragmentManager());
            return;
        }

        if (UserUtils.isLogin() && compalteFlag) {
            compalteFlag = false;
            Map<String, String> params = UserUtils.getTokenParams();
            if (stocks.size() == 0) {
                params.put("clear", "1");
            } else {
                params.put("stock_codes", ArrayUtils.mosaicArrayElement(stocks,"stock_code"));
            }

            GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
                @Override
                public void onSuccess(String responseInfo) {
                    UIHelper.toast(mContext, "编辑成功");
                    compalteFlag = true;
                    StockUtils.saveMyStock(stocks);
                    finish();
                }

                @Override
                public void onFailure(String msg) {
                    UIHelper.toast(mContext, "编辑失败");
                    compalteFlag = true;
                    finish();
                }
            };
            new GGOKHTTP(params, GGOKHTTP.RESET_MYSTOCKS, ggHttpInterface).startGet();

        }
    }

    /**
     * 删除按钮点击事件
     */
    private void deletBtnClick() {
        if (selecnum == stocks.size()) {
            stocks.clear();
            stocklist.clear();
            selecnum = 0;
            refresh();
            return;
        }

        Iterator<Object> it = stocks.iterator();
        while (it.hasNext()) {
            JSONObject stock = (JSONObject) it.next();
            String ischecked = stock.get("ischecked").toString();
            if ("1".equals(ischecked)) {
                it.remove();
            }
        }

        Iterator<Map<String, String>> it1 = stocklist.iterator();
        while (it1.hasNext()) {
            Map<String, String> stock = it1.next();
            String ischecked = stock.get("ischecked");
            if ("1".equals(ischecked)) {
                it1.remove();
            }
        }
        selecnum = 0;
        refresh();
    }

    private void refresh() {
        delete_btn.setText("删除" + "(" + selecnum + ")");
        myDragAdapater.notifyDataSetChanged();
    }

    /**
     * 全选按钮点击事件
     */
    private void selectAllClick() {
        String flag ;
        if (selecnum == stocks.size()) {
            flag = "0";
            selecnum = 0;
            delete_btn.setTextColor(getResColor(R.color.gray));
        } else {
            flag = "1";
            selecnum = stocks.size();
            delete_btn.setTextColor(getResColor(R.color.footer_text_color_selected));
        }

        for (int i = 0; i < stocks.size(); i++) {
            Map<String, String> stocksmap = stocklist.get(i);
            stocksmap.put("ischecked", flag);
            JSONObject stock = (JSONObject) stocks.get(i);
            stock.put("ischecked", flag);
        }
        refresh();
    }

    /**
     * 处理排序后的数据
     */
    private void handleListAfterSort(int startPosition, int endPosition) {

        JSONObject stock = (JSONObject) stocks.get(startPosition);
        stocks.remove(startPosition);
        stocks.add(endPosition, stock);

        Map<String, String> stockmap = stocklist.get(startPosition);
        stocklist.remove(startPosition);
        stocklist.add(endPosition, stockmap);
        refresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_btn:
                deletBtnClick();
                break;
            case R.id.select_all:
                selectAllClick();
                break;
        }
    }

    private class MyDragAdapater extends MyBaseAdapter {

        MyDragAdapater(Context context, ArrayList list) {
            super(list);
        }

        @Override
        public View getView(final int position, View view, ViewGroup group) {
            final ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(group.getContext()).inflate(R.layout.mystockedit_list_item, null);
                holder = new ViewHolder();
                holder.checkview = (ImageView) view.findViewById(R.id.stockedit_checkbox);
                holder.stockedit_check_layout = (LinearLayout) view.findViewById(R.id.stockedit_check_layout);
                holder.arrow_top = view.findViewById(R.id.arrow_top);
                holder.stockcode = (TextView) view.findViewById(R.id.stockcode);
                holder.stockname = (TextView) view.findViewById(R.id.stockname);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            final Map<String, String> map = stocklist.get(position);

            holder.stockcode.setText(map.get("stock_code"));
            holder.stockname.setText(map.get("stock_name"));

            if ("0".equals(map.get("ischecked"))) {
                holder.checkview.setImageResource(R.mipmap.checkbox_unchecked);
            } else {
                holder.checkview.setImageResource(R.mipmap.checkbox_checked);
            }


            holder.stockedit_check_layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    JSONObject stock = (JSONObject) stocks.get(position);
                    String ischecked = stock.get("ischecked").toString();
                    if ("0".equals(ischecked)) {
                        holder.checkview.setImageResource(R.mipmap.checkbox_checked);
                        map.put("ischecked", "1");
                        stock.put("ischecked", "1");
                        selecnum++;
                        delete_btn.setText("删除" + "(" + selecnum + ")");
                        if (selecnum != 0) {
                            delete_btn.setTextColor(ContextCompat.getColor(mContext,R.color.footer_text_color_selected));
                        } else {
                            delete_btn.setTextColor(ContextCompat.getColor(mContext,R.color.gray));
                        }
                    } else if ("1".equals(ischecked)) {
                        holder.checkview.setImageResource(R.mipmap.checkbox_unchecked);
                        map.put("ischecked", "0");
                        stock.put("ischecked", "0");
                        selecnum--;
                        delete_btn.setText("删除" + "(" + selecnum + ")");
                        if (selecnum != 0) {
                            delete_btn.setTextColor(ContextCompat.getColor(mContext,R.color.footer_text_color_selected));
                        } else {
                            delete_btn.setTextColor(ContextCompat.getColor(mContext,R.color.gray));
                        }
                    }
                }
            });

            holder.arrow_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject stock = (JSONObject) stocks.get(position);
                    stocks.remove(position);
                    stocks.add(0, stock);

                    Map<String, String> map = stocklist.get(position);
                    stocklist.remove(position);
                    stocklist.add(0, map);

                    notifyDataSetChanged();
                }
            });

            return view;
        }

        class ViewHolder {
            TextView stockname;
            TextView stockcode;
            ImageView checkview;
            LinearLayout stockedit_check_layout;
            View arrow_top;
        }
    }
}
