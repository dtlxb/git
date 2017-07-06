package cn.gogoal.im.bean;

import android.graphics.drawable.Drawable;

/**
 * author wangjd on 2017/5/24 0024.
 * Staff_id 1375
 * phone 18930640263
 * description :通讯录匹配单个结果实体
 */
public class PhoneContactData {

    private String name;
    private boolean in_use;
    private String mobile;
    private FriendInfoBean friend_info;
    private boolean is_friend;
    private AccountInfoBean account_info;

    //补充字段
    private Drawable phoneAvatar;//联系人头像
    //手机联系人数据库中Id
    private long contactid;

    public boolean is_friend() {
        return is_friend;
    }

    public void setIs_friend(boolean is_friend) {
        this.is_friend = is_friend;
    }

    public Drawable getPhoneAvatar() {
        return phoneAvatar;
    }

    public void setPhoneAvatar(Drawable phoneAvatar) {
        this.phoneAvatar = phoneAvatar;
    }

    public long getContactid() {
        return contactid;
    }

    public void setContactid(long contactid) {
        this.contactid = contactid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIn_use() {
        return in_use;
    }

    public void setIn_use(boolean in_use) {
        this.in_use = in_use;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public FriendInfoBean getFriend_info() {
        return friend_info;
    }

    public void setFriend_info(FriendInfoBean friend_info) {
        this.friend_info = friend_info;
    }

    public AccountInfoBean getAccount_info() {
        return account_info;
    }

    public void setAccount_info(AccountInfoBean account_info) {
        this.account_info = account_info;
    }

    public static class FriendInfoBean {
        /**
         * conv_id : 591a99fb2f301e00588bcfac
         * friend_id : 393028
         * group : null
         * remark : null
         * nickname : 杨春雨
         * avatar : http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg
         * duty : java开发工程师
         */

        private String conv_id;
        private String friend_id;
        private Object group;
        private Object remark;
        private String nickname;
        private String avatar;
        private String duty;

        public String getConv_id() {
            return conv_id;
        }

        public void setConv_id(String conv_id) {
            this.conv_id = conv_id;
        }

        public String getFriend_id() {
            return friend_id;
        }

        public void setFriend_id(String friend_id) {
            this.friend_id = friend_id;
        }

        public Object getGroup() {
            return group;
        }

        public void setGroup(Object group) {
            this.group = group;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }
    }

    public static class AccountInfoBean {
        /**
         * account_id : 393028
         * account_name : E00018282
         * avatar : http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg
         * city : 湖北省武汉市
         * duty : java开发工程师
         * mobile : 18796014744
         * nickname : 杨春雨
         */

        private int account_id;
        private String account_name;
        private String avatar;
        private String city;
        private String duty;
        private String mobile;
        private String nickname;

        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
            this.account_id = account_id;
        }

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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
    /**
     * name : 安安
     * in_use : false
     * mobile : 13333333333
     * friend_info : {"conv_id":"591a99fb2f301e00588bcfac","friend_id":393028,"group":null,"remark":null,"nickname":"杨春雨","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg","duty":"java开发工程师"}
     * is_friend : true
     * account_info : {"account_id":393028,"account_name":"E00018282","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg","city":"湖北省武汉市","duty":"java开发工程师","mobile":"18796014744","nickname":"杨春雨"}
     *//*

    private String name;
    private boolean in_use;
    private String mobile;
    private FriendInfoBean friend_info;
    private boolean is_friend;
    private AccountInfoBean account_info;

    //补充字段
    private Drawable phoneAvatar;//联系人头像
    //手机联系人数据库中Id
    private long contactid;

    public boolean is_friend() {
        return is_friend;
    }

    public Drawable getPhoneAvatar() {
        return phoneAvatar;
    }

    public void setPhoneAvatar(Drawable phoneAvatar) {
        this.phoneAvatar = phoneAvatar;
    }

    public long getContactid() {
        return contactid;
    }

    public void setContactid(long contactid) {
        this.contactid = contactid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIn_use() {
        return in_use;
    }

    public void setIn_use(boolean in_use) {
        this.in_use = in_use;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public FriendInfoBean getFriend_info() {
        return friend_info;
    }

    public void setFriend_info(FriendInfoBean friend_info) {
        this.friend_info = friend_info;
    }

    public boolean isIs_friend() {
        return is_friend;
    }

    public void setIs_friend(boolean is_friend) {
        this.is_friend = is_friend;
    }

    public AccountInfoBean getAccount_info() {
        return account_info;
    }

    public void setAccount_info(AccountInfoBean account_info) {
        this.account_info = account_info;
    }

    public static class FriendInfoBean {
        *//**
     * conv_id : 591a99fb2f301e00588bcfac
     * friend_id : 393028
     * group : null
     * remark : null
     * nickname : 杨春雨
     * avatar : http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg
     * duty : java开发工程师
     *//*

        private String conv_id;
        private int friend_id;
        private Object group;
        private Object remark;
        private String nickname;
        private String avatar;
        private String duty;

        public String getConv_id() {
            return conv_id;
        }

        public void setConv_id(String conv_id) {
            this.conv_id = conv_id;
        }

        public int getFriend_id() {
            return friend_id;
        }

        public void setFriend_id(int friend_id) {
            this.friend_id = friend_id;
        }

        public Object getGroup() {
            return group;
        }

        public void setGroup(Object group) {
            this.group = group;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }
    }

    public static class AccountInfoBean {
        *//**
     * account_id : 393028
     * account_name : E00018282
     * avatar : http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg
     * city : 湖北省武汉市
     * duty : java开发工程师
     * mobile : 18796014744
     * nickname : 杨春雨
     *//*

        private int account_id;
        private String account_name;
        private String avatar;
        private String city;
        private String duty;
        private String mobile;
        private String nickname;

        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
            this.account_id = account_id;
        }

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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }*/

}