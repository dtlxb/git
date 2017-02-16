package com.gogoal.app.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    /**
     * 初始化垂直列表的RecycleView
     * @param recyclerView:初始化对象;
     * @param dividerId:分割线对象 : 0时为默认一条直线;int值 shape资源；null(不要分割线)
     */
    public void initRecycleView(RecyclerView recyclerView, Integer dividerId) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        if (dividerId != null) {
            if (dividerId != 0x00) {
                try {
                    DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
                    itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), dividerId));//R.drawable.shape_divider
                    recyclerView.addItemDecoration(itemDecoration);
                } catch (Exception e) {
                    throw new IllegalArgumentException("initRecycleView(RecyclerView,Integer)第二个参数必须是一个分割线shape资源或者填0或者null");
                }
            } else {
                DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
                recyclerView.addItemDecoration(itemDecoration);
            }
        }
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
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
