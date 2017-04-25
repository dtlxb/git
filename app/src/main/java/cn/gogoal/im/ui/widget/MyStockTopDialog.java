package cn.gogoal.im.ui.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.stock.MyStockMarketBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.MyStockFragment;

/**
 * author wangjd on 2017/4/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class MyStockTopDialog extends BaseDialog {

    @Override
    public float getDimAmount() {
        return 0;
    }

    private List<MyStockMarketBean.MyStockMarketData> datas;

    private GridMarketAdapter marketAdapter;

    @Override
    public int getDialogStyle() {
        return R.style.TopDialog;
    }

    @Override
    public int gravity() {
        return Gravity.TOP;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_mystock_top_market;
    }

    @Override
    public void bindView(View v) {
        datas = new ArrayList<>();

        marketAdapter = new GridMarketAdapter(v.getContext(), datas);

        View spaceTop=v.findViewById(R.id.dialog_space);
        AppDevice.setViewWidth$Height(spaceTop,ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelOffset(R.dimen.titlebar_size)+1);

        v.findViewById(R.id.view_dialog_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStockTopDialog.this.dismiss();
            }
        });

        RecyclerView rvMystockMarket = (RecyclerView) v.findViewById(R.id.rv_mystock_market);
        rvMystockMarket.setLayoutManager(new GridLayoutManager(v.getContext(), 3));
        rvMystockMarket.setAdapter(marketAdapter);

        getMarketLittle();

    }

    @Subscriber(tag = "dialog_market_refresh")
    public void updata(String msg) {
        getMarketLittle();
    }

    /**
     * 获取大盘数据
     */
    private void getMarketLittle() {
        datas.clear();
        final Map<String, String> param = new HashMap<>();
        param.put("fullcode", "sh000001;sz399001;sh000300;csi930715;sz399006");
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    List<MyStockMarketBean.MyStockMarketData> marketDatas =
                            JSONObject.parseObject(responseInfo, MyStockMarketBean.class).getData();
                    datas.addAll(marketDatas);
                    //补一个空数据
                    datas.add(new MyStockMarketBean.MyStockMarketData());
                    marketAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.APP_HQ_GET, ggHttpInterface).startGet();
    }

    private class GridMarketAdapter extends CommonAdapter<MyStockMarketBean.MyStockMarketData, BaseViewHolder> {

        private int screenWidth;

        private GridMarketAdapter(Context context, List<MyStockMarketBean.MyStockMarketData> datas) {
            super(R.layout.item_mystock_market_4rv, datas);
            screenWidth = AppDevice.getWidth(context);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MyStockMarketBean.MyStockMarketData data, final int position) {
            View itemView=holder.getView(R.id.item_mystock_market_4rv);

            itemView.setTag(position);

            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            params.width = (screenWidth - AppDevice.dp2px(getContext(), 2)) / 3;
            itemView.setLayoutParams(params);

            itemView.setClickable(!TextUtils.isEmpty(data.getFullcode()));

            if (!TextUtils.isEmpty(data.getFullcode())) {

                holder.setText(R.id.tv_mystock_market_name, data.getName());
                holder.setText(R.id.tv_mystock_market_price, StringUtils.saveSignificand(data.getPrice(), 2));

                holder.setText(R.id.tv_mystock_market_price_change$rate,
                        StockUtils.plusMinus(data.getPrice_change(), false) + "  " +
                                StockUtils.plusMinus(data.getPrice_change_rate(), true));

                holder.setTextResColor(R.id.tv_mystock_market_price_change$rate,
                        StockUtils.getStockRateColor(String.valueOf(data.getPrice_change_rate())));

                holder.setTextResColor(R.id.tv_mystock_market_price,
                        StockUtils.getStockRateColor(String.valueOf(data.getPrice_change_rate())));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyStockTopDialog.this.dismiss();
                        SPTools.saveInt("choose_banner_item", position);

                        ((MyStockFragment)getParentFragment()).changeIitem(position);
                    }
                });
            }
        }
    }

}
