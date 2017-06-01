package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * author wangjd on 2017/6/1 0001.
 * Staff_id 1375
 * phone 18930640263
 * description :中间的通用弹窗——取消，确定按钮，消息体
 */
public class NormalAlertDialog extends BaseCentDailog {

    private View.OnClickListener listener;

    public static NormalAlertDialog newInstance(
            String message,
            String ok,
            View.OnClickListener okClickListener) {

        NormalAlertDialog dialog = new NormalAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putString("ok", ok);

        dialog.listener = okClickListener;

        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_norma_alert;
    }

    @Override
    public void bindView(View v) {
        TextView tvMsg = (TextView) v.findViewById(R.id.tv_dialog_message);
        TextView btnCancle = (TextView) v.findViewById(R.id.btn_cancle);
        final TextView btnOk = (TextView) v.findViewById(R.id.btn_ok);

        String ok = getArguments().getString("ok");
        String message = getArguments().getString("message");

        tvMsg.setText(message);
        btnCancle.setText("取消");
        btnOk.setText(ok);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalAlertDialog.this.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalAlertDialog.this.dismiss();
                listener.onClick(btnOk);
            }
        });
    }

    @Override
    public int getWidth() {
        return 4 * AppDevice.getWidth(getActivity()) / 5;
    }

}
