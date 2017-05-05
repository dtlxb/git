package cn.gogoal.im.common;

/**
 * author wangjd on 2017/2/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public class AppConst {

    public static final int MAX_PAGE = 50;//刷新页数上限
    //LeanCloud参数(沙盒)
//    public static final String LEANCLOUD_APP_ID = "R7vH8N41V1rqJIqrlTQ1mMnR-gzGzoHsz";
//    public static final String LEANCLOUD_APP_KEY = "4iXr2Ylh1VwVyYjaxs3ufFmo";

    //LeanCloud参数(预正式)
    public static final String LEANCLOUD_APP_ID = "TSmwX3vIIpOo6HGcI7Ykyj73-gzGzoHsz";
    public static final String LEANCLOUD_APP_KEY = "tKBwDIKoyyAbMK5i3yAsJ4bD";

    //UCloud  UFile
    public static final String publicKey = "ucloudgcqin@go-goal.com13648682571239575500";

    public static final String privatekey = "27f435a8c39f515b01a3db66acbdd7ef9b37d16c";

    //微信参数
    public static final String WEIXIN_APP_ID = "wx05acb31be27d76aa";
    public static final String WEIXIN_APP_SECRET = "cc2412a472da396eb16ea879d64613ad";

    public static final String WEXIN_GET_ACCESS_TOKEN_URL =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&grant_type=authorization_code&code=%s";

    public static final String WEXIN_IS_EXPIRE_ACCESS_TOKEN =
            "https://api.weixin.qq.com/sns/auth?access_token=%s&openid=&s";

    public static final String WEXIN_GET_USER_INFO =
            "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

    public static final String REFRESH_TOKEN_URL =
            "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";

    public static final String LEAN_CLOUD_TOKEN = "11";

    public static final String LEAN_CLOUD_CONVERSATION_ID = "58aaa02d8d6d8100636e8be9";

    //关于刷新的一些类型，抗干扰
    public static final int REFRESH_TYPE_FIRST = 0x50010;            //首次进入刷新

    public static final int REFRESH_TYPE_AUTO = 0x50011;            //自动刷新

    public static final int REFRESH_TYPE_RELOAD = 0x50012;          //出错重试按钮

    public static final int REFRESH_TYPE_SWIPEREFRESH = 0x50013;    //下拉刷新

    public static final int REFRESH_TYPE_LOAD_MORE = 0x50015;    //上拉加载刷新数据

    public static final int REFRESH_TYPE_PARENT_BUTTON = 0x50014;   //父activity的刷新按钮

    //创建群的方式
    public static final int CREATE_SQUARE_ROOM_BY_ONE = 1100;
    public static final int CREATE_SQUARE_ROOM_BUILD = 1101;
    //群加人删人@人
    public static final int SQUARE_ROOM_ADD_ANYONE = 1102;
    public static final int SQUARE_ROOM_DELETE_ANYONE = 1103;
    public static final int SQUARE_ROOM_AT_SOMEONE = 1104;
    //改群名，改群简介
    public static final int SQUARE_ROOM_EDIT_NAME = 1105;
    public static final int SQUARE_ROOM_EDIT_BRIEF = 1106;
    public static final int SQUARE_ROOM_EDIT_NOTICE = 1107;
    //直播连麦
    public static final int LIVE_CONTACT_SOMEBODY = 1108;
    //一般群缓存，直播群缓存(通讯录)
    public static final int CHAT_GROUP_CONTACT_BEANS = 1109;
    public static final int LIVE_GROUP_CONTACT_BEANS = 1110;
    //登录验证，注册
    public static final int LOGIN_FIND_CODE = 1111;
    public static final int LOGIN_RIGIST_NUMBER = 1112;
    //阿里云播放器参数
    public static final String businessId = "video_live";
    public static final String accessKeyId = "LTAI1KypPpiBhPAx";
    public static final String accessKeySecret = "vb0kunzWvhxV6WHrH4Znv1BeJFJ2xV";
    public static final int DISS_PROGRESSBAR = 7;


    //开发环境
//    public static final String WEB_URL = "http://192.168.72.155:9000/#";
    public static final String WEB_URL_LLJ = "http://192.168.52.156:9000/#";
    private static final String WEB_URL = "http://ggmobile.sandbox.go-goal.cn/#";
    //预正式环境
//    public static final String WEB_URL = "http://192.168.52.156:9000/#";
    //正式环境
//    public static final String WEB_URL = "http://211.144.193.162:8085/#";

    //个股-交易五档
    public static final int TREAT_TYPE_WU_DANG = 952;
    //个股-交易明细
    public static final int TREAT_TYPE_MING_XI = 459;

    //直播分享
    public static final String GG_LIVE_SHARE = WEB_URL + "/live/share/";
    //直播录播列表
    public static final String GG_LIVE_LIST = WEB_URL + "/live/list";
    //中国研网
    public static final String GG_RESEARCH = WEB_URL + "/research";
    //精要研报
    public static final String GG_REPORT = WEB_URL + "/report";
    //文字一分钟
    public static final String GG_TEXT = WEB_URL + "/text";

    //投研页帮助
    public static final String GG_HELP = WEB_URL + "/help/index";

    //设置页关于
    public static final String GG_SETTING_ABOUT = WEB_URL + "/help/about";
    //设置页免责申明
    public static final String GG_DISCLAIMER = WEB_URL + "/help/disclaimer";
    //设置页服务协议
    public static final String GG_SERVICE_AGREEMENT = WEB_URL + "/help/service";

    public static final int SOURCE_TYPE_YANBAO = 102;//个股 研报
    public static final int SOURCE_TYPE_GONGGAO = 105;//个股 公告
    public static final String WEB_NEWS = WEB_URL+"/report/info/";

    //新闻远端
    public static final int SOURCE_TYPE_NEWS = 100;//个股 新闻

}
