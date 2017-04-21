package cn.gogoal.im.bean;

import cn.gogoal.im.adapter.baseAdapter.entity.SectionEntity;

/**
 * author wangjd on 2017/4/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SectionTouYanData extends SectionEntity<TouYan.DataBean.Item> {

    private boolean simulatedArg;

    public SectionTouYanData(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SectionTouYanData(TouYan.DataBean.Item item,boolean simulatedArg) {
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
