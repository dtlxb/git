package com.gogoal.app.common.GGOKHTTP;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * 数据请求地址及方式
 */
public class GGOKHTTP {

//                                    ////////////
//-----------------------------------//DATA-API//----------------------------------------------
//                                  ////////////

    /**
     * 举例：
     * public static final String AD_LISR = "v1/ad/list";
     */


    //------------------------------------------------------------------------------------------


    /**
     * 判断注册时手机号是否可用
     */
    public static final String USER_CAN_REG = "v1/user/can_reg";
    /**
     * 登录
     */
    public static final String USER_LOGIN = "v1/user/get_user_by_account";

    /**
     * 发送验证码
     * */
    public static final String SEND_CAPTCHA="v1/base/send_sms";

    /**
     * 校验验证码
     * */
    public static final String VALID_SMS="v1/base/valid_sms";

    /**
     * 注册
     * */
    public static final String REGISTER_NEW="v1/user/register_new";
    /**
     * 三方登录
     * */
    public static final String USER_THIRDPARTYLOGIN="v1/user/thirdpartylogin";

    /**
     * 老用户激活——获取激活码
     * */
    public static final String USER_SEND_ACTIVE_CODE="v1/user/send_active_code";

    /**
     * 老用户激活——激活
     * */
    public static final String USER_DO_ACTIVE="v1/user/do_active";

    /**
     * 重置密码
     * */
    public static final String USER_RESET_PASSWORD_NEW="v1/user/reset_password_new";

    /**
     * 自动登录 刷新token
     * */
    public static final String USER_QUICK_LOGIN="v1/user/quick_login";

    /**
     * 获取客服电话
     * */
    public static final String GET_BASE_CONFIGURE="v1/systemset/get_base_configure";

    /**
     * 校验账号
     * */
    public static final String CHECK_ACCOUNT="v1/user/check_account";

    /**
     * 城市列表
     * */
    public static final String GET_CITY_DATA="v1/base/get_city_data";



//--------------------------------------------------------------------------------------------------

    /**
     * 设置超时时间
     */
    private GGHttpInterface httpInterface;
    private Map<String, String> param;
    private String url;

    public GGOKHTTP(Map<String, String> param, String url) {
        this.param = param;
        this.url = url;
    }

    public GGOKHTTP(Map<String, String> param, String url, GGHttpInterface httpInterface) {
        this.param = param;
        this.httpInterface = httpInterface;
        this.url = url;
    }

    public void startGet() {
        try {
            OkHttpUtils
                    .get()
                    .url(GGAPI.get(url, param))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            if (httpInterface != null) httpInterface.onFailure(e.toString());
                        }

                        @Override
                        public void onResponse(String response) {
                            if (httpInterface != null) httpInterface.onSuccess(response);
//                            try {
//                                KLog.e(GGAPI.get(url, param));
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            if (httpInterface != null) httpInterface.onFailure(e.toString());
        }
    }

    public void startPost() {
        try {
            Map<String, Object> posturl;
            Map<String, String> params;
            posturl = GGAPI.post(url, param);
            params = (Map<String, String>) posturl.get("params");
            OkHttpUtils
                    .post()
                    .url(posturl.get("url").toString())
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            if (httpInterface != null) httpInterface.onFailure(e.toString());
                        }

                        @Override
                        public void onResponse(String response) {
                            if (httpInterface != null) httpInterface.onSuccess(response);
                        }
                    });
        } catch (Exception e) {
            if (httpInterface != null) httpInterface.onFailure(e.toString());
        }
    }

    public interface GGHttpInterface {

        void onSuccess(String responseInfo);

        void onFailure(String msg);

    }
}
