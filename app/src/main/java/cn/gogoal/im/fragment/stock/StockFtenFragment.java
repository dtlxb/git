package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.StockFtenActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.StockFtenData;
import cn.gogoal.im.common.UIHelper;

import static cn.gogoal.im.base.BaseActivity.initRecycleView;

/**
 * Created by dave.
 * Date: 2017/6/6.
 * Desc: 股票详情F10
 */
public class StockFtenFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private FTenAdapter adapter;

    private String stockCode;
    private String stockName;

    public static StockFtenFragment getInstance(String stockCode, String stockName) {
        StockFtenFragment sff = new StockFtenFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        sff.setArguments(b);
        return sff;
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");

        initRecycleView(recyclerView, 0);

        String rawString = UIHelper.getRawString(getContext(), R.raw.stock_ften);
        final List<StockFtenData> ftenData = JSONObject.parseArray(rawString, StockFtenData.class);

        adapter = new FTenAdapter(ftenData);
        recyclerView.setAdapter(adapter);
    }

    class FTenAdapter extends CommonAdapter<StockFtenData, BaseViewHolder> {

        public FTenAdapter(List<StockFtenData> datas) {
            super(R.layout.item_stock_ften, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, StockFtenData data, final int position) {

            holder.setText(R.id.textTitle, data.getTitle());
            holder.setText(R.id.textDesc, data.getDesc());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), StockFtenActivity.class);
                    intent.putExtra("stockCode", stockCode);
                    intent.putExtra("stockName", stockName);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }
    }
}
