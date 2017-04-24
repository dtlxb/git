package cn.gogoal.im.ui.widget;

import android.view.Gravity;

import cn.gogoal.im.R;

/**
 * author wangjd on 2017/2/28 0028.
 * Staff_id 1375
 * phone 18930640263
 */

public abstract class BaseBottomDialog extends BaseDialog {
    @Override
    public int getDialogStyle() {
        return R.style.BaseBottomDialog;
    }

    @Override
    public int gravity() {
        return Gravity.BOTTOM;
    }

}