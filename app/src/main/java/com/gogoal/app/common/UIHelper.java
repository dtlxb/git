package com.gogoal.app.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gogoal.app.R;
import com.gogoal.app.base.MyApp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;


/**
 * 应用程序UI工具包：封装UI相关的一些操作
 */
public class UIHelper {

    private static Toast mToast = null;

    private static Snackbar mSnackBar = null;

    public static void toast(Context cont, String msg) {
        if (cont == null || msg == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void toast(Context cont, int msg) {
        if (cont == null || msg <= 0) {
            return;
        }
        if (mToast == null) {
            try {
                mToast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
            } catch (Exception e) {
                mToast = Toast.makeText(cont, String.valueOf(msg), Toast.LENGTH_SHORT);
            }
        } else {
            try {
                mToast.setText(msg);
            } catch (Exception e) {
                mToast = Toast.makeText(cont, String.valueOf(msg), Toast.LENGTH_SHORT);
            }
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void toast(Context cont, String msg, int time) {
        if (cont == null || msg == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(cont, msg, time);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void toastErro(Context cont, String msg) {
        if (cont == null || msg == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(cont, AppDevice.isNetworkConnected(cont) ? msg : "当前网络不可用", Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void toastInCenterShort(Context cont, String msg) {
        if (cont == null || msg == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void toastInCenterLong(Context cont, String msg) {
        if (cont == null || msg == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showSnack(Activity context, String msg) {
        if (context == null || msg == null) {
            return;
        }
        if (mSnackBar == null) {
            mSnackBar = Snackbar.make(context.getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT);
        } else {
            mSnackBar.setText(msg);
        }
        mSnackBar.show();
    }

    public static void showSnack(Activity context, String msg, String actionString, View.OnClickListener listener) {
        if (context == null || msg == null) {
            return;
        }
        if (mSnackBar == null) {
            mSnackBar = Snackbar.make(context.getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT);
        } else {
            mSnackBar.setText(msg);
        }
        mSnackBar.setAction(actionString, listener);
        mSnackBar.show();
    }

    /**
     * 读取raw文件夹中文本或超文本内容为字符串
     */
    public static String getRawString(Context context, int rawId) {
        InputStream in = context.getResources().openRawResource(rawId);
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                br.close();
                isr.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 微信分享
     *
     * @param sharetType  :分享类型===0：微信好友，1：朋友圈，2：微信收藏;
     * @param context     上下文
     * @param url         分享的url
     * @param imageUrl    分享内容中的小图标
     * @param title       分享内容的标题
     * @param description 分享内容头部
     */
    public static void WXshare(int sharetType, final Context context, String url, String imageUrl, String title, String description) {
        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        WXWebpageObject webpage = new WXWebpageObject();
        // 要跳转的地址
        webpage.webpageUrl = url;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title == null ? "中国研网" : title;
        msg.description = description;
        // 网络图片地址 png 格式
        // 0:发送到朋友 1:发送到朋友圈 2:收藏
        final int shareWhat = sharetType;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        final Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 140, 140, true);
        if (imageUrl == null || imageUrl.length() == 0) {
            msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = shareWhat;
            MyApp.sApi.sendReq(req);
        } else {
            //下载图片，第二个参数是否缓存至内存中
            OkHttpUtils.get().url(imageUrl).build().execute(new BitmapCallback() {
                public void onError(Call call, Exception e) {
                }

                public void onResponse(Bitmap bitmap) {
                    if (bitmap != null) {
                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
                        msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        req.scene = shareWhat;
                        MyApp.sApi.sendReq(req);
                    } else {
                        msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        req.scene = shareWhat;
                        MyApp.sApi.sendReq(req);
                    }

                    if (!thumbBmp.isRecycled()) {
                        thumbBmp.recycle();
                    }
                }
            });
        }
    }

    /*
    * 微信分享弹窗
    * */
    public static void showShareDialog(final Context context, final String url, final String imageUrl, final String title, final String description) {

        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_share_layout, new LinearLayout(context), false);

        final Dialog share_dialog = DialogHelp.getBottomSheelNormalDialog(context, dialogView);

        TextView share_gogoal = (TextView) dialogView.findViewById(R.id.tv_dialog_share_gogoal);
        TextView share_wx = (TextView) dialogView.findViewById(R.id.tv_dialog_share_wx);
        TextView share_wx_circle = (TextView) dialogView.findViewById(R.id.tv_dialog_share_wx_circle);
        TextView share_copy = (TextView) dialogView.findViewById(R.id.tv_dialog_share_copy);
        TextView btn_dialog_cancle = (TextView) dialogView.findViewById(R.id.btn_dialog_cancle);

        //设置按钮监听
        View.OnClickListener shareOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share_dialog.dismiss();

                switch (view.getId()) {
                    case R.id.tv_dialog_share_gogoal:
                        toast(context, "Go-Goal好友");
                        break;
                    case R.id.tv_dialog_share_wx:
                        toast(context, "微信好友");
                        WXshare(0, context, url, imageUrl, title, description);
                        break;
                    case R.id.tv_dialog_share_wx_circle:
                        toast(context, "微信朋友圈");
                        WXshare(1, context, url, imageUrl, title, description);
                        break;
                    case R.id.tv_dialog_share_copy:
                        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        clip.setText(url);
                        toast(context, "复制成功");
                        break;
                }
            }
        };

        share_gogoal.setOnClickListener(shareOnClick);
        share_wx.setOnClickListener(shareOnClick);
        share_wx_circle.setOnClickListener(shareOnClick);
        share_copy.setOnClickListener(shareOnClick);
        btn_dialog_cancle.setOnClickListener(shareOnClick);

        share_dialog.show();
    }

}
