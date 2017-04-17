package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/4/17 0017.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class BannerBean {

    /**
     * code : 0
     * message : 成功
     * requestId : null
     * data : [{"ad_position":7,"p_name":"投研","p_order":0,"image":"http://hackfile.ufile.ucloud.cn/ggimages/ad/dcb79b40.png","target_url":"","isout":0,"name":"","tab_id":""}]
     * executeTime : 13
     */

    private int code;
    private String message;
    private Object requestId;
    private int executeTime;
    private List<Banner> data;

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

    public Object getRequestId() {
        return requestId;
    }

    public void setRequestId(Object requestId) {
        this.requestId = requestId;
    }

    public int getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(int executeTime) {
        this.executeTime = executeTime;
    }

    public List<Banner> getData() {
        return data;
    }

    public void setData(List<Banner> data) {
        this.data = data;
    }

    public static class Banner {
        /**
         * ad_position : 7
         * p_name : 投研
         * p_order : 0
         * image : http://hackfile.ufile.ucloud.cn/ggimages/ad/dcb79b40.png
         * target_url :
         * isout : 0
         * name :
         * tab_id :
         */

        private int ad_position;
        private String p_name;
        private int p_order;
        private String image;
        private String target_url;
        private int isout;
        private String name;
        private String tab_id;

        public int getAd_position() {
            return ad_position;
        }

        public void setAd_position(int ad_position) {
            this.ad_position = ad_position;
        }

        public String getP_name() {
            return p_name;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public int getP_order() {
            return p_order;
        }

        public void setP_order(int p_order) {
            this.p_order = p_order;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTarget_url() {
            return target_url;
        }

        public void setTarget_url(String target_url) {
            this.target_url = target_url;
        }

        public int getIsout() {
            return isout;
        }

        public void setIsout(int isout) {
            this.isout = isout;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTab_id() {
            return tab_id;
        }

        public void setTab_id(String tab_id) {
            this.tab_id = tab_id;
        }
    }
}
