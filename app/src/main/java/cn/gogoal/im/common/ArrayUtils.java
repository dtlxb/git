package cn.gogoal.im.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


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


    /**获取搜索记录*/
    public static List<String> getSearchList(){
        return new ArrayList<>(SPTools.getSetData("search_history",new HashSet<String>()));
    }

    /**保存单个搜索关键字到本地*/
    public static void addSearchKeyword(String keyword){
        List<String> list=getSearchList();
        list.add(keyword);
        SPTools.saveSetData("search_history",new HashSet<>(list));
    }
}
