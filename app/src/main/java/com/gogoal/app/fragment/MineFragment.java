package com.gogoal.app.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.gogoal.app.R;
import com.gogoal.app.activity.PlayerActivity;
import com.gogoal.app.base.BaseFragment;
import com.gogoal.app.common.AppDevice;
import com.gogoal.app.common.AsyncTaskUtil;
import com.gogoal.app.common.DialogHelp;
import com.gogoal.app.common.ImageUtils.ImageDisplay;
import com.gogoal.app.common.ImageUtils.ImageTakeUtils;
import com.gogoal.app.common.UFileUpload;
import com.gogoal.app.common.UIHelper;
import com.hply.imagepicker.ITakePhoto;
import com.socks.library.KLog;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.image_ad)
    ImageView imageView;

    public MineFragment() {
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void doBusiness(Context mContext) {
        ImageDisplay.loadRoundedRectangleImage(
                mContext,
                imageView,
                AppDevice.dp2px(mContext,5),
                R.mipmap.image_found_top_ad);
    }

    @OnClick({R.id.setWatchLive, R.id.btn_share, R.id.btn_upload})
    public void WatchLive(View view) {
        switch (view.getId()) {
            case R.id.setWatchLive:
                startActivity(new Intent(getContext(), PlayerActivity.class));
                break;
            case R.id.btn_upload:
                ImageTakeUtils.getInstance().takePhoto(getContext(), 1, false, new ITakePhoto() {
                    @Override
                    public void sueecss(List<String> uriPaths, boolean isOriginalPic) {
                        KLog.e(uriPaths);
                        if (uriPaths != null) {
                            //返回的图片集合不为空，执行上传操作
                            doUpload(uriPaths);
                        }
                    }

                    @Override
                    public void erro() {

                    }
                });
                break;
            case R.id.btn_share:
                UIHelper.showShareDialog(getActivity(), null, null, "分享", "第一次分享");
                break;
        }
    }

    private void doUpload(final List<String> uriPaths) {
        final ProgressDialog[] waitDialog = new ProgressDialog[1];

        waitDialog[0] = DialogHelp.getWaitDialog(getContext(), "上传中...");
        waitDialog[0].setCanceledOnTouchOutside(false);
        waitDialog[0].show();

        AsyncTaskUtil.doAsync(new AsyncTaskUtil.AsyncCallBack() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void doInBackground() {
                UFileUpload.getInstance().upload(new File(uriPaths.get(0)), UFileUpload.Type.IMAGE, new UFileUpload.UploadListener() {

                    @Override
                    public void onUploading(final int progress) {
                        KLog.e("上传进度===" + progress);
                    }

                    @Override
                    public void onSuccess(String onlineUri) {
                        KLog.e("上传成功===" + onlineUri);
                        waitDialog[0].cancel();
                        UIHelper.toast(getContext(), "上传成功" + onlineUri);
                    }

                    @Override
                    public void onFailed() {
                        KLog.e("上传失败!!!!!!");
                    }
                });
            }

            @Override
            public void onPostExecute() {

            }
        });
    }
}
