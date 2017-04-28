package cn.gogoal.im.activity.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.view.StatusBarUtil;

import org.simple.eventbus.Subscriber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.copy.HistorySearchAdapter;
import cn.gogoal.im.adapter.copy.HotSearchAdapter;
import cn.gogoal.im.adapter.copy.SearchStockAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import hply.com.niugu.DeviceUtil;
import hply.com.niugu.bean.HistorySearchData;
import hply.com.niugu.bean.HotSearchStockBean;
import hply.com.niugu.bean.HotSearchStockData;
import hply.com.niugu.bean.SearchStockBean;
import hply.com.niugu.bean.SearchStockData;
import hply.com.niugu.view.InnerListView;
import hply.com.niugu.view.MyGridView;

/**
 * author wangjd on 2017/4/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :股票搜索
 */
public class StockSearchActivity extends BaseActivity {
    //搜索框
    @BindView(R.id.search_edit)
    SearchView searchStock;
    //返回
    @BindView(R.id.back_tv)
    TextView back_tv;
    //列表
    @BindView(R.id.search_listView)
    ListView mlistView;
    //搜索
    @BindView(R.id.search_linear)
    ScrollView search_linear;
    //历史搜索
    @BindView(R.id.search_history)
    LinearLayout search_history;
    //删除历史
    @BindView(R.id.search_trash)
    ImageView search_trash;
    //历史记录
    @BindView(R.id.search_history_grid)
    MyGridView search_history_grid;
    //热门搜索
    @BindView(R.id.search_hot)
    LinearLayout search_hot;
    @BindView(R.id.search_hot_list)
    InnerListView search_hot_list;
    //页面加载动画
    @BindView(R.id.load_animation)
    RelativeLayout load_animation;

    private SearchStockAdapter adapter;
    private ArrayList<SearchStockData> list = new ArrayList<SearchStockData>();
    private String content = null;
    private int num;

    private HistorySearchAdapter adapter_history;
    private ArrayList<HistorySearchData> list_history = new ArrayList<HistorySearchData>();
    private HotSearchAdapter adapter_hot;
    private ArrayList<HotSearchStockData> list_hot = new ArrayList<HotSearchStockData>();

    @Override
    public int bindLayout() {
        return R.layout.activity_stock_search;
    }

    @Override
    public void doBusiness(Context mContext) {

        StatusBarUtil barUtil = StatusBarUtil.with(this);

        barUtil.setStatusBarFontDark(true);

        init();
        Intent intent = getIntent();
        num = intent.getIntExtra("num", 0);

        initHotList();
    }

    //数据初始化
    private void init() {
        ((SearchView.SearchAutoComplete) searchStock.findViewById(R.id.search_src_text)).
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        ((SearchView.SearchAutoComplete) searchStock.findViewById(R.id.search_src_text)).
                setGravity(Gravity.CENTER_VERTICAL);

        back_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });

        searchStock.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String content) {
                if (content.length() == 0) {
                    mlistView.setVisibility(View.GONE);
                    search_linear.setVisibility(View.VISIBLE);
                } else {
                    mlistView.setVisibility(View.VISIBLE);
                    search_linear.setVisibility(View.GONE);
                    InitList(content);
                    adapter.notifyDataSetChanged();

                    setNoDataFlag();
                }
                return false;
            }
        });

        adapter = new SearchStockAdapter(list, searchStock);
        mlistView.setAdapter(adapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchStockData stock = list.get(position);
                String stockname = stock.getStock_name();
                String stockcode = stock.getStock_code();
                switch (num) {
                    case 0://点击去个股详情
                        Intent intent = new Intent();
                        intent.putExtra("stockName", stockname);
                        intent.putExtra("stockCode", stockcode);
                        JSONObject stockJson = new JSONObject();
                        stockJson.put("stockName", stockname);
                        stockJson.put("stockCode", stockcode);
                        SPTools.saveJsonObject("searchedStock", stockJson);
                        StockUtils.go2StockDetail(getActivity(), stockcode, stockname);
//                        setResult(ConstantUtils.RESULT_OK, intent);
//                        finish();
                        HistorySearchData data = new HistorySearchData(stockname, stockcode);
                        StockUtils.addSearchedStock(JSONObject.parseObject(JSONObject.toJSONString(data)));
                        initHotList();
                        break;
                    case 1:
                        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        StockUtils.go2StockDetail(getActivity(), stockcode, stockname);

                        JSONObject historyStock = new JSONObject();
                        historyStock.put("stock_name", stockname);
                        historyStock.put("stock_code", stockcode);
                        StockUtils.addStock2MyStock(historyStock);

                        InitSaveHots(stockname, stockcode);
                        break;
                    case 2:
                        Intent intent1 = new Intent();
                        intent1.putExtra("stock_name", stockname);
                        intent1.putExtra("stock_code", stockcode);
                        setResult(0x01, intent1);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });

        search_history_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String stockname = list_history.get(position).getStock_name();
                String stockcode = list_history.get(position).getStock_code();
                if (num == 2) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("stock_name", stockname);
                    intent1.putExtra("stock_code", stockcode);
                    setResult(0x01, intent1);
                    finish();
                } else {
                    StockUtils.go2StockDetail(getActivity(), stockcode,
                            stockname);
                }
            }
        });

        search_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StockUtils.clearLocalMyStock();
                search_history.setVisibility(View.GONE);
            }
        });

        adapter_hot = new HotSearchAdapter(list_hot,getIntent().getBooleanExtra("show_add_btn",true));

        search_hot_list.setAdapter(adapter_hot);
        search_hot_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String stockname = list_hot.get(position).getStock_name();
                String stockcode = list_hot.get(position).getStock_code();
                if (num == 2) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("stock_name", stockname);
                    intent1.putExtra("stock_code", stockcode);
                    setResult(0x01, intent1);
                    finish();
                } else {
                    StockUtils.go2StockDetail(getActivity(), list_hot.get(position).getStock_code(),
                            list_hot.get(position).getStock_name());

                    JSONObject historyStock = new JSONObject();
                    historyStock.put("stock_name", stockname);
                    historyStock.put("stock_code", stockcode);
                    StockUtils.addStock2MyStock(historyStock);
                }
            }
        });
    }

    private void setNoDataFlag() {
        final TextView emptyView = new TextView(getActivity());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        emptyView.setText("没有找到相关股票");
        emptyView.setGravity(Gravity.CENTER_HORIZONTAL);
        emptyView.setTextColor(Color.BLACK);
        emptyView.setPadding(0, DeviceUtil.dp2px(getActivity(), 5), 0, 0);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) mlistView.getParent()).addView(emptyView);
        mlistView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mlistView.setEmptyView(emptyView);
            }
        }, 1000);
    }

    //搜索股票
    private void InitList(String content) {
        final Map<String, String> param = new HashMap<String, String>();
        if (!UserUtils.isLogin()) {
            param.put("key", content);
        } else {
            param.put("token", UserUtils.getToken());
            param.put("key", content);
        }
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                SearchStockBean bean = JSONObject.parseObject(responseInfo, SearchStockBean.class);
                if (bean.getCode().equals("0")) {
                    list.clear();
                    ArrayList<SearchStockData> info = bean.getData();
                    list.addAll(info);
                    adapter.notifyDataSetChanged();
                } else {
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCKS, ggHttpInterface).startGet();
    }

    //历史搜索
    @Subscriber(tag = "updata_search_history")
    private void InitHistoryList() {
        if (!StockUtils.getSearchedStocks().isEmpty()) {
            search_history.setVisibility(View.VISIBLE);
            JSONArray data = StockUtils.getSearchedStocks();
            ArrayList<HistorySearchData> info = new ArrayList<>();
            int j = data.size() > 6 ? 6 : data.size();
            for (int i = 0; i < j; i++) {
                JSONObject stockData = data.getJSONObject(i);
                HistorySearchData historyData = new HistorySearchData(stockData.getString("stock_name"),
                        stockData.getString("stock_code"));
                info.add(historyData);
            }
            list_history.clear();
            list_history.addAll(info);
            adapter_history = new HistorySearchAdapter(list_history);
            search_history_grid.setAdapter(adapter_history);
            adapter_history.notifyDataSetChanged();
        } else {
            search_history.setVisibility(View.GONE);
        }
    }

    //热门搜索
    private void initHotList() {
        final Map<String, String> param = new HashMap<String, String>();
        param.put("type", "1");
        param.put("get_count", "0");
        param.put("page", "1");
        param.put("rows", "10");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (responseInfo != null) {
                    HotSearchStockBean bean = JSONObject.parseObject(responseInfo, HotSearchStockBean.class);
                    if (bean.getCode()==0) {
                        list_hot.clear();
                        search_hot.setVisibility(View.VISIBLE);
                        ArrayList<HotSearchStockData> info = bean.getData();
                        list_hot.addAll(info);
                        adapter_hot.notifyDataSetChanged();
                    } else if (bean.getCode()==1001) {
                        search_hot.setVisibility(View.GONE);
                    }
                    load_animation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String msg) {
                load_animation.setVisibility(View.GONE);
                UIHelper.toastError(getApplicationContext(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.STOCK_GET_HOTS, ggHttpInterface).startGet();
    }

    //记录热门搜索数据
    private void InitSaveHots(String stock_name, String stock_code) {
        final Map<String, String> param = new HashMap<String, String>();
        if (UserUtils.isLogin()) {
            param.put("token", UserUtils.getToken());
        }
        param.put("type", "1");
        param.put("stock_code", stock_code);
        param.put("stock_name", stock_name);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        param.put("insert_date", df.format(new Date()));

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.STOCK_SAVE_HOTS, ggHttpInterface).startGet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitHistoryList();
    }
}