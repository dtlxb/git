package com.gogoal.app.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.gogoal.app.bean.BaseMessage;

import org.simple.eventbus.EventBus;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }

        if (activityStack == null) {
            activityStack = new Stack<>();
        }

        return instance;
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束除当前Activity之外的Activity
     */

    public void finishBackActivity(Activity activity){
        for (Activity a : activityStack) {
            if (!a.getClass().equals(activity.getClass())) {
                finishActivity(a);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                (activityStack.get(i)).finish();
            }
        }
        activityStack.clear();

    }

    /**
     * 发送消息
     *
     * @param tag
     */
    public void sendMessage(String tag) {
        EventBus.getDefault().post(new BaseMessage(), tag);
    }

    public void sendMessage(String tag, String code) {
        EventBus.getDefault().post(code, tag);
    }

    public void sendMessage(String tag, BaseMessage msg) {
        EventBus.getDefault().post(msg, tag);
    }

    /*
    * 设置颜色
    * */
    public int getResourceColor(Context context, int color) {
        return ContextCompat.getColor(context, color);
    }
}
