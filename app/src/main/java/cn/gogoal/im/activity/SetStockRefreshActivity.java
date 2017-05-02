package cn.gogoal.im.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.SPTools;

/**
 * Created by huangxx on 2017/4/25.
 */

public class SetStockRefreshActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<String> timeList;
    private Long intervalTime;
    private RefreshAdapter refreshAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_set_stock_refresh;

    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("行情刷新频率", true);
        initData();
        intervalTime = SPTools.getLong("interval_time", 15000);
    }

    private void initData() {
        timeList = new ArrayList<>();
        timeList.add("不刷新");
        timeList.add("5s");
        timeList.add("15s");
        timeList.add("30s");
        timeList.add("60s");
        initRecycleView(recyclerView, R.drawable.shape_divider_1px);
        refreshAdapter = new RefreshAdapter(R.layout.item_refresh_time, timeList);
        recyclerView.setAdapter(refreshAdapter);
    }

    private class RefreshAdapter extends CommonAdapter<String, BaseViewHolder> {
        ImageView lastView;

        RefreshAdapter(int layoutResId, List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, String data, int position) {
            final long time;
            switch (data) {
                case "不刷新":
                    time = 0L;
                    break;
                default:
                    time = Long.parseLong(data.substring(0, data.length() - 1)) * 1000;
                    break;
            }
            TextView timeView = holder.getView(R.id.tv_time);
            final ImageView tagView = holder.getView(R.id.iv_tag);
            timeView.setText(data);

            if (intervalTime == time) {
                tagView.setVisibility(View.VISIBLE);
                lastView = tagView;
            } else {
                tagView.setVisibility(View.INVISIBLE);
            }

            holder.setOnClickListener(R.id.layout_time, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tagView.getVisibility() == View.INVISIBLE) {
                        tagView.setVisibility(View.VISIBLE);
                        lastView.setVisibility(View.INVISIBLE);
                        lastView = tagView;
                    }
                    SPTools.saveLong("interval_time", time);
                    AppManager.getInstance().sendMessage("updata_refresh_mode");
                }
            });
        }
    }
}
