package cn.gogoal.im.common;

import android.content.Context;

import cn.gogoal.im.activity.MainActivity;

/**
 * author wangjd on 2017/5/17 0017.
 * Staff_id 1375
 * phone 18930640263
 * description :专门处理banner跳转
 */
public class BannerUtils {

//    switch (data.getType()) {
//        case 10000:  //H5
//            break;
//        case 10001: //行情
//            break;
//        case 10002: //个股
//            break;
//        case 10003: //直播
//            break;
//        default:
//            break;
//    }

    private Context context;

    private int type;

    private String[] params;

    private BannerUtils(Context context, int type, String... params) {
        this.context = context;
        this.type = type;
        this.params = params;
    }

    public static BannerUtils getInstance(Context context, int type, String... params) {
        BannerUtils utils=new BannerUtils(context,type,params);
        return utils;
    }

    public void go() {
        switch (type) {
            case 10000:  //H5
                NormalIntentUtils.go2WebActivity(
                        context,
                        params[0],
                        params.length>1?params[1]:"");
                break;
            case 10001: //行情
                ((MainActivity) context).changeItem(1);
                ((MainActivity) context).mainStockFragment.changeItem(1);
                break;
            case 10002: //个股
                break;
            case 10003: //直播
                break;
            default:
                break;
        }
    }

}
