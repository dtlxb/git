package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/4/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class TouYan {

    /**
     * code : 0
     * message : 成功
     * requestId : null
     * data : [{"datas":[{"desc":"数据一分钟","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/2dcc4122.png","url":"/data","position":1,"isShow":0,"showHotFlag":0,"type":10000},{"desc":"文字一分钟","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/4581c95d.png","url":"/text","position":2,"isShow":0,"showHotFlag":0,"type":10000},{"desc":"图形一分钟","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/5c4cd54a.png","url":"www.sina.com","position":3,"isShow":0,"showHotFlag":1,"type":10000},{"desc":"同业比较","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/7385a823.png","url":"/industry","position":4,"isShow":0,"showHotFlag":1,"type":10000},{"desc":"历史季度业绩","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/f7c8d43c.png","url":"www.sina.com","position":5,"isShow":0,"showHotFlag":0,"type":10001},{"desc":"投资大事记","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/3db52632.png","url":"www.sina.com","position":6,"isShow":0,"showHotFlag":0,"type":10002},{"desc":"股票专家","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/9cc6d313.png","url":"www.sina.com","position":7,"isShow":0,"showHotFlag":0,"type":10000}],"title":"诊股工具"},{"datas":[{"desc":"好公司","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/8dfff119.png","url":"www.baidu.com","position":1,"isShow":0,"showHotFlag":0,"type":10001},{"desc":"希望之星","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/777ca15d.png","url":"www.sina.com","position":2,"isShow":0,"showHotFlag":0,"type":10000},{"desc":"寻宝组合","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/05150cd2.png","url":"www.sina.com","position":3,"isShow":0,"showHotFlag":0,"type":10002},{"desc":"朝阳88","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/c3d5fd09.png","url":"www.baidu.com","position":4,"isShow":0,"showHotFlag":1,"type":10000},{"desc":"GO-GOAL直播","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/52bdbf68.png","url":"/live/list","position":5,"isShow":0,"showHotFlag":0,"type":10000}],"title":"朝阳组合"},{"datas":[{"desc":"研报","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/c89e7b13.png","url":"/report","position":1,"isShow":0,"showHotFlag":0,"type":10000},{"desc":"研网","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/fa53dd5c.png","url":"/research","position":2,"isShow":0,"showHotFlag":1,"type":10000},{"desc":"资讯","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/fb97708d.png","url":"www.sina.com","position":3,"isShow":0,"showHotFlag":0,"type":10000},{"desc":"行情","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/d28ed4a4.png","url":"www.sina.com","position":4,"isShow":0,"showHotFlag":1,"type":10000}],"title":"市场资讯"},{"datas":[{"desc":"业绩","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/cb6fc869.png","url":"www.baidu.com","position":1,"isShow":0,"showHotFlag":0,"type":10000},{"desc":"事件","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/a1cefb20.png","url":"www.sina.com","position":2,"isShow":0,"showHotFlag":1,"type":10002},{"desc":"主题","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/e624a8fb.png","url":"www.baidu.com","position":3,"isShow":0,"showHotFlag":0,"type":10001}],"title":"朝阳线索"}]
     * executeTime : 38
     */

    private int code;
    private String message;
    private String requestId;
    private int executeTime;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(int executeTime) {
        this.executeTime = executeTime;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * datas : [{"desc":"数据一分钟","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/2dcc4122.png","url":"/data","position":1,"isShow":0,"showHotFlag":0,"type":10000},{"desc":"文字一分钟","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/4581c95d.png","url":"/text","position":2,"isShow":0,"showHotFlag":0,"type":10000},{"desc":"图形一分钟","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/5c4cd54a.png","url":"www.sina.com","position":3,"isShow":0,"showHotFlag":1,"type":10000},{"desc":"同业比较","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/7385a823.png","url":"/industry","position":4,"isShow":0,"showHotFlag":1,"type":10000},{"desc":"历史季度业绩","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/f7c8d43c.png","url":"www.sina.com","position":5,"isShow":0,"showHotFlag":0,"type":10001},{"desc":"投资大事记","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/3db52632.png","url":"www.sina.com","position":6,"isShow":0,"showHotFlag":0,"type":10002},{"desc":"股票专家","iconUrl":"http://hackfile.ufile.ucloud.cn/ggimages/aicon/9cc6d313.png","url":"www.sina.com","position":7,"isShow":0,"showHotFlag":0,"type":10000}]
         * title : 诊股工具
         */

        private String title;
        private List<Item> datas;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Item> getDatas() {
            return datas;
        }

        public void setDatas(List<Item> datas) {
            this.datas = datas;
        }

        public static class Item {
            /**
             * desc : 数据一分钟
             * iconUrl : http://hackfile.ufile.ucloud.cn/ggimages/aicon/2dcc4122.png
             * url : /data
             * position : 1
             * isShow : 0
             * showHotFlag : 0
             * type : 10000
             */

            private String desc;
            private String iconUrl;
            private String url;
            private int position;
            private int isShow;
            private int showHotFlag;
            private int type;

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

            public int getIsShow() {
                return isShow;
            }

            public void setIsShow(int isShow) {
                this.isShow = isShow;
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
        }
    }
}
