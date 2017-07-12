package cn.gogoal.im.ui.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * author wangjd on 2017/7/6 0006.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class RefreshLayout extends PtrFrameLayout {

    private GestureDetector detector;
    private boolean mIsDisallowIntercept = false;

    private void initGesture() {
        detector = new GestureDetector(getContext(),gestureListener);
    }

    private boolean mIsHorizontalMode = false;
    private boolean isFirst = true;

    /**
     * headerView
     */
    RefreshHeader mHeaderView;

    private float mDownPosX;
    private float mDownPosY;

    public RefreshLayout(Context context) {
        super(context);
        initView();
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    /**
     * 初始化view
     */
    private void initView() {
        initGesture();
        mHeaderView = new RefreshHeader(getContext());
        setHeaderView(mHeaderView);
        setRatioOfHeaderHeightToRefresh(0.8f);
        addPtrUIHandler(mHeaderView);
//        resistance
        setResistance(2.5f);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            isFirst = true;
            mIsHorizontalMode = false;
            mIsDisallowIntercept = false;
            return super.dispatchTouchEvent(e);
        }
        if (detector.onTouchEvent(e) && mIsDisallowIntercept && mIsHorizontalMode){
            return dispatchTouchEventSupper(e);
        }
        if (mIsHorizontalMode) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        this.mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();

        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (deltaX / 4 > deltaY) {
                    return false;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float disX, disY;
            if(distanceX < 0) {
                disX = -distanceX;
            } else {
                disX = distanceX;
            }
            if(distanceY < 0) {
                disY = -distanceY;
            } else {
                disY = distanceY;
            }

            if (disX > disY) {
                if (isFirst) {
                    mIsHorizontalMode = true;
                    isFirst = false;
                }
            } else {
                if (isFirst) {
                    mIsHorizontalMode = false;
                    isFirst = false;
                }
                return false;//垂直滑动会返回false
            }

            return true;//水平滑动会返回true
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };
}
