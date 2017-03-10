package cn.gogoal.im.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.openServices.IOpenCallback;
import cn.gogoal.im.common.openServices.OpenServiceFactory;
import com.socks.library.KLog;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

// 实现IWXAPIEventHandler 接口，以便于微信事件处理的回调

public class WXEntryActivity extends Activity{

    private static final String WEIXIN_ACCESS_TOKEN_KEY = "wx_access_token_key";
    private static final String WEIXIN_OPENID_KEY = "wx_openid_key";
    private static final String WEIXIN_REFRESH_TOKEN_KEY = "wx_refresh_token_key";

    private IOpenCallback wechatCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            wechatCallback= OpenServiceFactory.with(getContext()).wechat().getCallback();

            //用户同意
            String code = resp.code;
            String state = resp.state;
            // 如果不是登录
            if (!"wechat_login".equals(state)) {
                finish();
            } else {
                //上面的code就是接入指南里要拿到的code
                checkAccessToken(code);//先检查用户access_token，是否存在，是否有效
//                getAccessTokenAndOpenId(code);
            }

        } else {
            finish();
        }
    }

    // 从手机本地获取存储的授权口令信息，判断是否存在access_token，不存在请求获取，存在就判断是否过期
    private void checkAccessToken(String code) {
        String accessToken = SPTools.getString(WEIXIN_ACCESS_TOKEN_KEY, null);
        String openid = SPTools.getString(WEIXIN_OPENID_KEY, null);
        if (!TextUtils.isEmpty(accessToken)) {//存在access_token
            // 判断是否过期有效
            isExpireAccessToken(accessToken, openid);
        } else {// 没有access_token
            //获取
            getAccessTokenAndOpenId(code);
        }
    }

    //    判断access_token是否过期有效
    private void isExpireAccessToken(final String accessToken, final String openid) {
        String url = String.format(AppConst.WEXIN_IS_EXPIRE_ACCESS_TOKEN, accessToken, openid);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        KLog.e("鉴权失败：" + e.getMessage());
                        WXEntryActivity.this.finish();
                    }

                    @Override
                    public void onResponse(String response) {
//                        KLog.e(response);
                        if (JSONObject.parseObject(response).getString("errmsg").equals("ok")) {
                            // 说明 accessToken没有过期，获取用户信息
                            getUserInfo(accessToken, openid);
//                            KLog.e("accessToken有效");
                        } else {
                            // 过期了，使用refresh_token来刷新accesstoken
                            KLog.e("accessToken无效");
                            refreshAccessToken();
                        }
                    }
                });
    }

    private void refreshAccessToken() {
// 从本地获取以存储的refresh_token
        final String refreshToken = SPTools.getString(WEIXIN_REFRESH_TOKEN_KEY, "");
        if (TextUtils.isEmpty(refreshToken)) {
            return;
        }
        // 拼装刷新access_token的url请求地址
        String url = String.format(AppConst.REFRESH_TOKEN_URL, AppConst.WEIXIN_APP_ID, refreshToken);
        // 请求执行
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        KLog.e("错误信息" + e.getMessage());
                        WXEntryActivity.this.finish();
                    }

                    @Override
                    public void onResponse(String response) {
                        KLog.e("refreshAccessToken: " + response);
                        // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                        processGetAccessTokenResult(response);
                    }
                });
    }

    // 判断是否获取成功，成功则去获取用户信息，否则提示失败
    private void processGetAccessTokenResult(String response) {
        // 验证获取授权口令返回的信息是否成功
        if (response.contains("errcode")) {
            // 使用fastjson解析返回的授权口令信息
            String access_token = JSONObject.parseObject(response).getString("access_token");
            String openid = JSONObject.parseObject(response).getString("openid");
            // 保存信息到手机本地
            saveAccessInfotoLocation(access_token,openid);
            // 获取用户信息
            getUserInfo(access_token,openid);
        } else {
            // 授权口令获取失败，解析返回错误信息
//            // 提示错误信息
            try {
                Toast.makeText(this,JSONObject.parseObject(response).getString("errmsg"), Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(this,"鉴权出错！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**将刷新的access_token存储*/
    private void saveAccessInfotoLocation(String access_token, String openid) {
        SPTools.saveString(WEIXIN_ACCESS_TOKEN_KEY, access_token);
        SPTools.saveString(WEIXIN_OPENID_KEY, openid);
    }

    // 使用code获取微信的access_token和openid
    private void getAccessTokenAndOpenId(String code) {
        String tokenUrl = String.format(AppConst.WEXIN_GET_ACCESS_TOKEN_URL, AppConst.WEIXIN_APP_ID, AppConst.WEIXIN_APP_SECRET, code);

        if (!HasInternet()) return;

        GetBuilder builder = OkHttpUtils.get();
        builder.url(tokenUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                UIHelper.toast(getContext(), "GET ACCESS TOKENANDOPENID ERROR::" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                //{"access_token":"PQrnZblMXucR5YvyOI2tPJn7wcl4MA2FjnY2u6o3-BbYIbCo8Scj6KCqNda8MLLoE4ISJ8OzniEX5ylEHW2SAm1-5ghX-4GJX3st1Pi0CYc",
                // "expires_in":7200,
                // "refresh_token":"9zWxjda0plg8JoM6CYiYYOaH90si5tN4AF9Ovs6pF20m8SHlRxESRwWuP_QLocMnHKP5Y1PYNQUtk9Gf_j017fH4EUhes8DG-zS4mNE3R5E",
                // "openid":"oxmyNv4YfkwdzSYAlbrBBZmBoOHQ","scope":"snsapi_userinfo","unionid":"o4ZYIwU0Gq0DDGmHxgU76-DHsjds"}
                KLog.e(response);
                String access_token=JSONObject.parseObject(response).getString("access_token");
                String openid=JSONObject.parseObject(response).getString("openid");
                saveAccessInfotoLocation(access_token,openid);
                getUserInfo(access_token,openid);
            }
        });
    }

    /**获取到微信用户信息*/
    private void getUserInfo(String accessToken ,String openid){
        String url = String.format(AppConst.WEXIN_GET_USER_INFO, accessToken, openid);

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        KLog.e(e.getMessage());
                        WXEntryActivity.this.finish();
                    }

                    @Override
                    public void onResponse(String response) {
//                        KLog.e("微信用户信息: " + response);
                        if (wechatCallback!=null){
                            wechatCallback.onSuccess(response);
                        }
                        finish();
                    }
                });
    }

    private boolean HasInternet() {
        if (!AppDevice.isNetworkConnected(getContext())) {
            UIHelper.toast(getContext(), "网络不可用");
            return false;
        }
        return true;
    }

    private WXEntryActivity getContext() {
        return WXEntryActivity.this;
    }
}