package cn.gogoal.im.common;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
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
     */
    public static <T> T parseJsonObject(@NonNull String jsonString, @NonNull Class<T> cls) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.fromJson(jsonString, cls);//fastJson == JSONObject.parseObject(jsonString,cls);
    }

    /**
     * 解析json成JsonObject
     */
    public static JsonObject parseJsonObject(@NonNull String jsonString) {
        return new JsonParser().parse(jsonString).getAsJsonObject();//fastJson == JSONObject.parseObject(jsonString,cls);
    }

    public static Map<String, String> toMap(@NonNull String jsonString) {
        return JSONObject.parseObject(jsonString,new TypeToken<Map<String,String>>(){}.getType());
    }

    //=====json array
    public static JsonArray getJsonArray(@NonNull JsonObject jsonObject, @NonNull String key) {
        if (!jsonObject.has(key)) {
            return null;
        }
        return jsonObject.getAsJsonArray(key);
    }

    public static JsonArray getJsonArray(@NonNull String jsonString, @NonNull String key) {
        return parseJsonObject(jsonString).getAsJsonArray(key);
    }

    //=====json object
    public static JsonObject getJsonObject(@NonNull JsonObject jsonObject, @NonNull String key) {
        if (!jsonObject.has(key)) {
            return null;
        }
        return jsonObject.get(key).getAsJsonObject();
    }

    public static JsonObject getJsonObject(@NonNull String jsonString, @NonNull String key) {
        return parseJsonObject(jsonString).getAsJsonObject(key);
    }

    //===== int value
    public static int getIntValue(@NonNull String jsonString, @NonNull String key) {
        return parseJsonObject(jsonString).get(key).getAsInt();
    }

    public static int getIntValue(@NonNull JsonObject jsonObject, @NonNull String key) {
        if (!jsonObject.has(key)) {
            return 0;
        }
        return jsonObject.get(key).getAsInt();
    }

    //======String
    public static String getString(@NonNull JsonObject jsonObject, @NonNull String key) {
        if (!jsonObject.has(key)) {
            return null;
        }
        return jsonObject.get(key).getAsString();
    }

    public static String getString(@NonNull String jsonString, @NonNull String key) {
        return parseJsonObject(jsonString).get(key).getAsString();
    }

    //==========boolean
    public static boolean getBoolean(@NonNull JsonObject jsonObject, @NonNull String key) {
        return jsonObject.get(key).getAsBoolean();
    }

    public static boolean getBoolean(@NonNull String jsonString, @NonNull String key) {
        return parseJsonObject(jsonString).get(key).getAsBoolean();
    }

    //==========Float
    public static float getFloatValue(@NonNull JsonObject jsonObject, @NonNull String key) {
        if (!jsonObject.has(key)) {
            return 0.0f;
        }
        return jsonObject.get(key).getAsFloat();
    }

    public static float getFloatValue(@NonNull String jsonString, @NonNull String key) {
        return parseJsonObject(jsonString).get(key).getAsFloat();
    }

    //==========Double
    public static double getDoubleValue(@NonNull JsonObject jsonObject, @NonNull String key) {
        if (!jsonObject.has(key)) {
            return 0.0d;
        }
        return jsonObject.get(key).getAsDouble();
    }

    public static double getDoubleValue(@NonNull String jsonString, @NonNull String key) {
        return parseJsonObject(jsonString).get(key).getAsDouble();
    }

    //==========Long
    public static long getLongValue(@NonNull JsonObject jsonObject, @NonNull String key) {
        if (!jsonObject.has(key)) {
            return 0L;
        }
        return jsonObject.get(key).getAsLong();
    }

    public static long getLongValue(@NonNull String jsonString, @NonNull String key) {
        return parseJsonObject(jsonString).get(key).getAsLong();
    }

    public static <T> ArrayList<T> parseJsonArray(String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        if (gson == null) {
            gson = new Gson();
        }
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;
    }

    /**
     * 实体类转json
     */
    public static String toJsonString(@NonNull Object object) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(object);//fastJson == JSONObject.toJSONString(cls)
    }

    /**
     * 字符串类转jsonObject
     */
    public static JsonObject toJsonObject(@NonNull String jsonString) {
        return new JsonParser().parse(jsonString).getAsJsonObject();//fastJson == JSONObject.parseObject(jsonString)
    }

    /**
     * 字符串类转jsonArray
     */
    public static JsonArray toJsonArray(@NonNull String jsonString) {
        return new JsonParser().parse(jsonString).getAsJsonArray();//fastJson == JSONArray.parseArray(jsonString)
    }

    /**
     * 解析jsonElement成实体类
     */
    public static <T> T parseJsonObject(@NonNull JsonElement element, @NonNull Class<T> cls) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.fromJson(element, cls);//fastJson == JSONObject.parseObject(jsonString,cls);
    }
}
