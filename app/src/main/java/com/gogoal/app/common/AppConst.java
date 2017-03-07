package com.gogoal.app.common;

import android.os.Environment;

import java.io.File;

/**
 * author wangjd on 2017/2/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public class AppConst {

    //LeanCloud参数
    public static final String LEANCLOUD_APP_ID="dYRQ8YfHRiILshUnfFJu2eQM-gzGzoHsz";

    public static final String LEANCLOUD_APP_KEY="ye24iIK6ys8IvaISMC4Bs5WK";

    //UCloud  UFile
    public static final String publicKey = "ucloudgcqin@go-goal.com13648682571239575500";

    public static final String privatekey = "27f435a8c39f515b01a3db66acbdd7ef9b37d16c";

    public static final String UFILE_IMAGE_COMPRESS="?iopcmd=thumbnail&type=1&scale=%s";

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

    public static final String LEAN_CLOUD_TOKEN = "William";
    //public static final String LEAN_CLOUD_TOKEN = "比尔森007";

    public static final String LEAN_CLOUD_CONVERSATION_ID = "58aaa02d8d6d8100636e8be9";

}
