package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.LinearLayoutHelper;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.market.MainViewHolder;
import cn.gogoal.im.bean.f10.ExecutivesData;

/**
 * Created by dave.
 * Date: 2017/6/19.
 * Desc: description
 */
public class ExecutivesListAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    private Context context;
    private List<ExecutivesData> listData;

    public ExecutivesListAdapter(Context context, List<ExecutivesData> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_currency_ften, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.findView(R.id.linearHolding).setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_1));
        } else {
            holder.findView(R.id.linearHolding).setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_2));
        }

        if (position != 0) {
            holder.setTextResColor(R.id.textContent1, R.color.f10_executives);
        }
        holder.setText(R.id.textContent1, listData.get(position).getName() != null
                ? listData.get(position).getName() : "--");
        holder.setText(R.id.textContent2, listData.get(position).getDegree() != null
                ? listData.get(position).getDegree() : "--");
        holder.setText(R.id.textContent3, listData.get(position).getDuty() != null
                ? listData.get(position).getDuty() : "--");
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
