package cn.gogoal.im.common.GGOKHTTP;

import com.alibaba.fastjson.JSONObject;
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
     * 获取自选股列表
     * params token 用户令牌
     * params stock_codes 通过股票代码获取股票信息
     */
    public static final String GET_MYSTOCKS = "v1/mystock/list";

    /**
     * 自选股编辑
     * params token 用户令牌
     * params clear 是否全部删除
     * params stock_codes 操作股票代码参数
     */
    public static final String RESET_MYSTOCKS = "v1/mystock/reset";

    /*
    获取大盘数据
     * param.put("fullcode", "sh000001;sz399001;sh000300;sz399006");
     * param.put("category_type", "1");
     * */
    public static final String APP_HQ_INFORMATION = "v1/hq/get_app_hq";

    /*
     * 跑马灯 和 轮播图
     * param.put("product", "4");跑马灯
     *
     * 不传参为轮播图
     * */
    public static final String BANNER_LIST = "v1/ad/new_list";

    /**
     * 三方登录
     */
    public static final String USER_THIRDPARTYLOGIN = "v1/user/thirdpartylogin";

    /*
    * 直播详情
    * live_id
    * */
    public static final String GET_STUDIO_LIST = "v1/video_studio/get_studio_list";

    /*
    * 录播详情
    * video_id
    * */
    public static final String GET_RECORD_LIST = "v1/video_studio/get_record_list";

    /*
    * 相关视频
    * video_id
    * video_type
    * rows
    * */
    public static final String GET_RELATED_VIDEO = "v1/video_studio/get_related_video";

    /**
     * 登录
     * 账号
     * 密码
     * source=20
     */
    public static final String GET_USER_LOGIN = "v1/user/login";

    /**
     * 嘉宾获取直播推流地址
     * token
     * video_id
     */
    public static final String GET_PUSH_STREAM = "v1/video_studio/get_push_stream";

    /**
     * 获取在线人数
     * conv_id
     */
    public static final String GET_ONLINE_COUNT = "v1/ggm_chat/get_online_count";

    /**
     * 好友列表
     */
    public static final String GET_FRIEND_LIST = "v1/ggm_im/get_friend_list";

    /**
     * 城市列表
     */
    public static final String GET_CITY_DATA = "v1/base/get_city_data";

    /**
     * IM发送消息
     * token                        用户token
     * message                      消息内容（包括文字，语音，图片...）
     * conv_id                      对话ID
     */
    public static final String CHAT_SEND_MESSAGE = "v1/ggm_chat/send_message";

    /**
     * IM发送接收好友
     * token                        用户token
     * friend_id                    朋友ID
     * text                         消息内容（包括文字，语音，图片...）
     */
    public static final String ADD_FRIEND = "v1/ggm_im/add_friend";

    /**
     * IM发送接收好友
     * token                        用户token
     * id_list                      朋友ID
     */
    public static final String CREATE_GROUP_CHAT = "v1/ggm_chat/create_group_chat";

    /**
     * 本地没有群员缓存时拉取
     * token                        用户token
     * id_list                      朋友ID
     * conv_id                      会话ID
     */
    public static final String GET_MEMBER_INFO = "v1/ggm_chat/get_member_info";

    /**
     * IM群踢人
     * token                         用户token
     * id_list                       朋友ID
     * conv_id                       会话id
     */
    public static final String DELETE_MEMBER = "v1/ggm_chat/delete_member";

    /**
     * IM群加人
     * token                         用户token
     * id_list                       朋友ID
     * conv_id                       会话id
     */
    public static final String ADD_MEMBER = "v1/ggm_chat/add_member";

    /**
     * IM群主同意加群
     * token                         用户token
     * conv_id                       会话id
     */
    public static final String APPLY_INTO_GROUP = "v1/ggm_chat/apply_into_group";

    /**
     * IM获取群列表
     * token                         用户token
     */
    public static final String GET_GROUP_LIST = "v1/ggm_chat/get_group_list";

    /**
     * IM取消收藏群
     * token                         用户token
     * conv_id                       会话id
     */
    public static final String CANCEL_COLLECT_GROUP = "v1/ggm_chat/cancel_collect_group";

    /**
     * IM收藏群
     * token                         用户token
     * conv_id                       会话id
     */
    public static final String COLLECT_GROUP = "v1/ggm_chat/collect_group";

    /**
     * token                         用户token
     * keyword                       搜索关键字
     */
    public static final String SEARCH_FRIEND = "v1/ggm_im/search_friend";
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
        this.url = url.trim().replace(" ","");
    }

    public void startGet() {
        try {
            OkHttpUtils.get()
                    .url(GGAPI.get(url, param))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (httpInterface != null) httpInterface.onFailure(e.toString());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (httpInterface != null) httpInterface.onSuccess(response);
                        }
                    });
//            OkHttpUtils
//                    .get()
//                    .url(GGAPI.get(url, param))
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e) {
//                            if (httpInterface != null) httpInterface.onFailure(e.toString());
//                        }
//
//                        @Override
//                        public void onResponse(String response) {
//                            if (httpInterface != null) httpInterface.onSuccess(response);
////                            try {
////                                KLog.e(GGAPI.get(url, param));
////                            } catch (Exception e) {
////                                e.printStackTrace();
////                            }
//                        }
//                    });
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

            OkHttpUtils.post()
                    .url(posturl.get("url").toString())
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (httpInterface != null) httpInterface.onFailure(e.toString());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (httpInterface != null) httpInterface.onSuccess(response);
                        }
                    });

//            OkHttpUtils
//                    .post()
//                    .url(posturl.get("url").toString())
//                    .params(params)
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e) {
//                            if (httpInterface != null) httpInterface.onFailure(e.toString());
//                        }
//
//                        @Override
//                        public void onResponse(String response) {
//                            if (httpInterface != null) httpInterface.onSuccess(response);
//                        }
//                    });

        } catch (Exception e) {
            if (httpInterface != null) httpInterface.onFailure(e.toString());
        }
    }

    public static String getMessage(String responseInfo) {
        return JSONObject.parseObject(responseInfo).getString("messaage");
    }

    public interface GGHttpInterface {

        void onSuccess(String responseInfo);

        void onFailure(String msg);

    }
}
