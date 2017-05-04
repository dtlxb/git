package cn.gogoal.im.bean.stock;

/**
 * author wangjd on 2017/4/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :个股详情 头部
 */
public class StockDetail {

    /**
     * message : 成功
     * data : {"buy3_price":16.87,"volume_inner":7215783,"buy3_volume":2000,"sell2_volume":81100,"sell5_price":16.95,"change_rate":0.5948839976204725,"sell3_volume":65800,"buy4_volume":7900,"open_price":16.76,"capital":88210.8472,"change_value":0.10000000000000142,"pe_y1":100,"turnover":24892.5896,"stock_type":1,"mcap":1419463.427208,"pb_y1":5.6338,"buy4_price":16.86,"buy1_volume":92700,"quantity_ratio":1.3772186363188577,"high_price":16.98,"buy1_price":16.89,"price":16.91,"update_time":"2017-04-11 15:05:57","buy5_volume":3100,"low_price":16.6,"sell1_volume":15900,"sell4_price":16.94,"negotiable_capital":83942.2488,"sell3_price":16.93,"amplitude":2.2605591909577574,"buy2_price":16.88,"sell1_price":16.91,"symbol_type":1,"sell2_price":16.92,"stock_code":"600198","sell5_volume":294055,"buy2_volume":175577,"tcap":1491645.426152,"close_price":16.81,"fullcode":"sh600198","tdate":"2017-04-11 00:00:00","volume_outer":7577917,"stock_name":"大唐电信","eps_y1":-1.6261999999999999,"buy5_price":16.85,"sell4_volume":52600,"commission_rate":-28.856553168456568,"source":"sh","turnover_rate":0.017623664139910438,"volume":14793700,"commission_volume":-228178,"avg_price":16.826479920506703}
     * code : 0
     */

    private String message;
    private TreatData data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TreatData getData() {
        return data;
    }

    public void setData(TreatData data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
