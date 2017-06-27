package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.stockften.FinancialAnalysisActivity;
import cn.gogoal.im.activity.stock.stockften.FinancialStatementsActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.FinanceData;
import cn.gogoal.im.common.StringUtils;

/**
 * Created by dave.
 * Date: 2017/6/22.
 * Desc: description
 */
public class StockFinanceAdapter extends CommonAdapter<FinanceData, BaseViewHolder> {

    private ArrayList<FinanceData> contList;
    private Context context;
    private String stockCode;
    private String stockName;
    private String stype;

    public StockFinanceAdapter(Context context, ArrayList<FinanceData> contList, String stockCode,
                               String stockName, String stype) {
        super(R.layout.item_key_index, contList);
        this.context = context;
        this.contList = contList;
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.stype = stype;
    }

    @Override
    protected void convert(BaseViewHolder holder, FinanceData data, final int position) {

        if (position == 0 || position == 4 || position == 7 || position == 10) {
            holder.getView(R.id.relativeFinance).setVisibility(View.VISIBLE);
            holder.setText(R.id.textTitle, contList.get(position).getTitle());
            holder.setText(R.id.textHead, contList.get(position).getHead());
        } else {
            holder.getView(R.id.relativeFinance).setVisibility(View.GONE);
        }

        holder.setText(R.id.textName, contList.get(position).getName());

        if (position == 0 || position == 1 || position == 2) {
            holder.setText(R.id.textContent, contList.get(position).getContent() != null
                    ? contList.get(position).getContent() + "元" : "--");
        } else if (position == 3) {
            holder.setText(R.id.textContent, contList.get(position).getContent() != null
                    ? contList.get(position).getContent() + "%" : "--");
        } else {
            holder.setText(R.id.textContent, setTextContent(contList.get(position).getContent()));
        }

        if (position == 1 || position == 5 || position == 8 || position == 11) {
            holder.getView(R.id.imgSeeMore).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.imgSeeMore).setVisibility(View.INVISIBLE);
        }

        holder.setOnClickListener(R.id.linearFinance, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        Intent intent1 = new Intent(context, FinancialAnalysisActivity.class);
                        intent1.putExtra("stockCode", stockCode);
                        intent1.putExtra("stockName", stockName);
                        intent1.putExtra("stype", stype);
                        intent1.putExtra("type", "1");
                        context.startActivity(intent1);
                        break;
                    case 4:
                    case 5:
                    case 6:
                        JumpFinancialStatements(FinancialStatementsActivity.GENRE_PROFIT_STATEMENTS);
                        break;
                    case 7:
                    case 8:
                    case 9:
                        JumpFinancialStatements(FinancialStatementsActivity.GENRE_BALANCE_SHEET);
                        break;
                    case 10:
                    case 11:
                    case 12:
                        JumpFinancialStatements(FinancialStatementsActivity.GENRE_CASH_FLOW);
                        break;

                }
            }
        });
    }

    private String setTextContent(String cont) {
        String content = null;
        if (cont != null) {
            if (cont.length() > 7) {
                content = StringUtils.save2Significand(Double.parseDouble(cont) / 10000) + "亿元";
            } else {
                content = cont + "万元";
            }
        } else {
            content = "--";
        }

        return content;
    }

    private void JumpFinancialStatements(int genre) {
        Intent intent = new Intent(context, FinancialStatementsActivity.class);
        intent.putExtra("stockCode", stockCode);
        intent.putExtra("stockName", stockName);
        intent.putExtra("stype", stype);
        intent.putExtra("genre", genre);
        context.startActivity(intent);
    }
}
