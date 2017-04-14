package cn.gogoal.im.bean.stock;

import java.util.List;

/**
 * author wangjd on 2017/4/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :$整理后的数据.
 */
public class MarkteBean {
    private String title;
    private List<MarketItemData> datas;

    public MarkteBean(String title, List<MarketItemData> datas) {
        this.title = title;
        this.datas = datas;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MarketItemData> getDatas() {
        return datas;
    }

    public void setDatas(List<MarketItemData> datas) {
        this.datas = datas;
    }

    public static class MarketItemData {
        private String name;            //11    21
        private double price;          //12    24_0     33
        private String priceChange;   //13
        private String rate;           //14    24_1     34

        private String industryRate;  //22
        private String stockName;      //23    31
        private String code;           //32
        private int priceColor;//颜色字段

        public MarketItemData(String name, double price, String priceChange, String rate, String industryRate, String stockName, String code, int priceColor) {
            this.name = name;
            this.price = price;
            this.priceChange = priceChange;
            this.rate = rate;
            this.industryRate = industryRate;
            this.stockName = stockName;
            this.code = code;
            this.priceColor = priceColor;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getPriceChange() {
            return priceChange;
        }

        public void setPriceChange(String priceChange) {
            this.priceChange = priceChange;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getIndustryRate() {
            return industryRate;
        }

        public void setIndustryRate(String industryRate) {
            this.industryRate = industryRate;
        }

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getPriceColor() {
            return priceColor;
        }

        public void setPriceColor(int priceColor) {
            this.priceColor = priceColor;
        }
    }
}
