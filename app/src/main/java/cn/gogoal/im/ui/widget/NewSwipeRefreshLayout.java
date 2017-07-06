package cn.gogoal.im.ui.widget;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * author wangjd on 2017/7/6 0006.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class NewSwipeRefreshLayout extends SwipeRefreshLayout {

    public NewSwipeRefreshLayout(Context context) {
        super(context);
    }

    public NewSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.isRefreshing() || super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.isRefreshing() || super.onInterceptTouchEvent(ev);
    }
}
