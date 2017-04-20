package cn.gogoal.im.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import cn.gogoal.im.ui.widget.BottomSheetListDialog;
import cn.gogoal.im.ui.widget.BottomSheetNormalDialog;
import cn.gogoal.im.ui.widget.ShareBottomDialog;


/**
 * 对话框辅助类
 */
public class DialogHelp {

    /***
     * 获取一个dialog
     *
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

    /***
     * 获取一个耗时等待对话框
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog getWaitDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /***
     * 获取一个信息对话框，注意需要自己手动调用show方法显示
     *
     * @param context
     * @param message
     * @param onClickListener
     */

    public static AlertDialog.Builder getMessageDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onClickListener);
        return builder;
    }

    public static AlertDialog.Builder getMessageDialog(Context context, String message) {
        return getMessageDialog(context, message, null);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);

        builder.create().dismiss();
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onOkClickListener, DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onOkClickListener);
        builder.setNegativeButton("取消", onCancleClickListener);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context,
                                                       String message,
                                                       String okString,
                                                       String cancleString,
                                                       DialogInterface.OnClickListener onOkClickListener,
                                                       DialogInterface.OnClickListener onCancleClickListener) {
        return getConfirmDialog(context, "", message, okString, cancleString, onOkClickListener, onCancleClickListener);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context,
                                                       String title,
                                                       String message,
                                                       String okString,
                                                       String cancleString,
                                                       DialogInterface.OnClickListener onOkClickListener,
                                                       DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        builder.setPositiveButton(okString, onOkClickListener);
        builder.setNegativeButton(cancleString, onCancleClickListener);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setPositiveButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[] arrays, DialogInterface.OnClickListener onClickListener, boolean needButton) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (needButton) {
            builder.setPositiveButton("取消", null);
        }
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, "", arrays, onClickListener);
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setNegativeButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, "", arrays, selectIndex, onClickListener);
    }

    /**
     * 弹出window窗体
     *
     * @param context    上下文
     * @param dialogView 弹窗视图
     */
    public static AlertDialog getWindoDialog(Context context, View dialogView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setContentView(dialogView);

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = 5 * AppDevice.getWidth(context) / 6;
            window.setAttributes(params);
        }
        return dialog;
    }

    /**
     * 弹出window窗体
     *
     * @param context           上下文
     * @param dialogView        弹窗视图
     * @param dialogWindowWidth 窗体宽度
     */
    public static AlertDialog getWindoDialog(Context context, View dialogView, int dialogWindowWidth, int dialogWindowHeight) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dialogWindowWidth;
            lp.height = dialogWindowHeight;
            window.setAttributes(lp);

            window.setContentView(dialogView);
        }
        return dialog;
    }

    /**
     * 获取一个等待对话框
     */
    public static ProgressDialog getProgressDialog(Context context) {
        return new ProgressDialog(context);
    }

    /**
     * 获取一个等待对话框
     */
    public static ProgressDialog getProgressDialog(Context context, boolean cancelable) {
        ProgressDialog dialog = getProgressDialog(context);
        dialog.setCancelable(cancelable);
        return dialog;
    }

    /**
     * 获取一个等待对话框
     */
    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog dialog = getProgressDialog(context);
        dialog.setMessage(message);
        return dialog;
    }

    /**
     * 获取一个等待对话框
     */
    public static ProgressDialog getProgressDialog(
            Context context, String title, String message, boolean cancelable) {
        ProgressDialog dialog = getProgressDialog(context);
        dialog.setCancelable(cancelable);
        dialog.setTitle(title);
        dialog.setMessage(message);
        return dialog;
    }

    /**
     * 获取一个等待对话框
     */
    public static ProgressDialog getProgressDialog(
            Context context, String message, boolean cancelable) {
        ProgressDialog dialog = getProgressDialog(context);
        dialog.setCancelable(cancelable);
        dialog.setMessage(message);
        return dialog;
    }

    /**
     * 通用底部弹出窗
     */
    public static void getBottomSheelNormalDialog(FragmentActivity context, @LayoutRes int dialogViewId, BottomSheetNormalDialog.ViewListener listener) {
        if (context != null) {
            BottomSheetNormalDialog normalDialog = BottomSheetNormalDialog.create(context.getSupportFragmentManager())
                    .setLayoutRes(dialogViewId)
                    .setViewListener(listener)
                    .setTag("BottomSheetNormalDialog");
            normalDialog.show();
        }
    }

    /**
     * 好用到只需传一个数据的列表类弹窗
     */
    public static void getBottomSheetListDialog(FragmentActivity context, ArrayList<String> list, BottomSheetListDialog.DialogItemClick listener) {
        final BottomSheetListDialog listDialog = BottomSheetListDialog.getInstance(list);
        listDialog.show(context.getSupportFragmentManager());
        listDialog.setOnDialogItemClickListener(listener);
    }

    /*
   * 微信分享弹窗
   * */
    public static void showShareDialog(FragmentActivity context, final String url, final String imageUrl, final String title, final String description) {
        ShareBottomDialog shareDialog = ShareBottomDialog.getInstance(url, imageUrl, title, description);
        shareDialog.show(context.getSupportFragmentManager());
    }


}
