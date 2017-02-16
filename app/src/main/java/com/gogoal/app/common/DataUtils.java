package com.gogoal.app.common;

/**
 * 数据处理
 */
public class DataUtils {

    /**取小数点后N位  例：format="%.2f","%.3f","%.4f"...*/
    public static String getAnyPointFloat(String format, float floatData){
        return String.format(format, floatData);
    }

    /**取小数点后N位 例：format="%.2f","%.3f","%.4f"...*/
    public static String getAnyPointDouble(String format, double doubleData){
        return String.format(format, doubleData);
    }

    /**取整*/
    public static String getIntegerData(String floatData){
        return floatData.substring(0, floatData.indexOf("."));
    }
}
