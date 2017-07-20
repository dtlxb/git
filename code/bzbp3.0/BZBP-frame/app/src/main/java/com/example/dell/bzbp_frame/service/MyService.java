package com.example.dell.bzbp_frame.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.dell.bzbp_frame.R;
import com.example.dell.bzbp_frame.RouteActivity;
import com.example.dell.bzbp_frame.model.MyLatlng;
import com.example.dell.bzbp_frame.model.Route;
import com.example.dell.bzbp_frame.model.User;

/*
 *  用户从route界面离开app的时候，此Service启动，在后台继续记录路线。
 */
public class MyService extends Service implements AMapLocationListener{

    public static final String TAG = "MyService";
    private NotificationManager mNotifyManager;
    private Route route;
    private User user;

    public MyService() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent i = new Intent(this, RouteActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("user",user);
        b.putBoolean("service_back",true);
        i.putExtras(b);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        //通知栏提醒
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                //设置点击功能
                .setContentIntent(pi)
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle("记录路线中...")
                //设置通知内容
                .setContentText("若离开界面之前已暂停，则不会记录");
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        mNotifyManager.notify(1, builder.build());
        Log.d(TAG, "onCreate() executed");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mNotifyManager.cancel(1);
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }

        if(null != alarmReceiver){
            unregisterReceiver(alarmReceiver);
            alarmReceiver = null;
        }
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    public class LocalBinder extends Binder {
        public MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }


    //下面开始实现后台定位功能

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;
    private Intent alarmIntent;
    private PendingIntent alarmPi;
    private AlarmManager alarm;
    private BroadcastReceiver alarmReceiver;

    public void Doit(){
        //route.getLocation_list().clear();
        //已经能够来回传route，并调用service中的方法了。
        //下一步就是在这个函数里持续记录route。

        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);

        // 创建Intent对象，action为LOCATION
        alarmIntent = new Intent();
        alarmIntent.setAction("LOCATION");
        IntentFilter ift = new IntentFilter();

        // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
        // 也就是发送了action 为"LOCATION"的intent
        alarmPi = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        // AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        //动态注册一个广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOCATION");
        registerReceiver(alarmReceiver, filter);

        startWork();
    }

    private void startWork(){

        initOption();

        //设置闹钟间隔
        int alarmInterval = 5;

        // 设置定位参数
        locationClient.setLocationOption(locationOption);

        // 启动定位
        locationClient.startLocation();

        if(null != alarm){
            //设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 2*1000,
                    alarmInterval * 1000, alarmPi);
        }
    }

    // 根据控件的选择，重新设置定位参数
    private void initOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption.setInterval(2000);

    }

    //每次定位成功的回调函数：list+1
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        route.getLocation_list().add(new MyLatlng(aMapLocation.getLatitude(),aMapLocation.getLongitude()));
    }
}
