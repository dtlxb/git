package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/5/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :投资顾问
 */
public class Advisers {

//    "_id":10386,
//    "ass_saler":124442,   //副销售id
//    "id":10386,
//    "saler_name":"陈XX", //主销售名称
//    "saler":34125,               //主销售id
//    "user_name":"李XX",  //用户名称
//    "ass_saler_mobile":"137xxxx8871", //副销售电话
//    "user_id":108784,        //用户id
//    "ass_saler_name":"张迎梅",  //副销售名称
//    "saler_mobile":"137xxxx8871",  //主销售名称
//    "saler_photo":"http://118.26.238.118:10001/Upload/Photo/null",   //主销售头像
//    "ass_saler_photo":"http://118.26.238.118:10001/Upload/Photo/null"  //副销售头像

    private String ass_saler_mobile;
    private String ass_saler_photo;
    private int ass_saler_user_id;
    private String saler_mobile;
    private String saler_photo;
    private int saler_user_id;
    private String saler_name;
    private String ass_saler_name;

    public String getAss_saler_mobile() {
        return ass_saler_mobile;
    }

    public void setAss_saler_mobile(String ass_saler_mobile) {
        this.ass_saler_mobile = ass_saler_mobile;
    }

    public String getAss_saler_photo() {
        return ass_saler_photo;
    }

    public void setAss_saler_photo(String ass_saler_photo) {
        this.ass_saler_photo = ass_saler_photo;
    }

    public int getAss_saler_user_id() {
        return ass_saler_user_id;
    }

    public void setAss_saler_user_id(int ass_saler_user_id) {
        this.ass_saler_user_id = ass_saler_user_id;
    }

    public String getSaler_mobile() {
        return saler_mobile;
    }

    public void setSaler_mobile(String saler_mobile) {
        this.saler_mobile = saler_mobile;
    }

    public String getSaler_photo() {
        return saler_photo;
    }

    public void setSaler_photo(String saler_photo) {
        this.saler_photo = saler_photo;
    }

    public int getSaler_user_id() {
        return saler_user_id;
    }

    public void setSaler_user_id(int saler_user_id) {
        this.saler_user_id = saler_user_id;
    }

    public String getSaler_name() {
        return saler_name;
    }

    public void setSaler_name(String saler_name) {
        this.saler_name = saler_name;
    }

    public String getAss_saler_name() {
        return ass_saler_name;
    }

    public void setAss_saler_name(String ass_saler_name) {
        this.ass_saler_name = ass_saler_name;
    }
}
