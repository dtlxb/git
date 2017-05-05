package cn.gogoal.im.common;

import com.alibaba.fastjson.JSONArray;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.gogoal.im.bean.ContactBean;


/**
 * author wangjd on 2017/2/15 0015.
 * Staff_id 1375
 * phone 18930640263
 */
public class ArrayUtils {

    public static <T> T[] list2Array(List<T> list){

        if (list!=null && !list.isEmpty()){
            T[] array = (T[]) new Object[list.size()];

            for (int i=0;i<list.size();i++){
                array[i]=list.get(i);
            }

            return array;
        }
        return null;
    }

    public static <T> List<T> arr2List(T[] arr){
        List<T> list=new ArrayList<>();
        if (arr!=null && arr.length>0){
            list.addAll(Arrays.asList(arr));
            return list;
        }
        return null;
    }

    public static List<String> jsonArr2List(JSONArray arr){
        List<String> list=new ArrayList<>();
        if (!isEmpty(arr)){
            list.addAll(arr2List(arr.toArray(new String[]{})));
            return list;
        }
        return new ArrayList<>();
    }


    /**获取搜索记录*/
    public static JSONArray getSearchList(){
        JSONArray historyArray = SPTools.getJsonArray("search_history", new JSONArray());
        if (isEmpty(historyArray)){
            return new JSONArray();
        }
        return historyArray;
    }

    /**保存单个搜索关键字到本地*/
    public static void addSearchKeyword(String keyword){
        JSONArray searchList = getSearchList();
        searchList.add(keyword);
        KLog.e(SPTools.getJsonArray("search_history",null).toString());
    }

    /**
     * 集合拼Srtring
     */
    public static String mosaicListElement(Set<String> myStockArr) {
        StringBuilder builder = new StringBuilder();
        for (String stockCode : myStockArr) {
            builder.append(stockCode);
            builder.append(";");
        }
        return builder.toString().substring(0, builder.length() - 1);
    }
    /**
     * 集合拼Srtring
     */
    public static String mosaicListElement(List<String> myStockArr) {
        StringBuilder builder = new StringBuilder();
        for (String stockCode : myStockArr) {
            builder.append(stockCode);
            builder.append(";");
        }
        return builder.toString().substring(0, builder.length() - 1);
    }

    public static boolean isEmpty(Collection c){
        return null==c || c.isEmpty();
    }

    /**通过map的value获取key*/
    public static <T,E> T valueGetKey(Map<E,T> map, T value) {
        for (Map.Entry<E, T> entry:map.entrySet()){
            if (entry.getValue()==value){
                entry.getKey();
            }
        }
        return null;
    }

    private int valueGetKey(Map<Integer, ContactBean> map, ContactBean contactBean) {
        for (Map.Entry<Integer, ContactBean> entry : map.entrySet()) {
            if (entry.getValue() == contactBean) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static <K,E> LinkedHashSet mapValue2Set(Map<K,E> map){
        LinkedHashSet<E> set=new LinkedHashSet<>();
        for (Map.Entry<K,E> entry:map.entrySet()){
            set.add(entry.getValue());
        }
        return set;
    }
}
