
package cn.gogoal.im.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * author wangjd on 2017/4/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */

public class UnSlidingViewPager extends ViewPager {

    private boolean isScrollable;

    public UnSlidingViewPager(Context context) {
        super(context);
    }

    public UnSlidingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollable && super.onInterceptTouchEvent(ev);
    }
}
