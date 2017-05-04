package cn.gogoal.im.bean;

/**
 * Created by dave.
 * Date: 2017/5/3.
 * Desc: description
 */
public class BoxScreenData {

    /*{
        "programme_name":"基金研究平台",
            "sort":-1,
            "programme_id":39
    }*/

    private String programme_name;
    private String sort;
    private String programme_id;

    public String getProgramme_name() {
        return programme_name;
    }

    public void setProgramme_name(String programme_name) {
        this.programme_name = programme_name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getProgramme_id() {
        return programme_id;
    }

    public void setProgramme_id(String programme_id) {
        this.programme_id = programme_id;
    }
}
