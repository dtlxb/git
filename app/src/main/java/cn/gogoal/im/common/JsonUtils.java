package cn.gogoal.im.common;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

/**
 * author wangjd on 2017/7/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :解析封装
 */
public class JsonUtils {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private JsonUtils() {
    }

    public static <T> T parseJsonObject(@NonNull String jsonString, @NonNull Class<T> cls) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.fromJson(jsonString, cls);
    }
}
