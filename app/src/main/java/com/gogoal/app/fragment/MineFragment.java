package com.gogoal.app.fragment;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gogoal.app.R;
import com.gogoal.app.activity.PlayerActivity;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.common.ImageUtils.ImageTakeUtils;
import com.gogoal.app.common.UIHelper;
import com.hply.imagepicker.ITakePhoto;
import com.socks.library.KLog;

import java.util.List;

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

    @OnClick({R.id.setWatchLive, R.id.btn_share, R.id.btn_upload})
    public void WatchLive(View view) {
        switch (view.getId()) {
            case R.id.setWatchLive:
                startActivity(new Intent(getContext(), PlayerActivity.class));
                break;
            case R.id.btn_upload:
                ImageTakeUtils.getInstance().takePhoto(getContext(), 9, false, new ITakePhoto() {
                    @Override
                    public void sueecss(List<String> uriPaths, boolean isOriginalPic) {
                        KLog.e(uriPaths);
                    }

                    @Override
                    public void erro() {

                    }
                });
                break;
            case R.id.btn_share:
                UIHelper.showShareDialog(getContext(), null, null, "分享", "第一次分享");
                break;
        }
    }
}
