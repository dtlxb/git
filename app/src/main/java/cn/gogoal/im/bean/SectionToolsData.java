package cn.gogoal.im.bean;

import cn.gogoal.im.adapter.baseAdapter.entity.SectionEntity;

/**
 * author wangjd on 2017/4/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SectionToolsData extends SectionEntity<ToolData.Tool> {

    private int parentPosition;
    private int childPosition;
    private int childCountInParent;

    public int getChildCountInParent() {
        return childCountInParent;
    }

    public void setChildCountInParent(int childCountInParent) {
        this.childCountInParent = childCountInParent;
    }

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

    public SectionToolsData(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SectionToolsData(ToolData.Tool item, int childCountInParent) {
        super(item);
        this.childCountInParent = childCountInParent;
    }

}
