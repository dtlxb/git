package com.gogoal.app.fragment;


import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.gogoal.app.R;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.common.ArrayUtils;
import com.gogoal.app.common.DialogHelp;
import com.gogoal.app.common.image.ImageTakeUtils;
import com.hply.imagepicker.ITakePhoto;
import com.socks.library.KLog;

import java.util.List;

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
        
        ImageTakeUtils.getInstance().takePhoto(view.getContext(), 11, false, new ITakePhoto() {
            @Override
            public void sueecss(List<String> uriPaths, boolean originalPic) {
                DialogHelp.getSelectDialog(getContext(), ArrayUtils.list2Array(uriPaths),null).show();
            }

            @Override
            public void erro() {
                KLog.e("出错啦");
            }
        });
    }
}
