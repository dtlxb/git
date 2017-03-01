package com.gogoal.app.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.gogoal.app.ui.widget.BottomSheetListDialog;
import com.gogoal.app.ui.widget.BottomSheetNormalDialog;
import com.gogoal.app.ui.widget.EditTextDialog;
import com.hply.imagepicker.ITakePhoto;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
        //对显示图片圆角矩形处理
        ImageDisplay.loadRoundedRectangleImage(
                mContext,
                imageView,
                AppDevice.dp2px(mContext, 5),//圆角弧度
                R.mipmap.image_found_top_ad);
    }

    @OnClick({R.id.setWatchLive, R.id.btn_test, R.id.btn_upload, R.id.btn_item_dialog,R.id.btn_wechat,R.id.btn_item_edit})
    public void WatchLive(View view) {
        switch (view.getId()) {
            case R.id.setWatchLive:
                startActivity(new Intent(getContext(), PlayerActivity.class));
                break;

            case R.id.btn_wechat:
                DialogHelp.showShareDialog(getActivity(),
                        "https://m.baidu.com", "http://g1.dfcfw.com/g2/201702/20170216133526.png",
                        "分享",
                        "第一次分享");
                break;

            case R.id.btn_upload:
                ImageTakeUtils.getInstance().takePhoto(getContext(),9, false, new ITakePhoto() {
                    @Override
                    public void success(List<String> uriPaths, boolean isOriginalPic) {
                        KLog.e(uriPaths);
                        if (uriPaths != null) {
                            //返回的图片集合不为空，执行上传操作
//                            doUpload(uriPaths);
                            UIHelper.toast(getContext(),uriPaths.toString());
                        }
                    }

                    @Override
                    public void error() {

                    }
                });
                break;
            case R.id.btn_item_dialog:
                //传个集合就行啦，真是666 啊，东哥
                ArrayList<String> menu = new ArrayList<>(Arrays.asList(
                        new String[]{"保存图片", "查看原图", "转发微博", "赞", "举报"}));

                DialogHelp.getBottomSheetListDialog(getActivity(), menu, new BottomSheetListDialog.DialogItemClick() {
                    @Override
                    public void onItemClick(BottomSheetListDialog dialog, TextView view, int position) {
                        switch (position) {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                dialog.dismiss();
                                break;
                        }
                        UIHelper.toast(getContext(),view.getText().toString());
                    }
                });

                break;
            case R.id.btn_test:
                DialogHelp.getBottomSheelNormalDialog(getActivity(), R.layout.activity_imregister, new BottomSheetNormalDialog.ViewListener() {

                    @Override
                    public void bindDialogView(final BottomSheetNormalDialog dialog, View dialogView) {
                        dialogView.findViewById(R.id.chat_room_login).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });

            case R.id.btn_item_edit:
                EditTextDialog dialog=new EditTextDialog();
                dialog.show(getActivity().getSupportFragmentManager());

                dialog.setOnSendButtonClick(new EditTextDialog.OnSendMessageListener() {
                    @Override
                    public void doSend(View view, String msg) {
                        //noz9Z();
                        //
                    }
                });

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
