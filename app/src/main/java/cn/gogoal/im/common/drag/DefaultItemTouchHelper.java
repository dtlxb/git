package cn.gogoal.im.common.drag;

import android.support.v7.widget.helper.ItemTouchHelper;

public class DefaultItemTouchHelper extends ItemTouchHelper {

    private DefaultItemTouchHelpCallback itemTouchHelpCallback;

    private DefaultItemTouchHelper(Callback callback) {
        super(callback);
        itemTouchHelpCallback= (DefaultItemTouchHelpCallback) callback;
    }

    /**初始化*/
    public static DefaultItemTouchHelper init(DefaultItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener){
        return new DefaultItemTouchHelper(new DefaultItemTouchHelpCallback(onItemTouchCallbackListener));
    }

    /**
     * 设置是否可以被拖拽
     */
    public void setDragEnable(boolean canDrag) {
        itemTouchHelpCallback.setDragEnable(canDrag);
    }

    /**
     * 设置是否可以被滑动
     */
    public void setSwipeEnable(boolean canSwipe) {
        itemTouchHelpCallback.setSwipeEnable(canSwipe);
    }
}
