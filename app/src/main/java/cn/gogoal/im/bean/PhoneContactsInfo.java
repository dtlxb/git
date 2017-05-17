package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/5/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :匹配结果 返回
 */
public class PhoneContactsInfo {

    /**
     * message : 成功
     * data : [{"name":"安安","in_use":false,"mobile":"13333333333"},{"name":"格格","in_use":false,"mobile":"13564646464"},{"name":"格格","in_use":false,"mobile":"13694646464"},{"name":"123","in_use":false,"mobile":"13821546089"},{"name":"菲菲","in_use":false,"mobile":"14725839564"},{"name":"恩恩","in_use":false,"mobile":"15555555555"},{"name":"185 1685 3695","in_use":false,"mobile":"18516853695"},{"friend_info":{"conv_id":"591a99fb2f301e00588bcfac","friend_id":393028,"group":null,"remark":null,"nickname":"杨春雨","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg","duty":"java开发工程师"},"is_friend":true,"name":"大黄","in_use":true,"account_info":{"account_id":393028,"account_name":"E00018282","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg","city":"湖北省武汉市","duty":"java开发工程师","mobile":"18796014744","nickname":"杨春雨"},"mobile":"18796014744"},{"name":"舟舟","in_use":false,"mobile":"18888888888"},{"is_friend":false,"name":"王尼玛","in_use":true,"account_info":{"account_id":357006,"account_name":"E00003645","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_C317F15BB2B3AA91.jpg","city":"","duty":"android开发","mobile":"18930640263","nickname":"隔壁小王"},"mobile":"18930640263"}]
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
         * name : 安安
         * in_use : false
         * mobile : 13333333333
         * friend_info : {"conv_id":"591a99fb2f301e00588bcfac","friend_id":393028,"group":null,"remark":null,"nickname":"杨春雨","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg","duty":"java开发工程师"}
         * is_friend : true
         * account_info : {"account_id":393028,"account_name":"E00018282","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/ucloud_393028_1494820268748.jpg","city":"湖北省武汉市","duty":"java开发工程师","mobile":"18796014744","nickname":"杨春雨"}
         */

        private String name;
        private boolean in_use;
        private String mobile;
        private FriendInfoBean friend_info;
        private boolean is_friend;
        private AccountInfoBean account_info;

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
    }
}
