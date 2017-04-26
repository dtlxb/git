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

    public static WaitDialog getInstance(String text, int iconRes, boolean loading) {
        WaitDialog dialog = new WaitDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("DIALOG_ICON", iconRes);
        bundle.putString("DIALOG_TEXT", text);
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
        return 0;
    }

    @Override
    public void bindView(View v) {
        int dialogIcon = getArguments().getInt("DIALOG_ICON", 0);
        String dialogText = getArguments().getString("DIALOG_TEXT", "提示");

        imgIcon= (ImageView) v.findViewById(R.id.img_dialog_gg_submit);
        textView= (TextView) v.findViewById(R.id.tv_dialog_gg_submit);

        if (dialogIcon!=0) {
            imgIcon.setImageResource(dialogIcon);
            if (getArguments().getBoolean("DIALOG_LOADING")) {
                animation = AnimationUtils.getInstance().setLoadingAnime(imgIcon, dialogIcon);
                animation.startNow();
            }else {
                imgIcon.setImageResource(dialogIcon);
            }
        }

        textView.setText(dialogText);
    }

    @Override
    public int getWidth() {
        return 5 * AppDevice.getWidth(getActivity()) / 12;
    }

    @Override
    public int getHeight() {
        return 5 * AppDevice.getWidth(getActivity()) / 12;
    }

    public WaitDialog updataText(String text){
       textView.setText(text);
        return this;
    }

    public WaitDialog updataIcon(@DrawableRes int imgId){
       imgIcon.setImageResource(imgId);
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (null!=animation)
            animation.cancel();
        }catch (Exception e){
            e.getMessage();
        }
    }
}