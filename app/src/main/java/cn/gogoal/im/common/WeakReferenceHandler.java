package cn.gogoal.im.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * author wangjd on 2017/3/22 0022.
 * Staff_id 1375
 * phone 18930640263
 * description :上下文弱引用.
 */
public abstract class WeakReferenceHandler<T> extends Handler {
    private WeakReference<T> mReference;

    public WeakReferenceHandler(Looper looper, T reference) {
        super(looper);
        this.mReference = new WeakReference<>(reference);
    }

    public void handleMessage(Message msg) {
        T reference = this.mReference.get();
        if (reference != null) {
            this.handleMessage(reference, msg);
        }
    }

    protected abstract void handleMessage(T context, Message message);
}
