package cn.gogoal.im.ui.dialog.base;

import android.view.Gravity;

import cn.gogoal.im.R;

/**
 * author wangjd on 2017/4/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public abstract class BaseTopDialog extends BaseDialog {
    @Override
    public int getDialogStyle() {
        return R.style.BaseTopDialog;
    }

    @Override
    public int gravity() {
        return Gravity.TOP;
    }

}
