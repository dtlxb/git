package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVIMClientManager;
import cn.gogoal.im.common.LaunchRequest;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.KeyboardLaunchLinearLayout;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XEditText;

/**
 * Created by huangxx on 2017/4/17.
 */
public class TypeLoginActivity extends BaseActivity {

    @BindView(R.id.login_edite_name)
    EditText loginUserName;

    @BindView(R.id.login_edite_code)
    XEditText loginPassWord;

    @BindView(R.id.checkbox_psw)
    CheckBox chToggle;

    @BindView(R.id.forget_code)
    TextView forgetCode;

    @BindView(R.id.login_button)
    SelectorButton loginButton;

    @BindView(R.id.login_keyboard_layout)
    KeyboardLaunchLinearLayout keyboardLayout;

    @Override
    protected void setStatusColor(@ColorInt int color) {
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_type_login;
    }

    @Override
    public void doBusiness(Context mContext) {

        try {
            AppManager.getInstance().finishBackActivity(this);
        } catch (Exception e) {
            e.getMessage();
        }

        //存储设备是否低分屏，一定要竖屏
        SPTools.saveBoolean("low_dpi", AppDevice.getWidth(mContext) < AppDevice.DPI720P);

        loginPassWord.setInputType(InputType.TYPE_CLASS_TEXT);
        UIHelper.passwordToggle(loginPassWord, chToggle);
        initLoginInfo();
    }

    private void initLoginInfo() {
//        loginUserName.setText("E00018282");
//        loginPassWord.setText("ycy921150");

//        loginUserName.setText("13166270509");
//        loginPassWord.setText("888888");

//        loginUserName.setText("E00003645");//热
//        loginPassWord.setText("258369");

//        loginUserName.setText("E00018279");//冷
//        loginPassWord.setText("600255");

//        loginUserName.setText("E00002639");
//        loginPassWord.setText("412174");

//        loginUserName.setText("E00020181");
//        loginPassWord.setText("394495");

//        loginUserName.setText("E00002638");
//        loginPassWord.setText("123456");

//        loginUserName.setText("E00018281");//瑜
//        loginPassWord.setText("369520");

//        loginUserName.setText("E010399");
//        loginPassWord.setText("198122");

        loginUserName.setSelection(loginUserName.getText().length());

        //保存键盘高度
        keyboardLayout.setOnKeyboardChangeListener(new KeyboardLaunchLinearLayout.OnKeyboardChangeListener() {
            @Override
            public void OnKeyboardPop(int height) {
                SPTools.saveInt("soft_keybord_height", height);
            }

            @Override
            public void OnKeyboardClose() {

            }
        });
    }

    @OnClick({R.id.login_button,
            R.id.forget_code,
            R.id.tv_login_register})
    public void setLogin(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                Login();
                break;
            case R.id.forget_code:
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra("action_type", AppConst.LOGIN_FIND_CODE);
                startActivity(intent);
                break;
            case R.id.tv_login_register:
                Intent intent1 = new Intent(getActivity(), RegisterActivity.class);
                intent1.putExtra("action_type", AppConst.LOGIN_RIGIST_NUMBER);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    private void Login() {
        final WaitDialog loginDialog = WaitDialog.getInstance("登录中", R.mipmap.login_loading, true);
        loginDialog.show(getSupportFragmentManager());
        loginDialog.setCancelable(false);

        String name = loginUserName.getText().toString().toUpperCase(Locale.ENGLISH);
        String word = loginPassWord.getText().toString();

        if (TextUtils.isEmpty(word) || TextUtils.isEmpty(name)) {
            UIHelper.toast(TypeLoginActivity.this, R.string.str_login_edit_null);
            loginDialog.dismiss();
            WaitDialog dialog = WaitDialog.getInstance(getString(R.string.str_login_edit_null), R.mipmap.login_error, false);
            dialog.show(getSupportFragmentManager());
            dialog.dismiss(false);
            return;
        }

        if (!UIHelper.isGGPassWord(word, getActivity())) {
            loginDialog.dismiss();
            WaitDialog dialog = WaitDialog.getInstance("密码格式错误", R.mipmap.login_error, false);
            dialog.show(getSupportFragmentManager());
            dialog.dismiss(false);
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put("login_name", name);
        param.put("password", word);
        param.put("source", "20");

        loginButton.setClickable(false);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);

                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    final JSONObject data = object.getJSONObject("data");
                    if (data.getIntValue("code") == 0) {
                        final Intent intent;
                        UserUtils.saveUserInfo(data);

                        //建立数据库
                        //LitePalDBHelper.getInstance().createSQLite(UserUtils.getUserId());

                        //存在这个账号则直奔MainActivity
                        if (UserUtils.hadLogin(data.getString("account_id"))) {
                            intent = new Intent(TypeLoginActivity.this, MainActivity.class);
                        } else {
                            intent = new Intent(TypeLoginActivity.this, EditPersonInfoActivity.class);
                            UserUtils.saveLoginHistory(data.getString("account_id"));
                        }

                        intent.putExtra("isFromLogin", true);
                        //登录IM
                        try {
                            AVIMClientManager.getInstance().open(data.getString("account_id"), new AVIMClientCallback() {

                                @Override
                                public void done(AVIMClient avimClient, AVIMException e) {

                                    if (e == null) {
                                        KLog.e("IM登录成功");

                                        LaunchRequest.init();//初始化缓存数据

                                        startActivity(intent);
                                        PushService.subscribe(TypeLoginActivity.this, data.getString("account_id"), MainActivity.class);
                                        finish();
                                    } else {
                                        KLog.e(e.toString());
                                        UIHelper.toast(getActivity(), "即时通讯登录失败");
                                    }
                                    loginButton.setClickable(true);
                                    loginDialog.dismiss(true);
                                }
                            });


                        } catch (Exception ignored) {
                            loginButton.setClickable(true);
                            loginDialog.dismiss(true);
                            WaitDialog errorDialog = WaitDialog.getInstance(ignored.getMessage(),
                                    R.mipmap.login_error, false);
                            errorDialog.show(getSupportFragmentManager());
                            errorDialog.dismiss(false);
                        }
                    } else {
                        loginButton.setClickable(true);
                        loginDialog.dismiss(true);
                        WaitDialog errorDialog = WaitDialog.getInstance(
                                data.getIntValue("code") == 2 ? "账号或者密码有误" : data.getString("message"),
                                R.mipmap.login_error, false);
                        errorDialog.show(getSupportFragmentManager());
                        errorDialog.dismiss(false);

                    }
                } else {
                    loginButton.setClickable(true);
                    loginDialog.dismiss(true);
                    WaitDialog errorDialog = WaitDialog.getInstance(
                            "登录异常",
                            R.mipmap.login_error, false);
                    errorDialog.show(getSupportFragmentManager());
                    errorDialog.dismiss(false);
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.e(msg);

                loginDialog.dismiss(true);
                loginButton.setClickable(true);
                WaitDialog errorDialog = WaitDialog.getInstance("登录出错",
                        R.mipmap.login_without_net, false);
                errorDialog.show(getSupportFragmentManager());
                errorDialog.dismiss(false);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_USER_LOGIN, ggHttpInterface).startGet();
    }

}
