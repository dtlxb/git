package com.gogoal.app.ui.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.ViewGroup;
import android.view.Window;

import com.gogoal.app.common.AppDevice;

/**
 * author wangjd on 2017/2/23 0023.
 * Staff_id 1375
 * phone 18930640263
 */
public class BottomSheetDialog extends android.support.design.widget.BottomSheetDialog {

    public BottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public BottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int dialogHeight = AppDevice.getHeight(getContext()) - AppDevice.getStatusBarHeight(getContext());
        Window window=getWindow();
        try {
            if (window!=null)
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}
