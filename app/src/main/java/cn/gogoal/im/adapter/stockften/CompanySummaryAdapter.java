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
    protected void convert(BaseViewHolder holder, CompanyInforData data, final int position) {
        if (position == 0 || position == 4 || position == 7 || position == 10) {
            holder.setVisible(R.id.linearProfile, true);
            if (position == 10) {
                holder.setVisible(R.id.textCont, false);
            } else {
                holder.setVisible(R.id.textCont, true);
            }

            holder.setText(R.id.textTitle, listData.get(position).getInforTitle());
        } else {
            holder.setVisible(R.id.linearProfile, false);
        }

        if (position == 3) {
            holder.setVisible(R.id.linearProfile2, true);
            holder.setText(R.id.textName2, listData.get(position).getCompanyName());
            ExpandableLayout expandableLayout = holder.getView(R.id.textContent2);
            expandableLayout.setText(listData.get(position).getCompanyContent(), position);
        } else if (position == 10 || position == 11 || position == 12 || position == 13) {
            holder.setVisible(R.id.linearProfile3, true);
            holder.setText(R.id.textName3, listData.get(position).getCompanyName());
            if (position == 10) {
                holder.setText(R.id.textContent3, listData.get(position).getCompanyContent());
                holder.setText(R.id.textContent4, listData.get(position).getCompanyContent1());
            } else {
                holder.setText(R.id.textContent3, StringUtils.save2Significand(Double.parseDouble(
                        listData.get(position).getCompanyContent()) / 10000));
                holder.setText(R.id.textContent4, StringUtils.save2Significand(Double.parseDouble(
                        listData.get(position).getCompanyContent1()) * 100) + "%");
            }
        } else {
            holder.setVisible(R.id.linearProfile1, true);
            holder.setText(R.id.textName1, listData.get(position).getCompanyName());
            holder.setText(R.id.textContent1, listData.get(position).getCompanyContent());
        }

        holder.setOnClickListener(R.id.textCont, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        Intent intent1 = new Intent(context, CompanyProfileActivity.class);
                        intent1.putExtra("stockCode", stockCode);
                        intent1.putExtra("stockName", stockName);
                        context.startActivity(intent1);
                        break;
                    case 4:
                        Intent intent2 = new Intent(context, CapitalStructureActivity.class);
                        intent2.putExtra("stockCode", stockCode);
                        intent2.putExtra("stockName", stockName);
                        context.startActivity(intent2);
                        break;
                    case 7:
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