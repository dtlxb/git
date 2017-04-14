package cn.gogoal.im.bean.stock;

import java.util.List;

/**
 * author wangjd on 2017/4/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description 热门行业 详情列表.
 */
public class HotIndustryBean {

    /**
     * message : 成功
     * data : [{"industry_rate":"1.422133328795117","rate":"5.536369522987275","current_price":"97.79","stock_code":"002829","stock_name":"星网宇达","industry_code":"801740","industry_name":"国防军工"},{"industry_rate":"1.1618463687880043","rate":"10.077519379844952","current_price":"5.68","stock_code":"600008","stock_name":"首创股份","industry_code":"801160","industry_name":"公用事业"},{"industry_rate":"1.1593138930376137","rate":"5.981941309255093","current_price":"9.39","stock_code":"300164","stock_name":"通源石油","industry_code":"801020","industry_name":"采掘"},{"industry_rate":"1.0257556491931288","rate":"10.047393364928897","current_price":"11.61","stock_code":"002146","stock_name":"荣盛发展","industry_code":"801180","industry_name":"房地产"},{"industry_rate":"0.9778053528633951","rate":"10.012172854534384","current_price":"36.15","stock_code":"300428","stock_name":"四通新材","industry_code":"801050","industry_name":"有色金属"},{"industry_rate":"0.7207599250257929","rate":"10.022075055187653","current_price":"24.92","stock_code":"300374","stock_name":"恒通科技","industry_code":"801710","industry_name":"建筑材料"},{"industry_rate":"0.5591242449234426","rate":"10.016155088852976","current_price":"27.24","stock_code":"603041","stock_name":"美思德","industry_code":"801030","industry_name":"化工"},{"industry_rate":"0.4743564295760598","rate":"10.068649885583513","current_price":"4.81","stock_code":"000709","stock_name":"河钢股份","industry_code":"801040","industry_name":"钢铁"},{"industry_rate":"0.42392394621873014","rate":"10.024937655860338","current_price":"22.06","stock_code":"600149","stock_name":"廊坊发展","industry_code":"801230","industry_name":"综合"},{"industry_rate":"0.3099500788716293","rate":"43.99731723675385","current_price":"42.94","stock_code":"002859","stock_name":"N洁美","industry_code":"801080","industry_name":"电子"},{"industry_rate":"0.256653130467664","rate":"4.346942920983142","current_price":"51.37","stock_code":"002508","stock_name":"老板电器","industry_code":"801110","industry_name":"家用电器"},{"industry_rate":"0.23638786184615945","rate":"9.962406015037581","current_price":"5.85","stock_code":"601000","stock_name":"唐山港","industry_code":"801170","industry_name":"交通运输"},{"industry_rate":"0.14455249751381974","rate":"6.236559139784955","current_price":"83.98","stock_code":"300577","stock_name":"开润股份","industry_code":"801130","industry_name":"纺织服装"},{"industry_rate":"0.13832006336956226","rate":"5.471698113207548","current_price":"11.18","stock_code":"600828","stock_name":"茂业商业","industry_code":"801200","industry_name":"商业贸易"},{"industry_rate":"0.11015884032644044","rate":"10.019841269841267","current_price":"22.18","stock_code":"300075","stock_name":"数字政通","industry_code":"801750","industry_name":"计算机"},{"industry_rate":"0.09002386728706441","rate":"4.821878373515653","current_price":"29.13","stock_code":"600258","stock_name":"首旅酒店","industry_code":"801210","industry_name":"休闲服务"},{"industry_rate":"0.05414200804213103","rate":"10.04672897196261","current_price":"9.42","stock_code":"600550","stock_name":"保变电气","industry_code":"801730","industry_name":"电气设备"},{"industry_rate":"0.03695786082061417","rate":"6.758130081300822","current_price":"21.01","stock_code":"002807","stock_name":"江阴银行","industry_code":"801780","industry_name":"银行"},{"industry_rate":"0.03114335674375917","rate":"2.0987174504469457","current_price":"26.27","stock_code":"600487","stock_name":"亨通光电","industry_code":"801770","industry_name":"通信"},{"industry_rate":"0.02612445511884568","rate":"9.990485252140825","current_price":"23.12","stock_code":"603385","stock_name":"惠达卫浴","industry_code":"801140","industry_name":"轻工制造"},{"industry_rate":"0.012011619115992354","rate":"10.016694490818042","current_price":"32.95","industry_name":"汽车"},{"industry_rate":"-0.06715122566473392","rate":"10.02087682672233","current_price":"21.08","stock_code":"603969","stock_name":"银龙股份","industry_code":"801890","industry_name":"机械设备"},{"industry_rate":"-0.12789893972580962","rate":"43.97719173200285","current_price":"20.2","stock_code":"603538","stock_name":"N美诺华","industry_code":"801150","industry_name":"医药生物"},{"industry_rate":"-0.14795012924944453","rate":"3.2655576093653798","current_price":"16.76","stock_code":"600872","stock_name":"中炬高新","industry_code":"801120","industry_name":"食品饮料"},{"industry_rate":"-0.16641769816078017","rate":"1.7377567140600367","current_price":"6.44","stock_code":"000563","stock_name":"陕国投Ａ","industry_code":"801790","industry_name":"非银金融"},{"industry_rate":"-0.18372242640093872","rate":"6.586270871985166","current_price":"22.98","stock_code":"600298","stock_name":"安琪酵母","industry_code":"801010","industry_name":"农林牧渔"},{"industry_rate":"-1.02591820103742","rate":"10.028653295128928","current_price":"11.52","stock_code":"002542","stock_name":"中化岩土","industry_code":"801720","industry_name":"建筑装饰"}]
     * code : 0
     */

    private String message;
    private int code;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * industry_rate : 1.422133328795117
         * rate : 5.536369522987275
         * current_price : 97.79
         * stock_code : 002829
         * stock_name : 星网宇达
         * industry_code : 801740
         * industry_name : 国防军工
         */

        private String industry_rate;
        private double rate;
        private String current_price;
        private String stock_code;
        private String stock_name;
        private int stock_type;
        private String industry_code;
        private String industry_name;

        public int getStock_type() {
            return stock_type;
        }

        public void setStock_type(int stock_type) {
            this.stock_type = stock_type;
        }

        public String getIndustry_rate() {
            return industry_rate;
        }

        public void setIndustry_rate(String industry_rate) {
            this.industry_rate = industry_rate;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
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
}
