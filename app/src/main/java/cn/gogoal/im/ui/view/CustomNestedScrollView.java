package cn.gogoal.im.ui.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者 wangjd on 2017/3/5 0005 16:27.
 * 联系方式 18930640263 ;
 * <p>
 * 简介:
 */

public class CustomNestedScrollView extends NestedScrollView {

    private onScrolliingStatesChangeListener listener;

    public CustomNestedScrollView(Context context) {
        this(context, null, 0);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        if (listener != null) {
//            listener.onScrolling();
//        }
//        super.onScrollChanged(l, t, oldl, oldt);
//    }
//
//    @Override
//    public void onStopNestedScroll(View target) {
//        if (listener != null) {
//            listener.stopScrolling();
//        }
//        super.onStopNestedScroll(target);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (listener!=null){
                    listener.onScrolling();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (listener!=null){
                    listener.stopScrolling();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

//    @Override
//    public void stopNestedScroll() {
//        if (listener != null) {
//            listener.stopScrolling();
//        }
//        super.stopNestedScroll();
//    }

    public void setOnScrollingStateChangeListener(onScrolliingStatesChangeListener listener) {
        this.listener = listener;
    }

    public interface onScrolliingStatesChangeListener {
        void onScrolling();

        void stopScrolling();
    }
}
