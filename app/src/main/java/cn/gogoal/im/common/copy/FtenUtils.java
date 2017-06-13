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
            "发行量(万股)", "每股发行价(元)", "发行费用(万元)", "发行总市值(万元)", "募集资金金额(亿元)",
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

    /**
     * 经营分析
     */
    public static final String[] analysisName = {
            "主营构成", "主营收入(亿元)", "收入比例", "主营成本(亿元)", "成本比例",
            "主营利润(亿元)", "利润比例", "毛利率"
    };

    /**
     * 财务分析
     */
    public static final String getReportType(String title) {
        if ("2017年报".equals(title)) return "17Q4";
        if ("2017三季报".equals(title)) return "17Q3";
        if ("2017中报".equals(title)) return "17Q2";
        if ("2017一季报".equals(title)) return "17Q1";
        if ("2016年报".equals(title)) return "16Q4";
        if ("2016三季报".equals(title)) return "16Q3";
        if ("2016中报".equals(title)) return "16Q2";
        if ("2016一季报".equals(title)) return "16Q1";
        if ("2015年报".equals(title)) return "15Q4";
        if ("2015三季报".equals(title)) return "15Q3";
        if ("2015中报".equals(title)) return "15Q2";
        if ("2015一季报".equals(title)) return "15Q1";
        return null;
    }

    public static final String[] title1 = {"净利润(亿元)", "每股收益EPS-基本(元)", "营业利润率(%)",
            "资产负债率(%)", "流动比率(%)", "存货周转率(次)", "营业总收入同比增长率(%)"};

    public static final String[] title2 = {"净利润(亿元)", "每股收益EPS-基本(元)", "营业利润率(%)",
            "资产负债率(%)", "负债与所有者权益比率(%)", "固定资产周转率(次)", "营业总收入同比增长率(%)"};


    /**
     * 财务报表
     */
    //资产负债表
    public static final String[] liabilitiesName = {
            /*"流水号", "源表ID", "机构代码", "机构名称", "股票代码", "股票名称", "财报发布期",
            "报表类型（1 非单季 2 单季）", "报表期前端显示", "显示数据类别 1-绝对值，0-同比值",
            "公司类型", */
            "现金及存放中央银行款项(万元)",
            "存放同业和其它金融机构款项(万元)",
            "贵金属(万元)",
            "拆出资金(万元)",
            "交易性金融资产(万元)",
            "衍生金融资产(万元)",
            "买入返售金融资产(万元)",
            "应收利息(万元)",
            "发放贷款及垫款(万元)",
            "代理业务资产(万元)",
            "可供出售金融资产(万元)",
            "持有至到期投资(万元)",
            "长期股权投资(万元)",
            "固定资产(元)",
            "无形资产(万元)",
            "递延所得税资产(万元)",
            "投资性房地产(万元)",
            "其他资产(万元)",
            "资产总计(万元)",

            "同业和其它金融机构存放款项(万元)",
            "向中央银行借款(万元)",
            "拆入资金(万元)",
            "交易性金融负债(万元)",
            "衍生金融负债(万元)",
            "卖出回购金融资产款(万元)",
            "吸收存款(万元)",
            "应付职工薪酬(万元)",
            "应交税费(万元)",
            "应付利息(万元)",
            "代理业务负债(万元)",
            "应付债券(万元)",
            "递延所得税负债(万元)",
            "预计负债(万元)",
            "其他负债(万元)",
            "负债合计(万元)",

            "股本(万元)",
            "资本公积金(万元)",
            "减：库存股(万元)",
            "盈余公积金(万元)",
            "未分配利润(万元)",
            "一般风险准备(万元)",
            "外币报表折算差额(万元)",
            "少数股东权益(万元)",
            "归属于母公司所有者权益合计(万元)",
            "所有者权益合计(万元)",
            "负债及股东权益总计(万元)"
            /*"入库时间",
            "状态（1有效  0无效）",
            "时间戳"*/
    };

    public static final String[] liabilitiesContent = {
            /*"id", "source_id", "org_id", "org_name", "symbol", "sname", "report_date", "report_stype",
            "report_show", "stype", "compstype", */
            "ffsyh_01", "ffsyh_02", "ffsyh_03", "ffsyh_04", "ffsyh_05", "ffsyh_06", "ffsyh_07",
            "ffsyh_08", "ffsyh_09", "ffsyh_10", "ffsyh_11", "ffsyh_12", "ffsyh_13", "ffsyh_14",
            "ffsyh_15", "ffsyh_16", "ffsyh_17", "ffsyh_18", "ffsyh_19", "ffsyh_20", "ffsyh_21",
            "ffsyh_22", "ffsyh_23", "ffsyh_24", "ffsyh_25", "ffsyh_26", "ffsyh_27", "ffsyh_28",
            "ffsyh_29", "ffsyh_30", "ffsyh_31", "ffsyh_32", "ffsyh_33", "ffsyh_34", "ffsyh_35",
            "ffsyh_36", "ffsyh_37", "ffsyh_38", "ffsyh_39", "ffsyh_40", "ffsyh_41", "ffsyh_42",
            "ffsyh_43", "ffsyh_44", "ffsyh_45", "ffsyh_46"
            /*"entry_date", "status", "tmstamp"*/
    };

    //利润分配表
    public static final String[] profitName = {
            /*"流水号", "源表ID", "机构代码", "机构名称", "股票代码", "股票名称", "财报发布期",
            "报表类型（1 非单季 2 单季）", "报表期前端显示", "显示数据类别 1-绝对值，0-同比值",
            "公司类型", */
            "营业收入(万元)",
            "利息净收入(万元)",
            "利息收入(万元)",
            "减：利息支出(万元)",
            "手续费及佣金净收入(万元)",
            "手续费及佣金收入(万元)",
            "减：手续费及佣金支出(万元)",
            "投资收益(万元)",
            "其中：对联营企业和合营企业的投资收益(万元)",
            "公允价值变动收益(万元)",
            "汇兑收益(万元)",
            "其他业务收入(万元)",

            "营业支出(万元)",
            "营业税金及附加(万元)",
            "管理费用(万元)",
            "资产减值损失(万元)",
            "其他业务成本(万元)",

            "营业利润(万元)",
            "加:营业外收入(万元)",
            "减：营业外支出(万元)",

            "利润总额(万元)",
            "减：所得税(万元)",

            "净利润(万元)",
            "减：少数股东损益(万元)",
            "归属于母公司所有者的净利润(万元)",

            "(一)基本每股收益(元)",
            "(二)稀释每股收益(元)"
            /*"入库时间", "状态（1 有效  0 无效）", "时间戳"*/
    };

    public static final String[] profitContent = {
            /*"id", "source_id", "org_id", "org_name", "symbol", "sname", "report_date", "report_stype",
            "report_show", "stype", "compstype",*/
            "ffsyh_01", "ffsyh_02", "ffsyh_03", "ffsyh_04", "ffsyh_05", "ffsyh_06", "ffsyh_07",
            "ffsyh_08", "ffsyh_09", "ffsyh_10", "ffsyh_11", "ffsyh_12", "ffsyh_13", "ffsyh_14",
            "ffsyh_15", "ffsyh_16", "ffsyh_17", "ffsyh_18", "ffsyh_19", "ffsyh_20", "ffsyh_21",
            "ffsyh_22", "ffsyh_23", "ffsyh_24", "ffsyh_25", "ffsyh_26", "ffsyh_27",
            /*"entry_date", "status", "tmstamp"*/
    };

    //现金流量表
    public static final String[] cashFlowName = {
            /*"流水号", "源表ID", "机构代码", "机构名称", "股票代码", "股票名称", "财报发布期",
            "报表类型（1 非单季 2 单季）", "报表期前端显示", "显示数据类别 1-绝对值，0-同比值",
            "公司类型",*/
            "客户存款和同业存放款项净增加额(万元)",
            "向中央银行借款净增加额(万元)",
            "向其他金融机构拆入资金净增加额(万元)",
            "收取利息和手续费净增加额(万元)",
            "收到其他与经营活动有关的现金(万元)",
            "经营活动现金流入小计(万元)",
            "客户贷款及垫款净增加额(万元)",
            "存放央行和同业款项净增加额(万元)",
            "支付给职工以及为职工支付的现金(万元)",
            "支付的各项税费(万元)",
            "支付其他与经营活动有关的现金(万元)",
            "经营活动现金流出小计(万元)",
            "经营活动产生的现金流量净额(万元)",

            "收回投资收到的现金(万元)",
            "取得投资收益收到的现金(万元)",
            "处置固定资产、 无形资产和其他长期资产收回的现金(万元)",
            "收到其他与投资活动有关的现金(万元)",
            "投资活动现金流入小计(万元)",
            "投资支付的现金(万元)",
            "购建固定资产、 无形资产和其他长期资产支付的现金(万元)",
            "支付其他与投资活动有关的现金(万元)",
            "投资活动现金流出小计(万元)",
            "投资活动产生的现金流量净额(万元)",

            "吸收投资收到的现金(万元)",
            "发行债券收到的现金(万元)",
            "收到其他与筹资活动有关的现金(万元)",
            "筹资活动现金流入小计(万元)",
            "偿还债务支付的现金(万元)",
            "分配股利、 利润或偿付利息支付的现金(万元)",
            "支付其他与筹资活动有关的现金(万元)",
            "筹资活动现金流出小计(万元)",
            "筹资活动产生的现金流量净额(万元)",

            "汇率变动对现金的影响(万元)",

            "现金及现金等价物净增加额(万元)",
            "期初现金及现金等价物余额(万元)",
            "期末现金及现金等价物余额(万元)",

            "净利润(万元)",
            "加:资产减值准备(万元)",
            "固定资产折旧、油气资产折耗、 生产性生物资产折(万元)",
            "无形资产摊销(万元)",
            "长期待摊费用摊销(万元)",
            "处置固定资产、 无形资产和其他长期资产的损失(万元)",
            "固定资产报废损失(万元)",
            "公允价值变动损失(万元)",
            "财务费用(万元)",
            "投资损失(万元)",
            "递延所得税资产减少(万元)",
            "递延所得税负债增加(万元)",
            "存货的减少(万元)",
            "经营性应收项目的减少(万元)",
            "经营性应付项目的增加(万元)",
            "其他(万元)",
            "经营活动产生的现金流量净额(万元)",
            "债务转为资本(万元)",
            "一年内到期的可转换公司债券(万元)",
            "融资租入固定资产(万元)",
            "现金的期末余额(万元)",
            "减:现金的期初余额(万元)",
            "加:现金等价物的期末余额(万元)",
            "减:现金等价物的期初余额(万元)",
            "间接法- 现金及现金等价物净增加额(万元)"
            /*"入库时间", "状态（1有效  0无效）", "时间戳"*/
    };

    public static final String[] cashFlowContent = {
            /*"id", "source_id", "org_id", "org_name", "symbol", "sname", "report_date", "report_stype",
            "report_show", "stype", "compstype", */
            "ffsyh_01", "ffsyh_02", "ffsyh_03", "ffsyh_04", "ffsyh_05", "ffsyh_06", "ffsyh_07",
            "ffsyh_08", "ffsyh_09", "ffsyh_10", "ffsyh_11", "ffsyh_12", "ffsyh_13", "ffsyh_14",
            "ffsyh_15", "ffsyh_16", "ffsyh_17", "ffsyh_18", "ffsyh_19", "ffsyh_20", "ffsyh_21",
            "ffsyh_22", "ffsyh_23", "ffsyh_24", "ffsyh_25", "ffsyh_26", "ffsyh_27", "ffsyh_28",
            "ffsyh_29", "ffsyh_30", "ffsyh_31", "ffsyh_32", "ffsyh_33", "ffsyh_34", "ffsyh_35",
            "ffsyh_36", "ffsyh_37", "ffsyh_38", "ffsyh_39", "ffsyh_40", "ffsyh_41", "ffsyh_42",
            "ffsyh_43", "ffsyh_44", "ffsyh_45", "ffsyh_46", "ffsyh_47", "ffsyh_48", "ffsyh_49",
            "ffsyh_50", "ffsyh_51", "ffsyh_52", "ffsyh_53", "ffsyh_54", "ffsyh_55", "ffsyh_56",
            "ffsyh_57", "ffsyh_58", "ffsyh_59", "ffsyh_60", "ffsyh_61"
            /*"entry_date", "status", "tmstamp"*/
    };
}
