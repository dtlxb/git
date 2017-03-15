package cn.gogoal.im.bean.market;

import java.util.List;

/**
 * author wangjd on 2017/3/10 0010.
 * Staff_id 1375
 * phone 18930640263
 */
public class StockMarketBanner {

    /**
     * message : 成功
     * data : [{"position":1,"id":9,"name":"头条提示","data":{"id":"5684","newstitle":"[新股日历]新泉股份等三新股3月7日申购指南","type":"newsdetail","newstype":"100","comment_sum":0,"praise_sum":0,"favor_sum":0,"share_sum":0},"clickable":1,"image":"http://ggimage.go-goal.cn/prod/ad/1458609150012.jpg!828x266","date":"2017-03-06 16:14:52","target_type":"webview","target_url":"http://appmis.go-goal.com:8093/news/info?id=5684"},{"position":2,"id":10,"name":"头条提示","data":{"id":"5101","newstitle":"[热股榜]   华录百纳：2016年业绩预告同比增长20%\u201450%","type":"newsdetail","newstype":"106","comment_sum":18,"praise_sum":0,"favor_sum":0,"share_sum":0},"clickable":1,"image":"http://ggimage.go-goal.cn/prod/ad/1458610561589.jpg!828x266","date":"2017-01-26 15:28:41","target_type":"webview","target_url":"http://appmis.go-goal.com:8093/recommendaction/info?id=5101"}]
     * code : 0
     */

    private String message;
    private int code;
    private List<DataBeanX> data;

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

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * position : 1
         * id : 9
         * name : 头条提示
         * data : {"id":"5684","newstitle":"[新股日历]新泉股份等三新股3月7日申购指南","type":"newsdetail","newstype":"100","comment_sum":0,"praise_sum":0,"favor_sum":0,"share_sum":0}
         * clickable : 1
         * image : http://ggimage.go-goal.cn/prod/ad/1458609150012.jpg!828x266
         * date : 2017-03-06 16:14:52
         * target_type : webview
         * target_url : http://appmis.go-goal.com:8093/news/info?id=5684
         */

        private int position;
        private int id;
        private String name;
        private DataBean data;
        private int clickable;
        private String image;
        private String date;
        private String target_type;
        private String target_url;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public int getClickable() {
            return clickable;
        }

        public void setClickable(int clickable) {
            this.clickable = clickable;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTarget_type() {
            return target_type;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }

        public String getTarget_url() {
            return target_url;
        }

        public void setTarget_url(String target_url) {
            this.target_url = target_url;
        }

        public static class DataBean {
            /**
             * id : 5684
             * newstitle : [新股日历]新泉股份等三新股3月7日申购指南
             * type : newsdetail
             * newstype : 100
             * comment_sum : 0
             * praise_sum : 0
             * favor_sum : 0
             * share_sum : 0
             */

            private String id;
            private String newstitle;
            private String type;
            private String newstype;
            private int comment_sum;
            private int praise_sum;
            private int favor_sum;
            private int share_sum;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNewstitle() {
                return newstitle;
            }

            public void setNewstitle(String newstitle) {
                this.newstitle = newstitle;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getNewstype() {
                return newstype;
            }

            public void setNewstype(String newstype) {
                this.newstype = newstype;
            }

            public int getComment_sum() {
                return comment_sum;
            }

            public void setComment_sum(int comment_sum) {
                this.comment_sum = comment_sum;
            }

            public int getPraise_sum() {
                return praise_sum;
            }

            public void setPraise_sum(int praise_sum) {
                this.praise_sum = praise_sum;
            }

            public int getFavor_sum() {
                return favor_sum;
            }

            public void setFavor_sum(int favor_sum) {
                this.favor_sum = favor_sum;
            }

            public int getShare_sum() {
                return share_sum;
            }

            public void setShare_sum(int share_sum) {
                this.share_sum = share_sum;
            }
        }
    }
}
