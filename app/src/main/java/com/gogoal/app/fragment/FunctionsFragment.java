package com.gogoal.app.fragment;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gogoal.app.R;
import com.gogoal.app.activity.FunctionActivity;
import com.gogoal.app.base.BaseFragment;

import butterknife.OnClick;

/**
 * 应用
 */
public class FunctionsFragment extends BaseFragment {

    public FunctionsFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_functions;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick(R.id.btn_test)
    void test(View view) {
        startActivity(new Intent(view.getContext(), FunctionActivity.class));
    }

}
