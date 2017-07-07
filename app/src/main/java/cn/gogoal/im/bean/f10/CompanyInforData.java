package cn.gogoal.im.bean.f10;

/**
 * Created by dave.
 * Date: 2017/6/27.
 * Desc: description
 */
public class CompanyInforData {

    private String companyName;
    private String companyConten1;
    private String companyContent2;
    private int displayType; //1.公司概况 2.股本 3.高管 4.主营收入 5.公司名称、总股本、董事长 6.公司简介 7.主营收入构成

    public CompanyInforData(String companyName, String companyConten1, String companyContent2, int displayType) {
        this.companyName = companyName;
        this.companyConten1 = companyConten1;
        this.companyContent2 = companyContent2;
        this.displayType = displayType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyConten1() {
        return companyConten1;
    }

    public void setCompanyConten1(String companyConten1) {
        this.companyConten1 = companyConten1;
    }

    public String getCompanyContent2() {
        return companyContent2;
    }

    public void setCompanyContent2(String companyContent2) {
        this.companyContent2 = companyContent2;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }
}
