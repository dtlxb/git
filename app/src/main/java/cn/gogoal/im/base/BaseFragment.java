package cn.gogoal.im.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.gogoal.im.R;
import cn.gogoal.im.ui.view.XTitle;

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

        EventBus.getDefault().register(this);

        doBusiness(getActivity());

        return view;
    }

    public View getRootView() {
        return view;
    }

    public XTitle setFragmentTitle(String title) {
        XTitle fragmentTitle = (XTitle) view.findViewById(R.id.title_bar);
        fragmentTitle.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(title)) {
            fragmentTitle.setTitle(title);
            fragmentTitle.setTitleColor(Color.BLACK);
        }
        return fragmentTitle;
    }

    public XTitle setFragmentTitle(@StringRes int titleId) {
        XTitle fragmentTitle = (XTitle) view.findViewById(R.id.title_bar);
        fragmentTitle.setVisibility(View.VISIBLE);
        fragmentTitle.setTitle(getString(titleId));
        fragmentTitle.setTitleColor(Color.BLACK);
        return fragmentTitle;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void initView(View view) {
    }

    public int getResColor(@ColorRes int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    public Drawable getResDrawable(@DrawableRes int drawableid) {
        return ContextCompat.getDrawable(getContext(), drawableid);
    }

}
