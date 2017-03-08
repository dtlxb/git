package cn.gogoal.im.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * author wangjd on 2016/11/29 0029.
 * Staff_id 1375
 * phone 18930640263
 * <p>
 * 可以嵌套ListView、RecyclerView、消耗子列表的滑动；
 * 可以监听垂直方向的滑动距离
 * <p>
 * 继承CallBack 可以实现指定的View悬浮
 */

public class GreatScrollView extends ScrollView {
    private int downY;
    private int mTouchSlop;

    private Callbacks mCallbacks;

    public GreatScrollView(Context context) {
        super(context);
        init(context);
    }

    public GreatScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GreatScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(t);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCallbacks != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mCallbacks.onDownMotionEvent();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mCallbacks.onUpOrCancelMotionEvent();
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
    }

    public interface Callbacks {
        void onScrollChanged(int scrollY);

        void onDownMotionEvent();

        void onUpOrCancelMotionEvent();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }
}
