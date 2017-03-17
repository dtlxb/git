package cn.gogoal.im.base;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import org.simple.eventbus.EventBus;

import java.util.LinkedList;

import cn.gogoal.im.bean.BaseMessage;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {

    private static LinkedList<FragmentActivity> activityStack;

    private volatile static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (null == instance) {
            synchronized (AppManager.class) {
                if (null == instance) {
                    instance = new AppManager();
                    activityStack = new LinkedList<>();
                }
            }
        }

        return instance;
    }

    /**
     * 获取指定的Activity
     */
    public Activity getActivity(Class<?> cls) {
        if (activityStack != null && !activityStack.isEmpty())
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
    void addActivity(FragmentActivity activity) {
        activityStack.add(activity);
    }

    private boolean isStackEmpty() {
        return activityStack == null || activityStack.isEmpty();
    }

    private boolean isEmpty(Activity activity) {
        return isStackEmpty() || activity == null;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (isStackEmpty()) {
            return null;
        }
        return activityStack.get(activityStack.size() - 1);
    }

    private void removeActivity(FragmentActivity activity) {
        if (activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(FragmentActivity activity) {
        if (!isEmpty(activity)) {
            removeActivity(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (FragmentActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束除当前Activity之外的Activity
     */

    public void finishBackActivity(FragmentActivity activity) {
        for (FragmentActivity a : activityStack) {
            if (!a.getClass().equals(activity.getClass())) {
                finishActivity(a);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (!isStackEmpty()) {
            for (FragmentActivity activity:activityStack) {
               activity.finish();
            }
            activityStack.clear();
        }
    }

    /**
     * 发送消息
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

}
