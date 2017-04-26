package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.view.View;

import cn.gogoal.im.ui.dialog.base.BaseBottomDialog;
import cn.gogoal.im.ui.dialog.base.BaseDialog;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 *
 * 通用，布局dialog
 */
public class BottomSheetNormalDialog extends BaseBottomDialog {

    private static final String KEY_LAYOUT_RES = "bottom_layout_res";
    private static final String KEY_HEIGHT = "bottom_height";
    private static final String KEY_DIM = "bottom_dim";
    private static final String KEY_CANCEL_OUTSIDE = "bottom_cancel_outside";

    private FragmentManager mFragmentManager;

    private boolean mIsCancelOutside = super.getCancelOutside();

    private String mTag = super.getFragmentTag();

    private float mDimAmount = super.getDimAmount();

    private int mHeight = super.getHeight();

    @LayoutRes
    private int mLayoutRes;

    private ViewListener mViewListener;

    public static BottomSheetNormalDialog create(FragmentManager manager) {
        BottomSheetNormalDialog dialog = new BottomSheetNormalDialog();
        dialog.setFragmentManager(manager);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLayoutRes = savedInstanceState.getInt(KEY_LAYOUT_RES);
            mHeight = savedInstanceState.getInt(KEY_HEIGHT);
            mDimAmount = savedInstanceState.getFloat(KEY_DIM);
            mIsCancelOutside = savedInstanceState.getBoolean(KEY_CANCEL_OUTSIDE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_LAYOUT_RES, mLayoutRes);
        outState.putInt(KEY_HEIGHT, mHeight);
        outState.putFloat(KEY_DIM, mDimAmount);
        outState.putBoolean(KEY_CANCEL_OUTSIDE, mIsCancelOutside);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void bindView(final View dialogView) {
        if (mViewListener != null) {
            mViewListener.bindDialogView(getParentDialog(),dialogView);
        }
    }

    @Override
    public int getLayoutRes() {
        return mLayoutRes;
    }

    public BottomSheetNormalDialog setFragmentManager(FragmentManager manager) {
        mFragmentManager = manager;
        return this;
    }

    public BottomSheetNormalDialog setViewListener(ViewListener listener) {
        mViewListener = listener;
        return this;
    }

    public BottomSheetNormalDialog setLayoutRes(@LayoutRes int layoutRes) {
        mLayoutRes = layoutRes;
        return this;
    }

    public BottomSheetNormalDialog setCancelOutside(boolean cancel) {
        mIsCancelOutside = cancel;
        return this;
    }

    public BottomSheetNormalDialog setTag(String tag) {
        mTag = tag;
        return this;
    }

    public BottomSheetNormalDialog setDimAmount(float dim) {
        mDimAmount = dim;
        return this;
    }

    public BottomSheetNormalDialog setHeight(int heightPx) {
        mHeight = heightPx;
        return this;
    }

    private BottomSheetNormalDialog getParentDialog(){
        return BottomSheetNormalDialog.this;
    }
    @Override
    public float getDimAmount() {
        return mDimAmount;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    @Override
    public boolean getCancelOutside() {
        return mIsCancelOutside;
    }

    @Override
    public String getFragmentTag() {
        return mTag;
    }

    public interface ViewListener {
        void bindDialogView(BottomSheetNormalDialog dialog,View dialogView);
    }

    public BaseDialog show() {
        show(mFragmentManager);
        return this;
    }
}