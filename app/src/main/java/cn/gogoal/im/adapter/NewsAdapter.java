package cn.gogoal.im.adapter;

import android.view.View;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.StockNewsType;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UIHelper;
import hply.com.niugu.bean.StockDetailNewsData;

/**
 * Author wangjd on 2017/5/3 0003.
 * EmployeeNumber 1375
 * Phone 18930640263
 * Description :== 个股中新闻、公告适配器 ==
 */
public class NewsAdapter extends CommonAdapter<StockDetailNewsData, BaseViewHolder> {

    private StockNewsType stockNewsType;

    public NewsAdapter(List<StockDetailNewsData> data,StockNewsType stockNewsType) {
        super(R.layout.stock_detail_news_item, data);
        this.stockNewsType=stockNewsType;
    }

    @Override
    protected void convert(BaseViewHolder holder, final StockDetailNewsData data, int position) {
        holder.setText(R.id.big_event_tittle_tv,data.getTitle());
        holder.setText(R.id.big_event_date, CalendarUtils.getStringDate("yyyy-MM-dd HH:mm",data.getDate()));

        UIHelper.setRippBg(holder.itemView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stockNewsType.getNewsSource()==AppConst.SOURCE_TYPE_NEWS) {
                    NormalIntentUtils.go2WebActivity(v.getContext(),
                            AppConst.WEB_NEWS + data.getOrigin_id() + "?source=" + stockNewsType.getNewsSource(),
                            null, true);
                }else {
                    NormalIntentUtils.go2PdfDisplayActivity(v.getContext(),data.getOrigin_link(),"");
                }

            }
        });
    }
}
