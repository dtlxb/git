package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.gogoal.im.R;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * author wangjd on 2017/5/17 0017.
 * Staff_id 1375
 * phone 18930640263
 * description :双击消息全屏查看
 */
public class MessageFullScreen extends BaseCentDailog {

    public static MessageFullScreen newInstance(String text) {
        if (text == null) {
            text = "";
        }
        MessageFullScreen toast = new MessageFullScreen();
        Bundle bundle = new Bundle();
        bundle.putString("TOAST_TEXT", text);
        toast.setArguments(bundle);
        return toast;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_toast;
    }

    @Override
    public void bindView(View v) {
        String toastText = getArguments().getString("TOAST_TEXT");
        TextView tvToast = (TextView) v.findViewById(R.id.tv_dialog_toast);
        tvToast.setText(toastText);
    }

    @Override
    public boolean getCancelOutside() {
        return false;
    }
}
