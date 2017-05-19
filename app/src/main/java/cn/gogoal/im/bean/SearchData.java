package cn.gogoal.im.bean;

/**
 * Created by huangxx on 2017/5/19.
 */

public class SearchData extends SearchEntity<SearchBean> {

    private int parentPosition;
    private int childPosition;
    private int childCountInParent;

    public int getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    public int getChildCountInParent() {
        return childCountInParent;
    }

    public void setChildCountInParent(int childCountInParent) {
        this.childCountInParent = childCountInParent;
    }

    public SearchData(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SearchData(SearchBean searchBean, int childCountInParent) {
        super(searchBean);
        this.childCountInParent = childCountInParent;
    }
}
