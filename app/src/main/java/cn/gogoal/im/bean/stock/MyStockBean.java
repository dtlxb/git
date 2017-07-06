package cn.gogoal.im.bean.stock;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作者 wangjd on 2017/3/5 0005 16:27.
 * 联系方式 18930640263 ;
 * <p>
 * 简介:
 */

public class MyStockBean implements Serializable{

    /**
     * message : 成功
     * data : [{"group_id":137269,"date":"2016-10-13 17:50:46","remind_headlines":1,"remind_notice":1,"stock_class":0,"stock_code":"002101","stock_sort":8,"stock_name":"广东鸿图","change_value":0.09999999999999787,"price":23.15,"change_rate":0.43383947939261547,"stock_type":1},{"group_id":137269,"date":"2017-01-12 15:44:26","remind_headlines":1,"remind_notice":1,"stock_class":0,"stock_code":"603223","stock_sort":7,"stock_name":"恒通股份","change_value":-0.33000000000000185,"price":31.22,"change_rate":-1.0459587955626048,"stock_type":1},{"group_id":137269,"date":"2017-01-12 15:19:49","remind_headlines":1,"remind_notice":1,"stock_class":0,"stock_code":"000002","stock_sort":6,"stock_name":"万科A","change_value":0.010000000000001563,"price":20.8,"change_rate":0.04810004810005562,"stock_type":1},{"group_id":137269,"date":"2016-10-12 17:32:20","remind_headlines":1,"remind_notice":1,"stock_class":0,"stock_code":"002092","stock_sort":5,"stock_name":"中泰化学","change_value":0.019999999999999574,"price":13.58,"change_rate":0.14749262536872842,"stock_type":1},{"group_id":137269,"date":"2016-09-27 13:12:47","remind_headlines":1,"remind_notice":1,"stock_class":0,"stock_code":"000786","stock_sort":4,"stock_name":"北新建材","change_value":0.6500000000000004,"price":15.64,"change_rate":4.336224149432957,"stock_type":1},{"group_id":137269,"date":"2016-09-24 16:29:56","remind_headlines":1,"remind_notice":1,"stock_class":0,"stock_code":"000651","stock_sort":3,"stock_name":"格力电器","change_value":-0.19000000000000128,"price":31.81,"change_rate":-0.593750000000004,"stock_type":1},{"group_id":137269,"date":"2016-09-22 15:03:00","remind_headlines":1,"remind_notice":1,"stock_class":0,"stock_code":"600330","stock_sort":2,"stock_name":"天通股份","change_value":-0.010000000000001563,"price":10.37,"change_rate":-0.09633911368016919,"stock_type":1},{"group_id":137269,"date":"2016-09-22 14:56:08","remind_headlines":1,"remind_notice":1,"stock_class":0,"stock_code":"000725","stock_sort":1,"stock_name":"京东方A","change_value":0.0900000000000003,"price":3.66,"change_rate":2.521008403361353,"stock_type":1}]
     * code : 0
     */

    private String message;
    private int code;
    private ArrayList<MyStockData> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<MyStockData> getData() {
        return data;
    }

    public void setData(ArrayList<MyStockData> data) {
        this.data = data;
    }

}
