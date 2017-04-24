package cn.gogoal.im.bean.stock;

import java.io.Serializable;
import java.util.List;

/**
 * author wangjd on 2017/4/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :我的自选股 行情数据
 */
public class MyStockMarketBean implements Serializable{

    /**
     * code : 0
     * message : 成功
     * requestId : null
     * data : [{"amplitude":1.2495461035427302,"avg_price":10.070970890347377,"close_price":4826.8167,"code":"930715","fullcode":"csi930715","high_price":4851.7627,"insert_time":"2017-04-21 16:17:19","low_price":4791.4494,"name":"CS朝阳88","open_price":4832.8441,"price":4797.3821,"price_change":-29.4346,"price_change_rate":-0.61,"source":"csi","symbol_type":2,"tdate":"2017-04-21 00:00:00","turnover":1656292.2603,"type":"01","update_time":"2017-04-21 16:17:21","volume":1644620244},{"amplitude":0.6987736169628752,"close_price":3172.1003,"code":"000001","fall":663,"flat":135,"fullcode":"sh000001","high_price":3180.7944,"insert_time":"2017-04-21 15:06:00","low_price":3158.6286,"name":"上证指数","open_price":3170.2899,"price":3173.1512,"price_change":1.0508999999997286,"price_change_rate":0.03312946945592258,"rose":505,"source":"sh","stage":"E","symbol_type":2,"tdate":"2017-04-21 00:00:00","turnover":1.8413144069989998E7,"update_time":"2017-04-21 15:05:56","volume":16476174000},{"amplitude":0.7516896847396033,"close_price":3461.5481,"code":"000300","fullcode":"sh000300","high_price":3475.5622,"insert_time":"2017-04-21 15:06:00","low_price":3449.5421,"name":"沪深300","open_price":3461.389,"price":3466.7865,"price_change":5.238400000000183,"price_change_rate":0.151331134182425,"source":"sh","stage":"E","symbol_type":2,"tdate":"2017-04-21 00:00:00","turnover":1.1520883174619999E7,"update_time":"2017-04-21 15:05:56","volume":10441911000},{"amplitude":1.0365408944816985,"close_price":10359.0896,"code":"399001","deal_count":4314786,"fall":1036,"flat":224,"fullcode":"sz399001","high_price":10407.7747,"insert_time":"2017-04-21 15:05:03","low_price":10300.3985,"md_stream_id":"900","name":"深证成指","open_price":10357.8582,"price":10314.3535,"price_change":-44.73610000000008,"price_change_rate":-0.4318535868248507,"rose":729,"source":"sz","stage":"E","symbol_type":2,"tdate":"2017-04-21 00:00:00","turnover":1.983684647105165E7,"update_time":"2017-04-21 15:05:03","volume":14453633063},{"amplitude":1.4623295629569992,"close_price":1850.39,"code":"399006","deal_count":693856,"fullcode":"sz399006","high_price":1863.5824,"insert_time":"2017-04-21 15:05:03","low_price":1836.5236,"md_stream_id":"900","name":"创业板指","open_price":1849.2814,"price":1839.0064,"price_change":-11.383600000000115,"price_change_rate":-0.6152000389107223,"source":"sz","stage":"E","symbol_type":2,"tdate":"2017-04-21 00:00:00","turnover":4967775.803438,"update_time":"2017-04-21 15:05:03","volume":2645293031}]
     * executeTime : 13
     */

    private int code;
    private String message;
    private String requestId;
    private String executeTime;
    private List<MyStockMarketData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public List<MyStockMarketData> getData() {
        return data;
    }

    public void setData(List<MyStockMarketData> data) {
        this.data = data;
    }

    public static class MyStockMarketData implements Serializable{
        /**
         * amplitude : 1.2495461035427302
         * avg_price : 10.070970890347377
         * close_price : 4826.8167
         * code : 930715
         * fullcode : csi930715
         * high_price : 4851.7627
         * insert_time : 2017-04-21 16:17:19
         * low_price : 4791.4494
         * name : CS朝阳88
         * open_price : 4832.8441
         * price : 4797.3821
         * price_change : -29.4346
         * price_change_rate : -0.61
         * source : csi
         * symbol_type : 2
         * tdate : 2017-04-21 00:00:00
         * turnover : 1656292.2603
         * type : 01
         * update_time : 2017-04-21 16:17:21
         * volume : 1644620244
         * fall : 663
         * flat : 135
         * rose : 505
         * stage : E
         * deal_count : 4314786
         * md_stream_id : 900
         */

        private String fullcode;
        private String name;
        private String price;
        private String price_change;
        private String price_change_rate;

        public String getFullcode() {
            return fullcode;
        }

        public void setFullcode(String fullcode) {
            this.fullcode = fullcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPrice_change() {
            return price_change;
        }

        public void setPrice_change(String price_change) {
            this.price_change = price_change;
        }

        public String getPrice_change_rate() {
            return price_change_rate;
        }

        public void setPrice_change_rate(String price_change_rate) {
            this.price_change_rate = price_change_rate;
        }
    }
}
