package cn.gogoal.im.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hply.imagepicker.ITakePhoto;
import com.hply.roundimage.roundImage.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AsyncTaskUtil;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.ImageTakeUtils;
import cn.gogoal.im.common.ImageUtils.UFileImageHelper;
import cn.gogoal.im.common.UFileUpload;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.openServices.IOpenCallback;
import cn.gogoal.im.common.openServices.OpenServiceFactory;
import cn.gogoal.im.ui.dialog.BottomSheetListDialog;
import cn.gogoal.im.ui.dialog.BottomSheetNormalDialog;
import cn.gogoal.im.ui.dialog.EditTextDialog;

import static cn.gogoal.im.base.MyApp.getAppContext;

/**
 * author wangjd on 2017/3/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class DemoActivity extends BaseActivity {

    @BindView(R.id.image_ad)
    RoundedImageView imageView;

    @Override
    public int bindLayout() {
        return R.layout.activity_demo;
    }

    @Override
    public void doBusiness(Context mContext) {
        //对显示图片圆角矩形处理
        ImageDisplay.loadRoundedRectangleImage(
                mContext,
                R.mipmap.image_found_top_ad,
                imageView);

        UFileImageHelper.load("")
                .compress(40)
                .rotate(270)
                .scale(46)
                .get();
    }

    @OnClick({R.id.setWatchLive, R.id.btn_test, R.id.btn_upload, R.id.btn_item_dialog, R.id.btn_wechat, R.id.btn_item_edit})
    public void WatchLive(View view) {
        switch (view.getId()) {
            case R.id.setWatchLive:
                startActivity(new Intent(getAppContext(), PlayerActivity.class));
                break;

            case R.id.btn_wechat:
                DialogHelp.showShareDialog(getActivity(),
                        "https://m.baidu.com", "http://g1.dfcfw.com/g2/201702/20170216133526.png",
                        "分享",
                        "第一次分享");
                break;

            case R.id.btn_upload:
                ImageTakeUtils.getInstance().takePhoto(getAppContext(), 9, false, new ITakePhoto() {
                    @Override
                    public void success(List<String> uriPaths, boolean isOriginalPic) {
                        if (uriPaths != null) {
                            //返回的图片集合不为空，执行上传操作
                            doUpload(uriPaths);
                            UIHelper.toast(getAppContext(), uriPaths.toString());
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
                        new String[]{"微信登录", "分享到微信好友", "分享到朋友圈", "微信收藏", "测试"}));

                DialogHelp.getBottomSheetListDialog(getActivity(), menu, new BottomSheetListDialog.DialogItemClick() {
                    @Override
                    public void onItemClick(BottomSheetListDialog dialog, TextView view, int position) {
                        switch (position) {
                            case 0:
                                OpenServiceFactory.with(getAppContext())
                                        .wechat().login(new IOpenCallback() {
                                    @Override
                                    public void onFailed(String errorString) {
                                    }

                                    @Override
                                    public void onSuccess(String responceString) {
                                    }
                                });
                                dialog.dismiss();
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
                        UIHelper.toast(getAppContext(), view.getText().toString());
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
                EditTextDialog dialog = new EditTextDialog();
                dialog.show(getActivity().getSupportFragmentManager());

                dialog.setOnSendButtonClick(new EditTextDialog.OnSendMessageListener() {
                    @Override
                    public void doSend(View view, EditText msg) {
                        //noz9Z();
                        //
                    }
                });

                break;
        }
    }

    private void doUpload(final List<String> uriPaths) {
        final ProgressDialog[] waitDialog = new ProgressDialog[1];

        waitDialog[0] = DialogHelp.getWaitDialog(getAppContext(), "上传中...");
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
                    }

                    @Override
                    public void onSuccess(String onlineUri) {
                        waitDialog[0].cancel();
                        UIHelper.toast(getAppContext(), "上传成功" + onlineUri);
                    }

                    @Override
                    public void onFailed() {
                        waitDialog[0].cancel();
                    }
                });
            }

            @Override
            public void onPostExecute() {

            }
        });
    }
}
