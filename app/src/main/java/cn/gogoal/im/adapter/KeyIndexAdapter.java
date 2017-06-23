package cn.gogoal.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.LinearLayoutHelper;

import java.util.ArrayList;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.market.MainViewHolder;
import cn.gogoal.im.bean.ProfileData;

/**
 * Created by dave.
 * Date: 2017/6/22.
 * Desc: description
 */
public class KeyIndexAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    public static final int GENRE_KEY_INDEX = 1;
    public static final int GENRE_PROFIT_STATE = 2;
    public static final int GENRE_BALANCE_SHEET = 3;
    public static final int GENRE_FLOW_METER = 4;

    private ArrayList<ProfileData> contList;
    private Context context;
    private int genre;

    public KeyIndexAdapter(Context context, ArrayList<ProfileData> list, int genre) {
        this.context = context;
        this.contList = list;
        this.genre = genre;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_key_index, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.setText(R.id.textName, contList.get(position).getName());
        holder.setText(R.id.textContent, contList.get(position).getContent() != null
                ? contList.get(position).getContent() : "--");

        if (position == 1) {
            holder.findView(R.id.imgSeeMore).setVisibility(View.VISIBLE);
        } else {
            holder.findView(R.id.imgSeeMore).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return contList.size();
    }
}
