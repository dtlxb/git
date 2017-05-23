package cn.gogoal.im.bean;

/**
 * Created by dave.
 * Date: 2017/5/22.
 * Desc: description
 */
public class LiveOnlinePersonData {

    /*{
        "account_name":"E00002638",
        "avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_C348B5570D407B7D.jpg",
        "city":"吉林省长春市",
        "duty":"开发工程师",
        "friend_id":348635,
        "nickname":"davesally"
    },*/

    private String account_name;
    private String avatar;
    private String city;
    private String duty;
    private String friend_id;
    private String nickname;

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
