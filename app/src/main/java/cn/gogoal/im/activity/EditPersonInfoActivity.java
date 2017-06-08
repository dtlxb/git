package cn.gogoal.im.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.ITakePhoto;
import com.hply.roundimage.roundImage.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.ImageUtils.ImageDisplay;
import cn.gogoal.im.common.ImageUtils.ImageTakeUtils;
import cn.gogoal.im.common.UFileUpload;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.dialog.BottomSheetListDialog;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/18.
 */

public class EditPersonInfoActivity extends BaseActivity {

    private XTitle xTitle;

    @BindView(R.id.layout_person_headpic)
    FrameLayout layoutPersonHeadpic;

    @BindView(R.id.image_person_headpic)
    RoundedImageView imagePersonHeadpic;

    @BindView(R.id.edit_person_name)
    EditText editPersonName;

    @BindView(R.id.edit_company_name)
    EditText editCompanyName;

    @BindView(R.id.edit_job_name)
    EditText editJobName;

    @BindView(R.id.login_cofirm)
    SelectorButton loginConfirm;

    private String imageUri;

    @Override
    public int bindLayout() {
        return R.layout.activity_edit_person_info;
    }

    @Override
    public void doBusiness(Context mContext) {
        initTitle();
        ImageDisplay.loadCircleImage(EditPersonInfoActivity.this, UserUtils.getUserAvatar().equals("")
                ? R.mipmap.logo : UserUtils.getUserAvatar(), imagePersonHeadpic);
    }

    private void initTitle() {
        xTitle = setMyTitle(R.string.str_edit_person_info, false);
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
        TextView registerView = (TextView) xTitle.getViewByAction(jumpAction);
        registerView.setTextColor(getResColor(R.color.colorPrimary));

        editPersonName.setText(UserUtils.getNickname());
        editCompanyName.setText(UserUtils.getorgName());
        editJobName.setText(UserUtils.getDuty());
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
                    ImageDisplay.loadCircleImage(getActivity(), uriPaths.get(0), imagePersonHeadpic);
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
                new String[]{"拍照"})), new BottomSheetListDialog.DialogItemClick() {
            @Override
            public void onItemClick(BottomSheetListDialog dialog, TextView view, int position) {
                dialog.dismiss();
                switch (position) {
                    case 0:
                        //打开相机
                        openPicture();
                        break;
                    default:
                        break;
                    /*case 1:
                        //从手机相册中选择
                        openPicture();
                        break;*/
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
                if (TextUtils.isEmpty(imageUri) &&
                        editJobName.getText().toString().equals(UserUtils.getDuty()) &&
                        editCompanyName.getText().toString().equals(UserUtils.getorgName()) &&
                        editPersonName.getText().toString().equals(UserUtils.getNickname())) {
                    goToNextPage();
                    return;
                }

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
                loginConfirm.setClickable(false);

                final WaitDialog waitDialog = WaitDialog.getInstance("请稍后", R.mipmap.login_loading, true);
                waitDialog.show(getSupportFragmentManager());

                UserUtils.updataNetUserInfo(map, new UserUtils.UpdataListener() {
                    @Override
                    public void success(String responce) {
                        JSONObject result = JSONObject.parseObject(responce);
                        loginConfirm.setClickable(true);
                        if (result.getIntValue("code") == 0) {
                            JSONObject data = result.getJSONObject("data");
                            boolean success = data.getBoolean("success");
                            if (success) {
                                waitDialog.dismiss();
                                WaitDialog successDialog = WaitDialog.getInstance("资料修改成功", R.mipmap.login_success, false);
                                successDialog.show(getSupportFragmentManager());
                                successDialog.dismiss(false, new WaitDialog.DialogDismiss() {
                                    @Override
                                    public void dialogDismiss() {
                                        goToNextPage();
                                    }
                                });
                            } else {
                                waitDialog.dismiss();
                                WaitDialog errorDialog = WaitDialog.getInstance("资料修改失败", R.mipmap.login_error, false);
                                errorDialog.show(getSupportFragmentManager());
                                errorDialog.dismiss(false);
                            }
                        } else {
                            waitDialog.dismiss();
                            WaitDialog errorDialog = WaitDialog.getInstance("资料修改失败", R.mipmap.login_error, false);
                            errorDialog.show(getSupportFragmentManager());
                            errorDialog.dismiss(false);
                        }
                    }

                    @Override
                    public void failed(String errorMsg) {
                        loginConfirm.setClickable(true);
                        waitDialog.dismiss();
                        WaitDialog errorDialog = WaitDialog.getInstance("资料修改失败", R.mipmap.login_error, false);
                        errorDialog.show(getSupportFragmentManager());
                        errorDialog.dismiss(false);
                    }
                });

                break;
            case R.id.layout_person_headpic:
                /*Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
                intent.putExtra("account_Id", UserUtils.getMyAccountId());
                startActivity(intent);*/
                editDialogClick();
                break;
            default:
                break;
        }
    }

    private void goToNextPage() {
        Intent intent = new Intent(EditPersonInfoActivity.this, MainActivity.class);
        intent.putExtra("isFromLogin", true);
        startActivity(intent);
        finish();
    }
}
