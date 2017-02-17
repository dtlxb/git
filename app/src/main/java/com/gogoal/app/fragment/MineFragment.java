package com.gogoal.app.fragment;


import android.content.Context;
import android.content.Intent;

import com.gogoal.app.R;
import com.gogoal.app.activity.PlayerActivity;
import com.gogoal.app.base.BaseFragment;

import butterknife.OnClick;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {

    public MineFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void doBusiness(Context mContext) {
    }


    @OnClick({R.id.setWatchLive})
    public void WatchLive() {
        startActivity(new Intent(getContext(), PlayerActivity.class));
    }
}
