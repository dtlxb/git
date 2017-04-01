package cn.gogoal.im.common;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * author wangjd on 2017/3/30 0030.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class IconFontUtils {
    public static void setFont(Context context,TextView textview,String ttfPath){
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), ttfPath);
        textview.setTypeface(iconfont);
    }
}
