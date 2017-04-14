package cn.gogoal.im.ui;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author wangjd on 2017/4/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int verticalSpace, horizontalSpace;
    private int spaceColor= Color.TRANSPARENT;
    public SpaceItemDecoration(int verticalSpace, int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
        this.verticalSpace = verticalSpace;
    }

    public SpaceItemDecoration(int verticalSpace, int horizontalSpace, @ColorInt int spaceColor) {
        this.horizontalSpace = horizontalSpace;
        this.verticalSpace = verticalSpace;
        this.spaceColor=spaceColor;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (parent.getChildLayoutPosition(view) % parent.getLayoutManager().getChildCount() == 0) {
                outRect.bottom = verticalSpace;
                outRect.left = 0;
            }else {
                outRect.left = horizontalSpace;
                outRect.bottom = verticalSpace;
            }
        }else if (parent.getLayoutManager() instanceof GridLayoutManager){
            int hovCount=((GridLayoutManager)parent.getLayoutManager()).getSpanCount();
            if (parent.getChildLayoutPosition(view)%hovCount==0){
                outRect.left=0;
            }else {
                outRect.left = horizontalSpace;
                outRect.bottom = verticalSpace;
            }
        }
    }

}