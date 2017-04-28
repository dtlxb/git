
package cn.gogoal.im.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.gogoal.im.R;


/**
 * author wangjd on 2017/4/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */

public class UnSlidingViewPager extends ViewPager {

    private boolean scrollable;

    public UnSlidingViewPager(Context context) {
        super(context);
    }

    public UnSlidingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UnSlidingViewPager);
        scrollable = a.getBoolean(R.styleable.UnSlidingViewPager_canScroll, false);
        a.recycle();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.scrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.scrollable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item) {
        this.setCurrentItem(item,false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, false);
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}
