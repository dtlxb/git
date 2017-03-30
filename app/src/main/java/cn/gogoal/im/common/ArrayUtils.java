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

    /**清空搜索记录*/
    public static void clearHistory(){
        SPTools.clearItem("search_history");
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
    public static <T> List<T> sparseArrayGetValue(SparseArray<T> array) {
        ArrayList<T> arrayList=new ArrayList<>();
        for (int i=0;i<array.size();i++){
            arrayList.add(array.valueAt(i));
        }

        return arrayList;
    }


}
