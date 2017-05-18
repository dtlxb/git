package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.view.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.InteractiveBean;
import cn.gogoal.im.bean.stock.InteractiveData;
import cn.gogoal.im.bean.stock.Stock;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.XDividerItemDecoration;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.view.XTitle;

/**
 * author wangjd on 2017/5/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :投资者互动
 */
public class InteractiveInvestorActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.title_bar)
    XTitle titleBar;
    private int defaultPage = 1;

    private InteractiveInvestorAdapter adapter;

    private List<InteractiveData> datas;
    private String stockCode;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        Stock stock = getIntent().getParcelableExtra("stock_info");
        iniHead(stock);
        stockCode = stock.getStock_code();

        datas = new ArrayList<>();
        adapter = new InteractiveInvestorAdapter(mContext, datas);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new Divider());

        recyclerView.setAdapter(adapter);

        getInteractiveInvestorData(AppConst.REFRESH_TYPE_FIRST);

        adapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (defaultPage <= 50) {
                    defaultPage++;
                    adapter.loadMoreEnd(false);
                    getInteractiveInvestorData(AppConst.REFRESH_TYPE_LOAD_MORE);
                } else {
                    adapter.loadMoreEnd(true);
                    adapter.setEnableLoadMore(false);
                    UIHelper.toast(getActivity(), R.string.nomoredata_hint);

                }
                adapter.loadMoreComplete();
            }
        },recyclerView);

    }

    private void iniHead(Stock stock) {
        String titleText = stock.getStock_name() + "(" + stock.getStock_code() + ")"
                + "\n" + StockUtils.getTreatState() + " " + CalendarUtils.getCurrentTime("MM-dd HH:mm");
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitle(titleText).setSubTitleColor(Color.parseColor("#a0ffffff"))
                .setTitleColor(Color.WHITE)
                .setLeftText("返回")
                .setLeftTextColor(Color.WHITE)
                .setLeftImageResource(R.mipmap.image_title_back_255)
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setBackgroundResource(StockUtils.getStockRateColor(stock.getChangeValue()));

        StatusBarUtil.with(InteractiveInvestorActivity.this).
                setColor(getResColor(StockUtils.getStockRateColor(stock.getChangeValue())));
    }


    private void getInteractiveInvestorData(int refreshType) {
        adapter.setEnableLoadMore(false);

        if (refreshType == AppConst.REFRESH_TYPE_FIRST) {
            xLayout.setStatus(XLayout.Loading);
        }

        final Map<String, String> param = new HashMap<String, String>();
        param.put("stock_code", stockCode);
        param.put("type", "9");
        param.put("page", defaultPage + "");
        param.put("rows", "20");

        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_NEWS, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");

                if (code == 0) {
                    List<InteractiveData> interactiveDatas =
                            JSONObject.parseObject(responseInfo, InteractiveBean.class).getData();

                    datas.addAll(interactiveDatas);

                    adapter.notifyDataSetChanged();
                    adapter.setEnableLoadMore(true);
                    adapter.loadMoreComplete();

                    xLayout.setStatus(XLayout.Success);
                } else if (code == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        }).startGet();
    }

    private class InteractiveInvestorAdapter extends CommonAdapter<InteractiveData, BaseViewHolder> {
        private Context context;

        private InteractiveInvestorAdapter(Context context, List<InteractiveData> data) {
            super(R.layout.item_interactiveinvertor_list, data);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder holder, InteractiveData data, int position) {
            String title = data.getTitle();

            TextView interactiveProblem = holder.getView(R.id.interactive_problem);
            TextView interactiveAnswer = holder.getView(R.id.interactive_answer);
            TextView interactiveDate = holder.getView(R.id.interactive_date);

            if (title.contains("(" + data.getStock().get(0).getStock_code() + ")")) {
                String questioner = title.substring(0, title.indexOf("(" + data.getStock().get(0).getStock_code() + ")") + 8);
                interactiveProblem.setText(questioner + ":" + title.substring(questioner.length(), title.length()));
                SpannableStringBuilder builder = new SpannableStringBuilder(interactiveProblem.getText().toString());
                ForegroundColorSpan Span1 = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.questioner_name));
                ForegroundColorSpan Span2 = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_color_normal));
                builder.setSpan(Span1, 0, questioner.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(Span2, questioner.length() + 1, interactiveProblem.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                interactiveProblem.setText(builder);
            } else {
                interactiveProblem.setText(title);
            }

            String Answer = StringUtils.toDBC(data.getAnswer().replaceAll("\\s", ""));
            if (Answer.contains(data.getStock().get(0).getStock_name()) && Answer.contains(":")) {
                String answer = Answer.substring(0, Answer.indexOf(":") + 1);
                interactiveAnswer.setText(answer + "" + Answer.substring(answer.length(), Answer.length()));
                SpannableStringBuilder builder = new SpannableStringBuilder((interactiveAnswer.getText().toString()));
                ForegroundColorSpan Span1 = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.questioner_name));
                ForegroundColorSpan Span2 = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_color_normal));
                builder.setSpan(Span1, 0, answer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(Span2, answer.length(), Answer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                interactiveAnswer.setText(builder);
            } else {
                interactiveAnswer.setText(Answer);
            }

            interactiveDate.setText(CalendarUtils.formatDate("yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm", data.getDate()));


        }
    }

    private class Divider extends XDividerItemDecoration {

        private Divider() {
            super(InteractiveInvestorActivity.this, 10,
                    ContextCompat.getColor(InteractiveInvestorActivity.this, R.color.interval));
        }

        @Override
        public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
            boolean[] showDivider = new boolean[4];
            showDivider[3] = (itemPosition != (datas.size() - 1));
            showDivider[1] = (itemPosition == 0);
            return showDivider;
        }
    }
}
