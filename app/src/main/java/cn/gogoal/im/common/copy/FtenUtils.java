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
        if ("2014年报".equals(title)) return "14Q4";
        if ("2014三季报".equals(title)) return "14Q3";
        if ("2014中报".equals(title)) return "14Q2";
        if ("2014一季报".equals(title)) return "14Q1";
        if ("2013年报".equals(title)) return "13Q4";
        if ("2013三季报".equals(title)) return "13Q3";
        if ("2013中报".equals(title)) return "13Q2";
        if ("2013一季报".equals(title)) return "13Q1";
        if ("2012年报".equals(title)) return "12Q4";
        if ("2012三季报".equals(title)) return "12Q3";
        if ("2012中报".equals(title)) return "12Q2";
        if ("2012一季报".equals(title)) return "12Q1";
        return null;
    }

    public static final String[] title1 = {"净利润(亿元)", "每股收益EPS-基本(元)", "营业利润率(%)",
            "资产负债率(%)", "流动比率(%)", "存货周转率(次)", "营业总收入同比增长率(%)"};
    public static final String[] cotent1 = {"retained_profits10", "perbasic_eps20",
            "epopratio_profit35", "asset_liratio46", "debtflow_ratio53", "service_saveloan72",
            "business_totalrevenue_growthrate90"};

    public static final String[] title2 = {"净利润(亿元)", "每股收益EPS-基本(元)", "营业利润率(%)",
            "资产负债率(%)", "负债与所有者权益比率(%)", "固定资产周转率(次)", "营业总收入同比增长率(%)"};
    public static final String[] cotent2 = {"ffajr_09", "ffajr_19", "ffajr_29", "ffajr_38",
            "ffajr_45", "ffajr_50", "ffajr_57"};

    public static final String[] title1_1 = {"营业收入(亿元)", "每股现金流量净额(元)", "净资产收益率(%)",
            "长期资产负债率(%)", "资产负债率(%)", "总资产周转率(次)", "归属母公司股东的净利润同比增长率(%)"};
    public static final String[] cotent1_1 = {"business_income04", "pershare_netcash29",
            "epnetassets_inrate38", "asset_loliratio47", "debtliability_ratio57",
            "service_tassets74", "parentcompany_netprofit94"};

    public static final String[] title2_1 = {"营业收入(亿元)", "每股现金流量净额(元)", "净资产收益率(%)",
            "权益乘数(倍)", "资产负债率(%)", "总资产周转率(次)", "归属母公司股东的净利润同比增长率(%)"};
    public static final String[] cotent2_1 = {"ffajr_04", "ffajr_25", "ffajr_31", "ffajr_39",
            "ffajr_42", "ffajr_51", "ffajr_60"};

    //核心财务指标
    public static final String[] profit1 = {"基本每股收益(元)", "每股净资产(元)", "每股经营活动产生的现金流量净额(元)",
            "营业收入(万元)", "营业利润(万元)", "投资收益(万元)", "营业外收支净额(万元)", "利润总额(万元)", "净利润(万元)",
            "净利润(扣除非经常性损益后)(万元)", "经营活动产生的现金流量净额(万元)", "现金及现金等价物净增加额(万元)",
            "总资产(万元)", "流动资产(万元)", "总负债(万元)", "流动负债(万元)", "股东权益不含少数股东权益(万元)",
            "净资产收益率加权(%)"};
    public static final String[] profit1_1 = {"basic_eps01", "pershare_netasset02", "pershare_cash_activity03",
            "business_income04", "business_profit06", "invest_income07", "net_nonoperating_income08",
            "total_profit09", "retained_profits10", "profits11", "business_cash_flow12", "cash_and_casheq13",
            "total_assets14", "flow_assets15", "total_indebt16", "flow_indebt17", "shareholders_less18",
            "assets_weighting19"};
    public static final String[] profit2 = {"基本每股收益(元)", "每股净资产(元)", "每股经营活动产生的现金流量净额(元)",
            "营业收入(万元)", "营业利润(万元)", "投资收益(万元)", "营业外收支净额(万元)", "利润总额(万元)", "净利润(万元)",
            "净利润(扣除非经常性损益后)(万元)", "经营活动产生的现金流量净额(万元)", "现金及现金等价物净增加额(万元)",
            "总资产(万元)", "总负债(万元)", "股东权益不含少数股东权益(万元)", "净资产收益率加权(%)"};
    public static final String[] profit2_1 = {"ffajr_01", "ffajr_02", "ffajr_03", "ffajr_04",
            "ffajr_05", "ffajr_06", "ffajr_07", "ffajr_08", "ffajr_09", "ffajr_10", "ffajr_11",
            "ffajr_12", "ffajr_13", "ffajr_14", "ffajr_15", "ffajr_16"};

    //每股指标
    public static final String[] share_eps1 = {"每股收益EPS-基本(元)", "每股收益EPS-稀释(元)",
            "每股收益EPS-扣除/基本(元)", "每股收益EPS-扣除/稀释(元)", "每股净资产BPS(元)", "每股营收总收入(元)",
            "每股营收收入(元)", "每股资本公积(元)", "每股未分配利润(元)", "每股现金流量净额(元)"};
    public static final String[] share_eps1_1 = {"perbasic_eps20", "pereps_dilute21",
            "pereps_ductbasic22", "pereps_reduce23", "perstock_bps24", "perre_total25",
            "perre_income26", "pershare_reserves27", "udpps28", "pershare_netcash29"};
    public static final String[] share_eps2 = {"每股收益EPS-基本(元)", "每股收益EPS-稀释(元)",
            "每股收益EPS-扣除/基本(元)", "每股收益EPS-扣除/稀释(元)", "每股净资产BPS(元)", "每股营收总收入(元)",
            "每股资本公积(元)", "每股未分配利润(元)", "每股现金流量净额(元)"};
    public static final String[] share_eps2_1 = {"ffajr_17", "ffajr_18", "ffajr_19", "ffajr_20",
            "ffajr_21", "ffajr_22", "ffajr_23", "ffajr_24", "ffajr_25"};

    //盈利能力
    public static final String[] ProfitRate1 = {"总资产利润率(%)", "主营业务利润率(%)", "总资产净利润率(%)",
            "成本费用利润率(%)", "营业利润率(%)", "主营业务成本率(%)", "销售净利率(%)", "净资产收益率(%)",
            "股本报酬率(%)", "净资产报酬率(%)", "资产报酬率(%)", "销售毛利率(%)", "三项费用比重(%)",
            "非主营比重(%)", "主营利润比重(%)"};
    public static final String[] ProfitRate1_1 = {"eptotal_profit31", "epmbu_profit32", "eptasset_profit33",
            "epcost_profit34", "epopratio_profit35", "epmb_profitrate36", "epsales_netprofit37",
            "epnetassets_inrate38", "epreturn_rate39", "epnet_asset40", "epasset_rate41", "epgross_sale42",
            "epcost_three43", "epmain_proportion44", "epmajor_rate45"};
    public static final String[] ProfitRate2 = {"总资产利润率(%)", "总资产净利润率(%)", "成本费用利润率(%)",
            "营业利润率(%)", "销售净利率(%)", "净资产收益率(%)", "股本报酬率(%)", "净资产报酬率(%)",
            "资产报酬率(%)", "销售毛利率(%)", "非主营比重(%)", "主营利润比重(%)"};
    public static final String[] ProfitRate2_1 = {"ffajr_26", "ffajr_27", "ffajr_28", "ffajr_29", "ffajr_30",
            "ffajr_31", "ffajr_32", "ffajr_33", "ffajr_34", "ffajr_35", "ffajr_36", "ffajr_37"};

    //资本结构
    public static final String[] debtratio1 = {"资产负债率(%)", "长期资本负债率(%)", "权益乘数(倍)",
            "流动资产/总资产(%)", "非流动资产/总资产(%)", "非流动负债权益比率", "流动负债权益比率"};
    public static final String[] debtratio1_1 = {"asset_liratio46", "asset_loliratio47", "asset_eqmulti48",
            "asset_total49", "asset_ntotal50", "asset_nclratio51", "asset_clratio52"};
    public static final String[] debtratio2 = {"资产负债率(%)", "权益乘数(倍)"};
    public static final String[] debtratio2_1 = {"ffajr_38", "ffajr_39"};

    //偿债能力
    public static final String[] sovency1 = {"流动比率", "速动比率", "现金比率", "利息支付倍数(倍)",
            "资产负债率(%)", "长期债务与营运资金比率", "股东权益比率", "长期负债比率", "股东权益与固定资产比率",
            "负债与所有者权益比率", "长期资产与长期资金比率", "资本化比率", "固定资产净值率(%)", "资本固定化比率",
            "产权比率", "清算价值比率", "固定资产比重(%)"};
    public static final String[] sovency1_1 = {"debtflow_ratio53", "debt_velratio54", "debtcash_ratio55",
            "debtcover_ratio56", "debtliability_ratio57", "debt_longterm58", "debtshareholder_liabilty59",
            "debtlong_bcashratio60", "debtshareholder_fixedasset61", "debteower_ratio62", "debtlong_ratio63",
            "debt_capitrate64", "debtfixed_netassets65", "debtrate_assets66", "debtnet_equityratio67",
            "debtliquidate_ratio68", "debt_fixed_ratio69"};
    public static final String[] sovency2 = {"资产负债率(%)", "股东权益比率", "负债与所有者权益比率",
            "资本固定化比率"};
    public static final String[] sovency2_1 = {"ffajr_42", "ffajr_43", "ffajr_45", "ffajr_48"};

    //营运能力
    public static final String[] turnoverrate1 = {"应收账款周转率(次)", "应收账款周转天数(天)",
            "存货周转率(次)", "固定资产周转率(次)", "总资产周转率(次)", "存货周转天数(天)", "总资产周转天数(天)",
            "流动资产周转率(次)", "流动资产周转天数(天)", "经营现金净流量对销售收入比率", "资产的经营现金流量回报率(%)",
            "经营现金净流量与净利润的比率", "经营现金净流量对负债比率", "现金流量比率"};
    public static final String[] turnoverrate1_1 = {"service_receivable70", "service_receiday71",
            "service_saveloan72", "service_fixedassets73", "service_tassets74", "service_loandays75",
            "service_tassetsdays76", "service_currentasset77", "service_currentassday78",
            "service_opercashsales79", "service_assetflowcash80", "service_bcashbprofit81",
            "service_bcashflowdebtratio82", "service_flowcash83"};
    public static final String[] turnoverrate2 = {"固定资产周转率(次)", "总资产周转率(次)",
            "资产的经营现金流量回报率(%)", "经营现金净流量与净利润的比率", "经营现金净流量对负债比率"};
    public static final String[] turnoverrate2_1 = {"ffajr_50", "ffajr_51", "ffajr_52", "ffajr_53", "ffajr_54"};

    //成长能力
    public static final String[] grows1 = {"基本每股收益同比增长率(%)", "稀释每股收益同比增长率(%)",
            "营业总收入同比增长率(%)", "营业收入同比增长率(%)", "营业利润同比增长率(%)", "利润总额同比增长率(%)",
            "归属母公司股东的净利润同比增长率(%)", "经营活动产生的现金流量净额同比增长率(%)", "净资产同比增长率(%)",
            "总负债同比增长率(%)", "总资产同比增长率(%)"};
    public static final String[] grows1_1 = {"basic_eps_growthrate88", "dilution_eps_growthrate89",
            "business_totalrevenue_growthrate90", "business_income_growthrate91",
            "business_profit_growthrate92", "profit_lumpsum_growthrate93", "parentcompany_netprofit94",
            "operating_netflow95", "netassets_growthrate96", "total_liability_growthrate97",
            "totalassets_growthrate98"};
    public static final String[] grows2 = {"基本每股收益同比增长率(%)", "稀释每股收益同比增长率(%)",
            "营业总收入同比增长率(%)", "营业利润同比增长率(%)", "利润总额同比增长率(%)",
            "归属母公司股东的净利润同比增长率(%)", "经营活动产生的现金流量净额同比增长率(%)", "净资产同比增长率(%)",
            "总负债同比增长率(%)", "总资产同比增长率(%)"};
    public static final String[] grows2_1 = {"ffajr_55", "ffajr_56", "ffajr_57", "ffajr_58",
            "ffajr_59", "ffajr_60", "ffajr_61", "ffajr_62", "ffajr_63", "ffajr_64"};

    /**
     * 财务报表
     */
    //利润分配表
    public static final String[] profitForm1 = {
            "营业总收入(万元)", "营业收入(万元)", "利息收入(万元)", "已赚保费(万元)",
            "手续费及佣金收入(万元)", "营业总成本(万元)", "营业成本(万元)", "利息支出(万元)",
            "手续费及佣金支出(万元)", "研发费用(万元)", "退保金(万元)", "赔付支出净额(万元)",
            "提取保险合同准备金净额(万元)", "保单红利支出(万元)", "分保费用(万元)", "营业税金及附加(万元)",
            "销售费用(万元)", "管理费用(万元)", "财务费用(万元)", "资产减值损失(万元)",
            "公允价值变动净收益(万元)", "投资净收益(万元)", "其中:对联营企业和合营企业的投资收益(万元)", "汇兑净收益(万元)",
            "四、营业利润(万元)", "加:营业外收入(万元)", "减:营业外支出(万元)", "其中:非流动资产处置净损失(万元)",
            "利润总额(万元)", "减:所得税(万元)", "加:未确认的投资损失(万元)", "净利润(万元)",
            "减:少数股东损益(万元)", "归属于母公司所有者的净利润(万元)", "(一) 基本每股收益(元)", "(二) 稀释每股收益(元)"};
    public static final String[] profitForm1_1 = {
            "trading_income115", "trading_income116", "trading_income117", "trading_income118",
            "trading_income119", "trading_cost120", "trading_cost121", "trading_cost122",
            "trading_cost123", "trading_cost124", "trading_cost125", "trading_cost126",
            "trading_cost127", "trading_cost128", "trading_cost129", "trading_cost130",
            "trading_cost131", "trading_cost132", "trading_cost133", "trading_cost134",
            "trading_cost135", "trading_cost136", "trading_cost137", "trading_cost138",
            "trading_profit141", "trading_profit142", "trading_profit143", "trading_profit144",
            "total_profit147", "total_profit148", "total_profit149", "net_profit152",
            "net_profit153", "net_profit154", "net_profit155", "net_profit156"
    };
    public static final String[] profitForm2 = {
            "营业收入(万元)", "利息净收入(万元)", "利息收入(万元)",
            "减：利息支出(万元)", "手续费及佣金净收入(万元)", "手续费及佣金收入(万元)",
            "减：手续费及佣金支出(万元)", "投资收益(万元)", "其中：对联营企业和合营企业的投资收益(万元)",
            "公允价值变动收益(万元)", "汇兑收益(万元)", "其他业务收入(万元)",
            "营业支出(万元)", "营业税金及附加(万元)", "管理费用(万元)",
            "资产减值损失(万元)", "其他业务成本(万元)", "营业利润(万元)",
            "加:营业外收入(万元)", "减：营业外支出(万元)", "利润总额(万元)",
            "减：所得税(万元)", "净利润(万元)", "减：少数股东损益(万元)",
            "归属于母公司所有者的净利润(万元)", "(一)基本每股收益(元)", "(二)稀释每股收益(元)"};

    public static final String[] profitForm2_1 = {
            "profit_distribution01", "profit_distribution02", "profit_distribution03",
            "profit_distribution04", "profit_distribution05", "profit_distribution06",
            "profit_distribution07", "profit_distribution08", "profit_distribution09",
            "profit_distribution10", "profit_distribution11", "profit_distribution12",
            "profit_distribution13", "profit_distribution14", "profit_distribution15",
            "profit_distribution16", "profit_distribution17", "profit_distribution18",
            "profit_distribution19", "profit_distribution20", "profit_distribution21",
            "profit_distribution22", "profit_distribution23", "profit_distribution24",
            "profit_distribution25", "profit_distribution26", "profit_distribution27"
    };


    //资产负债表
    public static final String[] assetsForm1 = {
            "货币资金(万元)", "结算备付金(万元)", "拆出资金(万元)", "其中:交易性金融资产(万元)", "应收票据(万元)",
            "应收账款(万元)", "预付款项(万元)", "应收保费(万元)", "应收分保账款(万元)", "应收分保合同准备金(万元)",
            "应收利息(万元)", "其他应收款(万元)", "应收股利(万元)", "买入返售金融资产(万元)", "存货(万元)",
            "一年内到期的非流动资产(万元)", "其他流动资产(万元)(万元)", "流动资产合计(万元)", "发放贷款及垫款(万元)", "可供出售金融资产(万元)",
            "持有至到期投资(万元)", "长期应收款(万元)", "长期股权投资(万元)", "投资性房地产(万元)", "固定资产(元)",
            "在建工程(万元)", "工程物资(万元)", "固定资产清理(万元)", "生产性生物资产(万元)", "油气资产(万元)",
            "无形资产(万元)", "开发支出(万元)", "商誉(万元)", "长期待摊费用(万元)", "递延所得税资产(万元)",
            "其他非流动资产(万元)", "非流动资产合计(万元)", "资产总计(万元)", "短期借款(万元)", "向中央银行借款(万元)",
            "吸收存款及同业存放(万元)", "拆入资金(万元)", "其中:交易性金融负债(万元)", "应付票据(万元)", "应付账款(万元)",
            "预收款项(万元)", "卖出回购金融资产款(万元)", "应付手续费及佣金(万元)", "应付职工薪酬(万元)", "应交税费(万元)",
            "应付利息(万元)", "应付股利(万元)", "应付分保账款(万元)", "内部应付款(万元)", "其他应付款(万元)",
            "预计流动负债(万元)", "保险合同准备金(万元)", "代理买卖证券款(万元)", "代理承销证券款(万元)", "一年内的递延收益(万元)",
            "一年内到期的非流动负债(万元)", "应付短期债券(万元)", "其他流动负债(万元)", "流动负债合计(万元)", "长期借款(万元)",
            "应付债券(万元)", "长期应付款(万元)", "专项应付款(万元)", "预计负债(万元)", "递延收益(万元)",
            "递延所得税负债(万元)", "其他非流动负债(万元)", "非流动负债合计(万元)", "负债合计(万元)", "实收资本(万元)",
            "资本公积金(万元)", "减:库存股(万元)", "专项储备(万元)", "盈余公积金(万元)", "一般风险准备(万元)",
            "未分配利润(万元)", "外币报表折算差额(万元)", "未确认的投资损失(万元)", "少数股东权益(万元)", "归属于母公司所有者权益合计(万元)",
            "所有者权益合计(万元)", "负债和所有者权益总计(万元)"
    };
    public static final String[] assetsForm1_1 = {
            "money_fund1", "deposit_balance2", "lending_funds3", "trade_financial5", "notes_receivable7",
            "receivables8", "prepay9", "trade_debt10", "accounts_bill11", "accounts_billcash12",
            "interest_receivable13", "other_bill14", "dividend_receivable15", "buyfinance_assets16", "inventory17",
            "noncurrent_assets18", "other_current_assets19", "total_current_assets22", "noncurrent_assets23", "noncurrent_assets24",
            "noncurrent_assets25", "noncurrent_assets26", "noncurrent_assets27", "noncurrent_assets28", "noncurrent_assets29",
            "noncurrent_assets30", "noncurrent_assets31", "noncurrent_assets32", "noncurrent_assets33", "noncurrent_assets34",
            "noncurrent_assets35", "noncurrent_assets36", "noncurrent_assets37", "noncurrent_assets38", "noncurrent_assets39",
            "noncurrent_assets40", "noncurrent_assets43", "noncurrent_assets46", "flow_debt47", "flow_debt48",
            "flow_debt49", "flow_debt50", "flow_debt52", "flow_debt54", "flow_debt55",
            "flow_debt56", "flow_debt57", "flow_debt58", "flow_debt59", "flow_debt60",
            "flow_debt61", "flow_debt62", "flow_debt63", "flow_debt64", "flow_debt65",
            "flow_debt66", "flow_debt67", "flow_debt68", "flow_debt69", "flow_debt70",
            "flow_debt71", "flow_debt72", "flow_debt73", "flow_debt76", "nonflow_debt77",
            "nonflow_debt78", "nonflow_debt81", "nonflow_debt82", "nonflow_debt83", "nonflow_debt84",
            "nonflow_debt85", "nonflow_debt86", "nonflow_debt89", "nonflow_debt92", "owner_interest93",
            "owner_interest98", "owner_interest100", "owner_interest101", "owner_interest102", "owner_interest103",
            "owner_interest104", "owner_interest105", "owner_interest106", "owner_interest107", "owner_interest110",
            "owner_interest111", "owner_interest114"
    };
    public static final String[] assetsForm2 = {
            "现金及存放中央银行款项(万元)", "存放同业和其它金融机构款项(万元)", "贵金属(万元)", "拆出资金(万元)",
            "交易性金融资产(万元)", "衍生金融资产(万元)", "买入返售金融资产(万元)", "应收利息(万元)",
            "发放贷款及垫款(万元)", "代理业务资产(万元)", "可供出售金融资产(万元)", "持有至到期投资(万元)",
            "长期股权投资(万元)", "固定资产(元)", "无形资产(万元)", "递延所得税资产(万元)",
            "投资性房地产(万元)", "其他资产(万元)", "资产总计(万元)", "同业和其它金融机构存放款项(万元)",
            "向中央银行借款(万元)", "拆入资金(万元)", "交易性金融负债(万元)", "衍生金融负债(万元)",
            "卖出回购金融资产款(万元)", "吸收存款(万元)", "应付职工薪酬(万元)", "应交税费(万元)",
            "应付利息(万元)", "代理业务负债(万元)", "应付债券(万元)", "递延所得税负债",
            "预计负债(万元)", "其他负债(万元)", "负债合计(万元)", "股本(万元)",
            "资本公积金(万元)", "减：库存股(万元)", "盈余公积金(万元)", "未分配利润(万元)",
            "一般风险准备(万元)", "外币报表折算差额(万元)", "少数股东权益(万元)", "归属于母公司所有者权益合计(万元)",
            "所有者权益合计(万元)", "负债及股东权益总计(万元)"
    };
    public static final String[] assetsForm2_1 = {
            "money_fund1", "deposit_balance2", "lending_funds3", "liability_value4",
            "trade_financial5", "design_price6", "notes_receivable7", "receivables8",
            "prepay9", "trade_debt10", "accounts_bill11", "accounts_billcash12",
            "interest_receivable13", "other_bill14", "dividend_receivable15", "buyfinance_assets16",
            "inventory17", "noncurrent_assets18", "other_current_assets19", "current_assets20",
            "current_assets21", "total_current_assets22", "noncurrent_assets23", "noncurrent_assets24",
            "noncurrent_assets25", "noncurrent_assets26", "noncurrent_assets27", "noncurrent_assets28",
            "noncurrent_assets29", "noncurrent_assets30", "noncurrent_assets31", "noncurrent_assets32",
            "noncurrent_assets33", "noncurrent_assets34", "noncurrent_assets35", "noncurrent_assets36",
            "noncurrent_assets37", "noncurrent_assets38", "noncurrent_assets39", "noncurrent_assets40",
            "noncurrent_assets41", "noncurrent_assets42", "noncurrent_assets43", "noncurrent_assets44",
            "noncurrent_assets45", "noncurrent_assets46"
    };


    //现金流量表
    public static final String[] cashForm1 = {
            "销售商品、提供劳务收到的现金(万元)", "收到的税费返还(万元)", "收到其他与经营活动有关的现金(万元)",
            "经营活动现金流入小计(万元)", "购买商品、接受劳务支付的现金(万元)", "支付给职工以及为职工支付的现金(万元)",
            "支付的各项税费(万元)", "支付其他与经营活动有关的现金(万元)", "经营活动现金流出小计(万元)",
            "经营活动产生的现金流量净额(万元)", "收回投资收到的现金(万元)", "取得投资收益收到的现金(万元)",
            "处置固定资产、无形资产和其他长期资产收回的现金净额(万元)", "处置子公司及其他营业单位收到的现金净额(万元)", "收到其他与投资活动有关的现金(万元)",
            "投资活动现金流入小计(万元)", "购建固定资产、无形资产和其他长期资产支付的现金(万元)", "投资支付的现金(万元)",
            "取得子公司及其他营业单位支付的现金净额(万元)", "支付其他与投资活动有关的现金(万元)", "投资活动现金流出小计(万元)",
            "投资活动产生的现金流量净额(万元)", "吸收投资收到的现金(万元)", "其中:子公司吸收少数股东投资收到的现金(万元)",
            "取得借款收到的现金(万元)", "收到其他与筹资活动有关的现金(万元)", "发行债券收到的现金(万元)",
            "筹资活动现金流入小计(万元)", "偿还债务支付的现金(万元)", "其中:子公司支付给少数股东的股利、利润(万元)",
            "支付其他与筹资活动有关的现金(万元)", "筹资活动现金流出小计(万元)", "筹资活动产生的现金流量净额(万元)",
            "汇率变动对现金的影响(万元)", "期初现金及现金等价物余额(万元)", "期末现金及现金等价物余额(万元)",
            "净利润(万元)", "加:资产减值准备(万元)", "固定资产折旧、油气资产折耗、生产性生物资产折旧(万元)",
            "无形资产摊销(万元)", "长期待摊费用摊销(万元)", "待摊费用减少(万元)",
            "预提费用增加(万元)", "处置固定资产、无形资产和其他长期资产的损失(万元)", "固定资产报废损失(万元)",
            "公允价值变动损失(万元)", "财务费用(万元)", "投资损失(万元)",
            "递延所得税资产减少(万元)", "递延所得税负债增加(万元)", "存货的减少(万元)",
            "经营性应收项目的减少(万元)", "经营性应付项目的增加(万元)", "其他(万元)",
            "经营活动产生的现金流量净额(万元)", "债务转为资本(万元)", "一年内到期的可转换公司债券(万元)",
            "融资租入固定资产(万元)", "现金的期末余额(万元)", "减:现金的期初余额(万元)",
            "加:现金等价物的期末余额(万元)", "减:现金等价物的期初余额(万元)", "间接法-现金及现金等价物净增加额(万元)"
    };
    public static final String[] cashForm1_1 = {
            "manage_lowcash199", "manage_lowcash200", "manage_lowcash201",
            "manage_lowcash204", "manage_lowcash205", "manage_lowcash206",
            "manage_lowcash207", "manage_lowcash208", "manage_lowcash211",
            "manage_lowcash213", "invest_flowcash214", "invest_flowcash215",
            "invest_flowcash216", "invest_flowcash217", "invest_flowcash218",
            "invest_flowcash221", "invest_flowcash222", "invest_flowcash223",
            "invest_flowcash224", "invest_flowcash225", "invest_flowcash228",
            "invest_flowcash230", "financing_flowcash231", "financing_flowcash232",
            "financing_flowcash233", "financing_flowcash234", "financing_flowcash235",
            "financing_flowcash238", "financing_flowcash239", "financing_flowcash240",
            "financing_flowcash241", "financing_flowcash244", "financing_flowcash246",
            "equivalent_flowcash247", "further_information251", "further_information252",
            "further_information253", "further_information254", "further_information255",
            "further_information256", "further_information257", "further_information258",
            "further_information259", "further_information260", "further_information261",
            "further_information262", "further_information263", "further_information264",
            "further_information265", "further_information266", "further_information267",
            "further_information268", "further_information269", "further_information270",
            "further_information273", "further_information274", "further_information275",
            "further_information276", "further_information277", "further_information278",
            "further_information279", "further_information280", "further_information283"
    };
    public static final String[] cashForm2 = {
            "客户存款和同业存放款项净增加额(万元)", "向中央银行借款净增加额(万元)", "向其他金融机构拆入资金净增加额(万元)",
            "收取利息和手续费净增加额(万元)", "收到其他与经营活动有关的现金(万元)", "经营活动现金流入小计(万元)",
            "客户贷款及垫款净增加额(万元)", "存放央行和同业款项净增加额(万元)", "支付给职工以及为职工支付的现金(万元)",
            "支付的各项税费(万元)", "支付其他与经营活动有关的现金(万元)", "经营活动现金流出小计(万元)",
            "经营活动产生的现金流量净额(万元)", "收回投资收到的现金(万元)", "取得投资收益收到的现金(万元)",
            "处置固定资产、 无形资产和其他长期资产收回的现金(万元)", "收到其他与投资活动有关的现金(万元)", "投资活动现金流入小计(万元)",
            "投资支付的现金(万元)", "购建固定资产、 无形资产和其他长期资产支付的现金(万元)", "支付其他与投资活动有关的现金(万元)",
            "投资活动现金流出小计(万元)", "投资活动产生的现金流量净额(万元)", "吸收投资收到的现金(万元)",
            "发行债券收到的现金(万元)", "收到其他与筹资活动有关的现金(万元)", "筹资活动现金流入小计(万元)",
            "偿还债务支付的现金(万元)", "分配股利、 利润或偿付利息支付的现金(万元)", "支付其他与筹资活动有关的现金(万元)",
            "筹资活动现金流出小计(万元)", "筹资活动产生的现金流量净额(万元)", "汇率变动对现金的影响(万元)",
            "现金及现金等价物净增加额(万元)", "期初现金及现金等价物余额(万元)", "期末现金及现金等价物余额(万元)",
            "净利润(万元)", "加:资产减值准备(万元)", "固定资产折旧、油气资产折耗、生产性生物资产折(万元)",
            "无形资产摊销(万元)", "长期待摊费用摊销(万元)", "处置固定资产、 无形资产和其他长期资产的损失(万元)",
            "固定资产报废损失(万元)", "公允价值变动损失(万元)", "财务费用(万元)",
            "投资损失(万元)", "递延所得税资产减少(万元)", "递延所得税负债增加(万元)",
            "存货的减少(万元)", "经营性应收项目的减少(万元)", "经营性应付项目的增加(万元)",
            "其他(万元)", "经营活动产生的现金流量净额(万元)", "债务转为资本(万元)",
            "一年内到期的可转换公司债券(万元)", "融资租入固定资产(万元)", "现金的期末余额(万元)",
            "减:现金的期初余额(万元)", "加:现金等价物的期末余额(万元)", "减:现金等价物的期初余额(万元)",
            "间接法-现金及现金等价物净增加额(万元)"
    };
    public static final String[] cashForm2_1 = {
            "cash_flow01", "cash_flow02", "cash_flow03",
            "cash_flow04", "cash_flow05", "cash_flow06",
            "cash_flow07", "cash_flow08", "cash_flow09",
            "cash_flow10", "cash_flow11", "cash_flow12",
            "cash_flow13", "cash_flow14", "cash_flow15",
            "cash_flow16", "cash_flow17", "cash_flow19",
            "cash_flow20", "cash_flow21", "cash_flow22",
            "cash_flow24", "cash_flow25", "cash_flow26",
            "cash_flow27", "cash_flow28", "cash_flow29",
            "cash_flow30", "cash_flow31", "cash_flow33",
            "cash_flow34", "cash_flow35", "cash_flow36",
            "cash_flow37", "cash_flow38", "cash_flow39",
            "cash_flow40", "cash_flow41", "cash_flow42",
            "cash_flow43", "cash_flow44", "cash_flow45",
            "cash_flow46", "cash_flow47", "cash_flow48",
            "cash_flow49", "cash_flow50", "cash_flow51",
            "cash_flow52", "cash_flow53", "cash_flow54",
            "cash_flow55", "cash_flow56", "cash_flow57",
            "cash_flow58", "cash_flow59", "cash_flow60",
            "cash_flow61"
    };
}
