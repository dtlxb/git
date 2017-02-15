package com.gogoal.app.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gogoal.app.activity.MainActivity;
import com.gogoal.app.base.AppManager;
import com.gogoal.app.base.MyApp;
import com.gogoal.app.common.AppConst;
import com.gogoal.app.common.AppDevice;
import com.gogoal.app.common.GGOKHTTP.GGOKHTTP;
import com.gogoal.app.common.SPTools;
import com.gogoal.app.common.UIHelper;
import com.gogoal.app.wxapi.bean.WXAccessTokenInfo;
import com.gogoal.app.wxapi.bean.WXErrorInfo;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// 实现IWXAPIEventHandler 接口，以便于微信事件处理的回调
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String WEIXIN_ACCESS_TOKEN_KEY = "wx_access_token_key";
    private static final String WEIXIN_OPENID_KEY = "wx_openid_key";
    private static final String WEIXIN_REFRESH_TOKEN_KEY = "wx_refresh_token_key";

    public static int TYPE_WEIXIN_LOGIN = 10086;//微信登录

    public static int TYPE_WEIXIN_BINDING = 10010;//绑定微信

    private static int wxType=TYPE_WEIXIN_LOGIN;

    public static void setWxType(int type) {
        wxType = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 微信事件回调接口注册
        MyApp.sApi.handleIntent(getIntent(), this);
    }

    /**
     * 微信组件注册初始化
     *
     * @param context       上下文
     * @param weixin_app_id appid
     * @return 微信组件api对象
     */
    public static IWXAPI initWeiXin(Context context, @NonNull String weixin_app_id) {
        if (TextUtils.isEmpty(weixin_app_id)) {
            UIHelper.toast(context, "app_id 不能为空");
        }
        IWXAPI api = WXAPIFactory.createWXAPI(context, weixin_app_id, true);
        api.registerApp(weixin_app_id);
        return api;
    }

    /**
     * 登录微信
     *
     * @param api 微信服务api
     */
    public static void loginWeixin(Context context, IWXAPI api) {
        // 判断是否安装了微信客户端
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context.getApplicationContext(), "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
            return;
        }
        // 发送授权登录信息，来获取code
        SendAuth.Req req = new SendAuth.Req();

        // 应用的作用域，获取个人信息
        req.scope = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方
         * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验
         */
        req.state = "app_wechat";
        api.sendReq(req);
    }


    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            // 发送成功
            case BaseResp.ErrCode.ERR_OK:
                // 获取code
                String code = ((SendAuth.Resp) resp).code;
                // 通过code获取授权口令access_token
                getAccessToken(code);
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                KLog.e("认证被否决");
                finish();
                break;
            case BaseResp.ErrCode.ERR_COMM:
                KLog.e("一般错误");
                finish();
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                KLog.e("发送失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                KLog.e("不支持错误");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                KLog.e("用户取消");
                finish();
                break;
        }
    }

    /**
     * 获取授权口令
     */
    private void getAccessToken(String code) {
        String tokenUrl = String.format(AppConst.WEXIN_TAOKE_ACCESS_TOKEN_URL, AppConst.WEIXIN_APP_ID, AppConst.WEIXIN_APP_SECRET, code);
        OkHttpUtils
                .get()
                .url(tokenUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        KLog.e("鉴权失败：" + e.getMessage());

                        WXEntryActivity.this.finish();
                    }

                    @Override
                    public void onResponse(String response) {
                        // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                        processGetAccessTokenResult(response);
                        KLog.e(response);
                    }
                });
    }

    /**
     * 处理获取的授权信息结果
     *
     * @param response 授权信息结果
     */
    private void processGetAccessTokenResult(String response) {
        KLog.e(response);
        // 验证获取授权口令返回的信息是否成功
        if (validateSuccess(response)) {
            // 使用fastjson解析返回的授权口令信息
            WXAccessTokenInfo tokenInfo = JSONObject.parseObject(response, WXAccessTokenInfo.class);
            KLog.e(tokenInfo.toString());
            // 保存信息到手机本地
            saveAccessInfotoLocation(tokenInfo);
            // 获取用户信息
            getUserInfo(tokenInfo.getAccess_token(), tokenInfo.getOpenid());
        } else {
            // 授权口令获取失败，解析返回错误信息
            WXErrorInfo wxErrorInfo = JSONObject.parseObject(response, WXErrorInfo.class);
            KLog.e(wxErrorInfo.toString());
            // 提示错误信息
            KLog.e("错误信息: " + wxErrorInfo.getErrmsg());
        }
    }

    //保存tokenInfo
    private void saveAccessInfotoLocation(WXAccessTokenInfo tokenInfo) {
        SPTools.saveString(WEIXIN_ACCESS_TOKEN_KEY, tokenInfo.getAccess_token());
        SPTools.saveString(WEIXIN_OPENID_KEY, tokenInfo.getOpenid());
    }

    // 从手机本地获取存储的授权口令信息，判断是否存在access_token，不存在请求获取，存在就判断是否过期
    private void checkAccessToken(String code) {
        String accessToken = SPTools.getString(WEIXIN_ACCESS_TOKEN_KEY, "none");
        String openid = SPTools.getString(WEIXIN_OPENID_KEY, "");
        if (!"none".equals(accessToken)) {
            // 有access_token，判断是否过期有效
            isExpireAccessToken(accessToken, openid);
        } else {
            // 没有access_token
            getAccessToken(code);
        }
    }

    //    判断accesstoken是过期
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
                        KLog.e(response);
                        if (validateSuccess(response)) {
                            // accessToken没有过期，获取用户信息
                            getUserInfo(accessToken, openid);
                        } else {
                            // 过期了，使用refresh_token来刷新accesstoken
                            refreshAccessToken();
                        }
                    }
                });
    }

    /**
     * 刷新获取新的access_token
     */
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
                        // 重新请求授权
                        loginWeixin(WXEntryActivity.this.getApplicationContext(), MyApp.sApi);
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

    // 获取用户信息
    private void getUserInfo(String access_token, String openid) {
        String url = String.format(AppConst.WEXIN_GET_USER_INFO, access_token, openid);

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        WXEntryActivity.this.finish();
                        UIHelper.toast(WXEntryActivity.this, "获取信息失败");
                    }

                    @Override
                    public void onResponse(String response) {
                        KLog.e("微信用户信息: " + response);

                        if (wxType==TYPE_WEIXIN_BINDING) {
                            AppManager.getInstance().sendMessage("bind_weiChat", response);
                            return;
                        }
                        registerUserInfo(response);
                    }
                });
    }

    private void registerUserInfo(final String response) {
//        data type source
        Map<String, String> param = new HashMap<>();
        param.put("data", response);
        param.put("type", String.valueOf(3));
        param.put("source", "app");
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e("服务器返回用户信息: " + responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    //说明网络请求成功
                    if (object.getJSONObject("data").getIntValue("code") == 101000) {
//
                        SPTools.saveJsonObject("userMessage", JSON.parseObject(responseInfo).getJSONObject("data").getJSONObject("user"));
                        startActivity(new Intent(WXEntryActivity.this, MainActivity.class));
                        finish();
                    }
                }else {
                    UIHelper.toast(WXEntryActivity.this,JSONObject.parseObject(responseInfo).getString("message"));
                    finish();
                }
            }

            @Override
            public void onFailure(String msg) {
                if (!AppDevice.isNetworkConnected(WXEntryActivity.this)) {
                    UIHelper.toast(WXEntryActivity.this, "网络不可用");
                } else {
                    UIHelper.toast(WXEntryActivity.this, "请求出错("+msg+")");
                }
            }
        };
        new GGOKHTTP(param, GGOKHTTP.USER_THIRDPARTYLOGIN, ggHttpInterface).startGet();
    }

    /**
     * 验证是否成功
     *
     * @param response 返回消息
     * @return 是否成功
     */
    private boolean validateSuccess(String response) {
        String errFlag = "errmsg";
        return (errFlag.contains(response) && !"ok".equals(response))
                || (!"errcode".contains(response) && !errFlag.contains(response));
    }


    //==================================网络通讯模块====================================

    /**
     * Api通信回调接口
     */
    public interface ApiCallback<T> {
        /**
         * 请求成功
         *
         * @param response 返回结果
         */
        void onSuccess(T response);

        /**
         * 请求出错
         *
         * @param errorCode 错误码
         * @param errorMsg  错误信息
         */
        void onError(int errorCode, String errorMsg);

        /**
         * 请求失败
         */
        void onFailure(IOException e);
    }

    private OkHttpClient mHttpClient = new OkHttpClient.Builder().build();
    private Handler mCallbackHandler = new Handler(Looper.getMainLooper());

    /**
     * 通过Okhttp与微信通信
     * * @param url 请求地址
     *
     * @throws Exception
     */
    public void httpRequest(String url, final ApiCallback<String> callback) {
        KLog.e("url: %s", url);
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callback != null) {
                    mCallbackHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 请求失败，主线程回调
                            callback.onFailure(e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (callback != null) {
                    if (!response.isSuccessful()) {
                        mCallbackHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // 请求出错，主线程回调
                                callback.onError(response.code(), response.message());
                            }
                        });
                    } else {
                        mCallbackHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // 请求成功，主线程返回请求结果
                                    callback.onSuccess(response.body().string());
                                } catch (final IOException e) {
                                    // 异常出错，主线程回调
                                    mCallbackHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onFailure(e);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }

}