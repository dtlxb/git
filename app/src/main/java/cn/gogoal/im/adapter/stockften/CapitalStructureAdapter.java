package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.f10.ProfileData;

/**
 * Created by dave.
 * Date: 2017/6/30.
 * Desc: description
 */
public class CapitalStructureAdapter extends CommonAdapter<ProfileData, BaseViewHolder> {

    private Context context;

    public CapitalStructureAdapter(Context context, ArrayList<ProfileData> data) {
        super(R.layout.item_capital_structure, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, ProfileData data, int position) {
        if (position % 2 != 0) {
            holder.setBackgroundColor(R.id.linearCapital, ContextCompat.getColor(context, R.color.bg_color_1));
        } else {
            holder.setBackgroundColor(R.id.linearCapital, ContextCompat.getColor(context, R.color.bg_color_2));
        }

        holder.setText(R.id.textName, data.getName());

        if (data.getContent().equals("caption")) {
            holder.setTextColor(R.id.textName, Color.BLACK);
            holder.setText(R.id.textContent, "");
        } else {
            holder.setTextColor(R.id.textName, ContextCompat.getColor(context, R.color.textColor_333333));
            holder.setText(R.id.textContent, data.getContent() != null ? data.getContent() : "--");
        }
    }
}
