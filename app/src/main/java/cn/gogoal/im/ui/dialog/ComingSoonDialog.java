package cn.gogoal.im.ui.dialog;

import android.view.View;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * author wangjd on 2017/4/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :开发中
 */
public class ComingSoonDialog extends BaseCentDailog {
    @Override
    public int getLayoutRes() {
        return R.layout.dialog_touyan_coming_soon;
    }

    @Override
    public void bindView(View v) {
        v.findViewById(R.id.img_touyan_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComingSoonDialog.this.dismiss();
            }
        });
    }

    @Override
    public int getWidth() {
        return 2* AppDevice.getWidth(getContext()) / 3;
    }
}
