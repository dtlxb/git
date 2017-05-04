package cn.gogoal.im.ui.widget;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import cn.gogoal.im.R;

/*
* 动态布局，任意位置弹出
* */
public class PopupWindowHelper {

    private View popupView;
    private PopupWindow mPopupWindow;
    private static final int TYPE_WRAP_ALL = 1, TYPE_MATCH_ALL = 2,
            TYPE_WRAR_MATCH = 3, TYPE_MATCH_WRAR = 4;

    public PopupWindowHelper(View view) {
        popupView = view;
    }

    public void showAsDropDown(View anchor) {
        mPopupWindow.showAsDropDown(anchor);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        initPopupWindow(TYPE_WRAP_ALL);
        mPopupWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        initPopupWindow(TYPE_WRAP_ALL);
        mPopupWindow.showAtLocation(parent, gravity, x, y);
    }

    public void dismiss() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void showAsPopUp(View anchor) {
        showAsPopUp(anchor, 0, 0);
    }

    public void showAsPopUp(View anchor, int xoff, int yoff) {
        initPopupWindow(TYPE_WRAP_ALL);
        mPopupWindow.setAnimationStyle(R.style.AnimationUpPopup);
        popupView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = popupView.getMeasuredHeight();
        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, location[0] + xoff, location[1] - height + yoff);
    }

    public void showFromTop(View anchor) {
        initPopupWindow(TYPE_MATCH_WRAR);
        //mPopupWindow.setAnimationStyle(R.style.AnimationFromTop);
        mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, 0, getStatusBarHeight());
    }

    public void showFromBottom(View anchor) {
        initPopupWindow(TYPE_MATCH_WRAR);
        mPopupWindow.setAnimationStyle(R.style.AnimationFromButtom);
        mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
    }

    public void showFromLeft(View anchor) {
        initPopupWindow(TYPE_WRAR_MATCH);
        //mPopupWindow.setAnimationStyle(R.style.AnimationFromLeft);
        mPopupWindow.showAtLocation(anchor, Gravity.LEFT, 0, getStatusBarHeight());
    }

    public void showFromRight(View anchor) {
        initPopupWindow(TYPE_WRAR_MATCH);
        mPopupWindow.setAnimationStyle(R.style.AnimationFromRight);
        mPopupWindow.showAtLocation(anchor, Gravity.RIGHT, 0, getStatusBarHeight());
    }

    /**
     * 特殊需求的弹窗
     */
    public void showScreenFromRight(View anchor) {
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimationFromRight);
        mPopupWindow.showAsDropDown(anchor, 0, 0, Gravity.RIGHT);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                missCallBack.setBoxScreen();
            }
        });
    }

    /**
     * touch outside dismiss the popupwindow, default is ture
     *
     * @param isCancelable
     */
    public void setCancelable(boolean isCancelable) {
        if (isCancelable) {
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
        } else {
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setFocusable(false);
        }
    }

    public void initPopupWindow(int type) {
        if (type == TYPE_WRAP_ALL) {
            mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (type == TYPE_MATCH_ALL) {
            mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (type == TYPE_WRAR_MATCH) {
            mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else if (type == TYPE_MATCH_WRAR) {
            mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
    }

    private int getStatusBarHeight() {
        return Math.round(25 * Resources.getSystem().getDisplayMetrics().density);
    }

    PopDismissCallBack missCallBack;

    public PopDismissCallBack getMissCallBack() {
        return missCallBack;
    }

    public void setMissCallBack(PopDismissCallBack missCallBack) {
        this.missCallBack = missCallBack;
    }

    public interface PopDismissCallBack {
        public void setBoxScreen();
    }
}
