package com.hply.alilayout.layout;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hply.alilayout.LayoutManagerHelper;
import com.hply.alilayout.VirtualLayoutManager;

import static com.hply.alilayout.VirtualLayoutManager.VERTICAL;

/**
 * LayoutHelper contains only one view
 */
public class SingleLayoutHelper extends ColumnLayoutHelper {

    private static final String TAG = "SingleLayoutHelper";

    private int mPos = -1;

    public SingleLayoutHelper() {
        setItemCount(1);
    }


    @Override
    public void setItemCount(int itemCount) {
        if (itemCount > 0)
            super.setItemCount(1);
        else
            super.setItemCount(0);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Only start is used, use should not use this measured
     *
     * @param start position of items handled by this layoutHelper
     * @param end   will be ignored by {@link SingleLayoutHelper}
     */
    @Override
    public void onRangeChange(int start, int end) {
        this.mPos = start;
    }

    @Override
    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state, VirtualLayoutManager.LayoutStateWrapper layoutState, LayoutChunkResult result, LayoutManagerHelper helper) {
        // reach the end of this layout
        if (isOutOfRange(layoutState.getCurrentPosition())) {
            return;
        }

        View view = layoutState.next(recycler);

        if (view == null) {
            result.mFinished = true;
            return;
        }


        helper.addChildView(layoutState, view);
        final VirtualLayoutManager.LayoutParams params = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
        final boolean layoutInVertical = helper.getOrientation() == VERTICAL;
        int parentWidth = helper.getContentWidth() - helper.getPaddingLeft() - helper
                .getPaddingRight() - getHorizontalMargin() - getHorizontalPadding();
        int parentHeight = helper.getContentHeight() - helper.getPaddingTop() - helper
                .getPaddingBottom() - getVerticalMargin() - getVerticalPadding();

        if (!Float.isNaN(mAspectRatio)) {
            if (layoutInVertical) {
                parentHeight = (int) (parentWidth / mAspectRatio + 0.5f);
            } else {
                parentWidth = (int) (parentHeight * mAspectRatio + 0.5f);
            }
        }

        if (layoutInVertical) {
            final int widthSpec = helper.getChildMeasureSpec(parentWidth,
                     Float.isNaN(mAspectRatio) ? params.width : parentWidth, !layoutInVertical && Float.isNaN(mAspectRatio));
            final int heightSpec = helper.getChildMeasureSpec(parentHeight,
                    Float.isNaN(params.mAspectRatio) ? (Float.isNaN(mAspectRatio) ? params.height : parentHeight) : (int) (
                            parentWidth / params.mAspectRatio + 0.5f), layoutInVertical && Float.isNaN(mAspectRatio));

            // do measurement
            helper.measureChildWithMargins(view, widthSpec, heightSpec);
        } else {
            final int widthSpec = helper.getChildMeasureSpec(parentWidth,
                    Float.isNaN(params.mAspectRatio) ? (Float.isNaN(mAspectRatio) ? params.width : parentWidth) : (int) (
                            parentHeight * params.mAspectRatio + 0.5f), !layoutInVertical && Float.isNaN(mAspectRatio));
            final int heightSpec = helper.getChildMeasureSpec(parentHeight,
                     Float.isNaN(mAspectRatio) ? params.height : parentHeight, layoutInVertical && Float.isNaN(mAspectRatio));

            // do measurement
            helper.measureChildWithMargins(view, widthSpec, heightSpec);
        }

        OrientationHelper orientationHelper = helper.getMainOrientationHelper();

        result.mConsumed = orientationHelper.getDecoratedMeasurement(view);

        // do layout
        int left, top, right, bottom;
        if (layoutInVertical) {
            int viewWidth = orientationHelper.getDecoratedMeasurementInOther(view);
            int available = parentWidth - viewWidth;
            if (available < 0) {
                available = 0;
            }

            left = mMarginLeft + mPaddingLeft + helper.getPaddingLeft() + available / 2;
            right = helper.getContentWidth() - mMarginRight - mPaddingRight - helper.getPaddingRight() - available / 2;


            if (layoutState.getLayoutDirection() == VirtualLayoutManager.LayoutStateWrapper.LAYOUT_START) {
                bottom = layoutState.getOffset() - mMarginBottom - mPaddingBottom;
                top = bottom - result.mConsumed;
            } else {
                top = layoutState.getOffset() + mMarginTop + mPaddingTop;
                bottom = top + result.mConsumed;
            }
        } else {
            int viewHeight = orientationHelper.getDecoratedMeasurementInOther(view);
            int available = parentHeight - viewHeight;
            if (available < 0) {
                available = 0;
            }

            top = helper.getPaddingTop() + mMarginTop + mPaddingTop + available / 2;
            bottom = helper.getContentHeight() - -mMarginBottom - mPaddingBottom - helper.getPaddingBottom() - available / 2;

            if (layoutState.getLayoutDirection() == VirtualLayoutManager.LayoutStateWrapper.LAYOUT_START) {
                right = layoutState.getOffset() - mMarginRight - mPaddingRight;
                left = right - result.mConsumed;
            } else {
                left = layoutState.getOffset() + mMarginLeft + mPaddingLeft;
                right = left + result.mConsumed;
            }
        }

        if (layoutInVertical) {
            result.mConsumed += getVerticalMargin() + getVerticalPadding();
        } else {
            result.mConsumed += getHorizontalMargin() + getHorizontalPadding();
        }

        layoutChild(view, left, top, right, bottom, helper);
    }

}
