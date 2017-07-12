package cn.gogoal.im.activity;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.XEditText;

/**
 * author wangjd on 2017/5/24 0024.
 * Staff_id 1375
 * phone 18930640263
 * description :修改密码
 */
public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.et_psw_original)
    XEditText etPswOriginal;

    @BindView(R.id.et_psw_new)
    XEditText etPswNew;

    @BindView(R.id.et_psw_new_again)
    XEditText etPswNewAgain;

    @Override
    public int bindLayout() {
        return R.layout.activity_change_password;
    }

    @Override
    public void doBusiness(Context mContext) {
        setMyTitle("修改登录密码", true);

        etPswOriginal.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPswNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPswNewAgain.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }

    @OnClick(R.id.btn_ok)
    void click(View view) {
        if (TextUtils.isEmpty(etPswOriginal.getText())) {
            showErrorDialog("原密码不能为空！");
        } else {
            if (TextUtils.isEmpty(etPswNew.getText()) || TextUtils.isEmpty(etPswNewAgain.getText())) {
                showErrorDialog("新密码不能为空！");
            } else if (!etPswNew.getText().toString().equalsIgnoreCase(etPswNewAgain.getText().toString())) {
                showErrorDialog("新密码两次输入不一致！");
            } else if (etPswNew.getText().length() < 6) {
                showErrorDialog("新密码格式错误！");
            } else {
                final WaitDialog loadingDialog = showLoadingDialog("请稍后...");
                HashMap<String, String> map = new HashMap<>();
                map.put("token", UserUtils.getToken());
                map.put("old_pwd", etPswOriginal.getText().toString());
                map.put("new_pwd", etPswNew.getText().toString());

                new GGOKHTTP(map, GGOKHTTP.RESET_PASSWORD, new GGOKHTTP.GGHttpInterface() {
                    @Override
                    public void onSuccess(String responseInfo) {

                        JSONObject object = JSONObject.parseObject(responseInfo);
                        if (object.getIntValue("code") == 0) {
                            boolean success = object.getJSONObject("data").getBoolean("success");
                            if (success) {
                                loadingDialog.dismiss();
                                WaitDialog dialog = WaitDialog.getInstance("修改密码成功", R.mipmap.login_success, false);
                                dialog.show(getSupportFragmentManager());
                                dialog.dismiss(false, true);
                            } else {
                                loadingDialog.dismiss();
                                showErrorDialog("原密码错误");
                            }

                        } else {
                            loadingDialog.dismiss();
                            showErrorDialog(object.getString("message"));

                        }

                    }

                    @Override
                    public void onFailure(String msg) {
                        loadingDialog.dismiss();
                        showErrorDialog(msg);
                    }
                }).startGet();
            }
        }
    }

    private void showErrorDialog(String text) {
        WaitDialog dialog = WaitDialog.getInstance(0.5f, text, R.mipmap.login_error, false);
        dialog.show(getSupportFragmentManager());
        dialog.dismiss(false);
    }

    private WaitDialog showLoadingDialog(String text) {
        WaitDialog dialog = WaitDialog.getInstance(0.5f, text, R.mipmap.login_loading, true);
        dialog.show(getSupportFragmentManager());
        return dialog;
    }
}
