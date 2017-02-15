package com.gogoal.app.common;

/**
 * author wangjd on 2017/2/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public class AppConst {
    //微信参数
    public static final String WEIXIN_APP_ID = "wx05acb31be27d76aa";
    public static final String WEIXIN_APP_SECRET = "cc2412a472da396eb16ea879d64613ad";

    public static final String WEXIN_TAOKE_ACCESS_TOKEN_URL =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&grant_type=authorization_code&code=%s";

    public static final String WEXIN_IS_EXPIRE_ACCESS_TOKEN =
            "https://api.weixin.qq.com/sns/auth?access_token=%s&openid=&s";

    public static final String WEXIN_GET_USER_INFO =
            "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

    public static final String REFRESH_TOKEN_URL =
            "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
}
