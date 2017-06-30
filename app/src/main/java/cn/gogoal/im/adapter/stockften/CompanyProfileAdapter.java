package cn.gogoal.im.adapter.stockften;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.LinearLayoutHelper;

import java.util.ArrayList;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.market.MainViewHolder;
import cn.gogoal.im.bean.f10.ProfileData;

/**
 * Created by dave.
 * Date: 2017/6/8.
 * Desc: description
 */
public class CompanyProfileAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    private List<ProfileData> listData = new ArrayList<>();
    private Context context;

    public CompanyProfileAdapter(Context context, List<ProfileData> listData) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_company_profile, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.findView(R.id.linearProfile).setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_1));
        } else {
            holder.findView(R.id.linearProfile).setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color_2));
        }

        holder.setText(R.id.textName, listData.get(position).getName());
        holder.setText(R.id.textContent, listData.get(position).getContent() != null
                ? listData.get(position).getContent() : "--");

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
