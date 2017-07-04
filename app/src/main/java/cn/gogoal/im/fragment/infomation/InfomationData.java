package cn.gogoal.im.fragment.infomation;

import java.util.List;

/**
 * author wangjd on 2017/6/19 0019.
 * Staff_id 1375
 * phone 18930640263
 * description :要闻、朝阳会务等
 */
public class InfomationData {

    /**
     * message : 成功
     * data : [{"id":7055,"title":"\u201c6·18\u201d引爆新零售狂欢 折射消费升级三趋势","source":"证券时报","image":"http://ggimage.go-goal.cn/prod/news_headlines/1497834140954.jpg!828x266","type":1,"date":"2017-06-19 08:51:40"},{"id":7078,"title":"智能网联汽车分技术委员会获批 产业发展提速","source":"中国证券网","image":null,"date":"2017-06-19 16:36:15","type":1},{"id":7077,"title":"亚欧数字互联互通论坛今召开 聚焦数字经济发展","source":"中国证券网","image":null,"date":"2017-06-19 16:35:22","type":1},{"id":7076,"title":"2017机器人与人工智能大会召开在即","source":"中国证券网","image":null,"date":"2017-06-19 16:17:04","type":1},{"id":7075,"title":"三部门发文要求规范开展PPP项目资产证券化","source":"中国证券网","image":null,"date":"2017-06-19 16:06:54","type":1},{"id":7074,"title":"简并增值税税率7月1日将实施 传导机制扩大减税效应","source":"中国证券网","image":null,"date":"2017-06-19 16:03:24","type":1},{"id":7073,"title":"金融监管蓝皮书：金融科技监管步入风险专项整治阶段","source":"中国证券网","image":null,"date":"2017-06-19 15:34:14","type":1},{"id":7072,"title":"中国石油海外油气业务体制机制改革全面启动","source":"中国证券网","image":null,"date":"2017-06-19 13:24:34","type":1},{"id":7071,"title":"东航物流引战投 国家混改试点\u201c落子\u201d民航","source":"中国证券网","image":null,"date":"2017-06-19 13:23:02","type":1},{"id":7070,"title":"高盛清仓式减持 A股原始股成\u201c摇钱树\u201d","source":"上海证券报","image":null,"date":"2017-06-19 11:04:00","type":1},{"id":7069,"title":"统计局：5月份一二线城市房价平均涨幅继续回落","source":"中国证券网","image":null,"date":"2017-06-19 10:41:36","type":1},{"id":7068,"title":"腾讯电竞推五年计划 电竞产业望入黄金时代","source":"中国证券网","image":"http://ggimage.go-goal.cn/prod/news_headlines/1497841249679.jpg!198x144","date":"2017-06-19 09:01:23","type":1},{"id":7067,"title":"中国证券报：流动性跨季无忧偏紧难改","source":"中国证券报","image":null,"date":"2017-06-19 09:00:44","type":1},{"id":7066,"title":"人大研究报告预计今年GDP增速为6.7%","source":"经济参考报","image":"http://ggimage.go-goal.cn/prod/news_headlines/1497841272755.jpg!198x144","date":"2017-06-19 09:00:15","type":1},{"id":7065,"title":"经济参考报：延续经济复苏需把握政策平衡","source":"经济参考报","image":null,"date":"2017-06-19 08:59:40","type":1}]
     * code : 0
     */

    private String message;
    private int code;
    private List<Data> data;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        /**
         * id : 7055
         * title : “6·18”引爆新零售狂欢 折射消费升级三趋势
         * source : 证券时报
         * image : http://ggimage.go-goal.cn/prod/news_headlines/1497834140954.jpg!828x266
         * type : 1
         * date : 2017-06-19 08:51:40
         */

        private String id;
        private String title;
        private String source;
        private int type;
        private String date;
        private String summary;
        private String image;

        private String origin;

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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
    }
}
