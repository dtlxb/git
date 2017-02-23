package com.gogoal.app.base;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.gogoal.app.common.AppConst;
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

        app=this;

        sApi = WXEntryActivity.initWeiXin(this, AppConst.WEIXIN_APP_ID);//初始化组件
        SPTools.initSharedPreferences(this);

        //初始化参数依次this，AppId,AppKey
        AVOSCloud.initialize(this, "dYRQ8YfHRiILshUnfFJu2eQM-gzGzoHsz", "ye24iIK6ys8IvaISMC4Bs5WK");
        //AVOSCloud.initialize(this,"hi22KV7K693uIQLX5X4ROSbs-gzGzoHsz","qTkdjmpyuVdJAearcTthBw5N");
        //启用北美节点
//        AVOSCloud.useAVCloudUS();
        //必须在启动的时候注册 MessageHandler
        //注册默认的消息处理逻辑
        AVIMMessageManager.registerMessageHandler(AVIMMessage.class, new MyMessageHandler());
        AVIMMessageManager.setConversationEventHandler(new MyConversationHandler());

        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //TODO im初始化

            //TODO 注册消息接收器

        }

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


    public static Context getContext(){
        return app.getApplicationContext();
    }
}
