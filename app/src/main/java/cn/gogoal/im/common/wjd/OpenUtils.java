package cn.gogoal.im.common.wjd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;

/**
 * author wangjd on 2017/3/6 0006.
 * Staff_id 1375
 * phone 18930640263
 */
public class OpenUtils {

    public static Bitmap getShareBitmap(Context context, @IdRes int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System
                .currentTimeMillis();
    }
}
