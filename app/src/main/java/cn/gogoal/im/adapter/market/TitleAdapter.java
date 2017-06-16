package cn.gogoal.im.adapter.market;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.StickyLayoutHelper;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.RankListDetialActivity;


/**
 * author wangjd on 2017/6/15 0015.
 * Staff_id 1375
 * phone 18930640263
 * description :悬浮标题.
 */
public class TitleAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    public static final int RANK_LIST_TITLE_HOT_INDUSTRY = 0x0;

    private String[] rankListTitles = {"热门行业", "涨幅榜", "跌幅榜", "换手率", "振幅榜"};

    private int titleType;//标题类型，热门行业，还是涨跌振换标题
    private Context context;

    public TitleAdapter(Context context, int titleType) {
        this.context = context;
        this.titleType = titleType;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new StickyLayoutHelper(true);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.header_view_market, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        TextView title = (TextView) holder.itemView.findViewById(R.id.tv_stock_ranklist_title);
        title.setText(rankListTitles[titleType]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankListDetialActivity.class);
                intent.putExtra("MODULE_TITLE", rankListTitles[titleType]);

                switch (titleType) {
                    case RANK_LIST_TITLE_HOT_INDUSTRY:
                        intent.putExtra("MODULE_TYPE", RankListDetialActivity.MODULE_TYPE_TITLE_HOT_INDUSTRY);
                        context.startActivity(intent);
                        break;
                    default:
                        intent.putExtra("MODULE_TYPE",RankListDetialActivity.MODULE_TYPE_TTILE_RANK_LIST);
                        intent.putExtra("RANK_LIST_TYPE",titleType-1);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
