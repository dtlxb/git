package cn.gogoal.im.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author wangjd on 2017/6/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :虚线分割线
 */
public class DashlineItemDivider extends RecyclerView.ItemDecoration {

    private static final int dashWidth = 10;//dp value

    private static final int dashDivider = 4;//dp value

    private static final int dashColoe = 0xffe4e4e4;

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            //以下计算主要用来确定绘制的位置
            final int top = child.getBottom() + params.bottomMargin;

            //绘制虚线
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(/*dp2px(parent.getContext(), */1);
            paint.setColor(dashColoe);
            Path path = new Path();
            path.moveTo(left, top);
            path.lineTo(right, top);
            PathEffect effects = new DashPathEffect(new float[]{
                    dp2px(parent.getContext(), dashWidth),
                    dp2px(parent.getContext(), dashDivider),
                    dp2px(parent.getContext(), dashWidth),
                    dp2px(parent.getContext(), dashDivider)
            }, 0);
            paint.setPathEffect(effects);
            c.drawPath(path, paint);


        }
    }

    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f * (dpValue >= 0 ? 1 : -1));
    }

}
