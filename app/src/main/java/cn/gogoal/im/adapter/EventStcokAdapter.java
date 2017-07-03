package cn.gogoal.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.socks.library.KLog;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.EventDetailActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseSectionQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.bean.stock.SectionEventStockData;
import cn.gogoal.im.bean.stock.Stock;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.ui.view.TextDrawable;

/**
 * Author wangjd on 2017/7/2 0002.
 * EmployeeNumber 1375
 * Phone 18930640263
 * Description :==事件选股 适配器==
 */
public class EventStcokAdapter extends BaseSectionQuickAdapter<SectionEventStockData, BaseViewHolder> {

    private int[] headColor = {0xffff691d, 0xfff34858, 0xff599efd, 0xfffe9b1a};//热点颜色组

    private Context context;

    public EventStcokAdapter(Context context, List<SectionEventStockData> data) {
        super(R.layout.item_subject_event_stock_about,
                R.layout.head_event_stock_list, data);
        this.context = context;
    }

    @Override
    protected void convertHead(BaseViewHolder holder,final SectionEventStockData titleData) {

        holder.setVisible(R.id.view_divider,titleData.getEventTitle().getTitlePosition()!=0);

        holder.setImageDrawable(R.id.iv_item_event_list_name_icon, getTextDrawable("热点",
                headColor[titleData.getEventTitle().getTitlePosition() % 4]));

        holder.setText(R.id.tv_item_event_list_name, titleData.getEventTitle().getTitle());
        holder.setText(R.id.tv_item_event_list_date, CalendarUtils.formatDate(
                "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", titleData.getEventTitle().getDate()));
        holder.setText(R.id.tv_item_event_list_title, titleData.getEventTitle().getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventDetailActivity.class);
                intent.putExtra("event_type_id", titleData.getEventTitle().getId());
                intent.putExtra("event_type_title", titleData.header);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected void convert(final BaseViewHolder holder, SectionEventStockData data, final int position) {
        final Stock stockData = data.t;
        holder.setVisible(R.id.view_list_divider,(position+1)%7!=0);

        final ImageView ivAddToggle = holder.getView(R.id.iv_item_subject_about_add_stock);

        ivAddToggle.setImageResource(StockUtils.isMyStock(stockData.getStock_code()) ?
                R.mipmap.not_choose_stock : R.mipmap.choose_stock);

        ivAddToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StockUtils.toggleAddDelStock(stockData.getStock_code(), ivAddToggle);
            }
        });

        holder.setText(R.id.tv_item_subject_about_name, stockData.getStock_name());

        holder.setText(R.id.tv_item_subject_about_code, stockData.getStock_code());

        holder.setText(R.id.tv_item_subject_about_price,
                StringUtils.save2Significand(stockData.getStock_price()));
        holder.setTextResColor(R.id.tv_item_subject_about_price, StockUtils.getStockRateColor(
                stockData.getStock_rate()));

        holder.setText(R.id.tv_item_subject_about_rate,
                StockUtils.plusMinus(stockData.getStock_rate(), true));
        holder.setTextResColor(R.id.tv_item_subject_about_rate, StockUtils.getStockRateColor(
                stockData.getStock_rate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e(position);
                NormalIntentUtils.go2StockDetail(v.getContext(),
                        stockData.getStock_code(), stockData.getStock_name());
            }
        });
    }

    private Drawable getTextDrawable(String text, int bgColor) {
        TextDrawable.IBuilder iBuilder = TextDrawable
                .builder()
                .beginConfig()
                .fontSize(AppDevice.sp2px(context, 11))
                .endConfig().round();
        return iBuilder.build(text, bgColor);
    }
}
