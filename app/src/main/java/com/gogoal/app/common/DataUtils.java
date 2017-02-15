package com.gogoal.app.common;

/**
 * 数据处理
 */
public class DataUtils {

    //取小数点后N位  例：format="%.2f","%.3f","%.4f"...
    public static String getAnyPointFloat(String format, float floatData){
        String result = String.format(format, floatData);
        return result;
    }

    //取小数点后N位 例：format="%.2f","%.3f","%.4f"...
    public static String getAnyPointDouble(String format, double doubleData){
        String result = String.format(format, doubleData);
        return result;
    }

    //取整
    public static String getIntegerData(String floatData){
        String result = floatData.substring(0, floatData.indexOf("."));
        return result;
    }
}
