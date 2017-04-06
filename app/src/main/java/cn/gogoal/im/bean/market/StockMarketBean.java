package cn.gogoal.im.bean.market;

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
        /**
         * stockRanklist : {"gray_counts":358,"amplitude_list":[{"rate":23.99577167019028,"current_price":68.11,"stock_code":"002852","stock_name":"N道道全"},{"rate":23.98427260812582,"current_price":21.97,"stock_code":"603955","stock_name":"N大千"},{"rate":23.97679664840476,"current_price":44.68,"stock_code":"002853","stock_name":"N皮阿诺"},{"rate":13.210848643919526,"current_price":16.46,"stock_code":"300620","stock_name":"N光库"},{"rate":11.610633307271302,"current_price":28.05,"stock_code":"002349","stock_name":"精华制药"},{"rate":10.814814814814808,"current_price":7.07,"stock_code":"600577","stock_name":"精达股份"},{"rate":10.575355229443277,"current_price":47.22,"stock_code":"603090","stock_name":"宏盛股份"},{"rate":10.522999369880278,"current_price":16.86,"stock_code":"000980","stock_name":"金马股份"},{"rate":10.5080831408776,"current_price":19.05,"stock_code":"300304","stock_name":"云意电气"},{"rate":10.447987851176912,"current_price":69.45,"stock_code":"600892","stock_name":"大晟文化"}],"down_list":[{"rate":-10.009686793671287,"current_price":27.87,"stock_code":"603558","stock_name":"健盛集团"},{"rate":-6.397181531615062,"current_price":50.48,"stock_code":"603238","stock_name":"诺邦股份"},{"rate":-5.724357898270948,"current_price":56.16,"stock_code":"300611","stock_name":"美力科技"},{"rate":-5.078809106830123,"current_price":43.36,"stock_code":"603345","stock_name":"安井食品"},{"rate":-4.620462046204622,"current_price":20.23,"stock_code":"002807","stock_name":"江阴银行"},{"rate":-4.493260109835258,"current_price":19.13,"stock_code":"603323","stock_name":"吴江银行"},{"rate":-4.47368421052632,"current_price":25.41,"stock_code":"002742","stock_name":"三圣股份"},{"rate":-4.323483670295481,"current_price":61.52,"stock_code":"002849","stock_name":"威星智能"},{"rate":-4.043715846994546,"current_price":17.56,"stock_code":"002391","stock_name":"长青股份"},{"rate":-4.035512510088781,"current_price":23.78,"stock_code":"002839","stock_name":"张家港行"}],"green_counts":1288,"change_list":[{"rate":0.5924,"current_price":55.96,"stock_code":"002836","stock_name":"新宏泽"},{"rate":0.552,"current_price":40.47,"stock_code":"603603","stock_name":"博天环境"},{"rate":0.4723,"current_price":38.59,"stock_code":"300601","stock_name":"康泰生物"},{"rate":0.4335,"current_price":280,"stock_code":"300613","stock_name":"富瀚微"},{"rate":0.2838,"current_price":56.16,"stock_code":"300611","stock_name":"美力科技"},{"rate":0.2679,"current_price":63.88,"stock_code":"002796","stock_name":"世嘉科技"},{"rate":0.2662,"current_price":59.02,"stock_code":"603822","stock_name":"嘉澳环保"},{"rate":0.2482,"current_price":49.39,"stock_code":"002819","stock_name":"东方中科"},{"rate":0.2288,"current_price":55.5,"stock_code":"002820","stock_name":"桂发祥"},{"rate":0.2253,"current_price":43.36,"stock_code":"603345","stock_name":"安井食品"}],"increase_list":[{"rate":44.006999125109374,"current_price":16.46,"stock_code":"300620","stock_name":"N光库"},{"rate":43.99577167019028,"current_price":68.11,"stock_code":"002852","stock_name":"N道道全"},{"rate":43.989687399291,"current_price":44.68,"stock_code":"002853","stock_name":"N皮阿诺"},{"rate":43.97116644823066,"current_price":21.97,"stock_code":"603955","stock_name":"N大千"},{"rate":10.064239828693784,"current_price":5.14,"stock_code":"600208","stock_name":"新湖中宝"},{"rate":10.026109660574422,"current_price":21.07,"stock_code":"603138","stock_name":"海量数据"},{"rate":10.016764459346188,"current_price":26.25,"stock_code":"300618","stock_name":"寒锐钴业"},{"rate":10.00996015936256,"current_price":22.09,"stock_code":"603817","stock_name":"海峡环保"},{"rate":10.009624639076026,"current_price":34.29,"stock_code":"300308","stock_name":"中际装备"},{"rate":10.0093023255814,"current_price":59.13,"stock_code":"300609","stock_name":"汇纳科技"}],"red_counts":1481}
         * hostIndustrylist : [{"industry_rate":"1.6887662862564539","rate":"10.000000000000005","current_price":"25.85","stock_code":"603578","stock_name":"三星新材","industry_code":"801110","industry_name":"家用电器"},{"industry_rate":"0.9080869476290222","rate":"4.261890055589856","current_price":"16.88","stock_code":"000019","stock_name":"深深宝Ａ","industry_code":"801120","industry_name":"食品饮料"},{"industry_rate":"0.8606900912234597","rate":"3.2786885245901547","current_price":"3.78","stock_code":"600282","stock_name":"南钢股份","industry_code":"801040","industry_name":"钢铁"},{"industry_rate":"0.5109617998111036","rate":"10.064239828693784","current_price":"5.14","stock_code":"600208","stock_name":"新湖中宝","industry_code":"801180","industry_name":"房地产"},{"industry_rate":"0.48315991277612175","rate":"8.095854922279804","current_price":"16.69","stock_code":"002419","stock_name":"天虹商场","industry_code":"801200","industry_name":"商业贸易"},{"industry_rate":"0.4648402488360447","rate":"43.989687399291","current_price":"44.68","stock_code":"002853","stock_name":"N皮阿诺","industry_code":"801140","industry_name":"轻工制造"}]
         * hangqing : [{"amplitude":0.3323918331498947,"close_price":3216.7457,"code":"000001","fall":513,"flat":143,"fullcode":"sh000001","high_price":3222.3189,"insert_time":"2017-03-10 11:27:49","low_price":3211.6267,"name":"上证指数","open_price":3213.7294,"price":3215.4443,"price_change":-1.3013999999998305,"price_change_rate":-0.04045703706077327,"rose":615,"source":"sh","stage":"T","symbol_type":2,"tdate":"2017-03-10 00:00:00","turnover":9102933.54655,"update_time":"2017-03-10 11:27:47","volume":7281191500},{"amplitude":0.35778818432913656,"close_price":3426.9438,"code":"000300","fullcode":"sh000300","high_price":3436.2388,"insert_time":"2017-03-10 11:27:49","low_price":3423.9776,"name":"沪深300","open_price":3423.9776,"price":3430.1721,"price_change":3.228299999999763,"price_change_rate":0.09420347074264138,"source":"sh","stage":"T","symbol_type":2,"tdate":"2017-03-10 00:00:00","turnover":4360667.9288,"update_time":"2017-03-10 11:27:47","volume":3817816800},{"amplitude":0.49494591614297456,"close_price":10421.0578,"code":"399001","deal_count":2145362,"fall":793,"flat":232,"fullcode":"sz399001","high_price":10466.9571,"insert_time":"2017-03-10 11:27:48","low_price":10415.3785,"md_stream_id":"900","name":"深证成指","open_price":10415.3785,"price":10453.3205,"price_change":32.26269999999931,"price_change_rate":0.30959141211172736,"rose":931,"source":"sz","stage":"T","symbol_type":2,"tdate":"2017-03-10 00:00:00","turnover":1.286421598198897E7,"update_time":"2017-03-10 11:27:48","volume":7707975668},{"amplitude":0.6213232989200053,"close_price":1953.9425,"code":"399006","deal_count":397276,"fullcode":"sz399006","high_price":1957.7135,"insert_time":"2017-03-10 11:27:48","low_price":1945.5732,"md_stream_id":"900","name":"创业板指","open_price":1952.5013,"price":1951.5355,"price_change":-2.407000000000153,"price_change_rate":-0.12318683891671084,"source":"sz","stage":"T","symbol_type":2,"tdate":"2017-03-10 00:00:00","turnover":4123867.073909,"update_time":"2017-03-10 11:27:48","volume":1606790903}]
         */

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
            /**
             * gray_counts : 358
             * amplitude_list : [{"rate":23.99577167019028,"current_price":68.11,"stock_code":"002852","stock_name":"N道道全"},{"rate":23.98427260812582,"current_price":21.97,"stock_code":"603955","stock_name":"N大千"},{"rate":23.97679664840476,"current_price":44.68,"stock_code":"002853","stock_name":"N皮阿诺"},{"rate":13.210848643919526,"current_price":16.46,"stock_code":"300620","stock_name":"N光库"},{"rate":11.610633307271302,"current_price":28.05,"stock_code":"002349","stock_name":"精华制药"},{"rate":10.814814814814808,"current_price":7.07,"stock_code":"600577","stock_name":"精达股份"},{"rate":10.575355229443277,"current_price":47.22,"stock_code":"603090","stock_name":"宏盛股份"},{"rate":10.522999369880278,"current_price":16.86,"stock_code":"000980","stock_name":"金马股份"},{"rate":10.5080831408776,"current_price":19.05,"stock_code":"300304","stock_name":"云意电气"},{"rate":10.447987851176912,"current_price":69.45,"stock_code":"600892","stock_name":"大晟文化"}]
             * down_list : [{"rate":-10.009686793671287,"current_price":27.87,"stock_code":"603558","stock_name":"健盛集团"},{"rate":-6.397181531615062,"current_price":50.48,"stock_code":"603238","stock_name":"诺邦股份"},{"rate":-5.724357898270948,"current_price":56.16,"stock_code":"300611","stock_name":"美力科技"},{"rate":-5.078809106830123,"current_price":43.36,"stock_code":"603345","stock_name":"安井食品"},{"rate":-4.620462046204622,"current_price":20.23,"stock_code":"002807","stock_name":"江阴银行"},{"rate":-4.493260109835258,"current_price":19.13,"stock_code":"603323","stock_name":"吴江银行"},{"rate":-4.47368421052632,"current_price":25.41,"stock_code":"002742","stock_name":"三圣股份"},{"rate":-4.323483670295481,"current_price":61.52,"stock_code":"002849","stock_name":"威星智能"},{"rate":-4.043715846994546,"current_price":17.56,"stock_code":"002391","stock_name":"长青股份"},{"rate":-4.035512510088781,"current_price":23.78,"stock_code":"002839","stock_name":"张家港行"}]
             * green_counts : 1288
             * change_list : [{"rate":0.5924,"current_price":55.96,"stock_code":"002836","stock_name":"新宏泽"},{"rate":0.552,"current_price":40.47,"stock_code":"603603","stock_name":"博天环境"},{"rate":0.4723,"current_price":38.59,"stock_code":"300601","stock_name":"康泰生物"},{"rate":0.4335,"current_price":280,"stock_code":"300613","stock_name":"富瀚微"},{"rate":0.2838,"current_price":56.16,"stock_code":"300611","stock_name":"美力科技"},{"rate":0.2679,"current_price":63.88,"stock_code":"002796","stock_name":"世嘉科技"},{"rate":0.2662,"current_price":59.02,"stock_code":"603822","stock_name":"嘉澳环保"},{"rate":0.2482,"current_price":49.39,"stock_code":"002819","stock_name":"东方中科"},{"rate":0.2288,"current_price":55.5,"stock_code":"002820","stock_name":"桂发祥"},{"rate":0.2253,"current_price":43.36,"stock_code":"603345","stock_name":"安井食品"}]
             * increase_list : [{"rate":44.006999125109374,"current_price":16.46,"stock_code":"300620","stock_name":"N光库"},{"rate":43.99577167019028,"current_price":68.11,"stock_code":"002852","stock_name":"N道道全"},{"rate":43.989687399291,"current_price":44.68,"stock_code":"002853","stock_name":"N皮阿诺"},{"rate":43.97116644823066,"current_price":21.97,"stock_code":"603955","stock_name":"N大千"},{"rate":10.064239828693784,"current_price":5.14,"stock_code":"600208","stock_name":"新湖中宝"},{"rate":10.026109660574422,"current_price":21.07,"stock_code":"603138","stock_name":"海量数据"},{"rate":10.016764459346188,"current_price":26.25,"stock_code":"300618","stock_name":"寒锐钴业"},{"rate":10.00996015936256,"current_price":22.09,"stock_code":"603817","stock_name":"海峡环保"},{"rate":10.009624639076026,"current_price":34.29,"stock_code":"300308","stock_name":"中际装备"},{"rate":10.0093023255814,"current_price":59.13,"stock_code":"300609","stock_name":"汇纳科技"}]
             * red_counts : 1481
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
                /**
                 * rate : 23.99577167019028
                 * current_price : 68.11
                 * stock_code : 002852
                 * stock_name : N道道全
                 */

                private double rate;
                private double current_price;
                private String stock_code;
                private String stock_name;

                public double getRate() {
                    return rate;
                }

                public void setRate(double rate) {
                    this.rate = rate;
                }

                public double getCurrent_price() {
                    return current_price;
                }

                public void setCurrent_price(double current_price) {
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
            /**
             * industry_rate : 1.6887662862564539
             * rate : 10.000000000000005
             * current_price : 25.85
             * stock_code : 603578
             * stock_name : 三星新材
             * industry_code : 801110
             * industry_name : 家用电器
             */

            private double industry_rate;
            private double rate;
            private double current_price;
            private String stock_code;
            private String stock_name;
            private String industry_code;
            private String industry_name;

            public double getIndustry_rate() {
                return industry_rate;
            }

            public void setIndustry_rate(double industry_rate) {
                this.industry_rate = industry_rate;
            }

            public double getRate() {
                return rate;
            }

            public void setRate(double rate) {
                this.rate = rate;
            }

            public double getCurrent_price() {
                return current_price;
            }

            public void setCurrent_price(double current_price) {
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
            private double price_change;
            private double price_change_rate;
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
