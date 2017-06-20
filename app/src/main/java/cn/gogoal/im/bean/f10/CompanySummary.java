package cn.gogoal.im.bean.f10;

/**
 * author wangjd on 2017/6/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :公司概况
 */
public class CompanySummary {

    /**
     * message : 成功
     * data : {"basic_data":{"company_name":"成都佳发安泰科技股份有限公司","company_enname":"Chengdu Jafaantai Technology Co., Ltd.","name_before":null,"a_stock":"300559","a_unit":"佳发安泰","b_stock":null,"b_unit":null,"h_stock":null,"h_unit":null,"security_type":"深交所创业板A股","industry_owned":"软件和信息技术服务业","general_manager":"寇健","legal_represent":"袁斌","secretaries":"文晶","chairman":"袁斌","security_represent":"毛涓涓","independent_direct":"李勃,廖中新,尹治本","contact_phone":"028-65293708","email":"cdjiafaantai@163.com","fax":"028-85925609","company_website":"ww.jf-r.com","office_addr":"四川省成都市武侯区武兴五路433号（武侯新城管委会内）","regist_addr":"四川省成都市武侯区武兴五路433号（武侯新城管委会内）","region":"四川","post_code":"610046","regist_capital":"7180.0000","regist_regist":"915101077436163833","employees":316,"management_num":13,"law_office":"北京市金杜律师事务所","account_office":"大信会计师事务所(特殊普通合伙)","company_summary":"2002年10月25日，佳发有限（筹）召开股东会，全体股东一致通过公司章程。根据公司章程，佳发有限（筹）注册资本为50万元，其中寇勤、陈大强、凌军分别以货币资金出资28万元、11万元、11万元。2002年10月29日，四川中宇会计师事务所出具了《验资报告》（川中宇验字[2002]第10-28号）对该次出资进行了验证。根据该验资报告，股东出资已到位。2002年10月31日，成都市工商行政管理局向佳发有限核发了注册号为5101072007002的《企业法人营业执照》。2012年9月26日，佳发有限召开股东会，决定以经大信所审计的截至2012年5月31日的净资产75,248,862.73元人民币为基础，按照1:0.7150的比例折成股本5,380万股，每股面值1元，其余21,448,862.73元计入资本公积，佳发有限整体变更为股份有限公司。2012年10月15日，大信所对佳发有限整体变更为股份有限公司的注册资本实收情况进行了验证，并出具了大信验字[2012]第3-0027号《验资报告》。2012年11月27日，公司在成都市工商行政管理局完成工商变更登记手续，取得了注册号为510107000124298的《企业法人营业执照》。","business_scope":"销售经国家密码管理局审批并通过指定检测机构产品质量检测的商用密码产品（凭许可证有效期经营至2019年2月22日）；电子计算机硬件研制、开发、生产、销售及其应用技术服务，电子计算机软件研发、销售及技术服务，网络设备的研制、开发、生产、销售，安防设备的研制、开发、生产、销售，电子产品的销售；公共安全防范设施系统设计、安装、维修；智能建筑系统集成。（依法须经批准的项目，经相关部门批准后方可开展经营活动）。"},"issue_data":{"setup_date":"2002-10-31 00:00:00","list_date":"2016-11-01 00:00:00","ipo_rate":22.99,"online_release_date":"2016-10-19 00:00:00","release_type":"上网定价发行","per_value":1,"circulation":1800,"per_stock_price":17.56,"circulation_fee":3522.13,"total_market_value":31608,"fund_capital_net":28085.87,"fDay_open_price":23.18,"fDay_close_price":25.29,"fDay_trunover_rate":0.02,"fDay_ceiling_price":25.29,"success_rate":null,"success_rate_pricing":0.02}}
     * code : 0
     */

    private String message;
    private CompanyData data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CompanyData getData() {
        return data;
    }

    public void setData(CompanyData data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class CompanyData {
        /**
         * basic_data : {"company_name":"成都佳发安泰科技股份有限公司","company_enname":"Chengdu Jafaantai Technology Co., Ltd.","name_before":null,"a_stock":"300559","a_unit":"佳发安泰","b_stock":null,"b_unit":null,"h_stock":null,"h_unit":null,"security_type":"深交所创业板A股","industry_owned":"软件和信息技术服务业","general_manager":"寇健","legal_represent":"袁斌","secretaries":"文晶","chairman":"袁斌","security_represent":"毛涓涓","independent_direct":"李勃,廖中新,尹治本","contact_phone":"028-65293708","email":"cdjiafaantai@163.com","fax":"028-85925609","company_website":"ww.jf-r.com","office_addr":"四川省成都市武侯区武兴五路433号（武侯新城管委会内）","regist_addr":"四川省成都市武侯区武兴五路433号（武侯新城管委会内）","region":"四川","post_code":"610046","regist_capital":"7180.0000","regist_regist":"915101077436163833","employees":316,"management_num":13,"law_office":"北京市金杜律师事务所","account_office":"大信会计师事务所(特殊普通合伙)","company_summary":"2002年10月25日，佳发有限（筹）召开股东会，全体股东一致通过公司章程。根据公司章程，佳发有限（筹）注册资本为50万元，其中寇勤、陈大强、凌军分别以货币资金出资28万元、11万元、11万元。2002年10月29日，四川中宇会计师事务所出具了《验资报告》（川中宇验字[2002]第10-28号）对该次出资进行了验证。根据该验资报告，股东出资已到位。2002年10月31日，成都市工商行政管理局向佳发有限核发了注册号为5101072007002的《企业法人营业执照》。2012年9月26日，佳发有限召开股东会，决定以经大信所审计的截至2012年5月31日的净资产75,248,862.73元人民币为基础，按照1:0.7150的比例折成股本5,380万股，每股面值1元，其余21,448,862.73元计入资本公积，佳发有限整体变更为股份有限公司。2012年10月15日，大信所对佳发有限整体变更为股份有限公司的注册资本实收情况进行了验证，并出具了大信验字[2012]第3-0027号《验资报告》。2012年11月27日，公司在成都市工商行政管理局完成工商变更登记手续，取得了注册号为510107000124298的《企业法人营业执照》。","business_scope":"销售经国家密码管理局审批并通过指定检测机构产品质量检测的商用密码产品（凭许可证有效期经营至2019年2月22日）；电子计算机硬件研制、开发、生产、销售及其应用技术服务，电子计算机软件研发、销售及技术服务，网络设备的研制、开发、生产、销售，安防设备的研制、开发、生产、销售，电子产品的销售；公共安全防范设施系统设计、安装、维修；智能建筑系统集成。（依法须经批准的项目，经相关部门批准后方可开展经营活动）。"}
         * issue_data : {"setup_date":"2002-10-31 00:00:00","list_date":"2016-11-01 00:00:00","ipo_rate":22.99,"online_release_date":"2016-10-19 00:00:00","release_type":"上网定价发行","per_value":1,"circulation":1800,"per_stock_price":17.56,"circulation_fee":3522.13,"total_market_value":31608,"fund_capital_net":28085.87,"fDay_open_price":23.18,"fDay_close_price":25.29,"fDay_trunover_rate":0.02,"fDay_ceiling_price":25.29,"success_rate":null,"success_rate_pricing":0.02}
         */

        private BasicDataBean basic_data;
        private IssueDataBean issue_data;

        public BasicDataBean getBasic_data() {
            return basic_data;
        }

        public void setBasic_data(BasicDataBean basic_data) {
            this.basic_data = basic_data;
        }

        public IssueDataBean getIssue_data() {
            return issue_data;
        }

        public void setIssue_data(IssueDataBean issue_data) {
            this.issue_data = issue_data;
        }

        public static class BasicDataBean {
            /**
             * company_name : 成都佳发安泰科技股份有限公司
             * company_enname : Chengdu Jafaantai Technology Co., Ltd.
             * name_before : null
             * a_stock : 300559
             * a_unit : 佳发安泰
             * b_stock : null
             * b_unit : null
             * h_stock : null
             * h_unit : null
             * security_type : 深交所创业板A股
             * industry_owned : 软件和信息技术服务业
             * general_manager : 寇健
             * legal_represent : 袁斌
             * secretaries : 文晶
             * chairman : 袁斌
             * security_represent : 毛涓涓
             * independent_direct : 李勃,廖中新,尹治本
             * contact_phone : 028-65293708
             * email : cdjiafaantai@163.com
             * fax : 028-85925609
             * company_website : ww.jf-r.com
             * office_addr : 四川省成都市武侯区武兴五路433号（武侯新城管委会内）
             * regist_addr : 四川省成都市武侯区武兴五路433号（武侯新城管委会内）
             * region : 四川
             * post_code : 610046
             * regist_capital : 7180.0000
             * regist_regist : 915101077436163833
             * employees : 316
             * management_num : 13
             * law_office : 北京市金杜律师事务所
             * account_office : 大信会计师事务所(特殊普通合伙)
             * company_summary : 2002年10月25日，佳发有限（筹）召开股东会，全体股东一致通过公司章程。根据公司章程，佳发有限（筹）注册资本为50万元，其中寇勤、陈大强、凌军分别以货币资金出资28万元、11万元、11万元。2002年10月29日，四川中宇会计师事务所出具了《验资报告》（川中宇验字[2002]第10-28号）对该次出资进行了验证。根据该验资报告，股东出资已到位。2002年10月31日，成都市工商行政管理局向佳发有限核发了注册号为5101072007002的《企业法人营业执照》。2012年9月26日，佳发有限召开股东会，决定以经大信所审计的截至2012年5月31日的净资产75,248,862.73元人民币为基础，按照1:0.7150的比例折成股本5,380万股，每股面值1元，其余21,448,862.73元计入资本公积，佳发有限整体变更为股份有限公司。2012年10月15日，大信所对佳发有限整体变更为股份有限公司的注册资本实收情况进行了验证，并出具了大信验字[2012]第3-0027号《验资报告》。2012年11月27日，公司在成都市工商行政管理局完成工商变更登记手续，取得了注册号为510107000124298的《企业法人营业执照》。
             * business_scope : 销售经国家密码管理局审批并通过指定检测机构产品质量检测的商用密码产品（凭许可证有效期经营至2019年2月22日）；电子计算机硬件研制、开发、生产、销售及其应用技术服务，电子计算机软件研发、销售及技术服务，网络设备的研制、开发、生产、销售，安防设备的研制、开发、生产、销售，电子产品的销售；公共安全防范设施系统设计、安装、维修；智能建筑系统集成。（依法须经批准的项目，经相关部门批准后方可开展经营活动）。
             */

            private String company_name;
            private String company_enname;
            private Object name_before;
            private String a_stock;
            private String a_unit;
            private Object b_stock;
            private Object b_unit;
            private Object h_stock;
            private Object h_unit;
            private String security_type;
            private String industry_owned;
            private String general_manager;
            private String legal_represent;
            private String secretaries;
            private String chairman;
            private String security_represent;
            private String independent_direct;
            private String contact_phone;
            private String email;
            private String fax;
            private String company_website;
            private String office_addr;
            private String regist_addr;
            private String region;
            private String post_code;
            private String regist_capital;
            private String regist_regist;
            private int employees;
            private int management_num;
            private String law_office;
            private String account_office;
            private String company_summary;
            private String business_scope;

            public String getCompany_name() {
                return company_name;
            }

            public void setCompany_name(String company_name) {
                this.company_name = company_name;
            }

            public String getCompany_enname() {
                return company_enname;
            }

            public void setCompany_enname(String company_enname) {
                this.company_enname = company_enname;
            }

            public Object getName_before() {
                return name_before;
            }

            public void setName_before(Object name_before) {
                this.name_before = name_before;
            }

            public String getA_stock() {
                return a_stock;
            }

            public void setA_stock(String a_stock) {
                this.a_stock = a_stock;
            }

            public String getA_unit() {
                return a_unit;
            }

            public void setA_unit(String a_unit) {
                this.a_unit = a_unit;
            }

            public Object getB_stock() {
                return b_stock;
            }

            public void setB_stock(Object b_stock) {
                this.b_stock = b_stock;
            }

            public Object getB_unit() {
                return b_unit;
            }

            public void setB_unit(Object b_unit) {
                this.b_unit = b_unit;
            }

            public Object getH_stock() {
                return h_stock;
            }

            public void setH_stock(Object h_stock) {
                this.h_stock = h_stock;
            }

            public Object getH_unit() {
                return h_unit;
            }

            public void setH_unit(Object h_unit) {
                this.h_unit = h_unit;
            }

            public String getSecurity_type() {
                return security_type;
            }

            public void setSecurity_type(String security_type) {
                this.security_type = security_type;
            }

            public String getIndustry_owned() {
                return industry_owned;
            }

            public void setIndustry_owned(String industry_owned) {
                this.industry_owned = industry_owned;
            }

            public String getGeneral_manager() {
                return general_manager;
            }

            public void setGeneral_manager(String general_manager) {
                this.general_manager = general_manager;
            }

            public String getLegal_represent() {
                return legal_represent;
            }

            public void setLegal_represent(String legal_represent) {
                this.legal_represent = legal_represent;
            }

            public String getSecretaries() {
                return secretaries;
            }

            public void setSecretaries(String secretaries) {
                this.secretaries = secretaries;
            }

            public String getChairman() {
                return chairman;
            }

            public void setChairman(String chairman) {
                this.chairman = chairman;
            }

            public String getSecurity_represent() {
                return security_represent;
            }

            public void setSecurity_represent(String security_represent) {
                this.security_represent = security_represent;
            }

            public String getIndependent_direct() {
                return independent_direct;
            }

            public void setIndependent_direct(String independent_direct) {
                this.independent_direct = independent_direct;
            }

            public String getContact_phone() {
                return contact_phone;
            }

            public void setContact_phone(String contact_phone) {
                this.contact_phone = contact_phone;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getFax() {
                return fax;
            }

            public void setFax(String fax) {
                this.fax = fax;
            }

            public String getCompany_website() {
                return company_website;
            }

            public void setCompany_website(String company_website) {
                this.company_website = company_website;
            }

            public String getOffice_addr() {
                return office_addr;
            }

            public void setOffice_addr(String office_addr) {
                this.office_addr = office_addr;
            }

            public String getRegist_addr() {
                return regist_addr;
            }

            public void setRegist_addr(String regist_addr) {
                this.regist_addr = regist_addr;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getPost_code() {
                return post_code;
            }

            public void setPost_code(String post_code) {
                this.post_code = post_code;
            }

            public String getRegist_capital() {
                return regist_capital;
            }

            public void setRegist_capital(String regist_capital) {
                this.regist_capital = regist_capital;
            }

            public String getRegist_regist() {
                return regist_regist;
            }

            public void setRegist_regist(String regist_regist) {
                this.regist_regist = regist_regist;
            }

            public int getEmployees() {
                return employees;
            }

            public void setEmployees(int employees) {
                this.employees = employees;
            }

            public int getManagement_num() {
                return management_num;
            }

            public void setManagement_num(int management_num) {
                this.management_num = management_num;
            }

            public String getLaw_office() {
                return law_office;
            }

            public void setLaw_office(String law_office) {
                this.law_office = law_office;
            }

            public String getAccount_office() {
                return account_office;
            }

            public void setAccount_office(String account_office) {
                this.account_office = account_office;
            }

            public String getCompany_summary() {
                return company_summary;
            }

            public void setCompany_summary(String company_summary) {
                this.company_summary = company_summary;
            }

            public String getBusiness_scope() {
                return business_scope;
            }

            public void setBusiness_scope(String business_scope) {
                this.business_scope = business_scope;
            }
        }

        public static class IssueDataBean {
            /**
             * setup_date : 2002-10-31 00:00:00
             * list_date : 2016-11-01 00:00:00
             * ipo_rate : 22.99
             * online_release_date : 2016-10-19 00:00:00
             * release_type : 上网定价发行
             * per_value : 1
             * circulation : 1800
             * per_stock_price : 17.56
             * circulation_fee : 3522.13
             * total_market_value : 31608
             * fund_capital_net : 28085.87
             * fDay_open_price : 23.18
             * fDay_close_price : 25.29
             * fDay_trunover_rate : 0.02
             * fDay_ceiling_price : 25.29
             * success_rate : null
             * success_rate_pricing : 0.02
             */

            private String setup_date;
            private String list_date;
            private double ipo_rate;
            private String online_release_date;
            private String release_type;
            private int per_value;
            private int circulation;
            private double per_stock_price;
            private double circulation_fee;
            private int total_market_value;
            private double fund_capital_net;
            private double fDay_open_price;
            private double fDay_close_price;
            private double fDay_trunover_rate;
            private double fDay_ceiling_price;
            private Object success_rate;
            private double success_rate_pricing;

            public String getSetup_date() {
                return setup_date;
            }

            public void setSetup_date(String setup_date) {
                this.setup_date = setup_date;
            }

            public String getList_date() {
                return list_date;
            }

            public void setList_date(String list_date) {
                this.list_date = list_date;
            }

            public double getIpo_rate() {
                return ipo_rate;
            }

            public void setIpo_rate(double ipo_rate) {
                this.ipo_rate = ipo_rate;
            }

            public String getOnline_release_date() {
                return online_release_date;
            }

            public void setOnline_release_date(String online_release_date) {
                this.online_release_date = online_release_date;
            }

            public String getRelease_type() {
                return release_type;
            }

            public void setRelease_type(String release_type) {
                this.release_type = release_type;
            }

            public int getPer_value() {
                return per_value;
            }

            public void setPer_value(int per_value) {
                this.per_value = per_value;
            }

            public int getCirculation() {
                return circulation;
            }

            public void setCirculation(int circulation) {
                this.circulation = circulation;
            }

            public double getPer_stock_price() {
                return per_stock_price;
            }

            public void setPer_stock_price(double per_stock_price) {
                this.per_stock_price = per_stock_price;
            }

            public double getCirculation_fee() {
                return circulation_fee;
            }

            public void setCirculation_fee(double circulation_fee) {
                this.circulation_fee = circulation_fee;
            }

            public int getTotal_market_value() {
                return total_market_value;
            }

            public void setTotal_market_value(int total_market_value) {
                this.total_market_value = total_market_value;
            }

            public double getFund_capital_net() {
                return fund_capital_net;
            }

            public void setFund_capital_net(double fund_capital_net) {
                this.fund_capital_net = fund_capital_net;
            }

            public double getFDay_open_price() {
                return fDay_open_price;
            }

            public void setFDay_open_price(double fDay_open_price) {
                this.fDay_open_price = fDay_open_price;
            }

            public double getFDay_close_price() {
                return fDay_close_price;
            }

            public void setFDay_close_price(double fDay_close_price) {
                this.fDay_close_price = fDay_close_price;
            }

            public double getFDay_trunover_rate() {
                return fDay_trunover_rate;
            }

            public void setFDay_trunover_rate(double fDay_trunover_rate) {
                this.fDay_trunover_rate = fDay_trunover_rate;
            }

            public double getFDay_ceiling_price() {
                return fDay_ceiling_price;
            }

            public void setFDay_ceiling_price(double fDay_ceiling_price) {
                this.fDay_ceiling_price = fDay_ceiling_price;
            }

            public Object getSuccess_rate() {
                return success_rate;
            }

            public void setSuccess_rate(Object success_rate) {
                this.success_rate = success_rate;
            }

            public double getSuccess_rate_pricing() {
                return success_rate_pricing;
            }

            public void setSuccess_rate_pricing(double success_rate_pricing) {
                this.success_rate_pricing = success_rate_pricing;
            }
        }
    }
}
