package cn.gogoal.im.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * SharedPreferences
 */
public class SPTools {

    private static final String PREFERENCES_key = "gogoal_preferences";

    private static SharedPreferences mPreference = null;

    private static boolean isInited = false;

    public synchronized static void initSharedPreferences(Context context) {
        if (mPreference == null) {
            mPreference = context.getSharedPreferences(PREFERENCES_key,
                    Context.MODE_PRIVATE);
        }
        isInited = true;
    }

    public static boolean isSpInited() {
        return isInited;
    }

    public static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return mPreference.getBoolean(key, defaultValue);
    }

    public static void saveLong(String key, long value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLong(String key, long defaultValue) {
        return mPreference.getLong(key, defaultValue);
    }

    public static void saveInt(String key, int value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key, int defaultValue) {
        return mPreference.getInt(key, defaultValue);
    }

    public static void saveFloat(String key, float value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloat(String key, float defaultValue) {
        return mPreference.getFloat(key, defaultValue);
    }

    public static String saveString(String key, String value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(key, value);
        editor.apply();
        return key;
    }

    public static String getString(String key, String defaultValue) {
        return mPreference.getString(key, defaultValue);
    }

    public static String saveSetData(String key, Set<String> siteno) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putStringSet(key, siteno);
        editor.apply();
        return JSONObject.toJSONString(siteno);
    }

    public static LinkedHashSet<String> getSetData(String key, Set<String> defSet) {
        return new LinkedHashSet<>(mPreference.getStringSet(key, defSet));
    }


    public static void clear() {
        mPreference.edit().clear().apply();
    }

    public static boolean clearItem(String KEY_key) {
        mPreference.edit().remove(KEY_key).apply();
        return false;
    }

    public static JSONArray getJsonArray(String name, JSONArray defaultValue) {
        String result = mPreference.getString(name, "");
        if (TextUtils.isEmpty(result)) {
            return defaultValue;
        } else {
            return JSONArray.parseArray(result);
        }
    }

    public static void saveJsonArray(String name, JSONArray value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(name, value.toJSONString());
        editor.apply();
    }

    public static void saveJsonObject(String name, JSONObject value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(name, value.toJSONString());
        editor.apply();
    }

    public static JSONObject getJsonObject(String name, JSONObject defaultValue) {
        String result = mPreference.getString(name, "");
        if (TextUtils.isEmpty(result)) {
            return defaultValue;
        } else {
            return JSONObject.parseObject(result);
        }
    }


//======================================================================
    /**
     * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
     * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
     *
     * @param object 待加密的转换为String的对象
     * @return String   加密后的String
     */
    private static String Object2String(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            objectOutputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用Base64解密String，返回Object对象
     *
     * @param objectString 待解密的String
     * @return object      解密后的object
     */
    private static Object String2Object(String objectString) {
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 使用SharedPreference保存对象
     *
     * @param key        储存对象的key
     * @param saveObject 储存的对象
     */
    public static void saveSPObject(String key, Object saveObject) {
        SharedPreferences.Editor editor = mPreference.edit();
        String string = Object2String(saveObject);
        editor.putString(key, string);
        editor.apply();
    }

    /**
     * 获取SharedPreference保存的对象
     *
     * @param key     储存对象的key
     * @return object 返回根据key得到的对象
     */
    public static Object getSPObject(String key) {
        String string = mPreference.getString(key, null);
        if (string != null) {
            Object object = String2Object(string);
            return object;
        } else {
            return null;
        }
    }
}
