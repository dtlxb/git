package com.gogoal.app.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
* 通用的fragment碎片
* */
public abstract class BaseFragment extends Fragment implements IBase {

    private Unbinder bind;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(bindLayout(), container, false);
        bind = ButterKnife.bind(this, view);
        // 控件初始化
        initView(view);
        doBusiness(getActivity());

        return view;
    }

    public View getBaseView(){
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    @Override
    public void resume() {

    }

    @Override
    public void initView(View view) {
    }
}
