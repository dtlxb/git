package cn.gogoal.im.common;

import android.graphics.Bitmap;

/**
 * author wangjd on 2017/5/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public interface AvatarTakeListener {
    void success(Bitmap bitmap);
    void failed(Exception e);
}
