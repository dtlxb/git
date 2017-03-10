package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/3/9 0009.
 * Staff_id 1375
 * phone 18930640263
 */
public class HangQing {

    private int fall;

    private int flat;

    private int rose;

    private int symbol_type;

    private String volume;

    public int getFall() {
        return fall;
    }

    public void setFall(int fall) {
        this.fall = fall;
    }

    public int getFlat() {
        return flat;
    }

    public void setFlat(int flat) {
        this.flat = flat;
    }

    public int getRose() {
        return rose;
    }

    public void setRose(int rose) {
        this.rose = rose;
    }

    public int getSymbol_type() {
        return symbol_type;
    }

    public void setSymbol_type(int symbol_type) {
        this.symbol_type = symbol_type;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    private double amplitude;

    private double close_price;

    private String code;

    private String fullcode;

    private double high_price;

    private String insert_time;

    private double low_price;

    private String name;

    private double open_price;

    private double price;

    private double price_change;

    private double price_change_rate;

    private String source;

    private String stage;

    private String tdate;

    private double turnover;

    private String update_time;

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getClose_price() {
        return close_price;
    }

    public void setClose_price(double close_price) {
        this.close_price = close_price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullcode() {
        return fullcode;
    }

    public void setFullcode(String fullcode) {
        this.fullcode = fullcode;
    }

    public double getHigh_price() {
        return high_price;
    }

    public void setHigh_price(double high_price) {
        this.high_price = high_price;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public double getLow_price() {
        return low_price;
    }

    public void setLow_price(double low_price) {
        this.low_price = low_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOpen_price() {
        return open_price;
    }

    public void setOpen_price(double open_price) {
        this.open_price = open_price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice_change() {
        return price_change;
    }

    public void setPrice_change(double price_change) {
        this.price_change = price_change;
    }

    public double getPrice_change_rate() {
        return price_change_rate;
    }

    public void setPrice_change_rate(double price_change_rate) {
        this.price_change_rate = price_change_rate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "HangQing{" +
                "fall=" + fall +
                ", flat=" + flat +
                ", rose=" + rose +
                ", symbol_type=" + symbol_type +
                ", volume='" + volume + '\'' +
                ", amplitude=" + amplitude +
                ", close_price=" + close_price +
                ", code='" + code + '\'' +
                ", fullcode='" + fullcode + '\'' +
                ", high_price=" + high_price +
                ", insert_time='" + insert_time + '\'' +
                ", low_price=" + low_price +
                ", name='" + name + '\'' +
                ", open_price=" + open_price +
                ", price=" + price +
                ", price_change=" + price_change +
                ", price_change_rate=" + price_change_rate +
                ", source='" + source + '\'' +
                ", stage='" + stage + '\'' +
                ", tdate='" + tdate + '\'' +
                ", turnover=" + turnover +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
