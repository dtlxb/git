package cn.gogoal.im.adapter;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.UIHelper;
import hply.com.niugu.bean.StockDetailResearchData;

/**
 * Author wangjd on 2017/5/3 0003.
 * EmployeeNumber 1375
 * Phone 18930640263
 * Description :== 个股中 研报 列表适配器==
 */
public class ResearchAdapter extends CommonAdapter<StockDetailResearchData, BaseViewHolder> {

    private int newsSource;

    public ResearchAdapter(List<StockDetailResearchData> data, int newsSource) {
        super(R.layout.stock_detail_news_item, data);
        this.newsSource = newsSource;
    }

    @Override
    protected void convert(BaseViewHolder holder, final StockDetailResearchData data, int position) {
        holder.setText(R.id.big_event_tittle_tv, data.getReport_title());
        holder.setText(R.id.big_event_date, data.getCreate_date());

        UIHelper.setRippBg(holder.itemView);

        TextView tvOrganNameAndAuthor = holder.getView(R.id.big_event_organ_andr_author);
        tvOrganNameAndAuthor.setVisibility(View.VISIBLE);
        tvOrganNameAndAuthor.setText(data.getOrgan_name() + "  " + data.getAuthor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalIntentUtils.go2WebActivity(v.getContext(),
                        AppConst.WEB_NEWS + data.getGuid() + "?source=" + newsSource,
                        null, true);
            }
        });
    }
}
