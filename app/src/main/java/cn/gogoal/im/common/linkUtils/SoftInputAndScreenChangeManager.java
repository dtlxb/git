package cn.gogoal.im.common.linkUtils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by dave.
 * Date: 2017/6/2.
 * Desc: 监听输入法弹出和屏幕横竖屏切换的管理类
 */
public class SoftInputAndScreenChangeManager implements ViewTreeObserver.OnGlobalLayoutListener {

    private final static String TAG = SoftInputAndScreenChangeManager.class.getSimpleName();
    private View rootView;
    private Activity mActivity;
    private OnSoftInputWithDifferListener mOnSoftInputWithDifferListener;
    private boolean isSoftInputShow = false;

    public SoftInputAndScreenChangeManager(Activity activity) {
        mActivity = activity;
        this.rootView = mActivity.getWindow().getDecorView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * 如果仅仅为了获取键盘的弹起状态的话，不需要add这个listener
     */
    public void addOnSoftInputWithDifferListener(OnSoftInputWithDifferListener onSoftInputWithDifferListener) {
        mOnSoftInputWithDifferListener = onSoftInputWithDifferListener;
    }

    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        int displayHeight = rect.bottom - rect.top;
        int height = rootView.getHeight();
        onLayoutChange(displayHeight, height);
    }

    private int
            initInputTop, // 最初屏幕的可视高度，键盘未弹起的时候 默认键盘不弹起
            lastInputTop, // 前一次屏幕的可视高度
            initWindowHeight, // 最初的view的高度  默认屏幕是竖屏
            lastWindowHeight, // 前一次的view高度
            portraitWindowHeight, // 竖屏的屏幕高度
            landscapeWindowHeight; // 横屏的屏幕高度

    private void onLayoutChange(int intputTop, int windowHeight) {

        if(initInputTop == 0 || initWindowHeight == 0){
            lastInputTop = initInputTop = intputTop;
            lastWindowHeight = initWindowHeight = windowHeight;
            return;
        }

        if(windowHeight == lastWindowHeight){
            if(intputTop < lastInputTop && lastInputTop == initInputTop){
                Log.v(TAG , "RectHeight:" + intputTop + ",DecorViewHeight:" + windowHeight);
                isSoftInputShow = true;
                //键盘弹起
                Log.v(TAG,"输入法展示");
                if(mOnSoftInputWithDifferListener != null){
                    if(windowHeight == landscapeWindowHeight){
                        mOnSoftInputWithDifferListener.isLandscapeInputSoftOpen();
                    }else {
                        mOnSoftInputWithDifferListener.isPortraitInputSoftOpen(initInputTop - intputTop);

                    }

                }
            }else if(intputTop > lastInputTop && intputTop == initInputTop){
                Log.v(TAG , "RectHeight:" + intputTop + ",DecorViewHeight:" + windowHeight);
                isSoftInputShow = false;
                //键盘收起
                Log.v(TAG,"输入法隐藏");
                if(mOnSoftInputWithDifferListener != null){
                    if(windowHeight == landscapeWindowHeight){
                        mOnSoftInputWithDifferListener.isLandscapeInputSoftClose();
                    }else{
                        mOnSoftInputWithDifferListener.isPortraitInputSoftClosed();
                    }

                }
            }else{
                //状态保持

            }
        }else if(windowHeight > lastWindowHeight){
            //切换为竖屏
            Log.v(TAG,"切换为竖屏");
            //测试显示 切换横竖屏后 Variables里面的横竖屏高度并没有被改变
            initInputTop = intputTop;
            initWindowHeight = windowHeight;
            portraitWindowHeight = windowHeight;
            if(mOnSoftInputWithDifferListener != null){
                mOnSoftInputWithDifferListener.isPortrait();
            }
        }else if(windowHeight < lastWindowHeight ){
            //切换为横屏
            Log.v(TAG,"切换为横屏");
            initInputTop = intputTop;
            initWindowHeight = windowHeight;
            landscapeWindowHeight = windowHeight;
            if(mOnSoftInputWithDifferListener != null){
                mOnSoftInputWithDifferListener.isLandscape();
            }
        }

        lastInputTop = intputTop;
        lastWindowHeight = windowHeight;
    }

    /**
     * 获取键盘弹起状态
     */
    public boolean isSoftInputShow(){
        return isSoftInputShow;
    }
}
