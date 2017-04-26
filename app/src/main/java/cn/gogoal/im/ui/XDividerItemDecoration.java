package cn.gogoal.im.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * author wangjd on 2017/3/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */

public abstract class XDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int lineWidth = 1;//px 分割线宽

    private static int defaultColoe = 0XFFCCCCCC;

    /**
     * A single color value in the form 0xAARRGGBB.
     **/
    public XDividerItemDecoration(Context context, float lineWidthDp, @ColorInt int dividerColor) {
        this.lineWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                lineWidthDp,
                context.getResources().getDisplayMetrics());
        defaultColoe = dividerColor;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(defaultColoe);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public XDividerItemDecoration(Context context, int lineWidthDp, @ColorInt int dividerColor) {
        this(context, (float) lineWidthDp, dividerColor);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //left, top, right, bottom
        int childCount = parent.getChildCount();
        //        int childCount2 = parent.getLayoutManager().getChildCount();
        //        int childCount3 = parent.getAdapter().getItemCount();
        //        Log.e("count", "getChildCount()=" + childCount1 + "-----getLayoutManager().getChildCount()=" + childCount2 + "----getAdapter().getItemCount()=" + childCount3);
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            int itemPosition = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();

            boolean[] sideOffsetBooleans = getItemSidesIsHaveOffsets(itemPosition);
            if (sideOffsetBooleans[0]) {
                drawChildLeftVertical(child, c, parent);
            }
            if (sideOffsetBooleans[1]) {
                drawChildTopHorizontal(child, c, parent);
            }
            if (sideOffsetBooleans[2]) {
                drawChildRightVertical(child, c, parent);
            }
            if (sideOffsetBooleans[3]) {
                drawChildBottomHorizontal(child, c, parent);
            }
        }
    }

    private void drawChildBottomHorizontal(View child, Canvas c, RecyclerView parent) {

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int left = child.getLeft() - params.leftMargin - lineWidth;
        int right = child.getRight() + params.rightMargin + lineWidth;
        int top = child.getBottom() + params.bottomMargin;
        int bottom = top + lineWidth;

        c.drawRect(left, top, right, bottom, mPaint);

    }

    private void drawChildTopHorizontal(View child, Canvas c, RecyclerView parent) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int left = child.getLeft() - params.leftMargin - lineWidth;
        int right = child.getRight() + params.rightMargin + lineWidth;
        int bottom = child.getTop() - params.topMargin;
        int top = bottom - lineWidth;

        c.drawRect(left, top, right, bottom, mPaint);

    }

    private void drawChildLeftVertical(View child, Canvas c, RecyclerView parent) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int top = child.getTop() - params.topMargin - lineWidth;
        int bottom = child.getBottom() + params.bottomMargin + lineWidth;
        int right = child.getLeft() - params.leftMargin;
        int left = right - lineWidth;

        c.drawRect(left, top, right, bottom, mPaint);

    }

    private void drawChildRightVertical(View child, Canvas c, RecyclerView parent) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int top = child.getTop() - params.topMargin - lineWidth;
        int bottom = child.getBottom() + params.bottomMargin + lineWidth;
        int left = child.getRight() + params.rightMargin;
        int right = left + lineWidth;

        c.drawRect(left, top, right, bottom, mPaint);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        //outRect 看源码可知这里只是把Rect类型的outRect作为一个封装了left,right,top,bottom的数据结构,
        //作为传递left,right,top,bottom的偏移值来用的

        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        //
        boolean[] sideOffsetBooleans = getItemSidesIsHaveOffsets(itemPosition);

        int left = sideOffsetBooleans[0] ? lineWidth : 0;
        int top = sideOffsetBooleans[1] ? lineWidth : 0;
        int right = sideOffsetBooleans[2] ? lineWidth : 0;
        int bottom = sideOffsetBooleans[3] ? lineWidth : 0;

        outRect.set(left, top, right, bottom);
    }

    /**
     * 顺序:left, top, right, bottom
     *
     * @return boolean[4]
     */
    public abstract boolean[] getItemSidesIsHaveOffsets(int itemPosition);


}
















