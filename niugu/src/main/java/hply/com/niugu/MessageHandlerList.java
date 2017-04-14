package hply.com.niugu;

import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangxx on 2015/10/8.
 */
public class MessageHandlerList {
    private static MessageHandlerList mHandlerList = null;

    public static MessageHandlerList getInstance() {
        if (mHandlerList == null) {
            mHandlerList = new MessageHandlerList();
        }
        return mHandlerList;
    }

    private Map<String, Handler> mMap = new HashMap<String, Handler>();

    /**
     * 根据键名添加handler到消息池
     *
     * @param className
     * @param handler
     */
    public static void addHandler(String className, Handler handler) {
        if (getInstance().mMap.get(className) == null) {
            getInstance().mMap.put(className, handler);
        } else {
            getInstance().mMap.remove(className);
            getInstance().mMap.put(className, handler);
        }
    }

    /**
     * 根据类名将对应的handler移除消息池
     *
     * @param className
     */
    public static synchronized void removeHandler(String className) {
        getInstance().mMap.remove(className);
    }

    /**
     * 根据指定Handler发送消息
     *
     * @param handler
     * @param msgId
     * @param msgObj
     */
    public static void sendMessage(Handler handler, int msgId, Object msgObj, int arg1) {
        Message message = handler.obtainMessage();
        message.what = msgId;
        message.obj = msgObj;
        message.arg1 = arg1;
        handler.sendMessage(message);
    }


    /**
     * 全局发送消息--（只要存活的Act有抓取指定的msg就会执行接收消息)
     *
     * @param msgId
     * @param msgObj
     */
    public synchronized static void sendMessage(int msgId, Object msgObj, int arg1) {
        synchronized (getInstance().mMap) {
            for (Map.Entry<String, Handler> entry : getInstance().mMap.entrySet()) {
                Handler handler = entry.getValue();
                sendMessage(handler, msgId, msgObj, arg1);
            }
        }
    }

    /**
     * 发送空消息
     *
     * @param msgId
     */
    public static void sendMessage(int msgId, int agr1) {
        sendMessage(msgId, null, agr1);
    }

    /**
     * 根据指定类名发送消息
     *
     * @param className 类名
     * @param msgId
     * @param msgObj
     */
    public static void sendMessage(String className, int msgId, Object msgObj, int arg1) {
        Handler handler = getInstance().mMap.get(className);
        if (handler != null) {
            sendMessage(handler, msgId, msgObj, arg1);
        }
    }

    /**
     * 根据类名发送消息
     *
     * @param cls
     * @param msgId
     * @param msgObj
     */
    public static void sendMessage(@SuppressWarnings("rawtypes") Class cls,
                                   int msgId, Object msgObj, int arg1) {
        sendMessage(cls.getName(), msgId, msgObj, arg1);
    }

    /**
     * 根据类名发送空消
     *
     * @param cls
     * @param msgId
     */
    public static void sendMessage(@SuppressWarnings("rawtypes") Class cls,
                                   int msgId, int arg1) {
        sendMessage(cls.getName(), msgId, null, arg1);
    }

    /**
     * 根据类名获取handler
     *
     * @param className
     */
    public static Handler getHandler(String className) {
        Handler handler = getInstance().mMap.get(className);
        return handler;
        /*Set<String> set=getInstance().mMap.keySet();
		Iterator iterator=set.iterator();
		while (iterator.hasNext()) {

		}*/
    }
}

