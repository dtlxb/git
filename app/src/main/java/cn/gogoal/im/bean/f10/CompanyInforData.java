package cn.gogoal.im.bean.f10;

/**
 * Created by dave.
 * Date: 2017/6/27.
 * Desc: description
 */
public class CompanyInforData {

    private String inforTitle;
    private String companyName;
    private String companyContent;
    private String companyContent1;

    public CompanyInforData(String inforTitle, String companyName, String companyContent,
                            String companyContent1) {
        this.inforTitle = inforTitle;
        this.companyName = companyName;
        this.companyContent = companyContent;
        this.companyContent1 = companyContent1;
    }

    public String getInforTitle() {
        return inforTitle;
    }

    public void setInforTitle(String inforTitle) {
        this.inforTitle = inforTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyContent() {
        return companyContent;
    }

    public void setCompanyContent(String companyContent) {
        this.companyContent = companyContent;
    }

    public String getCompanyContent1() {
        return companyContent1;
    }

    public void setCompanyContent1(String companyContent1) {
        this.companyContent1 = companyContent1;
    }
}
