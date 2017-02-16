package com.gogoal.app.fragment;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.gogoal.app.R;
import com.gogoal.app.base.BaseFragment;

import butterknife.BindView;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    public MessageFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle("消息");
        initRecycleView(recycleView,0);
    }



}
