package cn.gogoal.im.adapter.market;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.LinearLayoutHelper;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.stock.stockRanklist.StockRankBean;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;

/**
 * author wangjd on 2017/6/15 0015.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class RankListAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    public static final int RANK_TYPE_INCREASE_LIST = 0x0;

    public static final int RANK_TYPE_DOWN_LIST = 0x1;

    public static final int RANK_TYPE_CHANGE_LIST = 0x2;

    public static final int RANK_TYPE_AMPLITUDE_LIST = 0x3;

    private List<StockRankBean> rankBeanList;

    private int rankType;//类型，区分涨跌振换

    public RankListAdapter(List<StockRankBean> rankBeanList, int rankType) {
        this.rankBeanList = rankBeanList;
        this.rankType = rankType;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper(1);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_stock_rank_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {

        final StockRankBean data = rankBeanList.get(position);

        holder.setText(R.id.tv_mystock_stockname, data.getStock_name());
        holder.setText(R.id.tv_mystock_stockcode, data.getStock_code());
        holder.setText(R.id.tv_mystock_price, StringUtils.saveSignificand(data.getCurrent_price(), 2));


        switch (rankType) {
            case RANK_TYPE_INCREASE_LIST:
            case RANK_TYPE_DOWN_LIST:
                holder.setText(R.id.tv_mystock_rate, StockUtils.plusMinus(data.getRate(), true));
                holder.setBackgroundRes(R.id.tv_mystock_rate, StockUtils.getStockRateBackgroundRes(data.getRate()));
                holder.setTextResColor(R.id.tv_mystock_price, StockUtils.getStockRateColor(data.getRate()));
                break;
            case RANK_TYPE_CHANGE_LIST://换手率
                holder.setText(R.id.tv_mystock_rate, StringUtils.saveSignificand(
                        StringUtils.pareseStringDouble(data.getRate()) * 100, 2) + "%");
                holder.setBackgroundRes(R.id.tv_mystock_rate, R.drawable.shape_my_stock_price_gray);
                break;
            case RANK_TYPE_AMPLITUDE_LIST://振幅榜
                holder.setText(R.id.tv_mystock_rate, StringUtils.saveSignificand(data.getRate(), 2) + "%");
                holder.setBackgroundRes(R.id.tv_mystock_rate, R.drawable.shape_my_stock_price_gray);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalIntentUtils.go2StockDetail(v.getContext(), data.getStock_code(), data.getStock_name());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
