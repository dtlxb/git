package cn.gogoal.im.adapter.stockften;

import android.content.Context;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.f10.DividendTransData;

/**
 * Created by dave.
 * Date: 2017/6/29.
 * Desc: description
 */
public class DividendTransAdapter extends CommonAdapter<DividendTransData, BaseViewHolder> {

    private Context context;
    private ArrayList<DividendTransData> transDatas;

    public DividendTransAdapter(Context context, ArrayList<DividendTransData> transDatas) {
        super(R.layout.item_dividend_trans, transDatas);
        this.context = context;
        this.transDatas = transDatas;
    }

    @Override
    protected void convert(BaseViewHolder holder, DividendTransData data, int position) {

        holder.setText(R.id.textTime, transDatas.get(position).getDate() == null ? "--"
                : transDatas.get(position).getDate());

        holder.setText(R.id.textProgram, transDatas.get(position).getDividend_program() != null
                ? transDatas.get(position).getDividend_program() : "--");
    }
}
