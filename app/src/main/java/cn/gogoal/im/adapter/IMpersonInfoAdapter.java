package cn.gogoal.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;

/**
 * Created by huangxx on 2017/3/17.
 */

public class IMpersonInfoAdapter extends RecyclerView.Adapter {

    //头部
    public static int ITEM_HEAD = 0x01;
    //其他
    public static int ITEM_ELSE = 0x02;
    private List<String> strings;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public IMpersonInfoAdapter(List<String> strings, Context mContext, LayoutInflater mLayoutInflater) {
        this.strings = strings;
        this.mContext = mContext;
        this.mLayoutInflater = mLayoutInflater;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEAD) {
            return new HeadHolder(mLayoutInflater.inflate(R.layout.item_person_detial, parent, false));
        } else {
            return new ElseHolder(mLayoutInflater.inflate(R.layout.item_person_detail_else, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadHolder) {

        } else {
            ((ElseHolder) holder).textView.setText(strings.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_HEAD;
        } else {
            return ITEM_ELSE;
        }
    }

    private class HeadHolder extends RecyclerView.ViewHolder {

        public HeadHolder(View itemView) {
            super(itemView);
        }

    }

    private class ElseHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ElseHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_join_in);
        }
    }
}
