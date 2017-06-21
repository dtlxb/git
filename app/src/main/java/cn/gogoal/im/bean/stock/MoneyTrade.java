package cn.gogoal.im.bean.stock;

/**
 * Created by huangxx on 2017/6/21.
 */

public class MoneyTrade {

    private String tradeType;
    private String tradeNum;
    private String tradePer;
    private String color;

    public MoneyTrade(String tradeType, String tradeNum, String tradePer, String color) {
        this.tradeType = tradeType;
        this.tradeNum = tradeNum;
        this.tradePer = tradePer;
        this.color = color;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }

    public String getTradePer() {
        return tradePer;
    }

    public void setTradePer(String tradePer) {
        this.tradePer = tradePer;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "MoneyTrade{" +
                "tradeType='" + tradeType + '\'' +
                ", tradeNum='" + tradeNum + '\'' +
                ", tradePer='" + tradePer + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
