package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/5/17 0017.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class Bam {

    /**
     * message : 成功
     * data : [{"ad_position":3,"image":"http://file.go-goal.cn/ggimages/ad/f2a94b0c.png","name":"","p_name":"投研","p_order":0,"tab_id":"","target_url":"http://ggmobile.sandbox.go-goal.cn/#/help/theme","type":10000}]
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
         * ad_position : 3
         * image : http://file.go-goal.cn/ggimages/ad/f2a94b0c.png
         * name :
         * p_name : 投研
         * p_order : 0
         * tab_id :
         * target_url : http://ggmobile.sandbox.go-goal.cn/#/help/theme
         * type : 10000
         */

        private int ad_position;
        private String image;
        private String name;
        private String p_name;
        private int p_order;
        private String tab_id;
        private String target_url;
        private int type;

        public int getAd_position() {
            return ad_position;
        }

        public void setAd_position(int ad_position) {
            this.ad_position = ad_position;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getTab_id() {
            return tab_id;
        }

        public void setTab_id(String tab_id) {
            this.tab_id = tab_id;
        }

        public String getTarget_url() {
            return target_url;
        }

        public void setTarget_url(String target_url) {
            this.target_url = target_url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
