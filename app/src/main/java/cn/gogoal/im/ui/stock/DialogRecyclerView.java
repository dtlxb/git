package cn.gogoal.im.ui.stock;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * author wangjd on 2017/6/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class DialogRecyclerView extends RecyclerView {
    public DialogRecyclerView(Context context) {
        super(context);
    }

    public DialogRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return true;
    }
}
