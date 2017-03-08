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
     * 三方登录
     * */
    public static final String USER_THIRDPARTYLOGIN="v1/user/thirdpartylogin";

    /*
    * 直播详情
    * */
    public static final String GET_STUDIO_LIST="v1/video_studio/get_studio_list";

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
