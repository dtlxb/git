package cn.gogoal.im.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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

    private String imageUri;

    @Override
    public int bindLayout() {
        return R.layout.activity_edit_person_info;
    }

    @Override
    public void doBusiness(Context mContext) {
        initTitle();
        ImageDisplay.loadResAvatar(EditPersonInfoActivity.this, R.mipmap.login_gogoal, imagePersonHeadpic);
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
                        imageUri = onlineUri;
                        UIHelper.toast(getActivity(), "修改成功");
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
                    ImageDisplay.loadNetAvatar(getActivity(), uriPaths.get(0), imagePersonHeadpic);
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
                //资料上传后台
                Map<String, String> map = new HashMap<>();
                if (!TextUtils.isEmpty(imageUri)) {
                    UserUtils.updataLocalUserInfo("simple_avatar", imageUri);
                    map.put("avatar", imageUri);
                }
                if (!TextUtils.isEmpty(editPersonName.getText().toString())) {
                    UserUtils.updataLocalUserInfo("nickname", editPersonName.getText().toString());
                    map.put("name", editPersonName.getText().toString());
                }
                if (!TextUtils.isEmpty(editCompanyName.getText().toString())) {
                    UserUtils.updataLocalUserInfo("organization_name", imageUri);
                    map.put("company", editCompanyName.getText().toString());
                }
                if (!TextUtils.isEmpty(editJobName.getText().toString())) {
                    UserUtils.updataLocalUserInfo("duty", editJobName.getText().toString());
                    map.put("duty", editJobName.getText().toString());
                }
                loginCofirm.setClickable(false);
                UserUtils.updataNetUserInfo(map, new UserUtils.UpdataListener() {
                    @Override
                    public void success(String responce) {
                        JSONObject result = JSONObject.parseObject(responce);
                        loginCofirm.setClickable(true);
                        if (result.getIntValue("code") == 0) {
                            JSONObject data = result.getJSONObject("data");
                            boolean success = data.getBoolean("success");
                            if (success) {
                                UIHelper.toast(EditPersonInfoActivity.this, "资料修改成功！");
                                Intent intent = new Intent(EditPersonInfoActivity.this, MainActivity.class);
                                intent.putExtra("isFromLogin", true);
                                startActivity(intent);
                                finish();
                            } else {
                                UIHelper.toast(EditPersonInfoActivity.this, "资料修改失败！");
                            }
                        } else {
                            UIHelper.toast(EditPersonInfoActivity.this, "资料修改失败！");
                        }
                    }

                    @Override
                    public void failed(String errorMsg) {
                        loginCofirm.setClickable(true);
                        UIHelper.toast(EditPersonInfoActivity.this, R.string.net_erro_hint);
                    }
                });

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
