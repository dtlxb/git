package cn.gogoal.im.bean.stock;

import java.util.List;

import cn.gogoal.im.bean.stock.stockRanklist.StockRankBean;

/**
 * author wangjd on 2017/4/6 0006.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class RankListStockBean {

    /**
     * code : 0
     * message : 成功
     * requestId : null
     * data : {"gray_counts":345,"green_counts":1408,"increase_list":[{"rate":43.986897519887684,"current_price":30.77,"stock_code":"603586","stock_type":1,"stock_name":"N金麒麟"},{"rate":43.97834912043304,"current_price":10.64,"stock_code":"300632","stock_type":1,"stock_name":"N光莆"},{"rate":43.97705544933078,"current_price":7.53,"stock_code":"300633","stock_type":1,"stock_name":"N开立"},{"rate":10.099009900990096,"current_price":5.56,"stock_code":"000897","stock_type":1,"stock_name":"津滨发展"},{"rate":10.07556675062972,"current_price":4.37,"stock_code":"000709","stock_type":1,"stock_name":"河钢股份"},{"rate":10.065359477124177,"current_price":8.42,"stock_code":"300117","stock_type":1,"stock_name":"嘉寓股份"},{"rate":10.04672897196261,"current_price":9.42,"stock_code":"002342","stock_type":1,"stock_name":"巨力索具"},{"rate":10.04464285714284,"current_price":4.93,"stock_code":"600169","stock_type":1,"stock_name":"太原重工"},{"rate":10.040160642570282,"current_price":13.7,"stock_code":"002494","stock_type":1,"stock_name":"华斯股份"},{"rate":10.033444816053505,"current_price":6.58,"stock_code":"000778","stock_type":1,"stock_name":"新兴铸管"},{"rate":10.03201707577376,"current_price":10.31,"stock_code":"300150","stock_type":1,"stock_name":"世纪瑞尔"},{"rate":10.03039513677812,"current_price":10.86,"stock_code":"000600","stock_type":1,"stock_name":"建投能源"},{"rate":10.02570694087404,"current_price":8.56,"stock_code":"600550","stock_type":1,"stock_name":"保变电气"},{"rate":10.025706940874034,"current_price":21.4,"stock_code":"300011","stock_type":1,"stock_name":"鼎汉技术"},{"rate":10.025220680958386,"current_price":17.45,"stock_code":"603178","stock_type":1,"stock_name":"圣龙股份"},{"rate":10.022779043280194,"current_price":4.83,"stock_code":"601228","stock_type":1,"stock_name":"广州港"},{"rate":10.021321961620462,"current_price":5.16,"stock_code":"600008","stock_type":1,"stock_name":"首创股份"},{"rate":10.019267822736023,"current_price":5.71,"stock_code":"002477","stock_type":1,"stock_name":"雏鹰农牧"},{"rate":10.019267822736023,"current_price":17.13,"stock_code":"002774","stock_type":1,"stock_name":"快意电梯"},{"rate":10.014727540500733,"current_price":37.35,"stock_code":"603717","stock_type":1,"stock_name":"天域生态"}],"red_counts":1413}
     * executeTime : 1411
     */

    private int code;
    private String message;
    private String requestId;
    private DataBean data;
    private int executeTime;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(int executeTime) {
        this.executeTime = executeTime;
    }

    public static class DataBean {
        /**
         * gray_counts : 345
         * green_counts : 1408
         * increase_list : [{"rate":43.986897519887684,"current_price":30.77,"stock_code":"603586","stock_type":1,"stock_name":"N金麒麟"},{"rate":43.97834912043304,"current_price":10.64,"stock_code":"300632","stock_type":1,"stock_name":"N光莆"},{"rate":43.97705544933078,"current_price":7.53,"stock_code":"300633","stock_type":1,"stock_name":"N开立"},{"rate":10.099009900990096,"current_price":5.56,"stock_code":"000897","stock_type":1,"stock_name":"津滨发展"},{"rate":10.07556675062972,"current_price":4.37,"stock_code":"000709","stock_type":1,"stock_name":"河钢股份"},{"rate":10.065359477124177,"current_price":8.42,"stock_code":"300117","stock_type":1,"stock_name":"嘉寓股份"},{"rate":10.04672897196261,"current_price":9.42,"stock_code":"002342","stock_type":1,"stock_name":"巨力索具"},{"rate":10.04464285714284,"current_price":4.93,"stock_code":"600169","stock_type":1,"stock_name":"太原重工"},{"rate":10.040160642570282,"current_price":13.7,"stock_code":"002494","stock_type":1,"stock_name":"华斯股份"},{"rate":10.033444816053505,"current_price":6.58,"stock_code":"000778","stock_type":1,"stock_name":"新兴铸管"},{"rate":10.03201707577376,"current_price":10.31,"stock_code":"300150","stock_type":1,"stock_name":"世纪瑞尔"},{"rate":10.03039513677812,"current_price":10.86,"stock_code":"000600","stock_type":1,"stock_name":"建投能源"},{"rate":10.02570694087404,"current_price":8.56,"stock_code":"600550","stock_type":1,"stock_name":"保变电气"},{"rate":10.025706940874034,"current_price":21.4,"stock_code":"300011","stock_type":1,"stock_name":"鼎汉技术"},{"rate":10.025220680958386,"current_price":17.45,"stock_code":"603178","stock_type":1,"stock_name":"圣龙股份"},{"rate":10.022779043280194,"current_price":4.83,"stock_code":"601228","stock_type":1,"stock_name":"广州港"},{"rate":10.021321961620462,"current_price":5.16,"stock_code":"600008","stock_type":1,"stock_name":"首创股份"},{"rate":10.019267822736023,"current_price":5.71,"stock_code":"002477","stock_type":1,"stock_name":"雏鹰农牧"},{"rate":10.019267822736023,"current_price":17.13,"stock_code":"002774","stock_type":1,"stock_name":"快意电梯"},{"rate":10.014727540500733,"current_price":37.35,"stock_code":"603717","stock_type":1,"stock_name":"天域生态"}]
         * red_counts : 1413
         */

        private int gray_counts;
        private int green_counts;
        private int red_counts;

        private List<StockRankBean> amplitude_list;
        private List<StockRankBean> down_list;
        private List<StockRankBean> change_list;
        private List<StockRankBean> increase_list;

        public int getGray_counts() {
            return gray_counts;
        }

        public void setGray_counts(int gray_counts) {
            this.gray_counts = gray_counts;
        }

        public int getGreen_counts() {
            return green_counts;
        }

        public void setGreen_counts(int green_counts) {
            this.green_counts = green_counts;
        }

        public int getRed_counts() {
            return red_counts;
        }

        public void setRed_counts(int red_counts) {
            this.red_counts = red_counts;
        }

        public List<StockRankBean> getIncrease_list() {
            return increase_list;
        }

        public void setIncrease_list(List<StockRankBean> increase_list) {
            this.increase_list = increase_list;
        }

        public List<StockRankBean> getAmplitude_list() {
            return amplitude_list;
        }

        public void setAmplitude_list(List<StockRankBean> amplitude_list) {
            this.amplitude_list = amplitude_list;
        }

        public List<StockRankBean> getDown_list() {
            return down_list;
        }

        public void setDown_list(List<StockRankBean> down_list) {
            this.down_list = down_list;
        }

        public List<StockRankBean> getChange_list() {
            return change_list;
        }

        public void setChange_list(List<StockRankBean> change_list) {
            this.change_list = change_list;
        }
    }
}
