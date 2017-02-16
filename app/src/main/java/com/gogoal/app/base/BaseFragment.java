package com.gogoal.app.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gogoal.app.R;
import com.gogoal.app.ui.view.XTitle;

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

    public XTitle setFragmentTitle(String title){
        XTitle fragmentTitle= (XTitle) view.findViewById(R.id.title_bar);
        if (!TextUtils.isEmpty(title)){
            fragmentTitle.setTitle(title);
            fragmentTitle.setTitleColor(Color.WHITE);
        }
        return fragmentTitle;
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
