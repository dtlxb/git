package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.PlayerUtils.MyDownTimer;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XEditText;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/17.
 */

public class RegisterActivity extends BaseActivity {

    private XTitle xTitle;
    private MyDownTimer downTimer;

    //手机号
    @BindView(R.id.edit_phone_number)
    EditText editPhoneNumber;
    //验证码
    @BindView(R.id.edit_pase_code)
    EditText editPaseCode;
    //密码
    @BindView(R.id.edit_code)
    XEditText editCode;
    @BindView(R.id.checkbox_psw)
    CheckBox chToggle;
    //验证密码
    @BindView(R.id.layout_valid)
    LinearLayout layoutValid;
    @BindView(R.id.valid_edit_code)
    XEditText validEditCode;
    @BindView(R.id.checkbox_valid_psw)
    CheckBox toggleValid;

    @BindView(R.id.tv_get_code)
    TextView tvGetCode;

    @BindView(R.id.login_layout)
    RelativeLayout loginLayout;

    @BindView(R.id.tv_login)
    TextView tvLogin;

    @BindView(R.id.login_button)
    SelectorButton loginButton;

    private int actionType;

    private WaitDialog waitDialog;

    @Override
    public int bindLayout() {
        return R.layout.activity_rigister;
    }

    @Override
    public void doBusiness(Context mContext) {
        actionType = getIntent().getIntExtra("action_type", -1);
        UIHelper.passwordToggle(editCode, chToggle);
        UIHelper.passwordToggle(validEditCode, toggleValid);
        initTitle();
        timeCounter();
    }

    private void initTitle() {
        if (actionType == AppConst.LOGIN_FIND_CODE) {
            loginLayout.setVisibility(View.GONE);
            layoutValid.setVisibility(View.VISIBLE);
            xTitle = setMyTitle(R.string.str_correct_code, true);
        } else {
            loginLayout.setVisibility(View.VISIBLE);
            layoutValid.setVisibility(View.GONE);
            xTitle = setMyTitle(R.string.str_login_register, true);
        }
    }

    @OnClick({R.id.edit_phone_number, R.id.edit_pase_code, R.id.edit_code, R.id.tv_get_code, R.id.tv_login, R.id.login_button})
    void function(View view) {
        switch (view.getId()) {
            case R.id.edit_phone_number:
                break;
            case R.id.edit_pase_code:
                break;
            case R.id.edit_code:

                break;
            case R.id.tv_get_code:
                getVerificationCode();
                break;
            case R.id.tv_login:
                finish();
                break;
            case R.id.login_button:
                waitDialog = WaitDialog.getInstance("请稍后!", R.mipmap.login_loading, true);
                waitDialog.show(RegisterActivity.this.getSupportFragmentManager());

                if (actionType == AppConst.LOGIN_RIGIST_NUMBER) {
                    registerNow();
                } else if (actionType == AppConst.LOGIN_FIND_CODE) {
                    //显示更改成功5s后自动跳转登录页面
                    correctPassCode();
                }
                break;
            default:
                break;
        }

    }

    //获取验证码
    private void getVerificationCode() {
        if (!UIHelper.GGPhoneNumber(editPhoneNumber.getText().toString().trim(), RegisterActivity.this))
            return;
        tvGetCode.setEnabled(false);

        final Map<String, String> params = new HashMap<>();
        params.put("phone", editPhoneNumber.getText().toString());
        params.put("source", "20");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
//                tvGetCode.setEnabled(true);
//                tvGetCode.setTextColor(getResColor(R.color.colorPrimary));
                if (result.getIntValue("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    int code = data.getInteger("code");
                    if (code == 0) {
                        downTimer.start();
                    } else {
                        resetTvGetCode();
                        UIHelper.toast(RegisterActivity.this, "短信发送失败！");
                    }
                } else {
                    resetTvGetCode();
                    UIHelper.toast(RegisterActivity.this, "短信发送失败！");
                }
            }

            @Override
            public void onFailure(String msg) {
                tvGetCode.setEnabled(true);
                tvGetCode.setTextColor(getResColor(R.color.colorPrimary));
                UIHelper.toast(RegisterActivity.this, R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.MOBILE_SEND_CAPTCHA, ggHttpInterface).startGet();

    }

    private void correctPassCode() {
        if (!UIHelper.GGPhoneNumber(editPhoneNumber.getText().toString().trim(), RegisterActivity.this)
                || !UIHelper.GGCode(editPaseCode.getText().toString().trim(), RegisterActivity.this)
                || !codeIsTheSame(editCode.getText().toString().trim(), validEditCode.getText().toString().trim())
                || !UIHelper.isGGPassWord(editCode.getText().toString().trim(), getActivity())
                || !UIHelper.isGGPassWord(validEditCode.getText().toString().trim(), getActivity())) {
            waitDialog.dismiss();
            return;
        }
        loginLayout.setEnabled(false);

        final Map<String, String> params = new HashMap<>();
        params.put("captcha", editPaseCode.getText().toString());
        params.put("new_pwd", validEditCode.getText().toString());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                waitDialog.dismiss();

                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                loginLayout.setEnabled(true);
                if (result.getIntValue("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    boolean success = data.getBoolean("success");
                    if (success) {
                        WaitDialog successDialog = WaitDialog.getInstance("密码重置成功,即将跳转登录页面", R.mipmap.login_success, false);
                        successDialog.show(getSupportFragmentManager());
                        successDialog.dismiss(false, true);
                    } else {
                        showErrorDialog("验证码失效，请重新获取验证码", false);
                    }
                } else {
                    showErrorDialog("密码重置失败,请重试", false);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (loginLayout != null) {
                    loginLayout.setEnabled(true);
                }
                UIHelper.toast(RegisterActivity.this, R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.RESET_PASSWORD_BY_MOBILE, ggHttpInterface).startGet();

    }

    private void registerNow() {
        if (!UIHelper.GGPhoneNumber(editPhoneNumber.getText().toString().trim(), RegisterActivity.this)
                || !UIHelper.GGCode(editPaseCode.getText().toString().trim(), RegisterActivity.this)
                || !UIHelper.isGGPassWord(editCode.getText().toString().trim(), getActivity())) {
            waitDialog.dismiss();
            return;
        }
        final Map<String, String> params = new HashMap<>();
        params.put("phone", editPhoneNumber.getText().toString().trim());
        params.put("password", editCode.getText().toString().trim());
        params.put("captcha", editPaseCode.getText().toString().trim());
        params.put("type", "116");
        params.put("role", "222");
        params.put("source", "20");
        loginLayout.setEnabled(false);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                waitDialog.dismiss();

                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                loginLayout.setEnabled(true);
                if (result.getIntValue("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    int dataCode = data.getInteger("code");
                    if (dataCode == 0) {
                        WaitDialog successDialog = WaitDialog.getInstance("注册成功,即将自动跳转登录页面", R.mipmap.login_success, false);
                        successDialog.show(getSupportFragmentManager());
                        successDialog.dismiss(false, true);
                    } else if (dataCode == 3) {
                        //账号已存在
                        showErrorDialog("该手机号已注册，请直接登录", true);
                    } else {
                        showErrorDialog(getString(R.string.str_rigister_error), false);
                    }
                } else {
                    showErrorDialog(getString(R.string.str_rigister_error), false);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (loginLayout != null)
                    loginLayout.setEnabled(true);
                showErrorDialog("数据请求出错(" + msg + ")", false);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.USER_REGISTER, ggHttpInterface).startGet();
    }

    private void showErrorDialog(String text, boolean finishSelf) {
        WaitDialog errorDialog = WaitDialog.getInstance(text, R.mipmap.login_error, false);
        errorDialog.show(getSupportFragmentManager());
        errorDialog.dismiss(false, finishSelf);
    }

    private boolean codeIsTheSame(String code1, String code2) {
        if (code1.equals(code2)) {
            return true;
        } else {
            UIHelper.toast(RegisterActivity.this, "两次密码不一致");
            return false;
        }
    }

    // 初始化计时器
    private void timeCounter() {

        downTimer = new MyDownTimer(60, new MyDownTimer.Runner() {
            @Override
            public void run(long sec) {
                tvGetCode.setText(sec + "s后重新发送");
                tvGetCode.setTextColor(getResColor(R.color.textColor_999999));
                tvGetCode.setBackgroundResource(R.drawable.login_edit_border);
            }

            @Override
            public void finish() {
                resetTvGetCode();
            }
        });
    }

    private void resetTvGetCode() {
        tvGetCode.setText("获取验证码");
        tvGetCode.setBackgroundResource(R.drawable.login_edit_red_border);
        tvGetCode.setTextColor(getResColor(R.color.colorPrimary));
        tvGetCode.setEnabled(true);
    }
}
