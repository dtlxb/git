package cn.gogoal.im.bean;

/**
 * author wangjd on 2017/5/3 0003.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class PdfData {
    private String pdfUrl;
    private String title;

    public PdfData(String pdfUrl, String title) {
        this.pdfUrl = pdfUrl;
        this.title = title;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
