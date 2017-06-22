package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.InvestmentResearchAdapter;
import cn.gogoal.im.bean.ToolData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.ui.dialog.base.BaseBottomDialog;

/**
 * author wangjd on 2017/6/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :个股详情、工具箱，底部点击popu
 */
public class StockDetailPopuDialog extends BaseBottomDialog {

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_stock_detail_popu;
    }

    public static StockDetailPopuDialog newInstance(ArrayList<ToolData.Tool> tools) {
        StockDetailPopuDialog dialog = new StockDetailPopuDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("stock_detail_tools", tools);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void bindView(View v) {
        ArrayList<ToolData.Tool> tools =
                getArguments().getParcelableArrayList("stock_detail_tools");


        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.rv_dialog_stockdetail);
        v.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StockDetailPopuDialog.this.dismiss();
            }
        });

        InvestmentResearchAdapter toolsAdapter = new InvestmentResearchAdapter(getContext(), tools, this);
        recyclerView.setLayoutManager(new GridLayoutManager(
                v.getContext(),
                (AppDevice.isLowDpi() ? 3 : 4),
                GridLayoutManager.VERTICAL,
                false));

        recyclerView.setAdapter(toolsAdapter);
    }
}
