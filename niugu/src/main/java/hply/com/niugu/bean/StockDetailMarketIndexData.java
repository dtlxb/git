package hply.com.niugu.bean;

/**
 * Created by daiwei on 2015/10/10.
 */
public class StockDetailMarketIndexData {
    /*{
        "amplitude":1.7505452328026796,
            "close_price":3143.3573,
            "code":"000001",
            "fullcode":"sh000001",
            "high_price":3192.7165,
            "insert_time":"2015-10-0915:04:05",
            "low_price":3137.7881,
            "name":"上证指数",
            "open_price":3146.6438,
            "price":3183.1516,
            "price_change":39.79430000000002,
            "price_change_rate":1.26598080339133,
            "source":"sh",
            "stage":"T",
            "tdate":"2015-10-0900:00:00",
            "turnover":25637911.21409,
            "type":1,
            "update_time":"2015-10-0915:04:03",
            "volume":23485144500
    }*/
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
    private String type;
    private String update_time;
    private String volume;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "StockDetailMarketIndexData{" +
                "amplitude=" + amplitude +
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
                ", type='" + type + '\'' +
                ", update_time='" + update_time + '\'' +
                ", volume='" + volume + '\'' +
                '}';
    }
}
