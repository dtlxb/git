package cn.gogoal.im.adapter;

import android.view.View;
import android.widget.ImageView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.stock.bigdata.BigDataDetailList;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;

/**
 * author wangjd on 2017/6/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class BigDataDetailAdapter extends
        CommonAdapter<BigDataDetailList, BaseViewHolder> {

    public BigDataDetailAdapter(List<BigDataDetailList> data) {
        super(R.layout.item_subject_event_stock_about, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final BigDataDetailList data, int position) {
        final ImageView imageAddmyStock = holder.getView(R.id.iv_item_subject_about_add_stock);

        holder.setText(R.id.tv_item_subject_about_name, data.getStock_name());
        holder.setText(R.id.tv_item_subject_about_code, data.getStock_code());
        holder.setText(R.id.tv_item_subject_about_rate, StockUtils.plusMinus(data.getPrice_change_rate(), true));
        holder.setText(R.id.tv_item_subject_about_price,
                StringUtils.save2Significand(data.getPrice()));

        holder.setTextColor(R.id.tv_item_subject_about_rate,
                getResColor(StockUtils.getStockRateColor(data.getPrice_change_rate())));

        holder.setTextColor(R.id.tv_item_subject_about_price,
                getResColor(StockUtils.getStockRateColor(data.getPrice_change_rate())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalIntentUtils.go2StockDetail(v.getContext(), data.getStock_code(), data.getStock_name());
            }
        });

        imageAddmyStock.setImageResource(StockUtils.isMyStock(data.getStock_code()) ?
                R.mipmap.not_choose_stock : R.mipmap.choose_stock);

        imageAddmyStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                StockUtils.toggleAddDelStock(data.getStock_code(), imageAddmyStock);
            }
        });
    }
}
