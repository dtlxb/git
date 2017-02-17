package com.gogoal.app.fragment;


import android.content.Context;

import com.gogoal.app.R;
import com.gogoal.app.base.BaseFragment;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment {

    public MessageFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void doBusiness(Context mContext) {
        setFragmentTitle("消息");
    }



}
