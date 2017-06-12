package cn.gogoal.im.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.gogoal.im.R;
import cn.gogoal.im.common.openServices.weixin.WechatOperator;
import cn.gogoal.im.ui.view.XLayout;
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

        KLog.e(cont.getClass().getSimpleName());

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
        KLog.e(cont.getClass().getSimpleName());
        mToast.show();
    }

    public static View getEmptyView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_no_data, new LinearLayout(context), false);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.img_no_data);
        AppDevice.setViewWidth$Height(imageView, AppDevice.getWidth(context) / 5, -2);
        return inflate;
    }

    public static void toastError(Context cont, String msg, XLayout xLayout) {
        if (cont == null || msg == null || xLayout == null) {
            KLog.e("执行空");
            return;
        }
        if (mToast == null) {
            if (AppDevice.isNetworkConnected(cont)) {
                mToast = Toast.makeText(cont,"服务器开小差了...", Toast.LENGTH_LONG);
                xLayout.setStatus(XLayout.Error);
            } else {
                mToast = Toast.makeText(cont, "网络断开了一下", Toast.LENGTH_LONG);
                xLayout.setStatus(XLayout.No_Network);
            }
        } else {
            if (AppDevice.isNetworkConnected(cont)) {
                mToast.setText("服务器开小差了...");
                xLayout.setStatus(XLayout.Error);
            } else {
                mToast.setText("网络断开了一下");
                xLayout.setStatus(XLayout.No_Network);
            }
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        KLog.e(cont.getClass().getSimpleName());
        mToast.show();
    }

    public static void toastError(Context cont, String msg) {
        if (cont == null || msg == null) {
            return;
        }
        if (mToast == null) {
            if (AppDevice.isNetworkConnected(cont)) {
                mToast = Toast.makeText(cont, "服务器开小差了...", Toast.LENGTH_LONG);
            } else {
                mToast = Toast.makeText(cont, "网络断开了一下", Toast.LENGTH_LONG);
            }
        } else {
            mToast.setText("服务器开小差了...");
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        KLog.e(cont.getClass().getSimpleName());

        mToast.show();
    }

    public static void toastResponseError(Context cont, String response) {
        if (cont == null || response == null) {
            return;
        }
        String errorMessage = JSONObject.parseObject(response).getString("message");

        if (mToast == null) {
            mToast = Toast.makeText(cont, errorMessage, Toast.LENGTH_LONG);
        } else {
            mToast.setText(errorMessage);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public static void toastInCenter(Context cont, String msg) {
        if (cont == null || msg == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast = null;
            toastInCenter(cont, msg);
        }
        mToast.show();
    }

    public static void toastInCenter(Context cont, String msg, int toastDuration) {
        if (cont == null || msg == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(cont, msg, toastDuration);
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

    public static <T extends View> T findViewById(View parent, @IdRes int id) {
        return (T) parent.findViewById(id);
    }

    /**
     * 读取raw文件夹中文本或超文本内容为字符串
     */
    public static String getRawString(Context context, int rawId) {
        InputStream in = context.getResources().openRawResource(rawId);
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
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
        final IWXAPI iwxapi = WechatOperator.init(context, AppConst.WEIXIN_APP_ID);
        if (iwxapi == null) {
            UIHelper.toast(context, context.getString(R.string.donot_found_wechat));
            return;
        }
        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        WXWebpageObject webpage = new WXWebpageObject();
        // 要跳转的地址
        webpage.webpageUrl = url;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title == null ? "GoGoal" : title;
        msg.description = description;
        // 网络图片地址 png 格式
        // 0:发送到朋友 1:发送到朋友圈 2:收藏
        final int shareWhat = sharetType;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
        final Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 140, 140, true);
        if (imageUrl == null || imageUrl.length() == 0) {
            msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = shareWhat;
            iwxapi.sendReq(req);
        } else {
            //下载图片，第二个参数是否缓存至内存中
            OkHttpUtils.get().url(imageUrl).build().execute(new BitmapCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(Bitmap bitmap, int id) {
                    if (bitmap != null) {
                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
                        msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        req.scene = shareWhat;
                        iwxapi.sendReq(req);
                    } else {
                        msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        req.scene = shareWhat;
                        iwxapi.sendReq(req);
                    }

                    if (!thumbBmp.isRecycled()) {
                        thumbBmp.recycle();
                    }
                }
            });
        }
    }

    //验证是否手机号
    public static boolean GGPhoneNumber(String string, Context ctx) {

        if (string.length() < 11) {

            UIHelper.toast(ctx, "手机号长度不足11位");
            return false;
        }

        if (!StringUtils.checkPhoneString(string)) {

            UIHelper.toast(ctx, "手机号输入不合法");
            return false;
        }

        return true;
    }

    //验证验证码
    public static boolean GGCode(String string, Context ctx) {

        if (string.length() == 0) {

            UIHelper.toast(ctx, "验证码不能为空");
            return false;
        }
        return true;
    }

    //验证密码是否6-16位的数字，字母
    public static boolean isGGPassWord(String string, Context ctx) {
        //String regex = "^([a-z]|[A-Z]|[0-9]){6,16}$";
        if (string.length() < 6 || string.length() > 16) {
            UIHelper.toast(ctx, "密码必须为6-16位,包含数字和字母");
            return false;
        }
        return true;
    }

    public static void setRippBg(View view) {
        if (view != null) {
            TypedValue typedValue = new TypedValue();
            view.getContext().getTheme().resolveAttribute(
                    android.R.attr.selectableItemBackground, typedValue, true);
            view.setBackgroundResource(typedValue.resourceId);
        }
    }

    public static void passwordToggle(final EditText etPsw, final CheckBox chToggle) {
        etPsw.setTransformationMethod(PasswordTransformationMethod
                .getInstance());  //以密文显示

        chToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPsw.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());  //密码以明文显示
                    etPsw.setSelection(etPsw.getText().length());
//                    chToggle.setButtonDrawable(ContextCompat.getDrawable(context, R.mipmap.img_psw_eye_open));
                } else {
                    etPsw.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());  //以密文显示
                    etPsw.setSelection(etPsw.getText().length());
//                    chToggle.setButtonDrawable(ContextCompat.getDrawable(context, R.mipmap.img_psw_eye_closed));
                }
            }
        });
    }
}
