package cn.gogoal.im.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

/**
 * author wangjd on 2017/3/17 0017.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ContextHelper {

    private Context context;

    private ContextHelper(Context context){
        this.context=context.getApplicationContext();
    };

    @SuppressLint("StaticFieldLeak")
    private volatile static ContextHelper helper;

    public static ContextHelper getInstance(Context context){
        if (null==helper){
            synchronized (ContextHelper.class){
                if (null==helper){
                    helper=new ContextHelper(context);
                }
            }
        }
        return helper;
    }

    public int getResColor(@ColorRes int colorId){
        return ContextCompat.getColor(context,colorId);
    }
}
