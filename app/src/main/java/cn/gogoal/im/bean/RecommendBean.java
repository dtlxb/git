package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/3/31 0031.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class RecommendBean {

    /**
     * code : 0
     * message : 成功
     * requestId : null
     * data : [{"c":"348638","conv_id":"58ddadb65c497d0064aa2908","name":"寒平洛一、E00002639、jasminejasmine......","attr":{"chat_type":1002},"m":[{"nickname":"寒平洛一","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif","account_id":357006,"account_name":"E00003645"},{"nickname":"E00002639","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif","account_id":348638,"account_name":"E00002639"},{"nickname":"jasminejasmine","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NOFTX.gif","account_id":96338,"account_name":"E039065"}],"is_in":true},{"c":"348638","conv_id":"58ddafef128fe1005dcf744b","name":"寒平洛一、E00002639、jasminejasmine......","attr":{"chat_type":1002},"m":[{"nickname":"寒平洛一","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif","account_id":357006,"account_name":"E00003645"},{"nickname":"E00002639","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif","account_id":348638,"account_name":"E00002639"},{"nickname":"jasminejasmine","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NOFTX.gif","account_id":96338,"account_name":"E039065"}],"is_in":true},{"c":"348638","conv_id":"58ddb0ab1b69e60062af3f2e","name":"寒平洛一、E00002639、davesally......","attr":{"chat_type":1002},"m":[{"nickname":"寒平洛一","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif","account_id":357006,"account_name":"E00003645"},{"nickname":"E00002639","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif","account_id":348638,"account_name":"E00002639"},{"nickname":"davesally","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NOFTX.GIF","account_id":348635,"account_name":"E00002638"},{"nickname":"jasminejasmine","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NOFTX.gif","account_id":96338,"account_name":"E039065"}],"is_in":true}]
     * executeTime : 32
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
         * c : 348638
         * conv_id : 58ddadb65c497d0064aa2908
         * name : 寒平洛一、E00002639、jasminejasmine......
         * attr : {"chat_type":1002}
         * m : [{"nickname":"寒平洛一","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif","account_id":357006,"account_name":"E00003645"},{"nickname":"E00002639","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif","account_id":348638,"account_name":"E00002639"},{"nickname":"jasminejasmine","avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NOFTX.gif","account_id":96338,"account_name":"E039065"}]
         * is_in : true
         */

        private String c;
        private String conv_id;
        private String name;
        private AttrBean attr;
        private boolean is_in;
        private List<MBean> m;

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getConv_id() {
            return conv_id;
        }

        public void setConv_id(String conv_id) {
            this.conv_id = conv_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public AttrBean getAttr() {
            return attr;
        }

        public void setAttr(AttrBean attr) {
            this.attr = attr;
        }

        public boolean isIs_in() {
            return is_in;
        }

        public void setIs_in(boolean is_in) {
            this.is_in = is_in;
        }

        public List<MBean> getM() {
            return m;
        }

        public void setM(List<MBean> m) {
            this.m = m;
        }

        public static class AttrBean {
            /**
             * chat_type : 1002
             */
            private String intro;

            private String notice;

            private int chat_type;

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getNotice() {
                return notice;
            }

            public void setNotice(String notice) {
                this.notice = notice;
            }

            public int getChat_type() {
                return chat_type;
            }

            public void setChat_type(int chat_type) {
                this.chat_type = chat_type;
            }
        }

        public static class MBean {
            /**
             * nickname : 寒平洛一
             * avatar : http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif
             * account_id : 357006
             * account_name : E00003645
             */

            private String nickname;
            private String avatar;
            private int account_id;
            private String account_name;

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
        }
    }
}
