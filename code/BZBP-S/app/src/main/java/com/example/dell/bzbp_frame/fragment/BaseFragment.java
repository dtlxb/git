package com.example.dell.bzbp_frame.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.bzbp_frame.R;

public abstract class BaseFragment extends Fragment {
    public View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        // 初始化ui
        initView(inflater);

        // 添加监听器
        initListener();

        doBusiness(getActivity());
        return view;
    }



    protected abstract void initData();

    protected abstract void initView(LayoutInflater inflater);

    protected abstract void initListener();

    protected abstract void doBusiness(FragmentActivity activity);
}
