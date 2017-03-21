package cn.gogoal.im.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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

  public static boolean IsInited() {
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

  public static void saveSetData(String key,Set<String> siteno){
    SharedPreferences.Editor editor=mPreference.edit();
    editor.putStringSet(key,siteno);
    editor.apply();
  }

  public static Set<String> getSetData(String key,Set<String> defSet){
    return mPreference.getStringSet(key, null);
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
}
