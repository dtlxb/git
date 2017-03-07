package com.gogoal.app.common.wjd;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.gogoal.app.R;
import com.gogoal.app.common.AppConst;
import com.gogoal.app.common.UIHelper;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * author wangjd on 2017/3/7 0007.
 * Staff_id 1375
 * phone 18930640263
 */
public class WechatOperator {

    private volatile static WechatOperator instance = null;

    public static WechatOperator getInstance(Context context) {
        //先检查实例是否存在，如果不存在才进入下面的同步块
        if (instance == null) {
            //同步块，线程安全的创建实例
            synchronized (WechatOperator.class) {
                //再次检查实例是否存在，如果不存在才真正的创建实例
                if (instance == null) {
                    instance = new WechatOperator(init(context, AppConst.WEIXIN_APP_ID));
                }
            }
        }
        return instance;
    }

    private IWXAPI iwxapi;

    private WechatOperator(IWXAPI iwxapi) {
        this.iwxapi=iwxapi;
    }

    public IWXAPI getIwxapi() {
        return iwxapi;
    }

    private IWechatCallback callback;

    public IWechatCallback getCallback() {
        return callback;
    }

    /**初始化微信sdk*/
    public static IWXAPI init(Context context,String appId) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, appId, true);

        if (iwxapi.isWXAppInstalled() && iwxapi.isWXAppSupportAPI() && iwxapi.registerApp(appId)) {
            return iwxapi;
        }else {
            UIHelper.toast(context,context.getString(R.string.donot_found_wechat), Toast.LENGTH_LONG);
            return null;
        }
    }

    public void login(IWechatCallback callback) {
        this.callback=callback;

        // 唤起微信登录授权
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_login";
        // 失败回调
        if (!iwxapi.sendReq(req) && callback != null) {
            callback.onFailed("WXEntryActivity回调出错");
        }
//        else {
//            if (callback != null)
//                callback.onSuccess("");
//        }
    }

    public void shareSession(Context context,Share share, IWechatCallback callback) {
        share(context,share, SendMessageToWX.Req.WXSceneSession, callback);
    }

    public void shareTimeLine(Context context,Share share, IWechatCallback callback) {
        share(context,share, SendMessageToWX.Req.WXSceneTimeline, callback);
    }

    private void share(Context context,Share share, int scene, IWechatCallback callback) {
        this.callback=callback;

        //1.初始化一个WXTextObject对象,填写分享的文本内容
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = share.getUrl();
        wxWebpageObject.extInfo = share.getDescription();

        //2.用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.title = share.getTitle();
        msg.mediaObject = wxWebpageObject;
        msg.description = share.getDescription();

        Bitmap bitmap = share.getThumbBitmap();
        if (bitmap == null) {
            bitmap = OpenUtils.getShareBitmap(context, share.getBitmapResID());
        }
        msg.setThumbImage(bitmap);

        //3.构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = OpenUtils.buildTransaction("webPage");
        //transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = scene;

        //4.发送这次分享
        boolean sendReq = iwxapi.sendReq(req);
        //发送请求失败,回调
        if (!sendReq && callback != null) {
            callback.onFailed("WXEntryActivity回调出错，发送请求失败");
            if (bitmap!=null)
            bitmap.recycle();
        } else {
//            if (callback != null)
//                callback.onSuccess("");
            if (bitmap!=null)
            bitmap.recycle();
        }
    }
}
