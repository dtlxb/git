package com.gogoal.app.base;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.duanqu.qupai.jni.ApplicationGlue;
import com.gogoal.app.common.AppConst;
import com.gogoal.app.common.IMHelpers.AVImClientManager;
import com.gogoal.app.common.IMHelpers.MyConversationHandler;
import com.gogoal.app.common.IMHelpers.MyMessageHandler;
import com.gogoal.app.common.SPTools;
import com.gogoal.app.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * author wangjd on 2017/2/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public class MyApp extends Application {

    public static IWXAPI sApi;

    private static MyApp app;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        sApi = WXEntryActivity.initWeiXin(this, AppConst.WEIXIN_APP_ID);//初始化组件
        SPTools.initSharedPreferences(this);

        //初始化参数依次this，AppId,AppKey
        AVOSCloud.initialize(this, "dYRQ8YfHRiILshUnfFJu2eQM-gzGzoHsz", "ye24iIK6ys8IvaISMC4Bs5WK");
        //AVOSCloud.initialize(this,"R7vH8N41V1rqJIqrlTQ1mMnR-gzGzoHsz","4iXr2Ylh1VwVyYjaxs3ufFmo");
        //启用北美节点
//        AVOSCloud.useAVCloudUS();
        //必须在启动的时候注册 MessageHandler
        //注册默认的消息处理逻辑
        AVIMMessageManager.registerMessageHandler(AVIMMessage.class, new MyMessageHandler());
        AVIMMessageManager.setConversationEventHandler(new MyConversationHandler());

        //连接服务器
        AVImClientManager.getInstance().open(AppConst.LEAN_CLOUD_TOKEN, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
            }
        });

        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            //TODO im初始化

            //TODO 注册消息接收器

        }

        //阿里云推流
        System.loadLibrary("gnustl_shared");
//        System.loadLibrary("ijkffmpeg");//目前使用微博的ijkffmpeg会出现1K再换wifi不重连的情况
        System.loadLibrary("qupai-media-thirdparty");
//        System.loadLibrary("alivc-media-jni");
        System.loadLibrary("qupai-media-jni");
        ApplicationGlue.initialize(this);

    }

    /**
     * @return 获取当前运行的进程名
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    public static Context getContext() {
        return app.getApplicationContext();
    }
}
