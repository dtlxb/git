package cn.gogoal.im.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Author wangjd on 2017/7/7 0007.
 * EmployeeNumber 1375
 * Phone 18930640263
 * Description :==描述==
 */
public class CatchLayoutManager extends LinearLayoutManager {

    public CatchLayoutManager(Context context) {
        super(context);
    }

    public CatchLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    //... constructor
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("probe", "meet a IOOBE in RecyclerView");
        }
    }
}
