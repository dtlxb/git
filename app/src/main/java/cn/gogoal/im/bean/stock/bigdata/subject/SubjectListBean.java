package cn.gogoal.im.bean.stock.bigdata.subject;

import java.util.List;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :主题选股
 */
public class SubjectListBean {

    /**
     * message : 成功
     * data : [{"id":224,"code":"BK0083","title":"下游应用多点开花，石墨烯将迎来产业化浪潮","insert_time":"2017-06-28 10:10:15","theme_name":"石墨烯","position_id":1,"tags":["导电","微电子"],"price_change_rate":-0.6928791504409777,"price_change":-6.939705557343628,"price":994.6354685443602},{"id":25,"code":"BK0162","title":"重资产周期来临","insert_time":"2017-06-28 10:10:07","theme_name":"有色金属","position_id":1,"tags":["贵金属","工业金属"],"price_change_rate":0.0109596405284194,"price_change":0.09549425699535762,"price":871.4220377483795},{"id":236,"code":"BK0095","title":"《网络安全法》将成为保障信息安全的新起点","insert_time":"2017-06-28 09:37:25","theme_name":"信息安全","position_id":1,"tags":["数据加密","安全存储"],"price_change_rate":0.1926515188417441,"price_change":1.4288806581556028,"price":743.1207535000062},{"id":200,"code":"BK0059","title":"密集事件催化","insert_time":"2017-06-28 09:37:06","theme_name":"量子通信","position_id":1,"tags":["量子力学","通信技术"],"price_change_rate":0.398072517928874,"price_change":3.8582029153413306,"price":973.0793225784421},{"id":241,"code":"BK0100","title":"《中国北斗卫星导航系统》白皮书发布","insert_time":"2017-06-28 09:36:56","theme_name":"北斗导航","position_id":1,"tags":["北斗卫星","导航"],"price_change_rate":-0.6946111305949739,"price_change":-6.435772084037435,"price":920.093015113028},{"id":202,"code":"BK0061","title":"LED芯片涨价，行业回暖进行时","insert_time":"2017-06-28 09:36:32","theme_name":"LED","position_id":1,"tags":["发光二极管","节能灯"],"price_change_rate":-0.5976586495097095,"price_change":-5.51124673792171,"price":916.6282960333361},{"id":269,"code":"BK0128","title":"为城市有机体赋予新生命","insert_time":"2017-06-28 09:36:15","theme_name":"智慧城市","position_id":1,"tags":["城市服务","通信技术"],"price_change_rate":-0.6045502383214091,"price_change":-5.472236670271028,"price":899.7026062783001},{"id":205,"code":"BK0064","title":"供需加速改善，旺季提前来临","insert_time":"2017-06-27 13:58:15","theme_name":"煤炭","position_id":1,"tags":["供给侧改革","业绩扭亏"],"price_change_rate":0.01947034061510148,"price_change":0.23150971964049744,"price":1189.2693607623444},{"id":21,"code":"BK0158","title":"进军智能制造，拥抱工业4.0","insert_time":"2017-06-27 09:38:44","theme_name":"智能制造","position_id":1,"tags":["中国制造2025","机器人"],"price_change_rate":-1.1924975390223458,"price_change":-9.494562692611138,"price":786.6968240330283},{"id":142,"code":"BK0001","title":"3D打印世界大会展现多行业应用前景","insert_time":"2017-06-27 09:38:34","theme_name":"3D打印","position_id":1,"tags":["3D打印机技术","3D打印房屋"],"price_change_rate":-1.3866397719387027,"price_change":-12.910840950988486,"price":918.1774786157763},{"id":6,"code":"BK0148","title":"激励、冲突与协调","insert_time":"2017-06-27 09:37:52","theme_name":"债转股","position_id":1,"tags":["不良资产","降杠杆"],"price_change_rate":1.052773394038358,"price_change":10.637492375837144,"price":1021.0631391555814},{"id":33,"code":"BK0170","title":"供需边际改善，海运回暖带动港口需求上升","insert_time":"2017-06-27 09:37:33","theme_name":"海运","position_id":1,"tags":["海运港口","BDI指数回暖"],"price_change_rate":-0.8435501489232423,"price_change":-8.156569619921378,"price":958.7770063308279},{"id":19,"code":"BK0156","title":"景气周期重启","insert_time":"2017-06-27 09:36:58","theme_name":"草甘膦","position_id":1,"tags":["农药农化","草甘膦"],"price_change_rate":-0.05018528493734522,"price_change":-0.4993579334052324,"price":994.5292326757052},{"id":186,"code":"BK0045","title":"雄安新区带来新机遇","insert_time":"2017-06-27 09:36:51","theme_name":"环保","position_id":1,"tags":["水污染治理","大气污染治理"],"price_change_rate":-0.9893458483857852,"price_change":-11.865973487448807,"price":1187.509705584668},{"id":20,"code":"BK0157","title":"稳增长中坚力量","insert_time":"2017-06-27 09:36:44","theme_name":"基建","position_id":1,"tags":["基建","一带一路"],"price_change_rate":-0.4249670532561126,"price_change":-4.936253421007636,"price":1156.625186033154},{"id":134,"code":"BK0175","theme_name":"高端制造","title":"高端制造创新担当","tags":["高端制造","中国制造"],"position_id":1,"insert_time":"2017-06-27 09:35:33","price_change_rate":-0.6662809418095541,"price_change":-6.660987470006979,"price":993.0655621017676},{"id":146,"code":"BK0005","title":"行业迎来爆发式增长","insert_time":"2017-06-26 14:14:57","theme_name":"OLED","position_id":1,"tags":["AMOLED","曲面屏幕","触摸显示屏","有机二极管"],"price_change_rate":-1.3556100392337533,"price_change":-12.42528731117696,"price":904.1574283347063},{"id":276,"code":"BK0135","title":"智能音箱颠覆人机交互体验，引爆智能家居市场","insert_time":"2017-06-26 14:14:21","theme_name":"智能家居","position_id":1,"tags":["智能家电","智能控制芯片"],"price_change_rate":-0.43490507649994015,"price_change":-4.331898492070221,"price":991.7241896393399},{"id":177,"code":"BK0036","title":"\u201c十三五\u201d高景气度确定性强","insert_time":"2017-06-26 14:14:17","theme_name":"高铁","position_id":1,"tags":["铁路设备","高速新线"],"price_change_rate":-0.6580179855192585,"price_change":-6.313516622280672,"price":953.1612638882425},{"id":254,"code":"BK0113","title":"新能源乘用车销量大增，验证三元大趋势","insert_time":"2017-06-26 14:12:38","theme_name":"新能源汽车","position_id":1,"tags":["三元电池","燃料电池"],"price_change_rate":-0.5164176580964075,"price_change":-3.9471127852830428,"price":760.3785688408428}]
     * code : 0
     */

    private String message;
    private int code;
    private List<SubjectData> data;

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

    public List<SubjectData> getData() {
        return data;
    }

    public void setData(List<SubjectData> data) {
        this.data = data;
    }

    public static class SubjectData {
        /**
         * id : 224
         * code : BK0083
         * title : 下游应用多点开花，石墨烯将迎来产业化浪潮
         * insert_time : 2017-06-28 10:10:15
         * theme_name : 石墨烯
         * position_id : 1
         * tags : ["导电","微电子"]
         * price_change_rate : -0.6928791504409777
         * price_change : -6.939705557343628
         * price : 994.6354685443602
         */

        private int id;
        private String code;
        private String title;
        private String insert_time;
        private String theme_name;
        private int position_id;
        private String price_change_rate;
        private String price_change;
        private String price;
        private List<String> tags;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getInsert_time() {
            return insert_time;
        }

        public void setInsert_time(String insert_time) {
            this.insert_time = insert_time;
        }

        public String getTheme_name() {
            return theme_name;
        }

        public void setTheme_name(String theme_name) {
            this.theme_name = theme_name;
        }

        public int getPosition_id() {
            return position_id;
        }

        public void setPosition_id(int position_id) {
            this.position_id = position_id;
        }

        public String getPrice_change_rate() {
            return price_change_rate;
        }

        public void setPrice_change_rate(String price_change_rate) {
            this.price_change_rate = price_change_rate;
        }

        public String getPrice_change() {
            return price_change;
        }

        public void setPrice_change(String price_change) {
            this.price_change = price_change;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}