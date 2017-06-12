package cn.gogoal.im.common.GGOKHTTP;

import android.util.Log;

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

    /*
    获取大盘数据
     * param.put("fullcode", "sh000001;sz399001;sh000300;sz399006");
     * param.put("category_type", "1");
     * */
    public static final String APP_HQ_INFORMATION = "v1/hq/get_app_hq";

    /*
     *获取大盘部分数据
     * param.put("fullcode", "sh000001;sz399001;sh000300;sz399006");
     * param.put("category_type", "1");
     * */
    public static final String APP_HQ_GET = "v1/hq/get";

    /*
       * 获取热门行业
       * params category_type 分类类型
       * */
    public static final String GET_HOT_INDUSTRY = "v1/industry/hot_industry_rank_list";

    /*
       * 指定 热门行业 的 股票列表
       * params category_type 分类类型
       * */
    public static final String GET_HOT_INDUSTRY_DETAIL_LIST = "v1/industry/hot_industry_detail_list";
    /**
     * 得到个股图表数据
     * params stock_code 股票代码
     * <p/>
     * (选填，默认为最近6个月)
     * params start_date 开始时间
     * params end_date 结束时间
     */
    public static final String GET_CHARTS_DATA = "v1/dm/get_charts_data";

    /**
     * 获取股票K线详情
     * params stock_code 股票代码
     * params Kline_type k线类型
     * avg_line_type 平均线类型
     * right_type 右边类型
     */
    public static final String STOCK_K_LINE = "v1/stock/kline";

    /*
  * 自选股的添加
  * params token=4967b285a82244d296b807a8fea9bc77&
  * params group_id=22&
  * params stock_code=600001&
  * params stock_class=333&
  * params source=54&
  * params group_class=1
  * */
    public static final String MYSTOCK_ADD = "v1/mystock/add";

    /*
  * 获取搜索框的筛选内容
  * params key
  * */
    public static final String GET_STOCKS = "v1/stock/get_stocks";

    /**
     * 获取五日分时数据
     * params stock_code 股票代码
     * params day 天数
     */
    public static final String STOCK_MINUTE = "v1/stock/minute";

    /*
  * 股票详情1
  * params fullcode=sh600519
  * */
    public static final String GET_HQ = "v1/hq/get";
    /*
    * 大盘详情
    * fullcode=sh000001
    * day=5
    * */
    public static final String GET_HQ_MINUTE = "v1/hq/index_minute";
    /*
    * 大盘K线
    * fullcode=sh000001
    * day=5
    * */
    public static final String GET_HQ_KLINE = "v1/hq/index_kline";

    /*
  *研报看点
  *params stock_code=600048&
  *params keyword=风险提示
  * */
    public static final String REPORT_RM = "v1/report/rm";

    /*
   * 获取[涨跌换振]行业
   * type 跌涨类型
   * channel 股票类型
   * */
    public static final String STOCK_RANK_LIST = "v1/stock/stock_rank_list";

    /**
     * 获取自选股列表
     * params token 用户令牌
     * params stock_codes 通过股票代码获取股票信息
     */
    public static final String GET_MYSTOCKS = "v1/mystock/mystock_list";

    /**
     * 自选股编辑
     * params token 用户令牌
     * params clear 是否全部删除
     * params stock_codes 操作股票代码参数
     */
    public static final String RESET_MYSTOCKS = "v1/mystock/reset";

    /**
     * 个股新闻、公告、投资者互动
     * <p>
     * params stock_code=002285&
     * params type=7(新闻)、3(公告)、9(看点);
     */
    public static final String GET_STOCK_NEWS = "v1/news/get_stock_news";

    /*
     *个股研报
     * params token=4aece3232ba149c7a0328faa8ec938aa&
     * params first_class=公司报告
     * */
    public static final String REPORT_LIST = "v1/report/list";

    /*
  * 股票详情1
  * params stock_code=600519
  * */
    public static final String ONE_STOCK_DETAIL = "v1/stock/data";

    /**
     * 获取指定日期之前的指定个交易日
     * 选填
     * date：结束日期 默认当天(yyyy-MM-dd)
     * count：获取几个交易日
     */
    public static final String GET_SOME_EXCHANGEDATE = "v1/stock/get_some_exchangedate";
    /**
     * 获取股票分时交易明细
     * params stock_code 股票代码
     * params limit  从现在开始limit分钟内的交易数据*
     */
    public static final String GET_STOCK_TIME_DETIAL = "v1/hq/get_transaction";

    /*
   * 获取个股一分钟情况
   * stock_code 股票代码
   * */
    public static final String DM_GET_IMG = "v1/dm/get_img";
    /**
     * 一致预期EPS
     */
    public static final String DM_GET_EPS = "v1/dm/get_eps";
    /**
     * 一致预期净利润
     */
    public static final String DM_GET_PE = "v1/dm/get_profit";
    /**
     * 一直预期估值波动带
     */
    public static final String DM_GET_ROE_BASE = "v1/dm/get_eps_base";
    public static final String DM_GET_ROE_RANG = "v1/dm/get_pe_range";

    /*
      * 自选股的删除
      * group_id=0
      * params token=4967b285a82244d296b807a8fea9bc77
      * params full_codes:sh600340
      * */
    public static final String DELETE_MY_STOCKS = "v1/mystock/delete_stocks";

    /*
      * 自选股的删除--老接口，不支持删除基金，债券，指数
      * params token=4967b285a82244d296b807a8fea9bc77
      * params stock_code=600001
      * */
    public static final String MYSTOCK_DELETE = "v1/mystock/delete";

    /**
     * 是否已收藏
     * params target_id 目标id
     * params type 目标类型
     * params token 用户令牌
     */
    public static final String FAVOR_IS_FAVORED = "v1/favor/is_favored";

    /**
     * 取消收藏
     * params target_id 目标id
     * params type 目标类型
     * params token 用户令牌
     */
    public static final String FAVOR_DELETE = "v1/favor/delete";

    /**
     * 添加收藏
     * params target_id 目标id
     * params type 目标类型
     * params token 用户令牌
     */
    public static final String FAVOR_ADD = "v1/favor/add";

    /**
     * 头条推荐列表
     */
    public static final String RECOMMEND_HEADLINES = "v1/news/get_recommend_headlines_list";

    /**
     * 点赞
     * params token
     * params target_id
     * params type
     * <p/>
     * 评论点赞
     * params target_id: 评论id
     * params type=20
     */
    public static final String PRAISE_ADD = "v1/praise/add";

    /**
     * 热点聚焦-新闻列表
     * params keyword
     * params page
     * params rows
     */
    public static final String FOCUS_TOPIC_SEARCH_NEWS = "v1/focus_topic/search_news";

    /**
     * 热点聚焦-行业报告列表
     * params base_auth 1
     * params keyword
     * params page
     * params rows
     */
    public static final String FOCUS_TOPIC_SEARCH_INDUSTRY_REPORT = "v1/focus_topic/search_industry_report";

    /**
     * 删除我的评论
     * params token 用户令牌
     * params id 评论ID
     */
    public static final String COMMENT_DELETE = "v1/comment/delete";

    /**
     * 添加分享次数
     * params type 功能模块
     * params target_id：被分享内容的id
     * params token：用户令牌  可选
     */
    public static final String SHARE_ADD = "v1/share/add";

    /* * 评论-添加
    params theme_id
    * params type
    * params content
    * params token
    */
    public static final String COMMENT_ADD = "v1/comment/add";


    /*
 * 记录热门搜索数据
 * params token(可选）,type, stock_code,stock_name,insert_date
 * */
    public static final String STOCK_SAVE_HOTS = "v1/stock/save_hots";

    /*
    * 热门搜索
    * params page ,rows，getcount（可选），type (必选，类别：1 代表股票）
    * */
    public static final String STOCK_GET_HOTS = "v1/stock/get_hots";

    /*
    * 股票评论
    * params stock_code  page  rows  get_count
    * */
    public static final String STOCKCOMMENT_LIST = "v1/stockcomment/list";

    /*
    * 股票评论添加
    * params token  stock_code  content  二级评论 parent_id
    * */
    public static final String STOCKCOMMENT_ADD = "v1/stockcomment/add";

    /*
    * 股票评论删除
    * params token  id
    * */
    public static final String STOCKCOMMENT_DELETE = "v1/stockcomment/delete";

    /**
     * 评论列表
     * params theme_id 主题id
     * params type 类型
     * params get_count 是否获得总数 0 1
     * params get_father_son=1
     */
    public static final String COMMENT_LIST = "v1/comment/list";

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

    /**
     * token                         用户token
     * account_id                    好友用户Id
     */
    public static final String GET_ACCOUNT_DETAIL = "v1/ggm_im/get_account_detail  ";

    /**
     * token                         用户token 必
     * keyword                       好友用户Id
     * is_recommend                  是否显示推荐
     */
    public static final String SEARCH_GROUP = "v1/ggm_chat/search_group";

    /**
     * token                         用户token
     * conv_id                       会话id
     */
    public static final String GET_GROUP_INFO = "v1/ggm_chat/get_group_info";

    /**
     * token                         用户token
     * conv_id                       会话id
     * group_name                    更改群名
     * group_notice                  更改群公告
     * group_intro                   更改群简介
     */
    public static final String UPDATE_GROUP_INFO = "v1/ ggm_chat/update_group_info";

    /**
     * token                         用户token
     * friend_id                    好友用户Id
     */
    public static final String del_friend = "v1/ggm_im/del_friend";

    /**
     * token                         用户token
     * product_line                  产品线
     */
    public static final String VIDEO_MOBILE = "v1/video_mobile/valid_ user_live_auth";

    /**
     * 投研板块
     * <p>
     * token                         用户token
     */
    public static final String GET_TOUYAN_LIST = "v1/ggm_icon/get_icon_list";

    /*
     *  获取全部小工具
     *  @prama token
     */
    public static final String GET_ALLCOLUMN = "v1/ggm_app/get_allapplist";

    /*
     *  获取用户添加的小工具
     *  @prama token
     */
    public static final String GET_USERCOLUMN = "v1/ggm_app/get_myapplist";

    /*
     *  首页小工具的编辑
     *
     *  @prama token
     *  @prama showid  展示应用id    多个已“；”隔开
     *  @prama nshowid 取消展示应用id    多个已“；”隔开
     */
    public static final String GET_EDITE_USERCOLUMN = "v1/ggm_app/set_isshow";

    /**
     * banner
     * <p>
     * ad_position=7类型
     */
    public static final String GET_AD_LIST = "v1/ggm_ad/get_ad_list";

    /**
     * token                         用户token
     * live_title                    直播标题
     * live_large_img                直播封面图片
     */
    public static final String ADD_LIVE_VIDEO = "v1/video_mobile/add_live_video";

    /**
     * token
     * mobile
     * 发送验证码 更换绑定手机号
     */
    public static final String SEND_CAPTCHA = "v1/ggm_im/send_captcha";

    /**
     * phone                         手机号
     * source                        来源=20
     * 注册找回密码用
     */
    public static final String MOBILE_SEND_CAPTCHA = "v1/mobile/send_captcha";

    /**
     * token                         用户token
     * mobile                        手机号
     */
    public static final String USER_REGISTER = "v1/user/register";

    /**
     * captcha                       验证码
     * new_pwd                       新密码
     */
    public static final String RESET_PASSWORD_BY_MOBILE = "v1/ggm_im/reset_password_by_mobile";

    /**
     * token                         用户token
     * avatar                        头像
     * name                          名称
     * company                       公司
     * duty                          职位
     * province                      省份
     * city                          城市
     */
    public static final String UPDATE_ACCOUNT_INFO = "v1/ggm_im/update_account_info";

    /**
     * 直播邀请连麦
     * token
     * invitee_id
     */
    public static final String VIDEOCALL_INVITE = "v1/ggm_videocall/invite";

    /**
     * 直播连麦反馈
     * token
     * invitee_id
     * feedback_result
     */
    public static final String VIDEOCALL_FEEDBACK = "v1/ggm_videocall/feedback";

    /**
     * 直播连麦关闭
     * token
     */
    public static final String VIDEOCALL_CLOSE = "v1/ggm_videocall/close";

    /**
     * 直播关闭
     * token
     * live_id
     */
    public static final String VIDEOCALL_CLOSE_LIVE = "v1/video_mobile/close_live";

    /**
     * 直播录播播放数据统计
     * video_id   视频id      String     视频id    否
     * source    观看来源    Number   观看来源：1终端，2手机端，3网页  否
     * token    用户token     String     用户token   是
     * equipment_id   设备id   String    设备id      是
     * type    统计类型   Number    统计类型：1播放，2分享。（默认播放）  是
     * video_type 视频类型 Number 视频类型：1直播2录播 否
     * product_line 产品线id  Number   产品线id，（默认4机构版）否
     */
    public static final String ADD_PALY_DATE = "v1/video_studio/add_paly_date";

    /*
     *  token
     *  captcha
     *  绑定手机号
     */
    public static final String BIND_MOBILE = "v1/ggm_im/bind_mobile";

    /*
     *  token
     *  获取个人信息
     */
    public static final String GET_MY_INFO = "v1/user/get_info";
    /*
     *  token
     *  old_pwd
     *  new_pwd
     *  修改个人密码——根据原密码
     */
    public static final String RESET_PASSWORD = "v1/ggm_im/reset_password";

    /**
     * 预约直播
     *
     * @prama token
     * @prama sourece 4(移动版)
     * @prama video_id 直播的id（live_id）
     */
    public static final String ORDER_LIVE = "v1/video_studio/order_live";

    /*
     * 获取节目单
     */
    public static final String GET_PROGRAMME_GUIDE = "v1/video_studio/get_programme_guide";

    /**
     * 验证邀请码
     *
     * @prama invite_code 邀请码
     * @prama user_token  token
     * @prama video_id    视频id
     */
    public static final String VALIDATE_CODE = "v1/video_auth/validate_code";

    /**
     * 验证本地邀请码
     *
     * @prama video_id    视频的id
     * @prama identifies  本地的邀请码
     */
    public static final String VALIDATE_IDENTIFIES = "v1/video_auth/validate_identifies";

    /**
     * token=6046f43ff7ae44bb91d6f3be483d2a98
     */
    public static final String GET_MY_ADVISERS = "v1/user/crm_user_saler";

    /**
     * 匹配手机通讯录
     * <p>
     * token=6046f43ff7ae44bb91d6f3be483d2a98
     * contacts=[{name:***,mobile:***},{name:***,mobile:***},{name:***,mobile:***}....]
     * 人有多号码时，拆成多个name-mobile
     */
    public static final String GET_CONTACTS = "v1/ggm_im/get_contacts";

    /**
     * 自动登录
     * params token 用户令牌
     */
    public static final String USER_AUTO_LOGIN = "v1/user/auto_login";

    /**
     * 取消打扰
     * token, conv_id
     */
    public static final String CANCEL_MUTE = "v1/ggm_chat/cancel_mute";

    /**
     * 设置打扰
     * token, conv_id
     */
    public static final String SET_MUTE = "v1/ggm_chat/set_mute ";

    /**
     * 面对面建群
     * "token",
     * "password",
     * "longitude",
     * "latitude"
     */
    public static final String FTF_CREATE_GROUP = "v1/ggm_chat/ftf_create_group";

    /**
     * 加入面对面群
     * "token",
     * "conv_id"
     */
    public static final String ADD_FTF_MEMBER = "v1/ggm_chat/add_ftf_member";

    /**
     * F10-公司概况
     * params stock_code
     */
    public static final String COMPANY_SUMMARY = "v1/f10/company_summary";

    /**
     * F10-经营分析
     * params stock_code
     */
    public static final String COMPANY_BUSINESS_ANALYSIS = "v1/f10/company_business_analysis";

    /**
     * F10-财务分析
     * params stock_code
     * params season 0【全部】1【一季度 】2【二季度】3【三 季度】4【四季度】
     * params type 0【全部】1【核心财 务指示】2【每股指标 】3【盈利能力】4【 资本结构】5【偿债能 力】6【营运能力】 7【成长能力】
     * params page
     */
    public static final String FINANCIAL_ANALYSIS = "v1/f10/financial_analysis";

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
        this.url = url.trim().replace(" ", "");
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
                            if (httpInterface != null) {
                                try {
                                    if (!JSONObject.parseObject(response).containsKey("code") &&
                                            JSONObject.parseObject(response).containsKey("data")) {
                                        httpInterface.onFailure("没有code或data字段");
                                    } else {
                                        httpInterface.onSuccess(response);
                                    }
                                } catch (Exception e) {//解析出错，返回就TM就不是json
                                    httpInterface.onFailure(e.getMessage());
                                }
                            }
                            ;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "==出错日志==e.getMessage()==出错接口：" + url + "==");
            if (httpInterface != null) httpInterface.onFailure(e.toString());
        }
    }

    public void startRealGet() {
        try {
            OkHttpUtils.get()
                    .url(GGAPI.getReal(url, param))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (httpInterface != null) httpInterface.onFailure(e.toString());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (httpInterface != null) {
                                try {
                                    if (!JSONObject.parseObject(response).containsKey("code") &&
                                            JSONObject.parseObject(response).containsKey("data")) {
                                        httpInterface.onFailure("没有code或data字段");
                                    } else {
                                        httpInterface.onSuccess(response);
                                    }
                                } catch (Exception e) {//解析出错，返回就TM就不是json
                                    httpInterface.onFailure(e.getMessage());
                                }
                            }
                            ;
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
