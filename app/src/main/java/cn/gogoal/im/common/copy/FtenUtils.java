package cn.gogoal.im.common.copy;

/**
 * Created by dave.
 * Date: 2017/6/8.
 * Desc: description
 */
public class FtenUtils {

    /**
     * 公司简况
     */
    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;

    public static final String[] profileName = {
            "公司名称", "英文名称", "曾用名", "A股代码", "A股简称", "B股代码",
            "B股简称", "H股代码", "H股简称", "证券类别", "所属行业", "总经理",
            "法人代表", "董秘", "董事长", "证券事务代表", "独立董事",
            "联系电话", "电子邮箱", "传真", "公司网址", "办公地址", "注册地址",
            "区域", "邮政编码", "注册资本(万元)", "工商登记", "雇佣员工", "管理人员人数",
            "律师事务所", "会计事务所",

            "成立日期", "上市日期", "发行市盈率", "网上发行日期", "发行方式", "每股面值(元)",
            "发行量(万股)", "每股发行价(元)", "发行费用(万元)", "发行总市值(万元)", "募集资金净额(万元)",
            "首日开盘价格(元)", "首日收盘价格(元)", "首日换手率", "首日最高价格(元)",
            "网下配售中签率", "定价中签率"
    };

    public static final String[] profileContent = {
            "company_name", "company_enname", "name_before", "a_stock", "a_unit", "b_stock",
            "b_unit", "h_stcok", "h_unit", "security_type", "industry_owned", "general_manager",
            "legal_represent", "secretaries", "chairman", "security_represent", "independent_dirct",
            "contact_phone", "email", "fax", "company_website", "office_addr", "regist_addr",
            "region", "post_code", "regist_capital", "regist_regist", "employees", "management_num",
            "low_office", "account_office",

            "setup_date", "list_date", "ipo_rate", "online_release_date", "release_type", "per_value",
            "circulation", "per_stock_price", "circulation_fee", "total_market_value", "fund_capital_net",
            "fDay_open_price", "fDay_close_price", "fDay_trunover_rate", "fDay_ceiling_price",
            "success_rate", "success_rate_pricing"
    };
}
