package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.stockften.CapitalStructureActivity;
import cn.gogoal.im.activity.stock.stockften.CompanyExecutivesActivity;
import cn.gogoal.im.activity.stock.stockften.CompanyProfileActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.f10.CompanyInforData;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.ui.view.ExpandableLayout;

/**
 * Created by dave.
 * Date: 2017/6/27.
 * Desc: description
 */
public class CompanySummaryAdapter extends CommonAdapter<CompanyInforData, BaseViewHolder> {

    private Context context;
    private ArrayList<CompanyInforData> listData;
    private String stockCode;
    private String stockName;

    public CompanySummaryAdapter(Context context, ArrayList<CompanyInforData> listData,
                                 String stockCode, String stockName) {
        super(R.layout.item_company_summary, listData);
        this.context = context;
        this.listData = listData;
        this.stockCode = stockCode;
        this.stockName = stockName;
    }

    @Override
    protected void convert(BaseViewHolder holder, final CompanyInforData data, final int position) {
        if (data.getDisplayType() == 1 || data.getDisplayType() == 2 || data.getDisplayType() == 3
                || data.getDisplayType() == 4) {

            holder.setVisible(R.id.linearProfile, true);
            if (data.getDisplayType() == 4) {
                holder.setVisible(R.id.textCont, false);
            } else {
                holder.setVisible(R.id.textCont, true);
            }
            holder.setText(R.id.textTitle, listData.get(position).getCompanyName());

        } else if (data.getDisplayType() == 6) {
            holder.setVisible(R.id.linearProfile2, true);
            holder.setText(R.id.textName2, listData.get(position).getCompanyName());
            ExpandableLayout expandableLayout = holder.getView(R.id.textContent2);
            expandableLayout.setText(listData.get(position).getCompanyConten1(), position);
        } else if (data.getDisplayType() == 7 || data.getDisplayType() == 8) {
            holder.setVisible(R.id.linearProfile3, true);
            holder.setText(R.id.textName3, listData.get(position).getCompanyName());
            if (data.getDisplayType() == 7) {
                holder.setText(R.id.textContent3, listData.get(position).getCompanyConten1());
                holder.setText(R.id.textContent4, listData.get(position).getCompanyContent2());
            } else {
                holder.setText(R.id.textContent3, StringUtils.save2Significand(Double.parseDouble(
                        listData.get(position).getCompanyConten1()) / 10000));
                holder.setText(R.id.textContent4, StringUtils.save2Significand(Double.parseDouble(
                        listData.get(position).getCompanyContent2()) * 100) + "%");
            }
        } else {
            holder.setVisible(R.id.linearProfile1, true);
            holder.setText(R.id.textName1, listData.get(position).getCompanyName());
            holder.setText(R.id.textContent1, listData.get(position).getCompanyConten1());
        }

        holder.setOnClickListener(R.id.textCont, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (data.getDisplayType()) {
                    case 1:
                        Intent intent1 = new Intent(context, CompanyProfileActivity.class);
                        intent1.putExtra("stockCode", stockCode);
                        intent1.putExtra("stockName", stockName);
                        context.startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context, CapitalStructureActivity.class);
                        intent2.putExtra("stockCode", stockCode);
                        intent2.putExtra("stockName", stockName);
                        context.startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(context, CompanyExecutivesActivity.class);
                        intent3.putExtra("stockCode", stockCode);
                        intent3.putExtra("stockName", stockName);
                        context.startActivity(intent3);
                        break;
                }
            }
        });
    }
}