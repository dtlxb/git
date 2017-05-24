package cn.gogoal.im.common;

import android.view.View;

/**
 * author wangjd on 2017/5/23 0023.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class OnLayoutChangeListener implements View.OnLayoutChangeListener{

    private ViewHeightChange li;

    public OnLayoutChangeListener(ViewHeightChange li) {
        this.li = li;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        li.getHeightMeasure(v.getHeight());
    }

    public interface ViewHeightChange{
        void getHeightMeasure(int height);
    }
}
