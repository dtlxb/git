package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
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
import cn.gogoal.im.common.DialogHelp;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.MessageUtils;
import cn.gogoal.im.common.PlayerUtils.MyDownTimer;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XEditText;
import cn.gogoal.im.ui.view.XTitle;

import static java.security.AccessController.getContext;

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

    @BindView(R.id.tv_get_code)
    TextView tvGetCode;

    @BindView(R.id.login_layout)
    RelativeLayout loginLayout;

    @BindView(R.id.tv_login)
    TextView tvLogin;

    @BindView(R.id.login_button)
    SelectorButton loginButton;

    int actionType;

    @Override
    public int bindLayout() {
        return R.layout.activity_rigister;
    }

    @Override
    public void doBusiness(Context mContext) {
        actionType = getIntent().getIntExtra("action_type", -1);

        initTitle();
        timeCounter();
    }

    private void initTitle() {
        if (actionType == AppConst.LOGIN_FIND_CODE) {
            loginLayout.setVisibility(View.GONE);
            loginButton.setText(R.string.str_code_confirm);
            editCode.setVisibility(View.GONE);
            xTitle = setMyTitle(R.string.str_login_confirm, true);
        } else {
            loginLayout.setVisibility(View.VISIBLE);
            loginButton.setText(R.string.str_login_sure);
            editCode.setVisibility(View.VISIBLE);
            xTitle = setMyTitle(R.string.str_login_register, true);
        }

        // 密码可见监听
        editCode.setDrawableRightListener(new XEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {

                if (editCode.getInputType() == 0x81) {
                    editCode.setInputType(0x90);
                } else if (editCode.getInputType() == 0x90) {
                    editCode.setInputType(0x81);
                }
                editCode.setSelection(editCode.getText().length());
            }
        });
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
                    startActivity(new Intent(RigisterActivity.this, FindCodeActivity.class));
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
                if (result.getIntValue("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    int code = data.getInteger("code");
                    if (code == 0) {
                        downTimer.start();
                    } else {
                        tvGetCode.setEnabled(true);
                        tvGetCode.setTextColor(getResColor(R.color.colorPrimary));
                        UIHelper.toast(RigisterActivity.this, "短信发送失败！");
                    }
                } else {
                    tvGetCode.setEnabled(true);
                    tvGetCode.setTextColor(getResColor(R.color.colorPrimary));
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

    private void rigisterNow() {
        if (!UIHelper.GGPhoneNumber(editPhoneNumber.getText().toString().trim(), RigisterActivity.this) || !UIHelper.GGCode(editPaseCode.getText().toString().trim(), RigisterActivity.this))
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
                        startActivity(new Intent(RigisterActivity.this, EditPersonInfoActivity.class));
                        finish();
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
