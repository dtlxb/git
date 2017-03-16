package cn.gogoal.im.bean;

/**
 * Created by dave.
 * Date: 2017/2/24.
 * Desc: description
 */
public class RelaterVideoData {

    /*{
        "programme_id":15,
            "chat_id":15,
            "anchor_id":9,
            "video_name":"fdsfdsfs",
            "type":0,
            "video_img_url":"http://hackfile.ufile.ucloud.cn
            "video_time":"01:10:00",
            "play_base":0,
            "auth":1,
            "phone":"400-1818959",
            "programme_name":"了解朝阳永续",
            "group_name":"直播室1",
            "app_name":"1",
            "anchor_name":"主播水母",
            "face_url":"http://hackfile.ufile.ucloud.cn/ggimages/ad/3b9eb02a.jpg",
            "video_id":"db7ab381-c392-481c-9040-b2e37b0774e8"
    },*/

    private String programme_id;
    private String chat_id;
    private String anchor_id;
    private String video_name;
    private int type;
    private String video_img_url;
    private String video_time;
    private String play_base;
    private String auth;
    private String phone;
    private String programme_name;
    private String group_name;
    private String app_name;
    private String anchor_name;
    private String face_url;
    private String video_id;

    public String getProgramme_id() {
        return programme_id;
    }

    public void setProgramme_id(String programme_id) {
        this.programme_id = programme_id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getAnchor_id() {
        return anchor_id;
    }

    public void setAnchor_id(String anchor_id) {
        this.anchor_id = anchor_id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVideo_img_url() {
        return video_img_url;
    }

    public void setVideo_img_url(String video_img_url) {
        this.video_img_url = video_img_url;
    }

    public String getVideo_time() {
        return video_time;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }

    public String getPlay_base() {
        return play_base;
    }

    public void setPlay_base(String play_base) {
        this.play_base = play_base;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProgramme_name() {
        return programme_name;
    }

    public void setProgramme_name(String programme_name) {
        this.programme_name = programme_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getAnchor_name() {
        return anchor_name;
    }

    public void setAnchor_name(String anchor_name) {
        this.anchor_name = anchor_name;
    }

    public String getFace_url() {
        return face_url;
    }

    public void setFace_url(String face_url) {
        this.face_url = face_url;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
