package cn.gogoal.im.ui.dialog;

import android.view.View;

import cn.gogoal.im.R;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * author wangjd on 2017/7/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ProgressDialog extends BaseCentDailog {

    @Override
    public float getDimAmount() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.widget_loading_page;
    }

    @Override
    public void bindView(View v) {

    }
}
