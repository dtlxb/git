package cn.gogoal.im.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.AVImClientManager;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.KeyboardLaunchListenLayout;
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

    @BindView(R.id.login_keyboard_layout)
    KeyboardLaunchListenLayout keyboardLayout;

    @Override
    public int bindLayout() {
        return R.layout.activity_type_login;
    }

    @Override
    public void doBusiness(Context mContext) {
        initTitle();
        loginPassWord.setInputType(InputType.TYPE_CLASS_TEXT);
        UIHelper.passwordToggle(loginPassWord,chToggle);
        initLoginInfo();
    }

    private void initLoginInfo() {
//        loginUserName.setText("E00020190");
//        loginPassWord.setText("955202");
        /*loginUserName.setText("E00020190");
        loginPassWord.setText("955202");*/

//        loginUserName.setText("E00003645");
//        loginPassWord.setText("147258369");

        /*loginUserName.setText("E00002639");
        loginPassWord.setEditTextText("412174");*/

        /*loginUserName.setText("E00003645");
        loginPassWord.setEditTextText("147258369");*/
        loginUserName.setText("E00003645");
        loginPassWord.setText("147258369");

        /*loginUserName.setText("E00002639");
        loginPassWord.setText("412174");*/

       /* loginUserName.setText("E00002638");
        loginPassWord.setText("123456");*/

        /*loginUserName.setText("E010399");
        loginPassWord.setText("198122");*/

        loginUserName.setSelection(loginUserName.getText().length());

        //保存键盘高度
        keyboardLayout.setOnKeyboardChangeListener(new KeyboardLaunchListenLayout.OnKeyboardChangeListener() {
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
        String name = loginUserName.getText().toString();
        String word = loginPassWord.getText().toString();

        if (TextUtils.isEmpty(word) || TextUtils.isEmpty(name)) {
            UIHelper.toast(TypeLoginActivity.this, R.string.str_login_edit_null);
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put("login_name", name);
        param.put("password", word);
        param.put("source", "20");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.getIntValue("code") == 0) {
                        Intent intent;
                        SPTools.saveJsonObject("userInfo", data);
                        if (UserUtils.isFirstLogin()) {
                            intent = new Intent(TypeLoginActivity.this, MainActivity.class);
                        } else {
                            intent = new Intent(TypeLoginActivity.this, EditPersonInfoActivity.class);
                            SPTools.saveBoolean("isFirstLogin", true);
                        }
                        intent.putExtra("isFromLogin", true);
                        startActivity(intent);
                        finish();

                        try {
                            //测试代码(登录IM)
                            AVImClientManager.getInstance().open(data.getString("account_id"), new AVIMClientCallback() {
                                @Override
                                public void done(AVIMClient avimClient, AVIMException e) {
                                }
                            });
                        }catch (Exception e){
                            //TODO crash,自己改
                        }

                    } else {
                        UIHelper.toast(TypeLoginActivity.this, R.string.str_login_error);
                    }
                } else {
                    UIHelper.toast(TypeLoginActivity.this, R.string.str_login_error);
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.json(msg);
                UIHelper.toast(TypeLoginActivity.this, R.string.net_erro_hint);
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
