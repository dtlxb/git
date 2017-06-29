package cn.gogoal.im.bean.stock.bigdata.event;

import java.util.List;

import cn.gogoal.im.bean.stock.Stock;

/**
 * author wangjd on 2017/6/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * description :事件内容
 */
public class EventDetailBean {

    /**
     * message : 成功
     * data : {"content":"<p>【<span style=\"color:#FF0000\">事件<\/span>】近日，在北京举办的&ldquo;阿里技术下午茶&rdquo;活动上，阿里集团首席通信科学家谢崇进博士介绍了阿里NASA计划-量子通信技术的发展规划。据透露，阿里巴巴除了持续深耕量子通信技术，还会通过阿里云，向全球企业输出量子通信的安全服务。<\/p><p>【<span style=\"color:#FF0000\">点评<\/span>】3月，阿里云发布全球首个云上量子通信商用服务，由此打开了量子信息科学普惠发展之门；5月，中科院、阿里巴巴等机构研发的世界上第一台超越早期经典计算机的光量子计算机在中国诞生。按照马云此前宣布的&ldquo;NASA&rdquo;计划，阿里目前正在为20年后储备核心科技，以实现换道超车，服务全球20亿人，成为世界第五大经济体。目前量子通信主要运用于金融、国防、政务等领域，实际规模有限，短期内只有100-130亿元左右，但长期来看市场规模可达千亿。<\/p><p>量子通信可关注：三力士、中天科技、浙江东方、华夏幸福、神州信息、福晶科技等。<\/p><p><span style=\"color:#808080\"><span style=\"font-size:12px\">以上数据和观点由Go-Goal金融终端提供支持,仅供参考,风险自担.股市有风险,投资需谨慎.<\/span><\/span><\/p>","id":5433,"title":"阿里首次公开量子通信技术 相关概念股受益","theme_words":["量子通信"],"source":"量子通信","type":12,"date":"2017-06-28 14:28:58","stocks":[{"stock_code":"002224","stock_name":"三力士"},{"stock_code":"600522","stock_name":"中天科技"},{"stock_code":"600120","stock_name":"浙江东方"},{"stock_code":"600340","stock_name":"华夏幸福"},{"stock_code":"000555","stock_name":"神州信息"},{"stock_code":"002222","stock_name":"福晶科技"}]}
     * code : 0
     */

    private String message;
    private EventDetailData data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EventDetailData getData() {
        return data;
    }

    public void setData(EventDetailData data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class EventDetailData {
        /**
         * content : <p>【<span style="color:#FF0000">事件</span>】近日，在北京举办的&ldquo;阿里技术下午茶&rdquo;活动上，阿里集团首席通信科学家谢崇进博士介绍了阿里NASA计划-量子通信技术的发展规划。据透露，阿里巴巴除了持续深耕量子通信技术，还会通过阿里云，向全球企业输出量子通信的安全服务。</p><p>【<span style="color:#FF0000">点评</span>】3月，阿里云发布全球首个云上量子通信商用服务，由此打开了量子信息科学普惠发展之门；5月，中科院、阿里巴巴等机构研发的世界上第一台超越早期经典计算机的光量子计算机在中国诞生。按照马云此前宣布的&ldquo;NASA&rdquo;计划，阿里目前正在为20年后储备核心科技，以实现换道超车，服务全球20亿人，成为世界第五大经济体。目前量子通信主要运用于金融、国防、政务等领域，实际规模有限，短期内只有100-130亿元左右，但长期来看市场规模可达千亿。</p><p>量子通信可关注：三力士、中天科技、浙江东方、华夏幸福、神州信息、福晶科技等。</p><p><span style="color:#808080"><span style="font-size:12px">以上数据和观点由Go-Goal金融终端提供支持,仅供参考,风险自担.股市有风险,投资需谨慎.</span></span></p>
         * id : 5433
         * title : 阿里首次公开量子通信技术 相关概念股受益
         * theme_words : ["量子通信"]
         * source : 量子通信
         * type : 12
         * date : 2017-06-28 14:28:58
         * stocks : [{"stock_code":"002224","stock_name":"三力士"},{"stock_code":"600522","stock_name":"中天科技"},{"stock_code":"600120","stock_name":"浙江东方"},{"stock_code":"600340","stock_name":"华夏幸福"},{"stock_code":"000555","stock_name":"神州信息"},{"stock_code":"002222","stock_name":"福晶科技"}]
         */

        private String content;
        private int id;
        private String title;
        private String source;
        private int type;
        private String date;
        private List<String> theme_words;
        private List<Stock> stocks;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<String> getTheme_words() {
            return theme_words;
        }

        public void setTheme_words(List<String> theme_words) {
            this.theme_words = theme_words;
        }

        public List<Stock> getStocks() {
            return stocks;
        }

        public void setStocks(List<Stock> stocks) {
            this.stocks = stocks;
        }

    }
}
