package cn.gogoal.im.common;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import cn.gogoal.im.bean.ContactBean;


/**
 * author wangjd on 2017/2/15 0015.
 * Staff_id 1375
 * phone 18930640263
 */
public class ArrayUtils {

    public static <T> T[] list2Array(List<T> list) {

        if (list != null && !list.isEmpty()) {
            T[] array = (T[]) new Object[list.size()];

            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }

            return array;
        }
        return null;
    }

    public static <T> List<T> arr2List(T[] arr) {
        List<T> list = new ArrayList<>();
        if (arr != null && arr.length > 0) {
            list.addAll(Arrays.asList(arr));
            return list;
        }
        return null;
    }

    public static List<String> jsonArr2List(JSONArray arr) {
        List<String> list = new ArrayList<>();
        if (!isEmpty(arr)) {
            list.addAll(arr2List(arr.toArray(new String[]{})));
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * 集合拼Srtring
     */
    public static String mosaicListElement(Collection<String> myStockArr) {
        StringBuilder builder = new StringBuilder();
        for (String stockCode : myStockArr) {
            builder.append(stockCode);
            builder.append(";");
        }
        if (builder.length() > 0) {
            return builder.toString().substring(0, builder.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * JsonArray中字段拼接Srtring
     */
    public static String mosaicArrayElement(@NonNull JSONArray array,@NonNull String key) {
        String stocks = "";
        for (int i = 0; i < array.size(); i++) {
            JSONObject stock = (JSONObject) array.get(i);
            stocks += stock.get(key);
            stocks += ";";
        }
        stocks = stocks.substring(0, stocks.length() - 1);
        return stocks;
    }

    public static boolean isEmpty(Collection c) {
        return null == c || c.isEmpty();
    }

    private int valueGetKey(Map<Integer, ContactBean> map, ContactBean contactBean) {
        for (Map.Entry<Integer, ContactBean> entry : map.entrySet()) {
            if (entry.getValue() == contactBean) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static <K, E> LinkedHashSet mapValue2Set(Map<K, E> map) {
        LinkedHashSet<E> set = new LinkedHashSet<>();
        for (Map.Entry<K, E> entry : map.entrySet()) {
            set.add(entry.getValue());
        }
        return set;
    }

    public static <T> List<T> subList(List<T> list, int from, int to) {
        List<T> newList = new ArrayList<>();
        if (isEmpty(list)) {
            return null;
        }
        if (from > list.size() - 1 || to > list.size() - 1) {
            throw new IndexOutOfBoundsException("下标越界哦!");
        }

        for (int i = from; i <= to - from + 1; i++) {
            newList.add(list.get(i));
        }
        return newList;
    }
}
