package cn.gogoal.im.common;

import com.alibaba.fastjson.JSONArray;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
    public static ArrayList valueGetKey(Map map, String value) {
        Set set = map.entrySet();//新建一个不可重复的集合
        ArrayList arr = new ArrayList<>();//新建一个集合
        Iterator it = set.iterator();//遍历的类
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();//找到所有key-value对集合
            if(entry.getValue().equals(value)) {//通过判断是否有该value值
                int s = (int)entry.getKey();//取得key值
                arr.add(s);
            }
        }
        return arr;
    }
    /**通过map的value获取key*/
    public static <T> int valueGetKey(Map<Integer,T> map, T value) {
        Set set = map.entrySet();//新建一个不可重复的集合
        Iterator it = set.iterator();//遍历的类

        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();//找到所有key-value对集合
            if(entry.getValue().equals(value)) {//通过判断是否有该value值
                return  (int)entry.getKey();//取得key值

            }
        }

        return -1;
    }
}
