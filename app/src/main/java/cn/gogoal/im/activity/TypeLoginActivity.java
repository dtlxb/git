package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
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
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.KeyboardLaunchRelativeLayout;
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

    @BindView(R.id.tv_login_status)
    TextView tvLoginStatus;

    @BindView(R.id.login_loading)
    ImageView loginLoading;

    @BindView(R.id.layout_loading)
    FrameLayout layoutLoading;

    @BindView(R.id.login_button)
    SelectorButton loginButton;

    @BindView(R.id.login_keyboard_layout)
    KeyboardLaunchRelativeLayout keyboardLayout;

    private RotateAnimation animation;


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

//        loginUserName.setText("E00003645");
//        loginPassWord.setText("147258369");

        loginUserName.setText("E00002639");
        loginPassWord.setText("412174");

        /*loginUserName.setText("E00020181");
        loginPassWord.setText("394495");*/

        /*loginUserName.setText("E00002638");
        loginPassWord.setText("123456");*/

        /*loginUserName.setText("E010399");
        loginPassWord.setText("198122");*/

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

        new AlertDialog.Builder(getActivity()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        xTitle = setMyTitle(R.string.str_login_in, false);
        //添加action
        XTitle.TextAction rigisterAction = new XTitle.TextAction("注册") {
            @Override
            public void actionClick(View view) {
                Intent intent = new Intent(getActivity(), RigisterActivity.class);
                intent.putExtra("action_type", AppConst.LOGIN_RIGIST_NUMBER);
                startActivity(intent);
            }
        };
        xTitle.addAction(rigisterAction, 0);
        TextView rigisterView = (TextView) xTitle.getViewByAction(rigisterAction);
        rigisterView.setTextColor(getResColor(R.color.colorPrimary));

        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(3000);
        animation.setFillAfter(true);
        animation.setStartOffset(0);
        animation.setInterpolator(new LinearInterpolator());

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
        String name = loginUserName.getText().toString().toUpperCase(Locale.ENGLISH);
        String word = loginPassWord.getText().toString();

        if (TextUtils.isEmpty(word) || TextUtils.isEmpty(name)) {
            UIHelper.toast(TypeLoginActivity.this, R.string.str_login_edit_null);
            return;
        }

        if (!UIHelper.isGGPassWord(word, getActivity()))
            return;

        Map<String, String> param = new HashMap<>();
        param.put("login_name", name);
        param.put("password", word);
        param.put("source", "20");

        loginButton.setClickable(false);
        layoutLoading.setVisibility(View.VISIBLE);
        animation.startNow();
        loginLoading.setAnimation(animation);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.getIntValue("code") == 0) {
                        final Intent intent;
                        SPTools.saveJsonObject("userInfo", data);
                        //存在这个账号则直奔MainActivity
                        if (UserUtils.isFirstLogin(data.getInteger("account_id"))) {
                            intent = new Intent(TypeLoginActivity.this, MainActivity.class);
                        } else {
                            intent = new Intent(TypeLoginActivity.this, EditPersonInfoActivity.class);
                            SPTools.saveInt(data.getInteger("account_id") + "_saved_account", data.getInteger("account_id"));
                        }
                        intent.putExtra("isFromLogin", true);
                        //登录IM
                        try {
                            AVImClientManager.getInstance().open(data.getString("account_id"), new AVIMClientCallback() {
                                @Override
                                public void done(AVIMClient avimClient, AVIMException e) {
                                    loginButton.setClickable(true);
                                    tvLoginStatus.setText(R.string.str_login_success);
                                    animation.cancel();
                                    loginLoading.clearAnimation();
                                    loginLoading.setBackgroundResource(R.mipmap.login_success);

                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } catch (Exception ignored) {
                            loginButton.setClickable(true);
                            layoutLoading.setVisibility(View.GONE);
                            getWindoDialog(R.mipmap.login_error, R.string.str_login_code_error);
                        }
                    } else {
                        loginButton.setClickable(true);
                        layoutLoading.setVisibility(View.GONE);
                        getWindoDialog(R.mipmap.login_error, R.string.str_login_code_error);
                    }
                } else {
                    loginButton.setClickable(true);
                    layoutLoading.setVisibility(View.GONE);
                    getWindoDialog(R.mipmap.login_error, R.string.str_login_code_error);
                }
            }

            @Override
            public void onFailure(String msg) {
                loginButton.setClickable(true);
                layoutLoading.setVisibility(View.GONE);
                getWindoDialog(R.mipmap.login_without_net, R.string.str_login_no_net);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_USER_LOGIN, ggHttpInterface).startGet();
    }

    public void getWindoDialog(int dialogRes, int dialogText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog_style);

        View dialogView = getLayoutInflater().inflate(R.layout.view_login_dialog, null);
        ImageView dialogIv = (ImageView) dialogView.findViewById(R.id.login_loading);
        TextView dialogTv = (TextView) dialogView.findViewById(R.id.tv_login_status);
        dialogIv.setBackgroundResource(dialogRes);
        dialogTv.setText(dialogText);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = AppDevice.dp2px(getActivity(), 120);
            lp.height = AppDevice.dp2px(getActivity(), 120);
            window.setAttributes(lp);
            window.setContentView(dialogView);
        }
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
