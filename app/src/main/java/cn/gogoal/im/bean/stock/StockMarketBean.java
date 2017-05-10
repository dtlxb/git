package cn.gogoal.im.bean.stock;

import java.util.List;

/**
 * author wangjd on 2017/3/10 0010.
 * Staff_id 1375
 * phone 18930640263
 */
public class StockMarketBean {

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

        private StockRanklistBean stockRanklist;
        private List<HostIndustrylistBean> hostIndustrylist;
        private List<HangqingBean> hangqing;

        public StockRanklistBean getStockRanklist() {
            return stockRanklist;
        }

        public void setStockRanklist(StockRanklistBean stockRanklist) {
            this.stockRanklist = stockRanklist;
        }

        public List<HostIndustrylistBean> getHostIndustrylist() {
            return hostIndustrylist;
        }

        public void setHostIndustrylist(List<HostIndustrylistBean> hostIndustrylist) {
            this.hostIndustrylist = hostIndustrylist;
        }

        public List<HangqingBean> getHangqing() {
            return hangqing;
        }

        public void setHangqing(List<HangqingBean> hangqing) {
            this.hangqing = hangqing;
        }

        public static class StockRanklistBean {

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

            public List<StockRankBean> getIncrease_list() {
                return increase_list;
            }

            public void setIncrease_list(List<StockRankBean> increase_list) {
                this.increase_list = increase_list;
            }

            public static class StockRankBean {

                private String rate;
                private String current_price;
                private String stock_code;
                private String stock_name;

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }

                public String getCurrent_price() {
                    return current_price;
                }

                public void setCurrent_price(String current_price) {
                    this.current_price = current_price;
                }

                public String getStock_code() {
                    return stock_code;
                }

                public void setStock_code(String stock_code) {
                    this.stock_code = stock_code;
                }

                public String getStock_name() {
                    return stock_name;
                }

                public void setStock_name(String stock_name) {
                    this.stock_name = stock_name;
                }
            }

        }

        public static class HostIndustrylistBean {

            private String industry_rate;
            private String rate;
            private String current_price;
            private String stock_code;
            private String stock_name;
            private String industry_code;
            private String industry_name;

            public String getIndustry_rate() {
                return industry_rate;
            }

            public void setIndustry_rate(String industry_rate) {
                this.industry_rate = industry_rate;
            }

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }

            public String getCurrent_price() {
                return current_price;
            }

            public void setCurrent_price(String current_price) {
                this.current_price = current_price;
            }

            public String getStock_code() {
                return stock_code;
            }

            public void setStock_code(String stock_code) {
                this.stock_code = stock_code;
            }

            public String getStock_name() {
                return stock_name;
            }

            public void setStock_name(String stock_name) {
                this.stock_name = stock_name;
            }

            public String getIndustry_code() {
                return industry_code;
            }

            public void setIndustry_code(String industry_code) {
                this.industry_code = industry_code;
            }

            public String getIndustry_name() {
                return industry_name;
            }

            public void setIndustry_name(String industry_name) {
                this.industry_name = industry_name;
            }
        }

        public static class HangqingBean {
            /**
             * amplitude : 0.3323918331498947
             * close_price : 3216.7457
             * code : 000001
             * fall : 513
             * flat : 143
             * fullcode : sh000001
             * high_price : 3222.3189
             * insert_time : 2017-03-10 11:27:49
             * low_price : 3211.6267
             * name : 上证指数
             * open_price : 3213.7294
             * price : 3215.4443
             * price_change : -1.3013999999998305
             * price_change_rate : -0.04045703706077327
             * rose : 615
             * source : sh
             * stage : T
             * symbol_type : 2
             * tdate : 2017-03-10 00:00:00
             * turnover : 9102933.54655
             * update_time : 2017-03-10 11:27:47
             * volume : 7281191500
             * deal_count : 2145362
             * md_stream_id : 900
             */

            private double amplitude;
            private double close_price;
            private String code;
            private int fall;
            private int flat;
            private String fullcode;
            private double high_price;
            private String insert_time;
            private double low_price;
            private String name;
            private double open_price;
            private double price;
            private String price_change;
            private String price_change_rate;
            private int rose;
            private String source;
            private String stage;
            private int symbol_type;
            private String tdate;
            private double turnover;
            private String update_time;
            private long volume;
            private int deal_count;
            private String md_stream_id;

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

            public int getRose() {
                return rose;
            }

            public void setRose(int rose) {
                this.rose = rose;
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

            public int getSymbol_type() {
                return symbol_type;
            }

            public void setSymbol_type(int symbol_type) {
                this.symbol_type = symbol_type;
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

            public long getVolume() {
                return volume;
            }

            public void setVolume(long volume) {
                this.volume = volume;
            }

            public int getDeal_count() {
                return deal_count;
            }

            public void setDeal_count(int deal_count) {
                this.deal_count = deal_count;
            }

            public String getMd_stream_id() {
                return md_stream_id;
            }

            public void setMd_stream_id(String md_stream_id) {
                this.md_stream_id = md_stream_id;
            }
        }
    }
}
