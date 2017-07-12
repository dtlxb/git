package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * author wangjd on 2017/7/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :版本更新弹窗
 */
public class UpdataDialog extends BaseCentDailog {

    public static UpdataDialog newDialog(String message, String versionName, String newAppUrl) {
        UpdataDialog dialog = new UpdataDialog();
        Bundle bundle = new Bundle();
        bundle.putString("updata_message", message);
        bundle.putString("version_name", versionName);
        bundle.putString("new_app_url", newAppUrl);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public int getWidth() {
        return 4 * AppDevice.getWidth(getContext()) / 5;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_updata;
    }

    @Override
    public void bindView(View v) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }

        TextView tvTitle = (TextView) v.findViewById(R.id.tv_dialog_title);
        TextView tvMessage = (TextView) v.findViewById(R.id.tv_dialog_msg);

        String versionName = bundle.getString("version_name");
        String versionMsg = bundle.getString("updata_message");
        String newAppUrl = bundle.getString("new_app_url");

        tvTitle.setText("发现新版本(" + versionName + ")");
        tvMessage.setText(StringUtils.getNotNullString(versionMsg));

        tvMessage.setMovementMethod(ScrollingMovementMethod.getInstance());

        v.findViewById(R.id.iv_dialog_dismis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdataDialog.this.dismiss();
            }
        });

        v.findViewById(R.id.btn_do_updata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下载 newAppUrl

            }
        });


    }
}
