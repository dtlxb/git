package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.PlayerUtils.MyDownTimer;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XEditText;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/17.
 */

public class RigisterActivity extends BaseActivity {

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

    private Handler handler;

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


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x01:
                        RigisterActivity.this.finish();
                        break;
                    case 0x02:
                        RigisterActivity.this.finish();
                        break;
                    default:
                        break;
                }
            }
        };
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
                if (actionType == AppConst.LOGIN_RIGIST_NUMBER) {
                    rigisterNow();
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
        if (!UIHelper.GGPhoneNumber(editPhoneNumber.getText().toString().trim(), RigisterActivity.this))
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
                tvGetCode.setEnabled(true);
                tvGetCode.setTextColor(getResColor(R.color.colorPrimary));
                if (result.getIntValue("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    int code = data.getInteger("code");
                    if (code == 0) {
                        downTimer.start();
                    } else {
                        UIHelper.toast(RigisterActivity.this, "短信发送失败！");
                    }
                } else {

                    UIHelper.toast(RigisterActivity.this, "短信发送失败！");
                }
            }

            @Override
            public void onFailure(String msg) {
                tvGetCode.setEnabled(true);
                tvGetCode.setTextColor(getResColor(R.color.colorPrimary));
                UIHelper.toast(RigisterActivity.this, R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.MOBILE_SEND_CAPTCHA, ggHttpInterface).startGet();

    }

    private void correctPassCode() {
        if (!UIHelper.GGPhoneNumber(editPhoneNumber.getText().toString().trim(), RigisterActivity.this)
                || !UIHelper.GGCode(editPaseCode.getText().toString().trim(), RigisterActivity.this)
                || !codeIsTheSame(editCode.getText().toString().trim(), validEditCode.getText().toString().trim())
                || !UIHelper.isGGPassWord(editCode.getText().toString().trim(), getActivity())
                || !UIHelper.isGGPassWord(validEditCode.getText().toString().trim(), getActivity()))
            return;
        loginLayout.setEnabled(false);

        final Map<String, String> params = new HashMap<>();
        params.put("captcha", editPaseCode.getText().toString());
        params.put("new_pwd", validEditCode.getText().toString());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                loginLayout.setEnabled(true);
                if (result.getIntValue("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    boolean success = data.getBoolean("success");
                    if (success) {
                        UIHelper.toastInCenter(RigisterActivity.this, "密码重置成功,将自动跳转登录页面", Toast.LENGTH_LONG);
                        handler.sendEmptyMessageDelayed(0x01, 2000);
                    } else {
                        UIHelper.toast(RigisterActivity.this, "验证码失效，请重新获取验证码");
                    }
                } else {
                    UIHelper.toast(RigisterActivity.this, "密码重置失败");
                }
            }

            @Override
            public void onFailure(String msg) {
                loginLayout.setEnabled(true);
                UIHelper.toast(RigisterActivity.this, R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.RESET_PASSWORD_BY_MOBILE, ggHttpInterface).startGet();

    }

    private void rigisterNow() {
        if (!UIHelper.GGPhoneNumber(editPhoneNumber.getText().toString().trim(), RigisterActivity.this)
                || !UIHelper.GGCode(editPaseCode.getText().toString().trim(), RigisterActivity.this)
                || !UIHelper.isGGPassWord(editCode.getText().toString().trim(), getActivity()))
            return;
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
                KLog.json(responseInfo);
                JSONObject result = JSONObject.parseObject(responseInfo);
                loginLayout.setEnabled(true);
                if (result.getIntValue("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    int dataCode = data.getInteger("code");
                    if (dataCode == 0) {
                        UIHelper.toastInCenter(RigisterActivity.this, "注册成功,将自动跳转登录页面", Toast.LENGTH_LONG);
                        handler.sendEmptyMessageDelayed(0x02, 2000);
                    } else if (dataCode == 3) {
                        //账号已存在
                        DialogHelp.getConfirmDialog(RigisterActivity.this, "该手机号已注册，请直接登录", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    } else {
                        UIHelper.toast(RigisterActivity.this, R.string.str_rigister_error);
                    }
                } else {
                    UIHelper.toast(RigisterActivity.this, R.string.str_rigister_error);
                }
            }

            @Override
            public void onFailure(String msg) {
                loginLayout.setEnabled(true);
                UIHelper.toast(RigisterActivity.this, R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.USER_REGISTER, ggHttpInterface).startGet();
    }

    private boolean codeIsTheSame(String code1, String code2) {
        if (code1.equals(code2)) {
            return true;
        } else {
            UIHelper.toast(RigisterActivity.this, "两次密码不一致");
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
                tvGetCode.setText("获取验证码");
                tvGetCode.setBackgroundResource(R.drawable.login_edit_red_border);
                tvGetCode.setTextColor(getResColor(R.color.colorPrimary));
                tvGetCode.setEnabled(true);
            }
        });
    }


}
