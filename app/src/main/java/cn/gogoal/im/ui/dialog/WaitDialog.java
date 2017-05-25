package cn.gogoal.im.ui.dialog;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import cn.gogoal.im.R;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.ui.dialog.base.BaseCentDailog;

/**
 * author wangjd on 2017/4/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :等待弹窗
 */
public class WaitDialog extends BaseCentDailog {

    private RotateAnimation animation;
    private ImageView imgIcon;
    private TextView textView;
    private boolean cancelOutside;

    private float mask;

    public static WaitDialog getInstance(String text, int iconRes, boolean loading) {
        WaitDialog dialog = new WaitDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("DIALOG_ICON", iconRes);
        bundle.putString("DIALOG_TEXT", text);
        bundle.putBoolean("DIALOG_LOADING", loading);
        dialog.setArguments(bundle);

        return dialog;
    }

    public static WaitDialog getInstance(float mask,String text, int iconRes, boolean loading) {
        WaitDialog dialog = new WaitDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("DIALOG_ICON", iconRes);
        bundle.putString("DIALOG_TEXT", text);
        bundle.putFloat("mask",mask);
        bundle.putBoolean("DIALOG_LOADING", loading);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_gg_submit;
    }

    @Override
    public float getDimAmount() {
        return mask;
    }

    @Override
    public void bindView(View v) {
        int dialogIcon = getArguments().getInt("DIALOG_ICON", 0);
        String dialogText = getArguments().getString("DIALOG_TEXT", "提示");
        mask=getArguments().getFloat("mask");

        imgIcon = (ImageView) v.findViewById(R.id.img_dialog_gg_submit);
        textView = (TextView) v.findViewById(R.id.tv_dialog_gg_submit);

        if (dialogIcon != 0) {
            imgIcon.setImageResource(dialogIcon);
            if (getArguments().getBoolean("DIALOG_LOADING")) {
                animation = AnimationUtils.getInstance().setLoadingAnime(imgIcon, dialogIcon);
                animation.startNow();
            } else {
                imgIcon.setImageResource(dialogIcon);
            }
        } else {
            imgIcon.setVisibility(View.GONE);
        }

        if (!StringUtils.isActuallyEmpty(dialogText)) {
            textView.setText(dialogText);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean getCancelOutside() {
        return cancelOutside;
    }

    public void setCancelOutside(boolean cancelOutside) {
        this.cancelOutside = cancelOutside;
    }

    @Override
    public int getWidth() {
        return 5 * AppDevice.getWidth(getActivity()) / 12;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    public WaitDialog updataText(String text) {
        textView.setText(text);
        return this;
    }

    public WaitDialog updataIcon(@DrawableRes int imgId) {
        imgIcon.setImageResource(imgId);
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (null != animation)
                animation.cancel();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void dismiss(boolean immediately) {
        if (immediately) {
            super.dismiss();
        } else {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    WaitDialog.this.dismiss();
                }
            }, 1000);
        }
    }

    public void dismiss(boolean immediately, final boolean finish) {
        if (immediately) {
            super.dismiss();
        } else {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    WaitDialog.this.dismiss();
                    if (finish) {
                        WaitDialog.this.getActivity().finish();
                    }
                }
            }, 1000);
        }
    }

    public void dismiss(boolean immediately, final DialogDismiss dialogDismiss) {
        if (immediately) {
            super.dismiss();
        } else {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    WaitDialog.this.dismiss();
                    dialogDismiss.dialogDismiss();
                }
            }, 1000);
        }
    }

    public enum GGDialogType {
        SUCCESS(0), LOADING(1), ERROR(2);

        private int dialogType;

        GGDialogType(int dialogType) {
            this.dialogType = dialogType;
        }
    }


    public interface DialogDismiss {
        void dialogDismiss();
    }

    public static WaitDialog showNormalDialog(String dialogText, GGDialogType type) {

        WaitDialog dialog = WaitDialog.getInstance(dialogText,
                type == GGDialogType.LOADING ? R.mipmap.login_loading :
                        (type == GGDialogType.SUCCESS ? R.mipmap.login_success : R.mipmap.login_error),
                type == GGDialogType.LOADING);

        if (type == GGDialogType.ERROR) {
            dialog.dismiss(false);
        }

        return dialog;
    }
}
