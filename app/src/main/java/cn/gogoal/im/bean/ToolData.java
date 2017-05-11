package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/5/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :投研小工具data
 */
public class ToolData {
    /**
     * datas : [{"_id":1,"title_id":1,"desc":"研报","iconUrl":"http://file.go-goal.cn/ggimages/aicon/2cb16692.png","introduce":"测试数据","url":"www.baidu.com","position":1,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-04-28 12:03:59","content":"测试数据","code":"G3_001","funintroduce":"测试功能数据","title":"模块","isShow":1},{"_id":8,"title_id":1,"desc":"数据","iconUrl":"http://file.go-goal.cn/ggimages/aicon/d73b7d1b.jpg","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":2,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 11:54:44","code":"G3_002","funintroduce":"测试功能数据","title":"模块","isShow":1},{"_id":10,"title_id":1,"desc":"诊股","iconUrl":"http://file.go-goal.cn/ggimages/aicon/9ac0f08d.jpg","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":3,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 13:11:11","code":"G3_003","funintroduce":"测试功能数据","title":"模块","isShow":1},{"_id":12,"title_id":1,"desc":"自选","iconUrl":"http://file.go-goal.cn/ggimages/aicon/d481b7f2.jpg","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":4,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 13:15:13","code":"G3_004","funintroduce":"测试功能数据","title":"模块","isShow":1},{"_id":9,"title_id":1,"desc":"个股","iconUrl":"http://file.go-goal.cn/ggimages/aicon/2834064c.jpg","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":5,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 12:01:41","code":"G3_005","funintroduce":"测试功能数据","title":"模块","isShow":1},{"_id":11,"title_id":1,"desc":"行情","iconUrl":"http://file.go-goal.cn/ggimages/aicon/64396903.jpg","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":6,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 13:12:49","code":"G3_006","funintroduce":"测试功能数据","title":"模块","isShow":1},{"_id":13,"title_id":1,"desc":"业绩","iconUrl":"http://file.go-goal.cn/ggimages/aicon/109bb942.jpg","introduce":"测试数据","content":"测试数据","code":"G3_007","url":"www.baidu.com","position":7,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 15:15:08","funintroduce":"测试功能数据","title":"模块","isShow":1},{"_id":7,"title_id":1,"desc":"主题","iconUrl":"http://file.go-goal.cn/ggimages/aicon/3c02549f.jpg","introduce":"测试数据","content":"测试数据","url":"www.baidu.com","position":8,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-05 11:53:50","code":"G3_008","funintroduce":"测试功能数据","title":"模块","isShow":1},{"_id":5,"title_id":1,"desc":"公告中心","iconUrl":"http://file.go-goal.cn/ggimages/aicon/ba1f4be8.jpg","introduce":"测试数据","url":"www.baidu.com","position":9,"showHotFlag":0,"type":10000,"isClick":0,"insert_time":"2017-05-02 13:51:30","content":"测试数据","code":"G3_010","funintroduce":"测试功能数据","title":"模块","isShow":1}]
     * title : 模块
     */

    private String title;
    private List<Tool> datas;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Tool> getDatas() {
        return datas;
    }

    public void setDatas(List<Tool> datas) {
        this.datas = datas;
    }

    public static class Tool {
        /**
         * _id : 1
         * title_id : 1
         * desc : 研报
         * iconUrl : http://file.go-goal.cn/ggimages/aicon/2cb16692.png
         * introduce : 测试数据
         * url : www.baidu.com
         * position : 1
         * showHotFlag : 0
         * type : 10000
         * isClick : 0
         * insert_time : 2017-04-28 12:03:59
         * content : 测试数据
         * code : G3_001
         * funintroduce : 测试功能数据
         * title : 模块
         * isShow : 1
         */

        private int _id;
        private int title_id;
        private String desc;
        private String iconUrl;
        private String introduce;
        private String url;
        private int position;
        private int showHotFlag;
        private int type;
        private int isClick;
        private String insert_time;
        private String content;
        private String code;
        private String funintroduce;
        private String title;
        private int isShow;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public int getTitle_id() {
            return title_id;
        }

        public void setTitle_id(int title_id) {
            this.title_id = title_id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getShowHotFlag() {
            return showHotFlag;
        }

        public void setShowHotFlag(int showHotFlag) {
            this.showHotFlag = showHotFlag;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIsClick() {
            return isClick;
        }

        public void setIsClick(int isClick) {
            this.isClick = isClick;
        }

        public String getInsert_time() {
            return insert_time;
        }

        public void setInsert_time(String insert_time) {
            this.insert_time = insert_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getFunintroduce() {
            return funintroduce;
        }

        public void setFunintroduce(String funintroduce) {
            this.funintroduce = funintroduce;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }
    }
}
