package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * author wangjd on 2017/6/1 0001.
 * Staff_id 1375
 * phone 18930640263
 * description :中间的通用弹窗——单按钮，消息体
 */
public class NormalSingleAlertDialog extends BaseCentDailog {

    private View.OnClickListener listener;

    /**
     * 双参数
     *
     * @param message:弹窗提示文本
     * @param okClickListener 确定按钮点击事件
     * @param submitText      确定按钮的文本
     */
    public static NormalSingleAlertDialog newInstance(
            String message,
            String submitText,
            View.OnClickListener okClickListener) {

        NormalSingleAlertDialog dialog = new NormalSingleAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putString("submit_text", submitText);
        dialog.listener = okClickListener;

        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_norma_single_alert;
    }

    @Override
    public void bindView(View v) {
        TextView tvMsg = (TextView) v.findViewById(R.id.tv_dialog_message);
        final TextView btnOk = (TextView) v.findViewById(R.id.btn_ok);

        String submitText = getArguments().getString("submit_text");
        String message = getArguments().getString("message");

        tvMsg.setText(message);
        btnOk.setText(StringUtils.isActuallyEmpty(submitText) ? "确定" : submitText);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {
                    NormalSingleAlertDialog.this.dismiss();
                } else {
                    listener.onClick(btnOk);
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalSingleAlertDialog.this.dismiss();
                listener.onClick(btnOk);
            }
        });
    }

    @Override
    public int getWidth() {
        return 4 * AppDevice.getWidth(getActivity()) / 5;
    }

}
