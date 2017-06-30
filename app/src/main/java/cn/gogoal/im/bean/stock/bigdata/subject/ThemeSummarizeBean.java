package cn.gogoal.im.bean.stock.bigdata.subject;

/**
 * author wangjd on 2017/6/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ThemeSummarizeBean {
    /**
     * img_url : http://ggimage.go-goal.cn/prod/theme/1478219626152.png
     * describe : 即&ldquo;工业4.0&rdquo;，是以智能制造为主导的第四次工业革命，或革命性的生产方法。包含了由集中式控制向分散式增强型控制的基本模式转变，目标是建立一个高度灵活的个性化和数字化的产品与服务的生产模式。在这种模式中，传统的行业界限将消失，并会产生各种新的活动领域和合作形式。该模式旨在通过充分利用信息通讯技术和网络空间虚拟系统&mdash;信息物理系统（Cyber-Physical System)相结合的手段，将制造业向智能化转型。该主题主要涉及：机器人、工业自动化、3D打印、智能机床、物联网等相关产业链个股。
     */

    private String img_url;
    private String describe;

    private String phone_image_url;

    public String getImg_url() {
        return img_url;
    }

    public String getPhone_image_url() {
        return phone_image_url;
    }

    public void setPhone_image_url(String phone_image_url) {
        this.phone_image_url = phone_image_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
