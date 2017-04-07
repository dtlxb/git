package cn.gogoal.im.bean;

import java.util.List;

/**
 * Created by huangxx on 2017/4/7.
 */

public class LcattrsBean {

    private String img_url;
    private ContentBean content;
    private TitleBean title;
    private String link;
    private ContentTitleBean content_title;
    private int link_type;
    private List<List<CoustomListBean>> coustom_list;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public TitleBean getTitle() {
        return title;
    }

    public void setTitle(TitleBean title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ContentTitleBean getContent_title() {
        return content_title;
    }

    public void setContent_title(ContentTitleBean content_title) {
        this.content_title = content_title;
    }

    public int getLink_type() {
        return link_type;
    }

    public void setLink_type(int link_type) {
        this.link_type = link_type;
    }

    public List<List<CoustomListBean>> getCoustom_list() {
        return coustom_list;
    }

    public void setCoustom_list(List<List<CoustomListBean>> coustom_list) {
        this.coustom_list = coustom_list;
    }

    @Override
    public String toString() {
        return "LcattrsBean{" +
                "img_url='" + img_url + '\'' +
                ", content=" + content +
                ", title=" + title +
                ", link='" + link + '\'' +
                ", content_title=" + content_title +
                ", link_type=" + link_type +
                ", coustom_list=" + coustom_list +
                '}';
    }
}