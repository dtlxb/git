package com.example.dell.bzbp_frame.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ManifestUtil;

/**
 * 这个类是微信回调的类
 */
public class WXEntryActivity extends Activity implements com.tencent.mm.sdk.openapi.IWXAPIEventHandler {

    private com.tencent.mm.sdk.openapi.IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = com.tencent.mm.sdk.openapi.WXAPIFactory.createWXAPI(this,  ManifestUtil.getWeixinKey(this), false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);

    }

    @Override
    public void onReq(com.tencent.mm.sdk.modelbase.BaseReq req) {

    }

    @Override
    public void onResp(com.tencent.mm.sdk.modelbase.BaseResp resp) {
        Intent intent = new Intent();
        intent.setAction(ShareConstant.ACTION_WEIXIN_CALLBACK);
        intent.putExtra(ShareConstant.EXTRA_WEIXIN_RESULT, resp.errCode);
        sendBroadcast(intent);
        finish();
    }
}
