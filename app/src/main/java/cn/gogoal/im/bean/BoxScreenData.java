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
    //private String sort;
    private String programme_id;

    private boolean isSelected;

    public BoxScreenData(String programme_name, String programme_id, boolean isSelected) {
        this.programme_name = programme_name;
        this.programme_id = programme_id;
        this.isSelected = isSelected;
    }

    public String getProgramme_name() {
        return programme_name;
    }

    public void setProgramme_name(String programme_name) {
        this.programme_name = programme_name;
    }

    public String getProgramme_id() {
        return programme_id;
    }

    public void setProgramme_id(String programme_id) {
        this.programme_id = programme_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
