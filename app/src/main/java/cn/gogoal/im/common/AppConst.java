package cn.gogoal.im.common;

/**
 * author wangjd on 2017/2/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public class AppConst {

    //LeanCloud参数
    public static final String LEANCLOUD_APP_ID = "R7vH8N41V1rqJIqrlTQ1mMnR-gzGzoHsz";

    public static final String LEANCLOUD_APP_KEY = "4iXr2Ylh1VwVyYjaxs3ufFmo";

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

    //阿里云播放器参数
    public static final String businessId = "video_live";
    public static final String accessKeyId = "LTAI1KypPpiBhPAx";
    public static final String accessKeySecret = "vb0kunzWvhxV6WHrH4Znv1BeJFJ2xV";

    //正式用
    public static final String WEB_DAMIN = "http://ggmobile.go-goal.cn/";
    public static final int RESULT_OK = 0;
    public static final int MAX_PAGE = 50;
    public static final int DISS_XCHART_DATA = 1;
    public static final int DISPLAY_XCHART_TIME_DATA = 2;
    public static final int XCHART_FLING = 3;
    public static final int FINSH_WECHARTLONGIN = 4;
    public static final int DISPLAY_FIVEDAYXCHART_TIME_DATA = 5;
    public static final int DISPLAY_XCHART_K_DATA = 6;
    public static final int DISS_PROGRESSBAR = 7;
    public static final int UPDATE_DISCUSS = 8;
    public static final int SUCESSACTIVITY_FINSH = 9;
    public static final int REFRESH_KLINE_DATA = 10;
}
