package com.gogoal.app.fragment;


import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.gogoal.app.R;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.common.image.GetImageConfig;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.btn_openGallery)
    Button btnOpenGallery;

    public MessageFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick(R.id.btn_openGallery)
    void openGallery(View view){
        GetImageConfig.getInstance()
                .setLimit(7)
//                .setCanCrop(false)
                .takePhoto(view.getContext());
    }
}
