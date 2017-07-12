package cn.gogoal.im.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * author wangjd on 2017/7/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ForbitRecycler extends RecyclerView {
    public ForbitRecycler(Context context) {
        super(context);
    }

    public ForbitRecycler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ForbitRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return true;
    }
}
