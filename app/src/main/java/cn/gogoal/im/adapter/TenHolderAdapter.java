package cn.gogoal.im.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.LinearLayoutHelper;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.market.MainViewHolder;
import cn.gogoal.im.bean.TenHolderData;

/**
 * Created by dave.
 * Date: 2017/6/15.
 * Desc: description
 */
public class TenHolderAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    private ArrayList<TenHolderData> contList;
    private Context context;

    public TenHolderAdapter(Context context, ArrayList<TenHolderData> list) {
        this.context = context;
        this.contList = list;
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_ten_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.findView(R.id.linearHolding).setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_1));
        } else {
            holder.findView(R.id.linearHolding).setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_2));
        }

        holder.setText(R.id.textContent1, contList.get(position).getStock_holder_name() != null
                ? contList.get(position).getStock_holder_name() : "--");
        holder.setText(R.id.textContent2, contList.get(position).getStock_holder_ratio() != null
                ? contList.get(position).getStock_holder_ratio() : "--");
        holder.setText(R.id.textContent3, contList.get(position).getStock_holding_quantity() != null
                ? contList.get(position).getStock_holding_quantity() : "--");
    }

    @Override
    public int getItemCount() {
        return contList.size();
    }
}
