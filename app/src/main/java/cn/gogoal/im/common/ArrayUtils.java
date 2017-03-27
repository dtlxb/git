package cn.gogoal.im.common;

import android.util.SparseArray;

import com.alibaba.fastjson.JSONArray;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * author wangjd on 2017/2/15 0015.
 * Staff_id 1375
 * phone 18930640263
 */
public class ArrayUtils {

    public static String[] list2Array(List<String> list){

        if (list!=null && !list.isEmpty()){
            String[] array=new String[list.size()];

            for (int i=0;i<list.size();i++){
                array[i]=list.get(i);
            }

            return array;
        }
        return null;
    }

    public static List<String> arr2List(String[] arr){
        List<String> list=new ArrayList<>();
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

    /**清空搜索记录*/
    public static void clearHistory(){
        SPTools.clearItem("search_history");
    }

    public static boolean isEmpty(Collection c){
        return null==c || c.isEmpty();
    }

    /**通过map的value获取key*/
    public static <T> ArrayList valueGetKey(Map<Integer,T> map, String value) {
        ArrayList<Integer> arr = new ArrayList<>();//新建一个集合
        for (Map.Entry<Integer, T> entry:map.entrySet()){
            if (entry.getValue()==value){
                arr.add(entry.getKey());
            }
        }
        return arr;
    }

    /**通过map的value获取key*/
    public static <T> int valueGetKey(Map<Integer,T> map, T value) {
        for (Map.Entry<Integer,T> entry:map.entrySet()){
            if (entry.getValue()==value){
                return entry.getKey();
            }
        }
        return -1;
    }

    /**通过SparseArray的value获取key*/
    public static <T> int valueGetKey(SparseArray<T> array, T value) {
        for (int i=0;i<array.size();i++){
            if (array.get(i)==value){
                return array.keyAt(i);
            }
        }

        return -1;
    }

    /**通过SparseArray的value获取key*/
    public static <T> int valueGetKey(List<T> array, T value) {
        for (int i=0;i<array.size();i++){
            if (array.get(i)==value){
                return i;
            }
        }

        return -1;
    }

    /**通过SparseArray的value获取key*/
    public static <T> List<T> sparseArrayGetValue(SparseArray<T> array) {
        ArrayList<T> arrayList=new ArrayList<>();
        for (int i=0;i<array.size();i++){
            arrayList.add(array.valueAt(i));
        }

        return arrayList;
    }


}
