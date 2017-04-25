package cn.gogoal.im.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * author wangjd on 2017/3/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :软键盘监听布局
 */
public class KeyboardLaunchRelativeLayout extends RelativeLayout {

    private Context mContext;
    private int mOldh = -1;
    private int mNowh = -1;
    protected int mScreenHeight = 0;
    protected boolean mIsSoftKeyboardPop = false;

    public KeyboardLaunchRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                if (mScreenHeight == 0) {
                    mScreenHeight = r.bottom;
                }
                mNowh = mScreenHeight - r.bottom;
                if (mOldh != -1 && mNowh != mOldh) {
                    if (mNowh > 0) {
                        mIsSoftKeyboardPop = true;
                        if (mListenerList != null) {
                            for (OnKeyboardChangeListener l : mListenerList) {
                                l.OnKeyboardPop(mNowh);
                            }
                        }
                    } else {
                        mIsSoftKeyboardPop = false;
                        if (mListenerList != null) {
                            for (OnKeyboardChangeListener l : mListenerList) {
                                l.OnKeyboardClose();
                            }
                        }
                    }
                }
                mOldh = mNowh;
            }
        });
    }

    public boolean isSoftKeyboardPop() {
        return mIsSoftKeyboardPop;
    }

    private List<OnKeyboardChangeListener> mListenerList;

    public void setOnKeyboardChangeListener(OnKeyboardChangeListener listener) {
        if (mListenerList == null) {
            mListenerList = new ArrayList<>();
        }
        mListenerList.add(listener);
    }

    public interface OnKeyboardChangeListener {
        /**
         * 软键盘弹起
         */
        void OnKeyboardPop(int height);

        /**
         * 软键盘关闭
         */
        void OnKeyboardClose();
    }
}
