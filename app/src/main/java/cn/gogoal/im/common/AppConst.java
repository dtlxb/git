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

    //======================关于刷新的一些类型，抗干扰===============================================
    public static final int REFRESH_TYPE_FIRST = 0x50010;            //首次进入刷新

    public static final int REFRESH_TYPE_RESUME = 0x50015;            //首次进入刷新

    public static final int REFRESH_TYPE_AUTO = 0x50011;            //自动刷新

    public static final int REFRESH_TYPE_RELOAD = 0x50012;          //出错重试按钮

    public static final int REFRESH_TYPE_SWIPEREFRESH = 0x50013;    //下拉刷新

    public static final int REFRESH_TYPE_LOAD_MORE = 0x50015;    //上拉加载刷新数据

    public static final int REFRESH_TYPE_PARENT_BUTTON = 0x50014;   //父activity的刷新按钮

    //======================关于ChooseContactActivity类型，选择好友列表==============================
    // ChooseContactActivity类型——选择好友和单聊好友创建群
    public static final int CREATE_SQUARE_ROOM_BY_ONE = 1100;
    // ChooseContactActivity类型——创群
    public static final int CREATE_SQUARE_ROOM_BUILD = 1101;
    // ChooseContactActivity类型——原来存在的群继续添加好友
    public static final int SQUARE_ROOM_ADD_ANYONE = 1102;
    // ChooseContactActivity类型——原来存在的群中移除好友
    public static final int SQUARE_ROOM_DELETE_ANYONE = 1103;
    // ChooseContactActivity类型——群@人
    public static final int SQUARE_ROOM_AT_SOMEONE = 1104;
    //ChooseContactActivity类型——分享到好友
    public static final int SQUARE_ROOM_AT_SHARE_MESSAGE = 1205;
    //股票创建群
    public static final int CREATE_SQUARE_ROOM_BY_STOCK = 1234;

    //改群名，改群简介
    public static final int SQUARE_ROOM_EDIT_NAME = 1105;
    public static final int SQUARE_ROOM_EDIT_BRIEF = 1106;
    public static final int SQUARE_ROOM_EDIT_NOTICE = 1107;
    //直播连麦
    public static final int LIVE_CONTACT_SOMEBODY = 1108;
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
    public static final String WEB_URL_LLJ = "http://192.168.52.156:9000/#/hello";
    //    public static final String WEB_URL = "http://ggmobile.sandbox.go-goal.cn/#";
//    public static final String WEB_URL_LLJ = "http://192.168.52.156:9000/#/hello";
    //    public static final String WEB_URL = "http://ggmobile.sandbox.go-goal.cn/#";
    public static final String WEB_URL = "http://app.go-goal.cn/#";
    //预正式环境
//    public static final String WEB_URL = "http://192.168.52.156:9000/#";
    //正式环境
//    public static final String WEB_URL = "http://211.144.193.162:8085/#";

    //个股-交易五档
    public static final int TREAT_TYPE_WU_DANG = 952;
    //个股-交易明细
    public static final int TREAT_TYPE_MING_XI = 459;
    //个股-资金
    public static final int TREAT_TYPE_MONEY = 901;

    //=================================关于跳转web页的配置=========================================
    //直播分享
    public static final String GG_LIVE_SHARE = WEB_URL + "/live/share/";
    //直播录播列表
    public static final String GG_LIVE_LIST = WEB_URL + "/live/list";
    //中国研网
    public static final String GG_RESEARCH = WEB_URL + "/research";
    //精要研报
    public static final String GG_REPORT = WEB_URL + "/report";

    //文字一分钟
    public static final String GG_TEXT1MINUTE = WEB_URL + "/text";
    //数据一分钟
    public static final String GG_DATA1MINUTE = WEB_URL + "/data";
    //同业比较
    public static final String GG_TONG_YE = WEB_URL + "/industry";

    //投研页帮助
    public static final String GG_HELP = WEB_URL + "/help/index";

    //设置页关于
    public static final String GG_SETTING_ABOUT = WEB_URL + "/help/about";
    //设置页免责申明
    public static final String GG_DISCLAIMER = WEB_URL + "/help/disclaimer";
    //设置页服务协议
    public static final String GG_SERVICE_AGREEMENT = WEB_URL + "/help/service";

    public static final String WEB_NEWS = WEB_URL + "/report/info/";

    public static final int SOURCE_TYPE_YANBAO = 102;//个股 研报
    public static final int SOURCE_TYPE_GONGGAO = 105;//个股 公告

    //新闻远端
    public static final int SOURCE_TYPE_NEWS = 100;//个股 新闻
    //股票消息标识
    public static final int TYPE_GET_STOCK = 1011;//个股 新闻

    //======================关于好公司标签==============================
    public static final int TYPE_FRAGMENT_HISTORY = 1300;//业绩鉴定-历史
    public static final int TYPE_FRAGMENT_INDUSTRY = 1301;//行业地位
    public static final int TYPE_FRAGMENT_INCIDENT_FUTURE = 1302;//未来事件提示
    public static final int TYPE_FRAGMENT_INCIDENT_FEET = 1303;//事件足迹
    public static final int TYPE_FRAGMENT_TURNOVER_RATE = 1304;//分歧度
    public static final int TYPE_FRAGMENT_REVENUE_RATE = 1305;//业绩季节性
    public static final int TYPE_FRAGMENT_STOCK_HOLDERS = 1306;//主流机构持仓
    public static final int TYPE_FRAGMENT_GOOD_COMPANY = 1307;//公司荣誉

    //======================关于LeanCloud会话类型，消息类型，发送失败类型==============================
    public static final int IM_CHAT_TYPE_SINGLE = 1001;                 //聊天室类型 单聊
    public static final int IM_CHAT_TYPE_SQUARE = 1002;                 //聊天室类型 群聊
    public static final int IM_CHAT_TYPE_LIVE = 1003;                   //聊天室类型 直播
    public static final int IM_CHAT_TYPE_SYSTEM = 1004;                 //聊天室类型 系统
    public static final int IM_CHAT_TYPE_CONTACTS_ACTION = 1005;        //聊天室类型 通讯录操作
    public static final int IM_CHAT_TYPE_CONSULTATION = 1006;           //聊天室类型 公众号
    public static final int IM_CHAT_TYPE_SQUARE_REQUEST = 1007;         //聊天室类型 入群申请
    public static final int IM_CHAT_TYPE_LIVE_MESSAGE = 1008;           //聊天室类型 直播消息
    public static final int IM_CHAT_TYPE_LIVE_REQUEST = 1009;           //聊天室类型 直播连麦
    public static final int IM_CHAT_TYPE_STOCK_SQUARE = 1010;           //聊天室类型 股票群聊


    public static final String IM_MESSAGE_TYPE_TEXT = "-1";             //消息类型 文字
    public static final String IM_MESSAGE_TYPE_PHOTO = "-2";            //消息类型 图片
    public static final String IM_MESSAGE_TYPE_AUDIO = "-3";            //消息类型 语音
    public static final String IM_MESSAGE_TYPE_FRIEND_DEL = "1";        //消息类型 删好友
    public static final String IM_MESSAGE_TYPE_FRIEND_ADD = "2";        //消息类型 加好友
    public static final String IM_MESSAGE_TYPE_CONTACT_ADD = "3";       //消息类型 好友加入通讯录
    public static final String IM_MESSAGE_TYPE_CONTACT_DEL = "4";       //消息类型 好友从通讯录移除
    public static final String IM_MESSAGE_TYPE_SQUARE_ADD = "5";        //消息类型 好友入群
    public static final String IM_MESSAGE_TYPE_SQUARE_DEL = "6";        //消息类型 群删除好友
    public static final String IM_MESSAGE_TYPE_SQUARE_REQUEST = "7";    //消息类型 申请入群
    public static final String IM_MESSAGE_TYPE_SQUARE_DETAIL = "8";     //消息类型 群公告,群简介
    public static final String IM_MESSAGE_TYPE_PUBLIC = "9";            //消息类型 公众号
    public static final String IM_MESSAGE_TYPE_STOCK = "11";            //消息类型 股票消息
    public static final String IM_MESSAGE_TYPE_SHARE = "13";            //消息类型 分享消息
    public static final String IM_MESSAGE_TYPE_SEND_FAIL = "14";        //消息类型 发送失败

    public static final int MESSAGE_SEND_FAIL_PARAMS_LACK = 101;        //失败类型 缺少参数
    public static final int MESSAGE_SEND_FAIL_NOT_FRIEND = 102;         //失败类型 不是好友
    public static final int MESSAGE_SEND_FAIL_NET_ERROR = 103;          //失败类型 网络异常
    public static final int MESSAGE_SEND_FAIL_NOT_MEMBER = 112;         //失败类型 不是群成员
    public static final int MESSAGE_SEND_FAIL_DISCONNECT = 119;         //失败类型 链接断开
    public static final int MESSAGE_SEND_FAIL_MEMBER_LIMIT = 120;       //失败类型 群员超限

    public static final int MESSAGE_SEND_STATUS_SENDING = 1200;         //消息正在发送
    public static final int MESSAGE_SEND_STATUS_SUCCESS = 1201;         //消息发送成功
    public static final int MESSAGE_SEND_STATUS_FAIL = 1202;            //消息发送失败


}
