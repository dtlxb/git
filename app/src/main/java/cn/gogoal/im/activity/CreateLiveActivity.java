package cn.gogoal.im.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.ITakePhoto;
import com.hply.imagepicker.view.StatusBarUtil;
import com.socks.library.KLog;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.AsyncTaskUtil;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.ImageTakeUtils;
import cn.gogoal.im.common.UFileUpload;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;

/*
* 创建直播
* */
public class CreateLiveActivity extends BaseActivity {

    @BindView(R.id.imgLive)
    ImageView imgLive;

    @BindView(R.id.editLive)
    EditText editLive;

    //图片地址
    private String liveLargeImg = null;

    @Override
    public int bindLayout() {
        return R.layout.activity_create_live;
    }

    @Override
    public void doBusiness(Context mContext) {
        //对显示图片圆角矩形处理
        ImageDisplay.loadRoundedRectangleImage(
                mContext,
                imgLive,
                AppDevice.dp2px(mContext, 5),//圆角弧度
                R.mipmap.logo);
    }

    @OnClick({R.id.imgClose, R.id.imgLive, R.id.btnStartLive, R.id.textAgreement})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.imgClose: //关闭
                finish();
                break;
            case R.id.imgLive: //设置直播封面图片
                ImageTakeUtils.getInstance().takePhoto(getContext(), 1, true, 750, new ITakePhoto() {
                    @Override
                    public void success(List<String> uriPaths, boolean isOriginalPic) {
                        if (uriPaths != null) {
                            doUpload(uriPaths);
                        }
                    }

                    @Override
                    public void error() {
                    }
                });
                break;
            case R.id.btnStartLive: //开始直播
                getStartLive();
                break;
            case R.id.textAgreement: //查看直播协议
                break;
        }
    }

    private void doUpload(final List<String> uriPaths) {

        //弹个窗
        final ProgressDialog waitDialog =
                DialogHelp.getWaitDialog(getActivity(), "上传中...");
        waitDialog.setCancelable(false);
        waitDialog.show();

        AsyncTaskUtil.doAsync(new AsyncTaskUtil.AsyncCallBack() {
            @Override
            public void onPreExecute() {
            }

            @Override
            public void doInBackground() {
                UFileUpload.getInstance().upload(new File(uriPaths.get(0)), UFileUpload.Type.IMAGE, new UFileUpload.UploadListener() {

                    @Override
                    public void onUploading(final int progress) {
                        //上传进度
                    }

                    @Override
                    public void onSuccess(String onlineUri) {
                        KLog.e("上传成功===" + onlineUri);
                        waitDialog.dismiss();

                        liveLargeImg = onlineUri;

                        ImageDisplay.loadRoundedRectangleImage(
                                getContext(),
                                imgLive,
                                AppDevice.dp2px(getContext(), 5),//圆角弧度
                                onlineUri);
                    }

                    @Override
                    public void onFailed() {
                        KLog.e("上传失败!!!!!!");

                        waitDialog.dismiss();
                        liveLargeImg = null;

                        UIHelper.toast(getContext(), "上传图片失败");
                    }
                });
            }

            @Override
            public void onPostExecute() {
            }
        });
    }

    private void getStartLive() {

        /*if (liveLargeImg == null) {
            UIHelper.toast(this, "请更换封面");
            return;
        }*/

        String title = editLive.getText().toString();
        if (title.equals("")) {
            UIHelper.toast(this, "请输入直播标题");
            return;
        }
        if (!title.matches(".*[^ ].*")) {
            UIHelper.toast(this, "不要全部输入空格");
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("live_title", title);
        param.put("live_large_img", liveLargeImg);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.getIntValue("code") == 1) {
                        if (data.getString("live_id") != null) {
                            finish();
                            Intent intent = new Intent(getContext(), LiveActivity.class);
                            intent.putExtra("live_id", data.getString("live_id"));
                            startActivity(intent);
                        } else {
                            UIHelper.toast(getContext(), "创建直播失败");
                        }
                    } else {
                        UIHelper.toast(getContext(), "创建直播失败");
                    }
                } else {
                    UIHelper.toast(getContext(), "创建直播失败");
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.ADD_LIVE_VIDEO, ggHttpInterface).startGet();
    }

    @Override
    public void setStatusBar(boolean light) {
        StatusBarUtil.with(this).setTranslucent();
    }

    private CreateLiveActivity getContext() {
        return CreateLiveActivity.this;
    }
}
