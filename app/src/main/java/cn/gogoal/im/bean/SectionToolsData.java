package cn.gogoal.im.bean;

import cn.gogoal.im.adapter.baseAdapter.entity.SectionEntity;

/**
 * author wangjd on 2017/4/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SectionToolsData extends SectionEntity<ToolData.Tool> {

    public SectionToolsData(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SectionToolsData(ToolData.Tool item) {
        super(item);
    }

}
