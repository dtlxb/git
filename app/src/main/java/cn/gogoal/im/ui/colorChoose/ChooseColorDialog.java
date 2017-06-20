package cn.gogoal.im.ui.colorChoose;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

/**
 * author wangjd on 2017/6/20 0020.
 * Staff_id 1375
 * phone 18930640263
 * description :颜色选择器
 */
public class ChooseColorDialog {
    private AlertDialog dialog;
    private ColorPalette colorPalette;
    private OnColorSelectListener onColorSelectListener;
    private int color=-1;

    public ChooseColorDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        colorPalette = new ColorPalette(context);
        colorPalette.setLastColor(Color.BLUE);

        builder.setView(colorPalette);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onColorSelectListener != null) {
                    color=colorPalette.getSelectColor();
                    onColorSelectListener.onSelectFinish(color);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        dialog = builder.create();

    }


    public void show() {
        if(color==-1){
            color=Color.BLACK;
        }
        colorPalette.setLastColor(color);
        colorPalette.setCurrColor(color);
        dialog.show();
    }

    public static interface OnColorSelectListener {
        public void onSelectFinish(int color);
    }

    public OnColorSelectListener getOnColorSelectListener() {
        return onColorSelectListener;
    }

    public void setOnColorSelectListener(OnColorSelectListener onColorSelectListener) {
        this.onColorSelectListener = onColorSelectListener;
    }

    public void setLastColor(int color){
        colorPalette.setLastColor(color);
    }
}
