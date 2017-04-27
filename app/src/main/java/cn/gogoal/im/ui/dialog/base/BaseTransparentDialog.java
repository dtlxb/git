package cn.gogoal.im.ui.dialog.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import cn.gogoal.im.R;

/**
 * Created by dave.
 * Date: 2017/4/26.
 * Desc: description
 */
public class BaseTransparentDialog extends DialogFragment {
    private boolean isShow = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.LiveClose);

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        isShow = true;
    }

    public boolean isShow() {
        return isShow;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShow = false;
    }
}
