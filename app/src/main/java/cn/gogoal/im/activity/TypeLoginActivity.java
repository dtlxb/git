package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
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
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.KeyboardLaunchRelativeLayout;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.view.SelectorButton;
import cn.gogoal.im.ui.view.XEditText;
import cn.gogoal.im.ui.view.XTitle;

/**
 * Created by huangxx on 2017/4/17.
 */
public class TypeLoginActivity extends BaseActivity {

    private XTitle xTitle;

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
    KeyboardLaunchRelativeLayout keyboardLayout;

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

        initTitle();
        loginPassWord.setInputType(InputType.TYPE_CLASS_TEXT);
        UIHelper.passwordToggle(loginPassWord, chToggle);
        initLoginInfo();
    }

    private void initLoginInfo() {
//        loginUserName.setText("E00018282");
//        loginPassWord.setText("ycy921150");

//        loginUserName.setText("13166270509");
//        loginPassWord.setText("888888");

//        loginUserName.setText("E00003645");
//        loginPassWord.setText("258");

//        loginUserName.setText("E00018279");冷
//        loginPassWord.setText("600255");

        loginUserName.setText("E00002639");
        loginPassWord.setText("412174");

//        loginUserName.setText("E00020181");
//        loginPassWord.setText("394495");

//        loginUserName.setText("E00002638");
//        loginPassWord.setText("123456");

//        loginUserName.setText("E010399");
//        loginPassWord.setText("198122");

        loginUserName.setSelection(loginUserName.getText().length());

        //保存键盘高度
        keyboardLayout.setOnKeyboardChangeListener(new KeyboardLaunchRelativeLayout.OnKeyboardChangeListener() {
            @Override
            public void OnKeyboardPop(int height) {
                SPTools.saveInt("soft_keybord_height", height);
            }

            @Override
            public void OnKeyboardClose() {

            }
        });
    }

    private void initTitle() {

        xTitle = setMyTitle(R.string.str_login_in, false);
        //添加action
        XTitle.TextAction registerAction = new XTitle.TextAction("注册") {
            @Override
            public void actionClick(View view) {
                Intent intent = new Intent(getActivity(), RigisterActivity.class);
                intent.putExtra("action_type", AppConst.LOGIN_RIGIST_NUMBER);
                startActivity(intent);
            }
        };
        xTitle.addAction(registerAction, 0);
        TextView registerView = (TextView) xTitle.getViewByAction(registerAction);
        registerView.setTextColor(getResColor(R.color.colorPrimary));

    }

    @OnClick({R.id.login_button, R.id.forget_code})
    public void setLogin(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                Login();
                break;
            case R.id.forget_code:
                Intent intent = new Intent(getActivity(), RigisterActivity.class);
                intent.putExtra("action_type", AppConst.LOGIN_FIND_CODE);
                startActivityForResult(intent, AppConst.LOGIN_FIND_CODE);
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

                                        //缓存我的群组
                                        UserUtils.getMyGroupList(null);

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
                        WaitDialog errorDialog = WaitDialog.getInstance(data.getString("message"),
                                R.mipmap.login_error, false);
                        errorDialog.show(getSupportFragmentManager());
                        errorDialog.dismiss(false);

                    }
                } else {
                    loginButton.setClickable(true);
                    loginDialog.dismiss(true);
                    WaitDialog errorDialog = WaitDialog.getInstance(
                            object.getString("message"),
                            R.mipmap.login_error, false);
                    errorDialog.show(getSupportFragmentManager());
                    errorDialog.dismiss(false);
                }
            }

            @Override
            public void onFailure(String msg) {
                loginDialog.dismiss(true);
                loginButton.setClickable(true);
                WaitDialog errorDialog = WaitDialog.getInstance(getString(R.string.str_login_no_net),
                        R.mipmap.login_without_net, false);
                errorDialog.show(getSupportFragmentManager());
                errorDialog.dismiss(false);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_USER_LOGIN, ggHttpInterface).startGet();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            if (requestCode == AppConst.LOGIN_FIND_CODE) {
                loginPassWord.setText("");
                loginUserName.requestFocus();
            }
        }
    }
}
