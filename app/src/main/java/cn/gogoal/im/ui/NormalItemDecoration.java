package cn.gogoal.im.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AppDevice;

/**
 * author wangjd on 2017/3/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class NormalItemDecoration extends android.support.v7.widget.DividerItemDecoration {

    public NormalItemDecoration(Context context) {
        super(context, LinearLayoutManager.VERTICAL);
        this.setDrawable(ContextCompat.getDrawable(context, R.drawable.shape_divider_1px));
    }

    public NormalItemDecoration(Context context, @ColorInt int color) {
        super(context, LinearLayoutManager.VERTICAL);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setSize(-1, AppDevice.dp2px(context, (float) 0.5));
        this.setDrawable(drawable);
    }

}
