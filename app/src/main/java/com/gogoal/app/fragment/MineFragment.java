package com.gogoal.app.fragment;


import android.content.Context;

import com.gogoal.app.R;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.ui.view.XTitle;

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
        XTitle xTitle = setFragmentTitle("Go-Goal金融终端");
    }

}
