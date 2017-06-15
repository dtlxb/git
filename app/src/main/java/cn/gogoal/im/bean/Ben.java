package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/6/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class Ben {

    /**
     * message : 成功
     * data : {"buy3_price":27.59,"volume_inner":891604,"buy3_volume":1703,"sell2_volume":2600,"sell5_price":27.69,"change_rate":1.098096632503663,"sell3_volume":2500,"buy4_volume":2100,"open_price":27.2,"capital":41559.7227,"change_value":0.3000000000000007,"pe_y1":28.0033,"turnover":4676.433072,"stock_type":1,"mcap":1090339.8528200001,"pb_y1":4.0554,"buy4_price":27.58,"buy1_volume":8821,"quantity_ratio":0.5888635722763532,"high_price":27.87,"buy1_price":27.62,"price":27.62,"update_time":"2017-06-13 15:05:03","buy5_volume":200,"low_price":27.18,"sell1_volume":200,"sell4_price":27.68,"negotiable_capital":39476.461,"sell3_price":27.67,"amplitude":2.5256222547584235,"buy2_price":27.6,"sell1_price":27.65,"symbol_type":1,"sell2_price":27.66,"stock_code":"002368","sell5_volume":1700,"buy2_volume":6500,"tcap":1147879.540974,"close_price":27.32,"fullcode":"sz002368","tdate":"2017-06-13 00:00:00","volume_outer":800326,"stock_name":"太极股份","eps_y1":0.9756,"buy5_price":27.57,"sell4_volume":4100,"commission_rate":27.031291085984748,"source":"sz","turnover_rate":0.004285921172113173,"volume":1691930,"commission_volume":8224,"avg_price":27.639636817126004}
     * code : 0
     */

    private String message;
    private DataBean data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * buy3_price : 27.59
         * volume_inner : 891604
         * buy3_volume : 1703
         * sell2_volume : 2600
         * sell5_price : 27.69
         * change_rate : 1.098096632503663
         * sell3_volume : 2500
         * buy4_volume : 2100
         * open_price : 27.2
         * capital : 41559.7227
         * change_value : 0.3000000000000007
         * pe_y1 : 28.0033
         * turnover : 4676.433072
         * stock_type : 1
         * mcap : 1090339.8528200001
         * pb_y1 : 4.0554
         * buy4_price : 27.58
         * buy1_volume : 8821
         * quantity_ratio : 0.5888635722763532
         * high_price : 27.87
         * buy1_price : 27.62
         * price : 27.62
         * update_time : 2017-06-13 15:05:03
         * buy5_volume : 200
         * low_price : 27.18
         * sell1_volume : 200
         * sell4_price : 27.68
         * negotiable_capital : 39476.461
         * sell3_price : 27.67
         * amplitude : 2.5256222547584235
         * buy2_price : 27.6
         * sell1_price : 27.65
         * symbol_type : 1
         * sell2_price : 27.66
         * stock_code : 002368
         * sell5_volume : 1700
         * buy2_volume : 6500
         * tcap : 1147879.540974
         * close_price : 27.32
         * fullcode : sz002368
         * tdate : 2017-06-13 00:00:00
         * volume_outer : 800326
         * stock_name : 太极股份
         * eps_y1 : 0.9756
         * buy5_price : 27.57
         * sell4_volume : 4100
         * commission_rate : 27.031291085984748
         * source : sz
         * turnover_rate : 0.004285921172113173
         * volume : 1691930
         * commission_volume : 8224
         * avg_price : 27.639636817126004
         */

        private double buy3_price;
        private int volume_inner;
        private int buy3_volume;
        private int sell2_volume;
        private double sell5_price;
        private double change_rate;
        private int sell3_volume;
        private int buy4_volume;
        private double open_price;
        private double capital;
        private double change_value;
        private double pe_y1;
        private double turnover;
        private int stock_type;
        private double mcap;
        private double pb_y1;
        private double buy4_price;
        private int buy1_volume;
        private double quantity_ratio;
        private double high_price;
        private double buy1_price;
        private double price;
        private String update_time;
        private int buy5_volume;
        private double low_price;
        private int sell1_volume;
        private double sell4_price;
        private double negotiable_capital;
        private double sell3_price;
        private double amplitude;
        private double buy2_price;
        private double sell1_price;
        private int symbol_type;
        private double sell2_price;
        private String stock_code;
        private int sell5_volume;
        private int buy2_volume;
        private double tcap;
        private double close_price;
        private String fullcode;
        private String tdate;
        private int volume_outer;
        private String stock_name;
        private double eps_y1;
        private double buy5_price;
        private int sell4_volume;
        private double commission_rate;
        private String source;
        private double turnover_rate;
        private int volume;
        private int commission_volume;
        private double avg_price;

        public double getBuy3_price() {
            return buy3_price;
        }

        public void setBuy3_price(double buy3_price) {
            this.buy3_price = buy3_price;
        }

        public int getVolume_inner() {
            return volume_inner;
        }

        public void setVolume_inner(int volume_inner) {
            this.volume_inner = volume_inner;
        }

        public int getBuy3_volume() {
            return buy3_volume;
        }

        public void setBuy3_volume(int buy3_volume) {
            this.buy3_volume = buy3_volume;
        }

        public int getSell2_volume() {
            return sell2_volume;
        }

        public void setSell2_volume(int sell2_volume) {
            this.sell2_volume = sell2_volume;
        }

        public double getSell5_price() {
            return sell5_price;
        }

        public void setSell5_price(double sell5_price) {
            this.sell5_price = sell5_price;
        }

        public double getChange_rate() {
            return change_rate;
        }

        public void setChange_rate(double change_rate) {
            this.change_rate = change_rate;
        }

        public int getSell3_volume() {
            return sell3_volume;
        }

        public void setSell3_volume(int sell3_volume) {
            this.sell3_volume = sell3_volume;
        }

        public int getBuy4_volume() {
            return buy4_volume;
        }

        public void setBuy4_volume(int buy4_volume) {
            this.buy4_volume = buy4_volume;
        }

        public double getOpen_price() {
            return open_price;
        }

        public void setOpen_price(double open_price) {
            this.open_price = open_price;
        }

        public double getCapital() {
            return capital;
        }

        public void setCapital(double capital) {
            this.capital = capital;
        }

        public double getChange_value() {
            return change_value;
        }

        public void setChange_value(double change_value) {
            this.change_value = change_value;
        }

        public double getPe_y1() {
            return pe_y1;
        }

        public void setPe_y1(double pe_y1) {
            this.pe_y1 = pe_y1;
        }

        public double getTurnover() {
            return turnover;
        }

        public void setTurnover(double turnover) {
            this.turnover = turnover;
        }

        public int getStock_type() {
            return stock_type;
        }

        public void setStock_type(int stock_type) {
            this.stock_type = stock_type;
        }

        public double getMcap() {
            return mcap;
        }

        public void setMcap(double mcap) {
            this.mcap = mcap;
        }

        public double getPb_y1() {
            return pb_y1;
        }

        public void setPb_y1(double pb_y1) {
            this.pb_y1 = pb_y1;
        }

        public double getBuy4_price() {
            return buy4_price;
        }

        public void setBuy4_price(double buy4_price) {
            this.buy4_price = buy4_price;
        }

        public int getBuy1_volume() {
            return buy1_volume;
        }

        public void setBuy1_volume(int buy1_volume) {
            this.buy1_volume = buy1_volume;
        }

        public double getQuantity_ratio() {
            return quantity_ratio;
        }

        public void setQuantity_ratio(double quantity_ratio) {
            this.quantity_ratio = quantity_ratio;
        }

        public double getHigh_price() {
            return high_price;
        }

        public void setHigh_price(double high_price) {
            this.high_price = high_price;
        }

        public double getBuy1_price() {
            return buy1_price;
        }

        public void setBuy1_price(double buy1_price) {
            this.buy1_price = buy1_price;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getBuy5_volume() {
            return buy5_volume;
        }

        public void setBuy5_volume(int buy5_volume) {
            this.buy5_volume = buy5_volume;
        }

        public double getLow_price() {
            return low_price;
        }

        public void setLow_price(double low_price) {
            this.low_price = low_price;
        }

        public int getSell1_volume() {
            return sell1_volume;
        }

        public void setSell1_volume(int sell1_volume) {
            this.sell1_volume = sell1_volume;
        }

        public double getSell4_price() {
            return sell4_price;
        }

        public void setSell4_price(double sell4_price) {
            this.sell4_price = sell4_price;
        }

        public double getNegotiable_capital() {
            return negotiable_capital;
        }

        public void setNegotiable_capital(double negotiable_capital) {
            this.negotiable_capital = negotiable_capital;
        }

        public double getSell3_price() {
            return sell3_price;
        }

        public void setSell3_price(double sell3_price) {
            this.sell3_price = sell3_price;
        }

        public double getAmplitude() {
            return amplitude;
        }

        public void setAmplitude(double amplitude) {
            this.amplitude = amplitude;
        }

        public double getBuy2_price() {
            return buy2_price;
        }

        public void setBuy2_price(double buy2_price) {
            this.buy2_price = buy2_price;
        }

        public double getSell1_price() {
            return sell1_price;
        }

        public void setSell1_price(double sell1_price) {
            this.sell1_price = sell1_price;
        }

        public int getSymbol_type() {
            return symbol_type;
        }

        public void setSymbol_type(int symbol_type) {
            this.symbol_type = symbol_type;
        }

        public double getSell2_price() {
            return sell2_price;
        }

        public void setSell2_price(double sell2_price) {
            this.sell2_price = sell2_price;
        }

        public String getStock_code() {
            return stock_code;
        }

        public void setStock_code(String stock_code) {
            this.stock_code = stock_code;
        }

        public int getSell5_volume() {
            return sell5_volume;
        }

        public void setSell5_volume(int sell5_volume) {
            this.sell5_volume = sell5_volume;
        }

        public int getBuy2_volume() {
            return buy2_volume;
        }

        public void setBuy2_volume(int buy2_volume) {
            this.buy2_volume = buy2_volume;
        }

        public double getTcap() {
            return tcap;
        }

        public void setTcap(double tcap) {
            this.tcap = tcap;
        }

        public double getClose_price() {
            return close_price;
        }

        public void setClose_price(double close_price) {
            this.close_price = close_price;
        }

        public String getFullcode() {
            return fullcode;
        }

        public void setFullcode(String fullcode) {
            this.fullcode = fullcode;
        }

        public String getTdate() {
            return tdate;
        }

        public void setTdate(String tdate) {
            this.tdate = tdate;
        }

        public int getVolume_outer() {
            return volume_outer;
        }

        public void setVolume_outer(int volume_outer) {
            this.volume_outer = volume_outer;
        }

        public String getStock_name() {
            return stock_name;
        }

        public void setStock_name(String stock_name) {
            this.stock_name = stock_name;
        }

        public double getEps_y1() {
            return eps_y1;
        }

        public void setEps_y1(double eps_y1) {
            this.eps_y1 = eps_y1;
        }

        public double getBuy5_price() {
            return buy5_price;
        }

        public void setBuy5_price(double buy5_price) {
            this.buy5_price = buy5_price;
        }

        public int getSell4_volume() {
            return sell4_volume;
        }

        public void setSell4_volume(int sell4_volume) {
            this.sell4_volume = sell4_volume;
        }

        public double getCommission_rate() {
            return commission_rate;
        }

        public void setCommission_rate(double commission_rate) {
            this.commission_rate = commission_rate;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public double getTurnover_rate() {
            return turnover_rate;
        }

        public void setTurnover_rate(double turnover_rate) {
            this.turnover_rate = turnover_rate;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public int getCommission_volume() {
            return commission_volume;
        }

        public void setCommission_volume(int commission_volume) {
            this.commission_volume = commission_volume;
        }

        public double getAvg_price() {
            return avg_price;
        }

        public void setAvg_price(double avg_price) {
            this.avg_price = avg_price;
        }
    }
}
