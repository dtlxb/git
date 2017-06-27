package cn.gogoal.im.fragment.stock.news_report.bean;

import java.util.List;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :
 */
public class MyStockNewsBean {

    /**
     * message : 成功
     * data : [{"change_rate":0.9743589743589809,"origin":"金融界 ","stock_code":"300024","date":"2017-06-27 10:49:24","author":null,"origin_link":"stock.jrj.com.cn/2017/06/27095522659981.shtml?3=3","title":"发改委赴辽宁调研新一轮东北振兴战略推进情况","price":19.69,"source":7,"origin_id":6229645,"stock_name":"机器人"},{"change_rate":-0.3795066413662254,"origin":"上证e互动1","stock_code":"603444","date":"2017-06-27 10:45:00","author":null,"origin_link":"rs.sns.sseinfo.com/resources/images/upload/201706/2017062710450371009282984.pdf","title":"厦门吉比特网络技术股份有限公司2017年6月15日至6月26日投资者调研沟通活动纪要.pdf ","price":294,"source":7,"origin_id":6229663,"stock_name":"吉比特"},{"change_rate":-0.19569471624267465,"origin":"金融界 ","stock_code":"601398","date":"2017-06-27 10:41:06","author":null,"origin_link":"stock.jrj.com.cn/2017/06/27090522659775.shtml?3=3","title":"中信银行资产管理业务中心原总裁马续田被提起公诉","price":5.1,"source":7,"origin_id":6229577,"stock_name":"工商银行"},{"change_rate":10.025188916876566,"origin":"金融界 ","stock_code":"300348","date":"2017-06-27 10:39:02","author":null,"origin_link":"stock.jrj.com.cn/2017/06/27095722660037.shtml?3=3","title":"6月27日涨停揭秘","price":21.84,"source":7,"origin_id":6229578,"stock_name":"长亮科技"},{"change_rate":10.025188916876566,"origin":"金融界 ","stock_code":"300348","date":"2017-06-27 10:27:42","author":null,"origin_link":"stock.jrj.com.cn/2017/06/27093522659861.shtml?3=3","title":"开评：两市小幅低开 沪指跌0.06%","price":21.84,"source":7,"origin_id":6229509,"stock_name":"长亮科技"},{"change_rate":9.997613934621821,"origin":"金融界 ","stock_code":"300467","date":"2017-06-27 09:44:23","author":null,"origin_link":"stock.jrj.com.cn/2017/06/27091522659719.shtml?3=3","title":"迅游科技：有信心与王者荣耀保持长期合作关系","price":46.1,"source":7,"origin_id":6229366,"stock_name":"迅游科技"},{"change_rate":10.025188916876566,"origin":"金融界 ","stock_code":"300348","date":"2017-06-27 09:32:31","author":null,"origin_link":"stock.jrj.com.cn/2017/06/27090322659617.shtml?3=3","title":"长亮科技与腾讯云战略合作 打造互联网金融联盟","price":21.84,"source":7,"origin_id":6229339,"stock_name":"长亮科技"},{"change_rate":0.4682274247491658,"origin":"上证e互动1","stock_code":"600900","date":"2017-06-27 09:22:00","author":null,"origin_link":"rs.sns.sseinfo.com/resources/images/upload/201706/2017062709220081363696111.pdf","title":"\u201c投资者保护·明规则、识风险\u201d案例\u2014\u2014\u201c尾市\u201d拉升藏玄机 盲目追涨落陷阱.pdf ","price":15.02,"source":7,"origin_id":6229317,"stock_name":"长江电力"},{"change_rate":-0.43399638336346275,"origin":"金融界 ","stock_code":"300288","date":"2017-06-27 09:11:49","author":null,"origin_link":"stock.jrj.com.cn/2017/06/27082522659607.shtml?3=3","title":"盘前：沪指逼近3200点 阶段性反弹逐步展开","price":27.53,"source":7,"origin_id":6229281,"stock_name":"朗玛信息"},{"change_rate":1.4595496246872304,"origin":"金融界 ","stock_code":"300347","date":"2017-06-27 09:10:56","author":null,"origin_link":"stock.jrj.com.cn/2017/06/27082222659384.shtml?3=3","title":"\u201c拉卡拉式\u201d重组再现江湖 闯关结果将树市场风向标","price":24.33,"source":7,"origin_id":6229280,"stock_name":"泰格医药"},{"change_rate":9.997613934621821,"origin":"大众证券报","stock_code":"300467","date":"2017-06-27 08:52:00","author":null,"origin_link":"finance.sina.com.cn/stock/s/2017-06-27/doc-ifyhmtek7817317.shtml","title":"迅游科技重组复牌首日跌停 三机构现身卖出席位","price":46.1,"source":7,"origin_id":6229249,"stock_name":"迅游科技"},{"change_rate":0.08532423208190944,"origin":"金融界 ","stock_code":"300377","date":"2017-06-27 08:39:59","author":null,"origin_link":"stock.jrj.com.cn/2017/06/27082222659383.shtml?3=3","title":"导演史上最悲催高送转赢时胜：若为减持故 老婆也可抛","price":11.73,"source":7,"origin_id":6229122,"stock_name":"赢时胜"},{"change_rate":2.454991816693944,"origin":"金融界 ","stock_code":"002179","date":"2017-06-27 08:39:54","author":null,"origin_link":"stock.jrj.com.cn/2017/06/27082022659369.shtml?3=3","title":"中金公司：军工行业否极泰来 有望实现超额收益","price":31.3,"source":7,"origin_id":6229124,"stock_name":"中航光电"},{"change_rate":0.06108735491751991,"origin":"金融投资报","stock_code":"601166","date":"2017-06-27 08:26:00","author":null,"origin_link":"www.p5w.net/stock/news/gsxw/201706/t20170627_1853565.htm","title":"绿色金融\u201c点亮\u201d一带一路 兴业银行牵手2017阿斯塔纳世博会","price":16.38,"source":7,"origin_id":6229112,"stock_name":"兴业银行"},{"change_rate":0.5012531328320695,"origin":"金融投资报","stock_code":"601818","date":"2017-06-27 08:26:00","author":null,"origin_link":"www.p5w.net/stock/news/gsxw/201706/t20170627_1853569.htm","title":"光大移动金融业务快速发展 手机银行客户量突破3000万","price":4.01,"source":7,"origin_id":6229111,"stock_name":"光大银行"}]
     * code : 0
     */

    private String message;
    private int code;
    private List<MyStockNews> data;

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

    public List<MyStockNews> getData() {
        return data;
    }

    public void setData(List<MyStockNews> data) {
        this.data = data;
    }

    public static class MyStockNews {
        /**
         * change_rate : 0.9743589743589809
         * origin : 金融界
         * stock_code : 300024
         * date : 2017-06-27 10:49:24
         * author : null
         * origin_link : stock.jrj.com.cn/2017/06/27095522659981.shtml?3=3
         * title : 发改委赴辽宁调研新一轮东北振兴战略推进情况
         * price : 19.69
         * source : 7
         * origin_id : 6229645
         * stock_name : 机器人
         */

        private String change_rate;
        private String origin;
        private String stock_code;
        private String date;
        private String author;
        private String origin_link;
        private String title;
        private double price;
        private int source;
        private int origin_id;
        private String stock_name;

        public String getChange_rate() {
            return change_rate;
        }

        public void setChange_rate(String change_rate) {
            this.change_rate = change_rate;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getStock_code() {
            return stock_code;
        }

        public void setStock_code(String stock_code) {
            this.stock_code = stock_code;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getOrigin_link() {
            return origin_link;
        }

        public void setOrigin_link(String origin_link) {
            this.origin_link = origin_link;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getOrigin_id() {
            return origin_id;
        }

        public void setOrigin_id(int origin_id) {
            this.origin_id = origin_id;
        }

        public String getStock_name() {
            return stock_name;
        }

        public void setStock_name(String stock_name) {
            this.stock_name = stock_name;
        }
    }
}
