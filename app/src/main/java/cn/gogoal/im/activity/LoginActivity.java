package cn.gogoal.im.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;

/*
* 登录页
* */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.loginUserName)
    EditText loginUserName;

    @BindView(R.id.loginPassWord)
    EditText loginPassWord;

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void doBusiness(Context mContext) {
        loginUserName.setText("e00002638");
        loginPassWord.setText("123456");
    }

    @OnClick({R.id.login})
    public void setLogin(View view) {
        switch (view.getId()) {
            case R.id.login:
                Login();
                break;
        }
    }

    private void Login() {

        String name = loginUserName.getText().toString();
        String word = loginPassWord.getText().toString();

        if (TextUtils.isEmpty(word) || TextUtils.isEmpty(name)) {
            UIHelper.toast(LoginActivity.this, "用户名或密码不能为空");
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
                    SPTools.saveJsonObject("userInfo", object.getJSONObject("data"));
                    startActivity(new Intent(getContext(), MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_USER_LOGIN, ggHttpInterface).startGet();
    }

    private LoginActivity getContext() {
        return LoginActivity.this;
    }
}
