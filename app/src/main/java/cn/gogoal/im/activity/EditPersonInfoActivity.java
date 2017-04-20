package cn.gogoal.im.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.ITakePhoto;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.DownloadCallBack;
import cn.gogoal.im.common.DownloadUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.ImageTakeUtils;
import cn.gogoal.im.common.UFileUpload;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XTitle;
import cn.gogoal.im.ui.widget.BottomSheetListDialog;

/**
 * Created by huangxx on 2017/4/18.
 */

public class EditPersonInfoActivity extends BaseActivity {

    private XTitle xTitle;

    @BindView(R.id.layout_person_headpic)
    FrameLayout layoutPersonHeadpic;

    @BindView(R.id.image_person_headpic)
    ImageView imagePersonHeadpic;

    @BindView(R.id.edit_person_name)
    EditText editPersonName;

    @BindView(R.id.edit_company_name)
    EditText editCompanyName;

    @BindView(R.id.edit_job_name)
    EditText editJobName;

    @BindView(R.id.login_cofirm)
    SelectorButton loginCofirm;

    @Override
    public int bindLayout() {
        return R.layout.activity_edit_person_info;
    }

    @Override
    public void doBusiness(Context mContext) {
        initTitle();
        ImageDisplay.loadResAvatar(EditPersonInfoActivity.this, R.mipmap.login_gogoal, imagePersonHeadpic);
    }

    private void editPersonInfos() {
        final Map<String, String> params = new HashMap<>();
        params.put("token", UserUtils.getToken());
        params.put("avatar", "20");
        params.put("name", editPersonName.getText().toString());
        params.put("company", editCompanyName.getText().toString());
        params.put("duty", editJobName.getText().toString());
        loginCofirm.setEnabled(false);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                loginCofirm.setEnabled(true);
                if (result.getIntValue("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    boolean success = data.getBoolean("success");
                    if (success) {
                        UIHelper.toast(EditPersonInfoActivity.this, "资料修改成功！");
                        Intent intent = new Intent(EditPersonInfoActivity.this, MainActivity.class);
                        intent.putExtra("isFromLogin", true);
                        startActivity(intent);
                    } else {
                        UIHelper.toast(EditPersonInfoActivity.this, "资料修改失败！");
                    }
                } else {
                    UIHelper.toast(EditPersonInfoActivity.this, "资料修改失败！");
                }
            }

            @Override
            public void onFailure(String msg) {
                loginCofirm.setEnabled(true);
                UIHelper.toast(EditPersonInfoActivity.this, R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.UPDATE_ACCOUNT_INFO, ggHttpInterface).startGet();
    }

    private void initTitle() {
        xTitle = setMyTitle(R.string.str_edit_person_info, true);
        //添加action
        XTitle.TextAction jumpAction = new XTitle.TextAction("跳过") {
            @Override
            public void actionClick(View view) {
                Intent intent = new Intent(EditPersonInfoActivity.this, MainActivity.class);
                intent.putExtra("isFromLogin", true);
                startActivity(intent);
                finish();
            }
        };
        xTitle.addAction(jumpAction, 0);
        TextView rigisterView = (TextView) xTitle.getViewByAction(jumpAction);
        rigisterView.setTextColor(getResColor(R.color.colorPrimary));
    }

    //上传头像到ufile
    private void uploadAvatar(File chooseAvatar) {
        //弹个窗
        final ProgressDialog waitDialog =
                DialogHelp.getWaitDialog(getActivity(), "上传中...");
        waitDialog.setCancelable(false);
        waitDialog.show();

        //上传
        UFileUpload.getInstance().upload(chooseAvatar,
                UFileUpload.Type.IMAGE,
                new UFileUpload.UploadListener() {
                    @Override
                    public void onUploading(int progress) {

                    }

                    @Override
                    public void onSuccess(String onlineUri) {
                        waitDialog.dismiss();
                        UIHelper.toast(getActivity(), "修改成功");
                        AppManager.getInstance().sendMessage("updata_cache_avatar",
                                onlineUri);
                        KLog.e("ufile图片外链::" + onlineUri);
//                                                    http://hackfile.ufile.ucloud.cn/GoGoal_3E21A216416826E307F2805796BE0C55.jpg@1000x1000
                        UserUtils.updataLocalUserInfo("simple_avatar", onlineUri);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("avatar", onlineUri);
                        UserUtils.updataNetUserInfo(map, new UserUtils.UpdataListener() {
                            @Override
                            public void success(String responce) {
                                KLog.e(responce);
                            }

                            @Override
                            public void failed(String errorMsg) {

                            }
                        });
                    }

                    @Override
                    public void onFailed() {
                        waitDialog.dismiss();
                        UIHelper.toast(getActivity(), "修改出错！");
                    }
                });
    }

    private void openPicture() {
        ImageTakeUtils.getInstance().takePhoto(getActivity(), 1, true, new ITakePhoto() {
            @Override
            public void success(List<String> uriPaths, boolean isOriginalPic) {
                if (uriPaths != null && (!uriPaths.isEmpty())) {
                    File chooseAvatar = new File(uriPaths.get(0));
                    ImageDisplay.loadNetAvatar(getActivity(),uriPaths.get(0),imagePersonHeadpic);
                    uploadAvatar(chooseAvatar);
                }
            }

            @Override
            public void error() {

            }
        });
    }

    /**
     * 头像预览 弹窗
     */
    private void editDialogClick() {
        DialogHelp.getBottomSheetListDialog(getActivity(), new ArrayList<>(Arrays.asList(
                new String[]{"拍照", "从手机相册中选择"})), new BottomSheetListDialog.DialogItemClick() {
            @Override
            public void onItemClick(BottomSheetListDialog dialog, TextView view, int position) {
                dialog.dismiss();
                switch (position) {
                    case 0:
                        //打开相机
                        //openCamera();

                        break;
                    case 1:
                        //从手机相册中选择
                        openPicture();
                        break;
                }
            }
        });
    }

    @OnClick({R.id.login_cofirm, R.id.layout_person_headpic})
    void function(View view) {
        switch (view.getId()) {
            case R.id.login_cofirm:
                editPersonInfos();
                break;
            case R.id.layout_person_headpic:
                /*Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
                intent.putExtra("account_Id", UserUtils.getUserAccountId());
                startActivity(intent);*/
                editDialogClick();
                break;
            default:
                break;
        }
    }
}
