package cn.gogoal.im.bean.stock;

import java.util.List;

/**
 * Created by daiwei on 2015/9/22.
 */
public class StockDecisionData {
    /*{
        "authors": [
       ]
        "code": "600048",
            "code_name": "保利地产",
            "core": 1,
            "create_date": "2015-07-01",
            "download_file": 1,
            "guid": "15BEED30-F177-4F0B-B70A-FEE70CDE8133",
            "is_worthed": 0,
            "look_abstract": 1,
            "open_file": 1,
            "organ_id": 9,
            "organ_name": "海通证券",
            "report_summary": "行业复苏不达预期。",
            "report_title": "保利地产公司公告点评：认购粤高速定增，加深战略合作关系",
           */
    private List<StockDecisionAuthors> authors;
    private String code;
    private String code_name;
    private String core;
    private String create_date;
    private String download_file;
    private String guid;
    private String is_worthed;
    private String look_abstract;
    private String open_file;
    private String organ_id;
    private String organ_name;
    private String report_summary;
    private String report_title;
    private Integer favor_sum;
    private Integer praise_sum;
    private Integer share_sum;

    public List<StockDecisionAuthors> getAuthors() {
        return authors;
    }

    public void setAuthors(List<StockDecisionAuthors> authors) {
        this.authors = authors;
    }

    public String getReport_title() {
        return report_title;
    }

    public void setReport_title(String report_title) {
        this.report_title = report_title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getDownload_file() {
        return download_file;
    }

    public void setDownload_file(String download_file) {
        this.download_file = download_file;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getIs_worthed() {
        return is_worthed;
    }

    public void setIs_worthed(String is_worthed) {
        this.is_worthed = is_worthed;
    }

    public String getLook_abstract() {
        return look_abstract;
    }

    public void setLook_abstract(String look_abstract) {
        this.look_abstract = look_abstract;
    }

    public String getOpen_file() {
        return open_file;
    }

    public void setOpen_file(String open_file) {
        this.open_file = open_file;
    }

    public String getOrgan_id() {
        return organ_id;
    }

    public void setOrgan_id(String organ_id) {
        this.organ_id = organ_id;
    }

    public String getOrgan_name() {
        return organ_name;
    }

    public void setOrgan_name(String organ_name) {
        this.organ_name = organ_name;
    }

    public String getReport_summary() {
        return report_summary;
    }

    public void setReport_summary(String report_summary) {
        this.report_summary = report_summary;
    }

    public Integer getFavor_sum() {
        return favor_sum;
    }

    public void setFavor_sum(Integer favor_sum) {
        this.favor_sum = favor_sum;
    }

    public Integer getPraise_sum() {
        return praise_sum;
    }

    public void setPraise_sum(Integer praise_sum) {
        this.praise_sum = praise_sum;
    }

    public Integer getShare_sum() {
        return share_sum;
    }

    public void setShare_sum(Integer share_sum) {
        this.share_sum = share_sum;
    }

    @Override
    public String toString() {
        return "StockDecisionData{" +
                "authors=" + authors +
                ", code='" + code + '\'' +
                ", code_name='" + code_name + '\'' +
                ", core='" + core + '\'' +
                ", create_date='" + create_date + '\'' +
                ", download_file='" + download_file + '\'' +
                ", guid='" + guid + '\'' +
                ", is_worthed='" + is_worthed + '\'' +
                ", look_abstract='" + look_abstract + '\'' +
                ", open_file='" + open_file + '\'' +
                ", organ_id='" + organ_id + '\'' +
                ", organ_name='" + organ_name + '\'' +
                ", report_summary='" + report_summary + '\'' +
                ", report_title='" + report_title + '\'' +
                ", favor_sum=" + favor_sum +
                ", praise_sum=" + praise_sum +
                ", share_sum=" + share_sum +
                '}';
    }
}
