package cn.gogoal.im.common.ImageUtils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;

import java.io.File;

/**
 * author wangjd on 2017/6/20 0020.
 * Staff_id 1375
 * phone 18930640263
 * description :Glide 辅助 uri 获取
 */
public class GlideUrilUtils {
    //    load ContentProvider资源：load("content://media/external/images/media/139469")
    private static final String ANDROID_RESOURCE = "android.resource://";

    public static String res2UriString(Context context, @DrawableRes int resourceId) {
        return ANDROID_RESOURCE + context.getPackageName() + File.separator + resourceId;
    }

    public static Uri res2Uri(Context context, @DrawableRes int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + File.separator + resourceId);
    }

    public static Uri assets2Uri(Context context, String assetNmae) {
        return Uri.parse("file:///android_asset" + File.separator + assetNmae);
    }

    public static Uri raw2Uri(@RawRes int rawId) {
        return Uri.parse(ANDROID_RESOURCE +
                "com.frank.glide" + File.separator +
                "raw" + File.separator + rawId);
    }
}
