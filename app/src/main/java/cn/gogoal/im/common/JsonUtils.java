package cn.gogoal.im.common;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

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

    /**
     * 解析json成实体类
     *
     * @param jsonString json串
     * @param cls        类型
     */
    public static <T> T parseJsonObject(@NonNull String jsonString, @NonNull Class<T> cls) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.fromJson(jsonString, cls);//fastjson == JSONObject.parseObject(jsonString,cls);
    }

    /**
     * 实体类转json
     *
     * @param cls 需要转换的类
     */
    public static String toJsonString(@NonNull Class cls) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(cls);//fastjson == JSONObject.toJSONString(cls)
    }

    /**
     * 字段key是否存在
     * @param key key
     * */
    public static boolean containsKey(@NonNull String jsonoString, @NonNull String key) {
        JsonObject object=new JsonParser().parse(jsonoString).getAsJsonObject();
        return object.has(key);//fastjson == JSONObject.parseObject(jsonoString).containsKey(key)
    }

    public static Map<String,String> toMap(@NonNull String jsonString){
        if (gson==null){
            gson=new Gson();
        }
        return gson.fromJson(jsonString,new TypeToken<Map<String,String>>(){}.getType());
    }
}
