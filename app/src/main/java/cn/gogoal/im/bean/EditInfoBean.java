package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/4/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * description :编辑页刷新的个人信息
 */
public class EditInfoBean {

    /**
     * message : 成功
     * data : {"phone":"","department":"","profession":"个人投资者","simple_avatar":"http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif","city":"上海市上海市","duty":"android开发","gender":"男","mind":"","login_type":0,"login_id":0,"account_id":357006,"nickname":"寒平洛一","code":0,"avatar":"http://info.china-yjy.com/Upload/Head/NoPhoto.gif","photo":"http://info.china-yjy.com/Upload/Photo/nophoto.gif","account_status":1,"investment_year":"20","account_name":"E00003645","email":"hanpingluoyi@163.com","organization_id":null,"paper_year":"20","organization_name":"上海朝阳永续信息技术股份有限公司","organization_address":"","is_tc_org":0,"mobile":"18930640263","full_name":"王结东","weibo":""}
     * code : 0
     */

    private String message;
    private EditInfoData data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EditInfoData getData() {
        return data;
    }

    public void setData(EditInfoData data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class EditInfoData {
        /**
         * phone :
         * department :
         * profession : 个人投资者
         * simple_avatar : http://www.go-goal.com/sample/ACC/ftx/forum/library/NoPhoto.gif
         * city : 上海市上海市
         * duty : android开发
         * gender : 男
         * mind :
         * login_type : 0
         * login_id : 0
         * account_id : 357006
         * nickname : 寒平洛一
         * code : 0
         * avatar : http://info.china-yjy.com/Upload/Head/NoPhoto.gif
         * photo : http://info.china-yjy.com/Upload/Photo/nophoto.gif
         * account_status : 1
         * investment_year : 20
         * account_name : E00003645
         * email : hanpingluoyi@163.com
         * organization_id : null
         * paper_year : 20
         * organization_name : 上海朝阳永续信息技术股份有限公司
         * organization_address :
         * is_tc_org : 0
         * mobile : 18930640263
         * full_name : 王结东
         * weibo :
         */

        private String phone;
        private String department;
        private String profession;
        private String simple_avatar;
        private String city;
        private String duty;
        private String gender;
        private String mind;
        private int login_type;
        private int login_id;
        private int account_id;
        private String nickname;
        private int code;
        private String avatar;
        private String photo;
        private int account_status;
        private String investment_year;
        private String account_name;
        private String email;
        private Object organization_id;
        private String paper_year;
        private String organization_name;
        private String organization_address;
        private int is_tc_org;
        private String mobile;
        private String full_name;
        private String weibo;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getSimple_avatar() {
            return simple_avatar;
        }

        public void setSimple_avatar(String simple_avatar) {
            this.simple_avatar = simple_avatar;
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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getMind() {
            return mind;
        }

        public void setMind(String mind) {
            this.mind = mind;
        }

        public int getLogin_type() {
            return login_type;
        }

        public void setLogin_type(int login_type) {
            this.login_type = login_type;
        }

        public int getLogin_id() {
            return login_id;
        }

        public void setLogin_id(int login_id) {
            this.login_id = login_id;
        }

        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
            this.account_id = account_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getAccount_status() {
            return account_status;
        }

        public void setAccount_status(int account_status) {
            this.account_status = account_status;
        }

        public String getInvestment_year() {
            return investment_year;
        }

        public void setInvestment_year(String investment_year) {
            this.investment_year = investment_year;
        }

        public String getAccount_name() {
            return account_name;
        }

        public void setAccount_name(String account_name) {
            this.account_name = account_name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getOrganization_id() {
            return organization_id;
        }

        public void setOrganization_id(Object organization_id) {
            this.organization_id = organization_id;
        }

        public String getPaper_year() {
            return paper_year;
        }

        public void setPaper_year(String paper_year) {
            this.paper_year = paper_year;
        }

        public String getOrganization_name() {
            return organization_name;
        }

        public void setOrganization_name(String organization_name) {
            this.organization_name = organization_name;
        }

        public String getOrganization_address() {
            return organization_address;
        }

        public void setOrganization_address(String organization_address) {
            this.organization_address = organization_address;
        }

        public int getIs_tc_org() {
            return is_tc_org;
        }

        public void setIs_tc_org(int is_tc_org) {
            this.is_tc_org = is_tc_org;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getWeibo() {
            return weibo;
        }

        public void setWeibo(String weibo) {
            this.weibo = weibo;
        }
    }
}
