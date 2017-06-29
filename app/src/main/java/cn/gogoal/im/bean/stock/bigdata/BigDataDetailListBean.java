package cn.gogoal.im.bean.stock.bigdata;

import java.util.List;

/**
 * author wangjd on 2017/6/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class BigDataDetailListBean {
    /**
     * message : 成功
     * data : [{"y1pe":50.1353,"y0eps":0.2512,"stock_code":"000555","y2eps":0.41,"y1eps":0.3327,"price":16.78,"y0pe":66.4013,"price_change_rate":0.599520383693054,"news_count":20,"numberRate":1.6195652173913042,"report_count":9,"tcp":1607003.3634,"stock_name":"神州信息","y2pe":40.6829,"price_change":0.10000000000000142},{"y1pe":24.231,"y0eps":0.9832,"stock_code":"600120","y2eps":1.1906,"y1eps":1.0858,"price":26.88,"y0pe":26.7596,"price_change_rate":2.1664766248574696,"news_count":11,"numberRate":0.644927536231884,"report_count":2,"tcp":1769626.978,"stock_name":"浙江东方","y2pe":22.0981,"price_change":0.5700000000000003},{"y1pe":51.6071,"y0eps":0.1645,"stock_code":"002222","y2eps":0.39,"y1eps":0.28,"price":14.42,"y0pe":87.8419,"price_change_rate":-0.20761245674740042,"news_count":8,"numberRate":0.43115942028985504,"report_count":1,"tcp":617737.5,"stock_name":"福晶科技","y2pe":37.0513,"price_change":-0.02999999999999936},{"y1pe":19.5419,"y0eps":0.3432,"stock_code":"002224","y2eps":0.7384,"y1eps":0.5588,"price":11.03,"y0pe":31.8182,"price_change_rate":1.0073260073260022,"news_count":8,"numberRate":0.34782608695652173,"report_count":0,"tcp":718607.7422,"stock_name":"三力士","y2pe":14.7887,"price_change":0.10999999999999943},{"y1pe":11.7865,"y0eps":2.1969,"stock_code":"600340","y2eps":3.9335,"y1eps":2.9483,"price":33.94,"y0pe":15.8177,"price_change_rate":-2.3309352517985675,"news_count":0,"numberRate":0,"report_count":0,"tcp":1.02684398138E7,"stock_name":"华夏幸福","y2pe":8.8344,"price_change":-0.8100000000000023},{"y1pe":16.3324,"y0eps":0.5179,"stock_code":"600522","y2eps":0.9014,"y1eps":0.7329,"price":11.94,"y0pe":23.1126,"price_change_rate":-0.2506265664160496,"news_count":0,"numberRate":0,"report_count":0,"tcp":3670088.8076,"stock_name":"中天科技","y2pe":13.2793,"price_change":-0.030000000000001137}]
     * code : 0
     */

    private String message;
    private int code;
    private List<BigDataDetailList> data;

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

    public List<BigDataDetailList> getData() {
        return data;
    }

    public void setData(List<BigDataDetailList> data) {
        this.data = data;
    }

}
