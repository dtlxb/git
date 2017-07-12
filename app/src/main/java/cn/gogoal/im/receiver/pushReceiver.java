package cn.gogoal.im.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avospush.notification.NotificationCompat;

import org.json.JSONObject;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.MainActivity;

/**
 * Created by dave.
 * Date: 2017/5/4.
 * Desc: 系统推送
 */
public class pushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
            String data = intent.getExtras().getString("com.avos.avoscloud.Data");

            if (action.equals("com.gogoal.action")) {
                JSONObject json = new JSONObject(data);
                String message = json.getString("alert");
                Intent resultIntent = new Intent(AVOSCloud.applicationContext, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(AVOSCloud.applicationContext,
                        0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AVOSCloud.applicationContext)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle("GoGoal")
                        .setContentText(message)
                        .setTicker(message);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true);

                int mNotificationId = 10001;
                NotificationManager mNotifyMgr = (NotificationManager) AVOSCloud.applicationContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }
        } catch (Exception e) {

        }
    }
}
