package cn.gogoal.im.bean;

import cn.gogoal.im.adapter.baseAdapter.entity.SectionEntity;

/**
 * author wangjd on 2017/4/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SectionToolsData extends SectionEntity<ToolData.Tool> {

    private boolean simulatedArg;//是不是模拟的空数据

    public SectionToolsData(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SectionToolsData(ToolData.Tool item, boolean simulatedArg) {
        super(item);
        this.simulatedArg=simulatedArg;
    }

    public boolean isSimulatedArg() {
        return simulatedArg;
    }

    public void setSimulatedArg(boolean simulatedArg) {
        this.simulatedArg = simulatedArg;
    }
}
